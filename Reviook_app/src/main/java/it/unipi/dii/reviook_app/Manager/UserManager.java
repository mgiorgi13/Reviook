package it.unipi.dii.reviook_app.Manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.Data.Book;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static org.neo4j.driver.Values.parameters;


public class UserManager {
    private MongoDriver md;
    private Neo4jDriver nd;
    private it.unipi.dii.reviook_app.Session session = it.unipi.dii.reviook_app.Session.getInstance();

    private static final String usersCollection = "users";
    private static final String authorCollection = "authors";
    private static final String bookCollection = "amazonBooks";
    private static final String genreCollection = "geners";


    public UserManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }
    class paramAuthor{
        String author_name;
        String author_role;
        String author_id;
    }
    // N4J
    public void addNewUsers(String type, String username,String id) {
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (ee:" + type + " { username: $username, author_id: $id})", parameters("username", username, "id", id));
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

    public void following(String username1, boolean type1, String username2, boolean type2) {
        String typ1;
        String typ2;
        if (type1)
            typ1 = "Author";
        else
            typ1 = "User";
        if (type2)
            typ2 = "Author";
        else
            typ2 = "User";
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n:" + typ1 + "),(nn:" + typ2 + ") WHERE n.username ='" + username1 + "' AND nn.username='" + username2 + "'" +
                        "CREATE (n)-[:FOLLOW]->(nn)");
                return null;
            });
        }
    }

    public void deleteFollowing(String username1, boolean type1, String username2, boolean type2) {
        String typ1;
        String typ2;
        if (type1) typ1 = "Author";
        else typ1 = "User";
        if (type2) typ2 = "Author";
        else typ2 = "User";


        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n:" + typ1 + " { username: '" + username1 + "' })-[r:FOLLOW]-> " +
                        "(c :" + typ2 + " { username: '" + username2 + "' })" +
                        "DELETE r");
                return null;
            });
        }
    }

    public ArrayList<Book> loadRelationsBook(String type, String username, String read) {
        ArrayList<Book> movieTitles = new ArrayList<Book>();
        ArrayList<Book> books = new ArrayList<>();
        try (Session session = nd.getDriver().session()) {
            movieTitles = session.readTransaction((TransactionWork<ArrayList<Book>>) tx -> {
                Result result = tx.run("MATCH (ee:" + type + ")-[:"+read+"]->(book) where ee.username = '" + username + "' " +
                        "return book.title, book.book_id");
                while (result.hasNext()) {
                    Record r = result.next();
                        books.add(new Book(((Record) r).get("book.title").asString(),((Record) r).get("book.book_id").asString()));
                }
                return books;
            });


        }
        return movieTitles;
    }
    public List<String> loadRelations(String type, String username) {

        List<String> movieTitles = new ArrayList();
        try (Session session = nd.getDriver().session()) {
            movieTitles = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run("MATCH (ee:" + type + ")-[:FOLLOW]->(friends) where ee.username = '" + username + "' " +
                        "return friends.username as Friends");
                ArrayList<String> movies = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    movies.add(((Record) r).get("Friends").asString());
                }
                return movies;
            });
//            for (String movieTitle : movieTitles) {
//                System.out.println("\t- " + movieTitle);
//            }

        }
        return movieTitles;
    }

    public List<String> loadRelationsFollower(String type, String username) {
        Neo4jDriver nd = Neo4jDriver.getInstance();
        List<String> movieTitles = new ArrayList();
        try (Session session = nd.getDriver().session()) {
            movieTitles = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run("MATCH (ee:" + type + ")<-[:FOLLOW]-(friends) where ee.username = '" + username + "' " +
                        "return friends.username as Friends");
                ArrayList<String> movies = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    movies.add(((Record) r).get("Friends").asString());
                }
                return movies;
            });
