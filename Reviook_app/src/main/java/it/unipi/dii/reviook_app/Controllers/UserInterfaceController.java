package it.unipi.dii.reviook_app.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import it.unipi.dii.reviook_app.Session;
import javafx.scene.control.Button;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterfaceController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private Text usernameUser;
    @FXML
    private JFXButton button;
    @FXML
    private CheckBox follow;
    @FXML
    private URL location;

    public UserInterfaceController() {
    }


    @FXML
    private Button editButtonUser;
    public void initialize() {
        Session session = Session.getInstance();
        usernameUser.setText(session.getLoggedUser().getNickname());
        follow.setVisible(false);
    }
    @FXML
    void viewEditButtonUser(ActionEvent event) throws IOException{
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) editButtonUser.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }




}
