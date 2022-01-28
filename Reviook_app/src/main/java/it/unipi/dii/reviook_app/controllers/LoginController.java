package it.unipi.dii.reviook_app.controllers;

import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.concurrent.TimeUnit;


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

    @FXML
    private ChoiceBox loginType;

    private UserManager userManager = new UserManager();

    private Session session = Session.getInstance();

    String username, password;


    public boolean logIn(String username, String password) throws NoSuchAlgorithmException, JSONException, InterruptedException {
        MessageDigest md;
        String pswHash;
        int result = userManager.verifyUsername(username, loginType.getSelectionModel().getSelectedItem().toString(), true);
        if (result == -1) {
            return false;
        }
        if (result != 2) {
            session.setIsAuthor(result == 1 ? true : false);
        }
        //Hashing control
        md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        Boolean c = userManager.verifyPassword(loginType.getSelectionModel().getSelectedItem().toString(), username, pswHash);
        if (!userManager.verifyPassword(loginType.getSelectionModel().getSelectedItem().toString(), username, pswHash)) {
            return false;
        }
        return true;
    }


    @FXML
    void loginButton(ActionEvent event) throws IOException, JSONException, NoSuchAlgorithmException, InterruptedException {
        Parent user_scene;
        username = usernameLogin.getText();
        password = passwordField.getText();

        if (usernameLogin.getText().isEmpty() || passwordField.getText().isEmpty() || loginType.getSelectionModel().getSelectedItem() == null) {
            actiontarget.setText("You must fill in all fields");
            return;
        }
        if (!logIn(username, password)) {
            actiontarget.setText("Wrong Login");
            return;
        }

        if (loginType.getSelectionModel().getSelectedItem().toString().equals("admin")) {
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/admin.fxml"));
        } else {
            if (session.getIsAuthor()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                user_scene = (Parent) fxmlLoader.load();
                AuthorInterfaceController authorInterfaceController = fxmlLoader.getController();
                authorInterfaceController.setAuthor(session.getLoggedAuthor());
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                user_scene = (Parent) fxmlLoader.load();
                UserInterfaceController userInterfaceController = fxmlLoader.getController();
                userInterfaceController.setUser(session.getLoggedUser());


//                List<String> Follow = userManager.loadRelations("User", username);
//                session.getLoggedUser().getInteractions().setNumberFollow(Follow.size());
//                for (int i = 0; i < Follow.size(); i++) {
//                    session.getLoggedUser().getInteractions().setFollower(Follow.get(i));
//                }
//                List<String> Follower = userManager.loadRelationsFollower("User", username);
//                session.getLoggedUser().getInteractions().setNumberFollow(Follower.size());
//                for (int i = 0; i < Follower.size(); i++) {
//                    session.getLoggedUser().getInteractions().setFollower(Follower.get(i));
//                }
            }
        }
        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(user_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
actual_stage.centerOnScreen();    }


    @FXML
    void openRegisterScene(ActionEvent event) throws IOException {
        Parent register_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/register.fxml"));
        Stage actual_stage = (Stage) registerButton.getScene().getWindow();
        actual_stage.setScene(new Scene(register_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
actual_stage.centerOnScreen();    }

    @FXML
    void initialize() {
        loginType.setItems(FXCollections.observableArrayList("user", "author", "admin"));
    }
}