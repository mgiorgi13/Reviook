package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AuthorInterfaceController {
    @FXML
    private ResourceBundle resources;



    @FXML
    private JFXButton button;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Text followCount;

    @FXML
    private Text followersCount;

    @FXML
    private Text likesCount;

    @FXML
    private JFXListView listToRead;

    @FXML
    private JFXListView listReaded;

    @FXML
    private Button editButtonAuthor;

    @FXML
    void viewEditButtonAuthor(ActionEvent event) throws IOException{
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) editButtonAuthor.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }



    @FXML
    void searchInterface(ActionEvent event) throws IOException {
        Parent searchInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/search-interface.fxml"));
        Stage actual_stage = (Stage) searchButton.getScene().getWindow();
        actual_stage.setScene(new Scene(searchInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    public void initialize() {
        Random rand = new Random();
        likesCount.setText(String.valueOf(rand.nextInt(9999)));
        followCount.setText(String.valueOf(rand.nextInt(9999)));
        followersCount.setText(String.valueOf(rand.nextInt(9999)));

        listToRead.getItems().add("Book to read 1");
        listToRead.getItems().add("Book to read 2");
        listToRead.getItems().add("Book to read 3");

        listReaded.getItems().add("Book readed 1");
        listReaded.getItems().add("Book readed 2");
        listReaded.getItems().add("Book readed 3");

    }
}
