package it.unipi.dii.reviook_app.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;


public class LoginController {
    @FXML
    private Text actiontarget;

    @FXML
    private ImageView logout;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    void loginButton(ActionEvent event) throws IOException {
        Parent user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author-interface.fxml"));
        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(user_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void openRegisterScene(ActionEvent event) throws IOException {
        Parent register_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/register.fxml"));
        Stage actual_stage = (Stage) registerButton.getScene().getWindow();
        actual_stage.setScene(new Scene(register_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
}
