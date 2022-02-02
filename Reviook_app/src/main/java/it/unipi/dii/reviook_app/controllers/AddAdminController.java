package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unipi.dii.reviook_app.manager.AdminManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.bind.DatatypeConverter;

public class AddAdminController {

    @FXML
    private JFXButton addAdminButton;

    @FXML
    private Text actionTarget;

    @FXML
    private JFXButton loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;


    AdminManager adminManager = new AdminManager();

    @FXML
    void loginAction() throws IOException {
        Parent loginInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(loginInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();actual_stage.centerOnScreen();    }

    @FXML
    void addAdminAction(ActionEvent event) throws NoSuchAlgorithmException, IOException {
        actionTarget.setText("");
        if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
            return;
        }
        //Password Hash
        MessageDigest md = MessageDigest.getInstance("MD5");
        //md.update(signUpPassword.getText().getBytes());
        md.update(passwordField.getText().getBytes());
        byte[] digest = md.digest();
        if(adminManager.addNewAdmin(usernameField.getText(), DatatypeConverter.printHexBinary(digest).toUpperCase()))
            loginAction();
        else
            actionTarget.setText("Error: can't add new admin");
    }
}
