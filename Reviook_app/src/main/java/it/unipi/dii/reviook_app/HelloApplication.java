package it.unipi.dii.reviook_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import javafx.scene.Parent;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        Scene scene;


        FXMLLoader a = new FXMLLoader();
        scene = new Scene(a.load(getClass().getResource("/author-interface.fxml")));
        //scene.getStylesheets().add(getClass().getResource("css/BalanceUS.css").toExternalForm());
        stage.setTitle("SingIn");
        //stage.setResizable(false);
        stage.setScene(scene);
        stage.show();




    }

    public static void main(String[] args) {
        launch();
    }
}