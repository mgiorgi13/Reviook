package it.unipi.dii.reviook_app.controllers;

import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import org.json.JSONException;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.IllegalFormatCodePointException;
import java.util.List;


public class LoginController {
    private User users;
    @FXML
    private Text actiontarget;

    @FXML
    private TextField usernameLogin;

    @FXML
    private TextField passwordField;

    @FXML
    private ImageView logout;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    private UserManager userManager = new UserManager();

    private Session session = Session.getInstance();

    public boolean logIn(String username, String password) throws NoSuchAlgorithmException, JSONException {
        MessageDigest md;
        String pswHash;
        int result = userManager.verifyUsername(username, true);
        if (result == -1)
            return false;
        else
            session.setIsAuthor(result == 1 ? true : false);

        //Hashing controll
        md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        Boolean c = userManager.verifyPassword(session.getIsAuthor(), username, pswHash);
        if (!userManager.verifyPassword(session.getIsAuthor(), username, pswHash))
            return false;

        return true;
    }

    String username, password;

    @FXML
    void loginButton(ActionEvent event) throws IOException, NoSuchAlgorithmException, JSONException {
        username = usernameLogin.getText();
        password = passwordField.getText();

        // TODO modificare caso in cui si è ADMIN per ora mockato
        if (username.equals("admin") && password.equals("root")) {
            Parent admin = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/admin.fxml"));
            Stage actual_stage = (Stage) loginButton.getScene().getWindow();
            actual_stage.setScene(new Scene(admin));
            actual_stage.setResizable(false);
            actual_stage.show();
            return;
        }
        if (usernameLogin.getText().isEmpty() || passwordField.getText().isEmpty()) {
            actiontarget.setText("You must fill in all fields");
            return;
        }
        if (!logIn(username, password)) {
            actiontarget.setText("Wrong Login");
            return;
        }
        Parent user_scene;
        if (session.getIsAuthor() == null) {
            return;
        }

        if (session.getIsAuthor()) {
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
        } else {
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
            List<String> Follow = userManager.loadRelations("User", username);
            session.getLoggedUser().getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                session.getLoggedUser().getInteractions().setFollower(Follow.get(i));
            }
            List<String> Follower = userManager.loadRelationsFollower("User", username);
            session.getLoggedUser().getInteractions().setNumberFollow(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                session.getLoggedUser().getInteractions().setFollower(Follower.get(i));
            }
        }

        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(user_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    public User getUsers() {
        return users;
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