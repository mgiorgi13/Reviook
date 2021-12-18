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


public class LoginController {
    @FXML private Text actiontarget;

    @FXML
    protected void loginButton(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
    @FXML protected void openRegisterScene(ActionEvent event)  throws IOException {

        Parent register = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage = new Stage();
        Scene sceneRegister = new Scene(register);
        stage.setTitle("Register");
        stage.setScene(sceneRegister);
        stage.show();
        // Hide this current window (if this is what you want)
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
