package it.unipi.dii.reviook_app;

import it.unipi.dii.reviook_app.Data.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javafx.scene.Parent;

public class HelloApplication extends Application {
    Scanner myObj = new Scanner(System.in);
    String Name, username,nickname,email, password;
    public void UsersRegister(){


        // Enter username and press Enter
        System.out.println("Enter username");
        Name = myObj.nextLine();
        System.out.println("Enter username");
        username = myObj.nextLine();
        System.out.println("Enter nickname");
        nickname = myObj.nextLine();
        System.out.println("Enter email");
        email = myObj.nextLine();
        System.out.println("Enter password");
        password = myObj.nextLine();

        Users newUser;
        newUser = new Users();
        newUser.SignIn(Name,username,nickname,email,password);
    }
    @Override
    public void start(Stage stage) throws IOException {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        Scene scene;
        UsersRegister();

        FXMLLoader a = new FXMLLoader();
        scene = new Scene(a.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml")));
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