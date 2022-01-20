module it.unipi.dii.reviook_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires json;
    requires org.neo4j.driver;
    requires json.simple;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires javafaker;
    requires java.desktop;


    opens it.unipi.dii.reviook_app to javafx.fxml;
    exports it.unipi.dii.reviook_app;
    exports it.unipi.dii.reviook_app.controllers;
    opens it.unipi.dii.reviook_app.controllers to javafx.fxml;
    exports it.unipi.dii.reviook_app.components;
    opens it.unipi.dii.reviook_app.components to javafx.fxml;
}