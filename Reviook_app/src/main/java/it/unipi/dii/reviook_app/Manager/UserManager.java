package it.unipi.dii.reviook_app.Manager;

import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.UUID;


import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static org.neo4j.driver.Values.parameters;


public class UserManager {
    private MongoDriver md;
    private Neo4jDriver nd;
    private it.unipi.dii.reviook_app.Session session = it.unipi.dii.reviook_app.Session.getInstance();

    private static final String usersCollection = "users";
    private static final String authorCollection = "authors";
    private static final String bookCollection = "books";

    public UserManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }

    // N4J
    public void addNewUsers(String type, String username) {

        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (ee:" + type + " { username: $username})", parameters("username", username));
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
        if (type1) typ1 = "Author";
        else typ1 = "User";
        if (type2) typ2 = "Author";
        else typ2 = "User";


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
            for (String movieTitle : movieTitles) {
                System.out.println("\t- " + movieTitle);
            }

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
            for (String movieTitle : movieTitles) {
                System.out.println("\t- " + movieTitle);
            }

        }
        return movieTitles;
    }

    //MongoDB

    public int verifyUsername(String Username) {
        MongoCollection<Document> users = md.getCollection(usersCollection);
        MongoCollection<Document> authors = md.getCollection(authorCollection);
        try (MongoCursor<Document> cursor = users.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
                session.setLoggedUser(user.get("name").toString(), "", user.get("username").toString(), user.get("email").toString(), user.get("password").toString());
                return 0;
            }
        }
        try (MongoCursor<Document> cursor = authors.find(eq("username", Username)).iterator()) {
            while (cursor.hasNext()) {
                Document user = cursor.next();
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

    public void register(String name, String surname, String email, String nickname, String password, String type) {

        Document doc = new Document("name", name + " " + surname)
                .append("password", password)
                .append("count_reviews", "")
                .append("average_reviews", "")
                .append("email", email)
                .append("username", nickname);

        String concat = nickname + email;
        String id = UUID.nameUUIDFromBytes(concat.getBytes()).toString();

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

    public boolean updatePassword(String newPassword){
        MongoCollection<Document> user = md.getCollection(session.getIsAuthor() ? authorCollection : usersCollection);
        String username;
        if (session.getIsAuthor())
            username = session.getLoggedAuthor().getNickname();
        else
            username = session.getLoggedUser().getNickname();

        UpdateResult updateResult = user.updateOne(eq("username", username), Updates.set("password",newPassword));
        if (updateResult.getModifiedCount() == 1)
            return true;
        return false;
    }

}