//            for (String movieTitle : movieTitles) {
//                System.out.println("\t- " + movieTitle);
//            }

        }
        return movieTitles;
    }
    public void toReadAdd(String type, String username, String book_id){
        Neo4jDriver nd = Neo4jDriver.getInstance();
        List<String> movieTitles = new ArrayList();
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("MATCH (n:" + type + "),(nn:Book) WHERE n.username ='" + username + "' AND nn.book_id='" + book_id + "'" +
                        "CREATE (n)-[:toRead]->(nn)");
                return null;
            });
        }


    }
    public void readedAdd(String type, String username, String book_id){
        Neo4jDriver nd = Neo4jDriver.getInstance();
        List<String> movieTitles = new ArrayList();
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {

                tx.run("MATCH (n:" + type + "),(nn:Book) WHERE n.username ='" + username + "' AND nn.book_id='" + book_id + "'" +
                        "CREATE (n)-[:readed]->(nn)" );
                return null;
            });
        }


    }

    //MongoDB
    public boolean verifyISBN(String ISBN) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        try (MongoCursor<Document> cursor = book.find(eq("ISBN", ISBN)).iterator()) {
            while (cursor.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public DBObject paramAuthor(String Username){
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        DBObject author = new BasicDBObject();
        try (MongoCursor<Document> cursor = authors.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                author.put("author_name",(String) user.get("name"));
                author.put("author_role","");
                author.put("author_id",(String) user.get("author_id"));
            }
        }

        return author;
    }
    public String retriveID(String Username){
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
                if (main == true)
                    session.setLoggedUser(user.get("name").toString(), "", user.get("username").toString(), user.get("email").toString(), user.get("password").toString());
                return 0;
            }
        }
        try (MongoCursor<Document> cursor = authors.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                if (main == true)
                    session.setLoggedAuthor(user.get("name").toString(), "", user.get("username").toString(), user.get("email").toString(), user.get("password").toString());

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
    public void addBook(String title, String ISBN, String Description, ArrayList<String> Genre,ArrayList<DBObject>  UsernameTagged){


        String concat =ISBN+title+UsernameTagged;
        String id = UUID.nameUUIDFromBytes(concat.getBytes()).toString();

        ArrayList<String> reviews = new ArrayList<String>();
        Document doc = new Document("image_url", "null")
                .append("num_pages", "")
                .append("isbn", ISBN)
                .append("description", Description)
                .append("average_rating", "")
                .append("book_id",id)
                .append("title", title)
                .append("language_code","")
                .append("publication_month", "")
                .append("publication_year", "")
                .append("reviews",reviews)
                .append("genres", Genre)
                .append("asin", "")
                .append("publication_day", "")
                .append("ratings_count", "")
                .append("authors", UsernameTagged);


        md.getCollection(bookCollection).insertOne(doc);
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (ee: Book { book_id : $book_id, title: $ title})", parameters("book_id", id, "title", title));
                for (int i = 0; i<UsernameTagged.size(); i++ ) {
                    tx.run("MATCH (dd:Author),(ee: Book) WHERE dd.author_id = '" + UsernameTagged.get(i).get("author_id") + "' AND ee.book_id='" + id + "'" +
                            "CREATE (dd)-[:WROTE]->(ee)");

                }
                return null;
            });
        }
    }

    public void register(String name, String surname, String email, String nickname, String password, String type, String id) {
        Document doc = new Document("name", name + " " + surname)
                .append("password", password)
                .append("count_reviews", "")
                .append("average_reviews", "")
                .append("email", email)
                .append("username", nickname);


        if (type.equals("Author")) {
            doc.append("author_id", id).append("avarage_reviewsSelf", "");
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

    public void AddReviewToBook(String reviewText, Integer ratingBook, String book_id) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        Document newReview = new Document();
        String reviewID = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        newReview.append("date_added", date);
        newReview.append("date_updated", "");
        newReview.append("review_id", reviewID);
        newReview.append("n_votes", "0");
        newReview.append("rating", ratingBook);
        newReview.append("review_text", reviewText);
        newReview.append("helpful", "0");
        if (session.getLoggedUser() != null) {
            String loggedUserID = session.getLoggedUser().getNickname();
//            System.out.println("book ID: " + book_id + " review Text: " + reviewText + " stars:" + ratingBook + " by " + loggedUserID);
            newReview.append("user_id", loggedUserID);
        } else {
            String loggedAuthorID = session.getLoggedAuthor().getNickname();
//            System.out.println("book ID: " + book_id + " review Text: " + reviewText + " stars:" + ratingBook + " by " + loggedAuthorID);
            newReview.append("user_id", loggedAuthorID);
        }
        Bson getBook = eq("book_id", book_id);
        DBObject elem = new BasicDBObject("reviews", new BasicDBObject(newReview));
        DBObject insertReview = new BasicDBObject("$push", elem);
        book.updateOne(getBook, (Bson) insertReview);
    }

    public void EditReview(String reviewText, Integer ratingBook, String book_id, String review_id) {
//        System.out.println(review_id + ".." + book_id + "------" + reviewText);
        MongoCollection<Document> books = md.getCollection(bookCollection);
        Bson getBook = eq("book_id", book_id);
        Bson getReview = eq("reviews.review_id", review_id);
        UpdateResult updateResult = books.updateOne(getReview, Updates.set("reviews.$.review_text", reviewText));
        UpdateResult updateResult2 = books.updateOne(getReview, Updates.set("reviews.$.rating", ratingBook));


    }
    //==================================================================================================================
}
