package it.unipi.dii.reviook_app.controllers;

import it.unipi.dii.reviook_app.manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateController {

    @FXML
    private Text actiontarget;

    @FXML
    private Button deleteButton, updateButton, homeButton;

    @FXML
    private Text nameUpdate;

    @FXML
    private Text emailUpdate;

    @FXML
    private Text usernameUpdate;

    @FXML
    private TextField passwordUpdate;

    @FXML
    private TextField repeatPasswordUpdate;

    private UserManager userManager = new UserManager();

    private Session session = Session.getInstance();


    @FXML
    void deleteAccountButton(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/securityDelete.fxml"));
        Stage actual_stage = (Stage) deleteButton.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.setTitle("Are you sure you want to delete your account?");
        actual_stage.show();
    }

    @FXML
    void homeAction(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;
        if (session.getIsAuthor())
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
        else
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void updateButtonFun(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        String password, repeatPsw;
        password = passwordUpdate.getText();
        repeatPsw = repeatPasswordUpdate.getText();
        Session session = Session.getInstance();

        if (passwordUpdate.getText().isEmpty()) {
            actiontarget.setText("You must enter the new password");
            return;
        }
        if (!password.equals(repeatPsw)) {
            actiontarget.setText("Passwords must be the same");
            return;
        }

        // check if the password entered is at least 8
        //and maximum 20 characters long and contains at least one letter and at least one number:
        MessageDigest md;
        String pswHash;
        Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-zA-Z]).{8,20})");
        Matcher mpsw = pattern.matcher(password);
        if (!mpsw.find())
            actiontarget.setText("Password entered is at least 8 and maximum 20 characters long and contains at least one letter and at least one number");

        //Password Hash
        md = MessageDigest.getInstance("MD5");
        //md.update(signUpPassword.getText().getBytes());
        md.update(password.getBytes());
        byte[] digest = md.digest();
        pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        if (userManager.updatePassword(pswHash)) {
            //update ok so go to login
            session.setSession(null);
            session.setCurrentLoggedUser(null);
            session.setCurrentLoggedAuthor(null);

            Parent login = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));

            Stage actual_stage = (Stage) updateButton.getScene().getWindow();
            actual_stage.setScene(new Scene(login));
            actual_stage.setResizable(false);
            actual_stage.setTitle("Login");
            actual_stage.show();
        } else {
            actiontarget.setText("update failed");
        }

    }

    @FXML
    public void initialize() {
        if (session.getIsAuthor()) {
            nameUpdate.setText(session.getLoggedAuthor().getName());
            emailUpdate.setText(session.getLoggedAuthor().getEmail());
            usernameUpdate.setText(session.getLoggedAuthor().getNickname());
        } else {
            nameUpdate.setText(session.getLoggedUser().getName());
            emailUpdate.setText(session.getLoggedUser().getEmail());
            usernameUpdate.setText(session.getLoggedUser().getNickname());
        }
    }
}
