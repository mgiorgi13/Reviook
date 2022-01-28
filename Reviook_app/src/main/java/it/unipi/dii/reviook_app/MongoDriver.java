package it.unipi.dii.reviook_app;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@SuppressWarnings("unchecked")
public class MongoDriver {

    private static MongoDriver driver = null;
    private MongoClient client;
    private MongoDatabase database;
    private ConnectionString uri;
    private MongoDriver() {
        client = MongoClients.create("mongodb://localhost:27017/");
        database = client.getDatabase("reviook");
    }
//    private MongoDriver() {
//        try {
//            uri = new ConnectionString("mongodb://172.16.4.102:27020,172.16.4.103:27020,172.16.4.104:27020/");
//            MongoClientSettings msc = MongoClientSettings.builder()
//                    .applyConnectionString(uri)
//                    .readPreference(ReadPreference.nearest())
//                    .retryWrites(true)
//                    .writeConcern(WriteConcern.MAJORITY).build();
//            client = MongoClients.create(msc);
//            database = client.getDatabase("reviook");
//            System.out.println("Connected to MongoDB");
//        }catch (Exception e){
//            System.out.println(e);
//        }
//        System.out.println("Servers are ready");
//    }

    public static MongoDriver getInstance() {
        if(driver == null)
            driver = new MongoDriver();

        return driver;
    }

    public MongoCollection<Document> getCollection(String collection) {
        if(driver == null)
            throw new RuntimeException("Connection doesn't exist.");
        else
            return driver.database.getCollection(collection);
    }

    public void close() {
        if(driver == null)
            throw new RuntimeException("Connection doesn't exist.");
        else
            driver.client.close();
    }

}
