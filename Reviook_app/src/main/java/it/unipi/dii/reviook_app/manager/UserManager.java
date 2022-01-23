package it.unipi.dii.reviook_app.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private static final String genreCollection = "genres";


    public UserManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }


    // N4J
    public void addNewUsers(String type, String id, String name, String username) {
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (ee:" + type + " { id: $id,  name: $name, username: $username})", parameters("id", id, "name", name, "username", username));
                return null;
            });
        }
    }

    public boolean deleteUserN4J() {
        boolean result = false;
        String username;
        String type = this.session.getIsAuthor() ? "Author" : "User";

        if (this.session.getIsAuthor())
            username = this.session.getLoggedAuthor().getNickname();
        else
            username = this.session.getLoggedUser().getNickname();
        try (Session session = nd.getDriver().session()) {
            result = session.writeTransaction((TransactionWork<Boolean>) tx -> {
                tx.run("MATCH (n : " + type + " { username: '" + username + "'}) DETACH DELETE n");
                return true;
            });
        }
        return result;
    }

    public void following(String username1, String type1, String username2, String type2) {
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n:" + type1 + "),(nn:" + type2 + ") WHERE n.username ='" + username1 + "' AND nn.username='" + username2 + "'" +
                        "CREATE (n)-[:FOLLOW]->(nn)");
                return null;
            });
        }
    }

    public void deleteFollowing(String username1, String type1, String username2, String type2) {
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n:" + type1 + " { username: '" + username1 + "' })-[r:FOLLOW]-> " +
                        "(c :" + type2 + " { username: '" + username2 + "' })" +
                        "DELETE r");
                return null;
            });
        }
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

    public List<String> loadRelations(String type, String username) {

        List<String> relationship;
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
        List<String> relationship;
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

    public void toReadAdd(String type, String username, String book_id) {
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n:" + type + "),(nn:Book) WHERE n.username ='" + username + "' AND nn.id='" + book_id + "'" +
                        "MERGE (n)-[:TO_READ]->(nn)");
                return null;
            });
        }


    }

    public void readAdd(String type, String username, String book_id) {
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {

                tx.run("MATCH (n:" + type + "),(nn:Book) WHERE n.username ='" + username + "' AND nn.id='" + book_id + "'" +
                        "MERGE (n)-[:READ]->(nn)");
                return null;
            });
        }


    }
    //==================================================================================================================

    //MongoDB ==========================================================================================================

    public DBObject paramAuthor(String Username) {
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        DBObject author = new BasicDBObject();
        try (MongoCursor<Document> cursor = authors.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                author.put("author_name", (String) user.get("name"));
                author.put("author_role", "");
                author.put("author_id", (String) user.get("author_id"));
            }
        }

        return author;
    }

    public String retrieveID(String Username) {
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        String ID = null;
        try (MongoCursor<Document> cursor = authors.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                ID = user.get("author_id").toString();

            }
        }
        return ID;
    }

    public int verifyUsername(String Username, boolean main) {
        MongoCollection<Document> users = md.getCollection(usersCollection);
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        try (MongoCursor<Document> cursor = users.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                if (main) {
                    ArrayList<String> listReviewID = (ArrayList<String>) user.get("liked_review");
                    session.setLoggedUser(user.getString("user_id"), user.get("name").toString(), "", user.get("username").toString(), user.get("email").toString(), user.get("password").toString(), listReviewID, (Integer) user.get("follower_count"));
                }
                return 0;
            }
        }
        try (MongoCursor<Document> cursor = authors.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                if (main) {
                    ArrayList<String> listReviewID = (ArrayList<String>) user.get("liked_review");
                    session.setLoggedAuthor(user.getString("author_id"), user.get("name").toString(), "", user.get("username").toString(), user.get("email").toString(), user.get("password").toString(), listReviewID, (Integer) user.get("follower_count"));
                }
                return 1;
            }
        }
        return -1;
    }

    public boolean verifyPassword(boolean type, String Username, String Password) {
        MongoCollection<Document> users = md.getCollection(type ? authorCollection : usersCollection);
        try (MongoCursor<Document> cursor = users.find(and(eq("username", Username), eq("password", Password))).iterator()) {
            while (cursor.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public boolean verifyEmail(String Email) {
        MongoCollection<Document> users = md.getCollection(usersCollection);
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        try (MongoCursor<Document> cursor = users.find(eq("email", Email)).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        }
        try (MongoCursor<Document> cursor = authors.find(eq("email", Email)).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        }
        return true;
    }

    public void register(String name, String surname, String email, String nickname, String password, String type, String id) {
        ArrayList<String> liked_review = new ArrayList<>();
        Document doc = new Document("name", name + " " + surname)
                .append("password", password)
                .append("follower_count", 0)
                .append("liked_review", liked_review)
                .append("email", email)
                .append("username", nickname);


        if (type.equals("Author")) {
            doc.append("author_id", id);
            md.getCollection(authorCollection).insertOne(doc);
        } else {
            doc.append("user_id", id);
            md.getCollection(usersCollection).insertOne(doc);
        }

    }

    public boolean deleteUserMongo() {
        MongoCollection<Document> user = md.getCollection(session.getIsAuthor() ? authorCollection : usersCollection);
        String username;
        if (session.getIsAuthor())
            username = session.getLoggedAuthor().getNickname();
        else
            username = session.getLoggedUser().getNickname();

        DeleteResult deleteResult = user.deleteOne(eq("username", username));
        if (deleteResult.getDeletedCount() == 1)
            return true;
        return false;
    }

    public boolean updatePassword(String newPassword) {
        MongoCollection<Document> user = md.getCollection(session.getIsAuthor() ? authorCollection : usersCollection);
        String username;
        if (session.getIsAuthor())
            username = session.getLoggedAuthor().getNickname();
        else
            username = session.getLoggedUser().getNickname();

        UpdateResult updateResult = user.updateOne(eq("username", username), Updates.set("password", newPassword));
        if (updateResult.getModifiedCount() == 1)
            return true;
        return false;
    }

    private void incrementFollowerCount(String id) {
        //increment follower count
        if (session.getIsAuthor() != null) {
            MongoCollection<Document> authors = md.getCollection(authorCollection);
            Bson getAuthor = eq("author_id", id);
            authors.updateOne(getAuthor, Updates.inc("follower_count", 1));
        } else if (session.getLoggedUser() != null) {
            MongoCollection<Document> users = md.getCollection(usersCollection);
            Bson getUser = eq("user_id", id);
            users.updateOne(getUser, Updates.inc("follower_count", 1));
        }
    }

    private void decrementFollowerCount(String id) {
        //increment follower count
        if (session.getIsAuthor() != null) {
            MongoCollection<Document> authors = md.getCollection(authorCollection);
            Bson getAuthor = eq("author_id", id);
            authors.updateOne(getAuthor, Updates.inc("follower_count", -1));
        } else if (session.getLoggedUser() != null) {
            MongoCollection<Document> users = md.getCollection(usersCollection);
            Bson getUser = eq("user_id", id);
            users.updateOne(getUser, Updates.inc("follower_count", -1));
        }
    }

    //==================================================================================================================

    //ANALYTICS ==========================================================================================================

    public ArrayList<Genre> averageRatingCategoryAuthor(String username){
        MongoCollection<Document> author = md.getCollection(authorCollection);
        MongoCollection<Document> books = md.getCollection(bookCollection);
        String author_id = null;
        ArrayList<Genre> topRated= new ArrayList<>();

        Bson getAuthor;
        Bson unwindGenres;
        Bson groupGenres;
        Bson sortAvg;


        //get id of the author using the username
        try (MongoCursor<Document> cursor = author.find(eq ("username",username)).iterator()) {
            while (cursor.hasNext()) {
                author_id = cursor.next().getString("author_id");
            }
        }

        if(author_id == null)
            return null;

        getAuthor = match(eq("authors.author_id",author_id));
        unwindGenres = unwind("$genres", new UnwindOptions().preserveNullAndEmptyArrays(false));
        groupGenres = group("$genres", avg("average_rating", "$average_rating"));
        sortAvg = sort(orderBy(descending("average_rating")));

        try (MongoCursor<Document> cursor = books.aggregate(Arrays.asList(getAuthor, unwindGenres, groupGenres, sortAvg)).iterator()) {
            while (cursor.hasNext()) {
                Document stat = cursor.next();
                Double avg = Math.round((stat.getDouble("average_rating")) * 100) / 100.0;

                Genre genre = new Genre(stat.getString("_id"),Double.valueOf(avg));
                topRated.add(genre);
            }
        }
        return topRated;
    }

    public ArrayList<Genre> averageRatingCategoryUser(String username){
        MongoCollection<Document> books = md.getCollection(bookCollection);
        ArrayList<Genre> topRated= new ArrayList<>();

        Bson getUser;
        Bson unwindReviews;
        Bson unwindGenres;
        Bson groupGenres;
        Bson sortAvg;

        getUser =  match(eq("reviews.username",username));
        unwindReviews = unwind("$reviews", new UnwindOptions().preserveNullAndEmptyArrays(false));
        unwindGenres = unwind("$genres", new UnwindOptions().preserveNullAndEmptyArrays(false));
        groupGenres = group("$genres", avg("average_rating", "$reviews.rating"));
        sortAvg = sort(orderBy(descending("average_rating")));

        try (MongoCursor<Document> cursor = books.aggregate(Arrays.asList(unwindReviews, getUser, unwindGenres, groupGenres, sortAvg)).iterator()) {
            while (cursor.hasNext()) {
                Document stat = cursor.next();
                Double avg = Math.round((stat.getDouble("average_rating")) * 100) / 100.0;

                Genre genre = new Genre(stat.getString("_id"),Double.valueOf(avg));
                topRated.add(genre);
            }
        }
        return topRated;
    }

    public ArrayList<User> similarUsers(String username,String type){
        ArrayList<User> suggestion = new ArrayList<>();

        try (Session session = nd.getDriver().session()) {
            suggestion = (ArrayList<User>) session.readTransaction((TransactionWork<List<User>>) tx -> {
                Result result = tx.run("MATCH (u1:" + type + ")-[]-(b:Book)-[]-(u2:User) " +
                        "WHERE u1.username = '" + username + "' AND u1<>u2" +
                        "RETURN DISTINCT u2");
                ArrayList<User> queryResult = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    queryResult.add(new User(r.get("u2.id").toString(),r.get("u2.name").toString(),"",r.get("u2.username").toString(),"","",new ArrayList<>(),0));
                }
                return queryResult;
            });
        }
        return suggestion;
    }

     public ArrayList<Author> similarAuthors(String username,String type){
        ArrayList<Author> suggestion;

        try (Session session = nd.getDriver().session()) {
            suggestion = (ArrayList<Author>) session.readTransaction((TransactionWork<List<Author>>) tx -> {
                Result result = tx.run("MATCH (u:" + type + ")-[]-(b:Book)-[]-(a:Author) " +
                        "WHERE u.username = '" + username + "' AND u<>a" +
                        "RETURN DISTINCT a");
                ArrayList<Author> queryResult = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    queryResult.add(new Author(r.get("a.id").toString(),r.get("a.name").toString(),"",r.get("a.username").toString(),"","",new ArrayList<>(),0));
                }
                return queryResult;
            });
        }
        return suggestion;
    }

    //==================================================================================================================

}
