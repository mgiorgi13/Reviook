package it.unipi.dii.reviook_app.Controllers;

import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LoginController {
    private Users users;
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

    String str = "[{\"type\":\"author\",\"name\":\"Mattia\",\"surname\":\"Di Donato\",\"username\":\"Mattiax\",\"email\":\"mattia@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Salvo\",\"surname\":\"Arancio Febbo\",\"username\":\"Salvox\",\"email\":\"salvo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Matteo\",\"surname\":\"Giorgi\",\"username\":\"Matteox\",\"email\":\"matteo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}]";

    public LoginController() {
    }


    public String verifyType(String username) throws JSONException {
        JSONArray array = new JSONArray(str);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("username").equals(username))
                return object.getString("type");
            // System.out.println(object.getString("name"));
            //  System.out.println(object.getString("surname"));
            //  System.out.println(object.getString("email"));
        }
        return null;
    }

    public boolean logIn(String username, String password) throws NoSuchAlgorithmException, JSONException {
        MessageDigest md;
        String pswHash;
        int result = userManager.verifyUsername(username);
        if(result == -1)
            return false;
        else
            session.setIsAuthor(result == 1 ? true : false);

        //Hashing controll
        md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        if (!userManager.verifyPassword(session.getIsAuthor(),username,pswHash))
            return false;

        return true;
    }

    String username, password;

    @FXML
    void loginButton(ActionEvent event) throws IOException, NoSuchAlgorithmException, JSONException {
        username = usernameLogin.getText();
        password = passwordField.getText();
        if (usernameLogin.getText().isEmpty() || passwordField.getText().isEmpty()) {
            actiontarget.setText("You must fill in all fields");
            return;
        }

        actiontarget.setText(logIn(username, password) ? "Connected" : "Wrong Login");

        Parent user_scene;

        if (session.getIsAuthor() == null) {
            return;
        }

        if (session.getIsAuthor()) {
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
        }else{
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
            List<String> Follow = userManager.loadRelations("User",username);
            session.getLoggedUser().getInteractions().setNumberFollow(Follow.size());
            for(int i = 0; i<Follow.size(); i++)
            {
                session.getLoggedUser().getInteractions().setFollower(Follow.get(i));
            }
            List<String> Follower= userManager.loadRelationsFollower("User",username);
            session.getLoggedUser().getInteractions().setNumberFollow(Follower.size());
            for(int i = 0; i<Follower.size(); i++)
            {
                session.getLoggedUser().getInteractions().setFollower(Follower.get(i));
            }
        }

        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(user_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    public Users getUsers() {
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