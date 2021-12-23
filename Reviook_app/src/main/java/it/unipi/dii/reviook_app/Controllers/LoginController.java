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
    @FXML private Text actiontarget;

    @FXML Button signInButton, ID_loginButton;

    private ImageView logout;
    @FXML
    protected void loginButton(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }

    @FXML protected void openRegisterScene(ActionEvent event)  throws IOException {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        try {

           ;


            Parent register = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/register.fxml"));
            Scene sceneLogin = new Scene(register);
            Stage stage = (Stage) signInButton.getScene().getWindow();
            System.out.print("mbareeee");
            stage.setTitle("Login");
            stage.setScene(sceneLogin);
            stage.setResizable(false);
            stage.show();



        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
    }
}
