package it.unipi.dii.reviook_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
public class UpdateController {
    @FXML private Text actiontarget;

    @FXML
    void updateAccountButton(ActionEvent event) throws IOException {
        actiontarget.setText("account updated");
    }
    @FXML
    void deleteAccountButton(ActionEvent event) throws IOException {
        Parent deleteAccount = FXMLLoader.load(getClass().getResource("securityDelete.fxml"));
        Stage stage = new Stage();
        Scene sceneSure = new Scene(deleteAccount);
        stage.setTitle("Are you sure you want to delete your account?");


        stage.setScene(sceneSure);
        stage.show();
        // Hide this current window (if this is what you want)
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    @FXML
    void acceptDeleteButton(ActionEvent event) throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();
        Scene sceneLogin = new Scene(login, 300, 275);
        stage.setTitle("Create new account");


        stage.setScene(sceneLogin);
        stage.show();
        // Hide this current window (if this is what you want)
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    @FXML
    void negateDeleteButton(ActionEvent event) throws IOException {
        Parent updateAccount = FXMLLoader.load(getClass().getResource("updateAccount.fxml"));
        Stage stage = new Stage();
        Scene sceneUpdate = new Scene(updateAccount);
        stage.setTitle("Update Account");
        stage.setScene(sceneUpdate);
        stage.show();
        // Hide this current window (if this is what you want)
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
