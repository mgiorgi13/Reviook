package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SearchInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private JFXListView resultList;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton profileButton;

    @FXML
    void initialize() {

        for (int i = 0; i < 30; i++) {
            resultList.getItems().add("Book to read " + i);
        }


    }

    @FXML
    void homeInterface(ActionEvent event) throws IOException {
        Parent homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author-interface.fxml"));
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void profileInterface(ActionEvent event) throws IOException{
        Parent userInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user-interface.fxml"));
        Stage actual_stage = (Stage) profileButton.getScene().getWindow();
        actual_stage.setScene(new Scene(userInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
}
