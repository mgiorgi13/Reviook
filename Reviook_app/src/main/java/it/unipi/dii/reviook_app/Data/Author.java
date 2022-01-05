package it.unipi.dii.reviook_app.Data;

import com.jfoenix.controls.JFXButton;
import it.unipi.dii.reviook_app.Neo4jDriver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.io.IOException;
import java.util.ArrayList;

import static org.neo4j.driver.Values.parameters;

public class Author extends Users {
    private ArrayList<String> writtenBook;
    private ArrayList<String> writtenBookStatisitc;
    private int likeNumber;

    public Author(String name, String surname, String nickname, String email, String password) {
        super( name,  surname,  nickname, email,  password);
        this.writtenBook = new ArrayList<String>();
        this.writtenBookStatisitc = new ArrayList<String>();
        this.likeNumber = 0;
    }

}
