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
    requires java.xml.bind;
    requires javafaker;
    requires java.desktop;

    opens it.unipi.dii.reviook_app to javafx.fxml;
    exports it.unipi.dii.reviook_app;
    exports it.unipi.dii.reviook_app.Controllers;
    opens it.unipi.dii.reviook_app.Controllers to javafx.fxml;
    exports it.unipi.dii.reviook_app.Components;
    opens it.unipi.dii.reviook_app.Components to javafx.fxml;
}