package it.unipi.dii.reviook_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
//        VBox layout = new VBox();
//        VBox layout2 = new VBox();
//        layout.setAlignment(Pos.CENTER);
//        layout2.setAlignment(Pos.CENTER);
//
//        Scene scene = new Scene(layout, 300, 300);
//        Scene scene2 = new Scene(layout2, 300, 300);
//
//        Label label1 = new Label("This is the First Scene");
//        Label label2 = new Label("This is the Second Scene");
//
//        Button button = new Button("Forward");
//        button.setOnAction(e -> stage.setScene(scene2));
//
//        Button button2 = new Button("Backwards");
//        button2.setOnAction(e -> stage.setScene(scene));
//
//        TextField text = new TextField();
//        text.setMaxWidth(100);
//
//        layout.getChildren().addAll(label1, button);
//        layout2.getChildren().addAll(label2, button2, text);
//
//        stage.setTitle("CodersLegacy");
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}