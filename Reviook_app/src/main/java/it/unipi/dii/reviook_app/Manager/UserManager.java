package it.unipi.dii.reviook_app.Manager;

import com.mongodb.client.*;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import org.bson.Document;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import java.util.UUID;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.neo4j.driver.Values.parameters;

public class UserManager {
    private MongoDriver md;
    private Neo4jDriver nd;

    public UserManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }

    // N4J
    public void addNewUsers(String type, String username){

        try ( Session session = nd.getDriver().session() )
        {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "CREATE (ee:"+type+" { username: $username})", parameters("username", username));
                return null;
            });
        }
    }

    public void following(String username1,boolean type1, String username2, boolean type2){
        String typ1;
        String typ2;
        if (type1) typ1 = "Author";
        else typ1 = "User";
        if (type2) typ2 = "Author";
        else typ2 = "User";


        try ( Session session = nd.getDriver().session() )
        {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run(  "MATCH (n:"+typ1+"),(nn:"+typ2+") WHERE n.username ='"+username1+"' AND nn.username='"+username2+"'"+
                        "CREATE (n)-[:FOLLOW]->(nn)");
                return null;
            });
        }
    }
    public void deleteFollowing(String username1,boolean type1, String username2, boolean type2){
        String typ1;
        String typ2;
        if (type1) typ1 = "Author";
        else typ1 = "User";
        if (type2) typ2 = "Author";
        else typ2 = "User";


        try ( Session session = nd.getDriver().session() )
        {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run(  "MATCH (n:"+ typ1 +" { username: '"+username1+"' })-[r:FOLLOW]-> " +
                        "(c :"+ typ2 +" { username: '"+ username2 +"' })" +
                        "DELETE r");
                return null;
            });
        }
    }

    public List<String> loadRelations(String type, String username)
    {

        List<String> movieTitles = new ArrayList();
        try ( Session session = nd.getDriver().session() )
        {
            movieTitles = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run( "MATCH (ee:"+type+")-[:FOLLOW]->(friends) where ee.username = '"+username+"' " +
                        "return friends.username as Friends" );
                ArrayList<String> movies = new ArrayList<>();
                while(result.hasNext())
                {
                    Record r = result.next();
                    movies.add(((Record) r).get("Friends").asString());
                }
                return movies;
            });
            for (String movieTitle: movieTitles)
            {
                System.out.println("\t- " + movieTitle);
            }

        }
        return  movieTitles;
    }
    public List<String> loadRelationsFollower(String type, String username)
    {
        Neo4jDriver nd = Neo4jDriver.getInstance();
        List<String> movieTitles = new ArrayList();
        try ( Session session = nd.getDriver().session() )
        {
            movieTitles = session.readTransaction((TransactionWork<List<String>>) tx -> {
                Result result = tx.run( "MATCH (ee:"+type+")<-[:FOLLOW]-(friends) where ee.username = '"+username+"' " +
                        "return friends.username as Friends" );
                ArrayList<String> movies = new ArrayList<>();
                while(result.hasNext())
                {
                    Record r = result.next();
                    movies.add(((Record) r).get("Friends").asString());
                }
                return movies;
            });
            for (String movieTitle: movieTitles)
            {
                System.out.println("\t- " + movieTitle);
            }

        }
        return  movieTitles;
    }

    //MongoDB

    public boolean verifyUsername(String Username){
        MongoCollection<Document> users = md.getCollection("usersnew");
        MongoCollection<Document> authors = md.getCollection("authorsnew");
        try (MongoCursor<Document> cursor = users.find( eq( "username",Username) ).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        }
        try (MongoCursor<Document> cursor = authors.find( eq( "username",Username) ).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyEmail(String Email){
        MongoCollection<Document> users = md.getCollection("usersnew");
        MongoCollection<Document> authors = md.getCollection("authorsnew");
        try (MongoCursor<Document> cursor = users.find( eq( "email",Email) ).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        }
        try (MongoCursor<Document> cursor = authors.find( eq( "email",Email) ).iterator()) {
            while (cursor.hasNext()) {
                return false;
            }
        }
        return true;
    }

    public void register(String name, String surname, String email ,String nickname, String password, String type){

        Document doc = new Document("name", name + " " + surname)
                .append("password", password)
                .append("count_reviews", "")
                .append("average_reviews","")
                .append("email",email)
                .append("username",nickname);

        String concat = nickname+email;
        String id = UUID.nameUUIDFromBytes(concat.getBytes()).toString();

        if(type.equals("Author")) {
            doc.append("author_id", id).append("avarage_reviewsSelf", "");
            md.getCollection("authorsnew").insertOne(doc);
        }
        else{
            doc.append("user_id", id);
            md.getCollection("usersnew").insertOne(doc);
        }

    }

}
