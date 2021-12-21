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
    @FXML
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

            Stage stage = new Stage();;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/unipi/dii/reviook_app/fxml/register.fxml"));
            Pane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
    }
}
