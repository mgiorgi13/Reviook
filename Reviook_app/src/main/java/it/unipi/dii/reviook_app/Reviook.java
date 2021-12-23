package it.unipi.dii.reviook_app;

import it.unipi.dii.reviook_app.Data.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Reviook extends Application {
    Scanner myObj = new Scanner(System.in);
    String Name, username, nickname, email, password;
//        public void UsersRegister(){
//            // Enter username and press Enter
//            System.out.println("Enter username");
//            Name = myObj.nextLine();
//            System.out.println("Enter username");
//            username = myObj.nextLine();
//            System.out.println("Enter nickname");
//            nickname = myObj.nextLine();
//            System.out.println("Enter email");
//            email = myObj.nextLine();
//            System.out.println("Enter password");
//            password = myObj.nextLine();
//
//            Users newUser;
//            newUser = new Users();
//            newUser.SignIn(Name,username,nickname,email,password);
//        }
    @Override
    public void start(Stage stage) throws IOException {
//        UsersRegister();
        Parent root = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        stage.setTitle("Reviook");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}