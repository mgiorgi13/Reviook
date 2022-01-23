package it.unipi.dii.reviook_app.controllers;

import it.unipi.dii.reviook_app.manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecurityController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text actiontarget;

    @FXML
    private Button acceptDelete;

    @FXML
    private Button negateDelete;

    private UserManager userManager = new UserManager();

    private Session session = Session.getInstance();

    @FXML
    void acceptDeleteButton(ActionEvent event) throws IOException {
        if (userManager.deleteUserMongo() && userManager.deleteUserN4J()) {
            Session session = Session.getInstance();
            session.setSession(null);
            session.setCurrentLoggedUser(null);
            session.setCurrentLoggedAuthor(null);
            Parent login = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
            Stage actual_stage = (Stage) acceptDelete.getScene().getWindow();
            actual_stage.setScene(new Scene(login));
            actual_stage.setResizable(false);
            actual_stage.setTitle("Create new account");
            actual_stage.show();
        } else {
            actiontarget.setText("Error during delete");
        }
    }

    @FXML
    void negateDeleteButton(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) negateDelete.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.setTitle("Are you sure you want to delete your account?");
        actual_stage.show();
    }
}
