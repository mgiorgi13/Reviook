package it.unipi.dii.reviook_app.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML private Text actiontarget;

    @FXML protected void registerButton(ActionEvent event) {
        actiontarget.setText("Registrato");
    }

    @FXML
    void openLoginScene(ActionEvent event) throws IOException {

        Parent login = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Stage stage = new Stage();
        Scene sceneLogin = new Scene(login, 300, 275);
        stage.setTitle("Login");
        stage.setScene(sceneLogin);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }
}
