package it.unipi.dii.reviook_app.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import it.unipi.dii.reviook_app.entity.Genre;
import it.unipi.dii.reviook_app.entity.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static org.neo4j.driver.Values.parameters;
import static org.neo4j.driver.Values.value;


public class UserManager {
    private MongoDriver md;
    private Neo4jDriver nd;
    private it.unipi.dii.reviook_app.Session session = it.unipi.dii.reviook_app.Session.getInstance();
    private static final String usersCollection = "users";
    private static final String authorCollection = "authors";
    private static final String bookCollection = "books";
    private static final String adminsCollection = "admins";

    public UserManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }

    // N4J
    public boolean addNewUsers(User user, String type) {
        boolean result;
        try (Session session = nd.getDriver().session()) {
            result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                tx.run("CREATE (ee:" + type + " { id: $id,  name: $name, username: $username})", parameters("id", user.getId(), "name", user.getName(), "username", user.getNickname()));
                return true;
            });
        }
        if (!result)
            deleteUserMongo(user, type);
        return result;
    }

    public boolean deleteUserN4J(User user, String type) {
        boolean result;
        String t = type.equals("author") ? "Author" : "User";
        try (Session session = nd.getDriver().session()) {
            result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                tx.run("MATCH (n : " + t + " { username: '" + user.getNickname() + "'}) DETACH DELETE n");
                return true;
            });
        }
        if (!result)
            addNewUsers(user, type);
        return result;
    }

    public boolean following(String username1, String type1, String username2, String type2) {
        boolean result = false;
        if (incrementFollowerCount(username2)) {
            try (Session session = nd.getDriver().session()) {
                result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                    tx.run("MATCH (n:" + type1 + "),(nn:" + type2 + ") WHERE n.username ='" + username1 + "' AND nn.username='" + username2 + "'" +
                            "CREATE (n)-[:FOLLOW]->(nn)");
                    return true;
                });
            }
            if (result != true)
                decrementFollowerCount(username2);
        }
        return result;
    }

    public boolean deleteFollowing(String username1, String type1, String username2, String type2) {
        boolean result = false;
        if (decrementFollowerCount(username2)) {
            try (Session session = nd.getDriver().session()) {
                result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                    tx.run("MATCH (n:" + type1 + " { username: '" + username1 + "' })-[r:FOLLOW]-> " +
                            "(c :" + type2 + " { username: '" + username2 + "' })" +
                            "DELETE r");
                    return true;
                });
            }
            if (result != true)
                incrementFollowerCount(username2);
        }
        return result;
    }

    public ArrayList<Book> loadRelationsBook(String type, String username, String read) {
        ArrayList<Book> readings = new ArrayList<Book>();
        ArrayList<Book> books = new ArrayList<>();
        try (Session session = nd.getDriver().session()) {
            readings = session.readTransaction((TransactionWork<ArrayList<Book>>) tx -> {
                Result result = tx.run("MATCH (ee:" + type + ")-[:" + read + "]->(book) where ee.username = '" + username + "' " +
                        "return book.title, book.id");
                while (result.hasNext()) {
                    Record r = result.next();
                    books.add(new Book(((Record) r).get("book.title").asString(), ((Record) r).get("book.id").asString()));
                }
                return books;
            });
        }
        return readings;
    }

    public List<String> loadRelationsFollowing(String type, String username) {
        List<String> relationship = new ArrayList<>();
        try (Session session = nd.getDriver().session()) {
            relationship = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run("MATCH (ee:" + type + ")-[:FOLLOW]->(friends) where ee.username = '" + username + "' " +
                        "return friends.username as Friends");
                ArrayList<String> following = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    following.add(((Record) r).get("Friends").asString());
                }
                return following;
            });
        }
        return relationship;
    }

    public List<String> loadRelationsFollower(String type, String username) {
        List<String> relationship = new ArrayList<>();
        try (Session session = nd.getDriver().session()) {
            relationship = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run("MATCH (ee:" + type + ")<-[:FOLLOW]-(friends) where ee.username = '" + username + "' " +
                        "return friends.username as Friends");
                ArrayList<String> follower = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    follower.add(((Record) r).get("Friends").asString());
                }
                return follower;
            });
        }
        return relationship;
    }

    public boolean toReadAdd(String type, String username, String book_id) {
        boolean result = false;
        try (Session session = nd.getDriver().session()) {
            result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                tx.run("MATCH (n:" + type + "),(nn:Book) WHERE n.username ='" + username + "' AND nn.id='" + book_id + "'" +
                        "MERGE (n)-[:TO_READ]->(nn)");
                return true;
            });
        }
        return result;
    }

    public boolean readAdd(String type, String username, String book_id) {
        boolean result = false;
        try (Session session = nd.getDriver().session()) {
            result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                tx.run("MATCH (n:" + type + "),(nn:Book) WHERE n.username ='" + username + "' AND nn.id='" + book_id + "'" +
                        "MERGE (n)-[:READ]->(nn)");
                return true;
            });
        }
        return result;
    }
    //==================================================================================================================

    //MongoDB ==========================================================================================================

    public int verifyUsername(String Username, String type, boolean loggingIn) {
        MongoCollection<Document> admins = md.getCollection(adminsCollection);
        MongoCollection<Document> users = md.getCollection(usersCollection);
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        MongoCursor<Document> cursor = null;
        try {
            if (type.equals("admin")) {
                cursor = admins.find(eq("username", Username)).iterator();
                while (cursor.hasNext()) {
                    Document admin = cursor.next();
                    session.setAdmin(admin.getString("username"));
                    return 2;
                }

            }
            if (type.equals("author") || type.equals("")) {
                cursor = authors.find(eq("username", Username)).iterator();
                while (cursor.hasNext()) {
                    Document user = cursor.next();
                    if (loggingIn) {
                        ArrayList<String> listReviewID = (ArrayList<String>) user.get("liked_review");
                        session.setLoggedAuthor(user.getString("author_id"), user.get("name").toString(), user.get("username").toString(), user.get("email").toString(), user.get("password").toString(), listReviewID, (Integer) user.get("follower_count"));
                    }
                    return 1;
                }

            }
            if (type.equals("user") || type.equals("")) {
                cursor = users.find(eq("username", Username)).iterator();
                while (cursor.hasNext()) {
                    Document user = cursor.next();
                    if (loggingIn) {
                        ArrayList<String> listReviewID = (ArrayList<String>) user.get("liked_review");
                        session.setLoggedUser(user.getString("user_id"), user.get("name").toString(), user.get("username").toString(), user.get("email").toString(), user.get("password").toString(), listReviewID, (Integer) user.get("follower_count"));
                    }
                    return 0;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return -1;
    }

    public boolean verifyPassword(String loginType, String Username, String Password) {
        MongoCollection<Document> collection = md.getCollection(loginType.equals("author") ? authorCollection : loginType.equals("user") ? usersCollection : adminsCollection);
        try (MongoCursor<Document> cursor = collection.find(and(eq("username", Username), eq("password", Password))).iterator()) {
            while (cursor.hasNext()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyEmail(String Email, String type) {
        MongoCollection<Document> users = md.getCollection(type.equals("author") ? authorCollection : usersCollection);
        try (MongoCursor<Document> cursor = users.find(eq("email", Email)).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean register(User user, String type) {
        ArrayList<String> liked_review = new ArrayList<>();
        InsertOneResult result = null;
        try {
            Document doc = new Document("name", user.getName())
                    .append("password", user.getPassword())
                    .append("follower_count", 0)
                    .append("liked_review", liked_review)
                    .append("email", user.getEmail())
                    .append("username", user.getNickname());
            if (type.equals("Author")) {
                doc.append("author_id", user.getId());
                result = md.getCollection(authorCollection).insertOne(doc);
            } else {
                doc.append("user_id", user.getId());
                result = md.getCollection(usersCollection).insertOne(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean deleteUserMongo(User userDel, String type) {
        MongoCollection<Document> user = md.getCollection(type.equals("author") ? authorCollection : usersCollection);
        DeleteResult deleteResult = null;
        try {
            deleteResult = user.deleteOne(eq("username", userDel.getNickname()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deleteResult != null && deleteResult.getDeletedCount() == 1)
            return true;
        return false;
    }

    public boolean updatePassword(String newPassword) {
        MongoCollection<Document> user = md.getCollection(session.getIsAuthor() ? authorCollection : usersCollection);
        UpdateResult updateResult = null;
        String username;
        try {
            if (session.getIsAuthor())
                username = session.getLoggedAuthor().getNickname();
            else
                username = session.getLoggedUser().getNickname();
            updateResult = user.updateOne(eq("username", username), Updates.set("password", newPassword));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (updateResult != null & updateResult.getModifiedCount() == 1)
            return true;
        return false;
    }

    private boolean incrementFollowerCount(String id) {
        //increment follower count
        UpdateResult result = null;
        try {
            if (session.getIsAuthor() != null) {
                MongoCollection<Document> authors = md.getCollection(authorCollection);
                Bson getAuthor = eq("author_id", id);
                result = authors.updateOne(getAuthor, Updates.inc("follower_count", 1));
            } else if (session.getLoggedUser() != null) {
                MongoCollection<Document> users = md.getCollection(usersCollection);
                Bson getUser = eq("user_id", id);
                result = users.updateOne(getUser, Updates.inc("follower_count", 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null)
            return result.wasAcknowledged();
        return false;
    }

    private boolean decrementFollowerCount(String id) {
        //increment follower count
        UpdateResult result = null;
        try {
            if (session.getIsAuthor() != null) {
                MongoCollection<Document> authors = md.getCollection(authorCollection);
                Bson getAuthor = eq("author_id", id);
                result = authors.updateOne(getAuthor, Updates.inc("follower_count", -1));
            } else if (session.getLoggedUser() != null) {
                MongoCollection<Document> users = md.getCollection(usersCollection);
                Bson getUser = eq("user_id", id);
                result = users.updateOne(getUser, Updates.inc("follower_count", -1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null)
            return result.wasAcknowledged();
        return false;
    }

    //==================================================================================================================

    //ANALYTICS ==========================================================================================================

    public ArrayList<Genre> averageRatingCategoryAuthor(String username) {
        MongoCollection<Document> author = md.getCollection(authorCollection);
        MongoCollection<Document> books = md.getCollection(bookCollection);
        String author_id = null;
        ArrayList<Genre> topRated = new ArrayList<>();
        Bson getAuthor;
        Bson unwindGenres;
        Bson groupGenres;
        Bson sortAvg;
        //get id of the author using the username
        try (MongoCursor<Document> cursor = author.find(eq("username", username)).iterator()) {
            while (cursor.hasNext()) {
                author_id = cursor.next().getString("author_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return topRated;
        }
        if (author_id == null)
            return null;
        getAuthor = match(eq("authors.author_id", author_id));
        unwindGenres = unwind("$genres", new UnwindOptions().preserveNullAndEmptyArrays(false));
        groupGenres = group("$genres", avg("average_rating", "$average_rating"));
        sortAvg = sort(orderBy(descending("average_rating")));
        try (MongoCursor<Document> cursor = books.aggregate(Arrays.asList(getAuthor, unwindGenres, groupGenres, sortAvg)).iterator()) {
            while (cursor.hasNext()) {
                Document stat = cursor.next();
                Double avg = Math.round((stat.getDouble("average_rating")) * 100) / 100.0;
                Genre genre = new Genre(stat.getString("_id"), Double.valueOf(avg));
                topRated.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topRated;
    }

    public ArrayList<Genre> averageRatingCategoryUser(String username) {
        MongoCollection<Document> books = md.getCollection(bookCollection);
        ArrayList<Genre> topRated = new ArrayList<>();
        Bson getUser;
        Bson unwindReviews;
        Bson unwindGenres;
        Bson groupGenres;
        Bson sortAvg;
        getUser = match(eq("reviews.username", username));
        unwindReviews = unwind("$reviews", new UnwindOptions().preserveNullAndEmptyArrays(false));
        unwindGenres = unwind("$genres", new UnwindOptions().preserveNullAndEmptyArrays(false));
        groupGenres = group("$genres", avg("average_rating", "$reviews.rating"));
        sortAvg = sort(orderBy(descending("average_rating")));
        try (MongoCursor<Document> cursor = books.aggregate(Arrays.asList(unwindReviews, getUser, unwindGenres, groupGenres, sortAvg)).iterator()) {
            while (cursor.hasNext()) {
                Document stat = cursor.next();
                Double avg = Math.round((stat.getDouble("average_rating")) * 100) / 100.0;
                Genre genre = new Genre(stat.getString("_id"), Double.valueOf(avg));
                topRated.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topRated;
    }

    public ArrayList<User> similarUsers(String username, String type) {
        ArrayList<User> suggestion = new ArrayList<>();
        ArrayList<User> queryResult = new ArrayList<>();
        try (Session session = nd.getDriver().session()) {
            suggestion = session.readTransaction((TransactionWork<ArrayList<User>>) tx -> {
                Result result = tx.run("MATCH (u1:" + type + ")-[:READ|:TO_READ]->(b:Book)<-[]-(u2:User) " +
                        "WHERE u1.username = '" + username + "' AND u1<>u2 " +
                        "RETURN DISTINCT u2.id,u2.name,u2.username");
                while (result.hasNext()) {
                    Record r = result.next();
                    queryResult.add(new User(r.get("u2.id").asString(), r.get("u2.name").asString(), r.get("u2.username").asString(), "", "", new ArrayList<>(), 0));
                }
                return queryResult;
            });
        }
        return suggestion;
    }

    public ArrayList<Author> similarAuthors(String username, String type) {
        ArrayList<Author> suggestion;
        ArrayList<Author> queryResult = new ArrayList<>();
        try (Session session = nd.getDriver().session()) {
            suggestion = (ArrayList<Author>) session.readTransaction((TransactionWork<ArrayList<Author>>) tx -> {
                Result result = tx.run("MATCH (u:" + type + ")-[:READ|:TO_READ]->(b:Book)<-[:READ|:TO_READ]-(a:Author) " +
                        "WHERE u.username = '" + username + "' AND u<>a " +
                        "RETURN DISTINCT a.id,a.name,a.username");
                while (result.hasNext()) {
                    Record r = result.next();
                    queryResult.add(new Author(r.get("a.id").asString(), r.get("a.name").asString(), r.get("a.username").asString(), "", "", new ArrayList<>(), 0));
                }
                return queryResult;
            });
        }
        return suggestion;
    }

    //==================================================================================================================

}
