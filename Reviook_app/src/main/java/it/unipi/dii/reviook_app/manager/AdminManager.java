package it.unipi.dii.reviook_app.manager;

import it.unipi.dii.reviook_app.MongoDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

public class AdminManager {
    private MongoDriver md;

    private static final String adminCollection = "admins";

    public AdminManager() {
        this.md = MongoDriver.getInstance();
    }

    public void AddNewAdmin(String username, String password) {
        Document doc = new Document("username", username).append("password", password);
        md.getCollection(adminCollection).insertOne(doc);
    }
}
