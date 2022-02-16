package it.unipi.dii.reviook_app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.ParseException;

@SuppressWarnings("unchecked")
public class Reviook extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        stage.setTitle("Reviook");
        stage.setResizable(false);
        stage.getIcons().add(new Image("/book.jpg"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        MongoDriver md = MongoDriver.getInstance();
        Neo4jDriver nd = Neo4jDriver.getInstance();
        md.close();
        nd.close();
    }

}

