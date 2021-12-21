package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AuthorInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button;

    @FXML
    void initialize() {

    }

    @FXML
    protected void changeAuthor(ActionEvent event) throws IOException{
        Parent userInterface = FXMLLoader.load(getClass().getResource("/user-interface.fxml"));
        Stage actual_stage = (Stage) button.getScene().getWindow();
        actual_stage.setScene(new Scene(userInterface));
        actual_stage.setResizable(false);
        actual_stage.show();

    }
}
