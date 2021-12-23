package it.unipi.dii.reviook_app.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Text actiontarget;

    @FXML
    private Button loginButton;

    @FXML
    protected void registerButton(ActionEvent event) {
        actiontarget.setText("Registrato");
    }

    @FXML
    void openLoginScene(ActionEvent event) throws IOException {
        Parent login_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(login_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
}
