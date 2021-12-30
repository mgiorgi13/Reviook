package it.unipi.dii.reviook_app.Controllers;

import it.unipi.dii.reviook_app.Data.Users;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


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


    String str = "[{\"type\":\"author\",\"name\":\"Mattia\",\"surname\":\"Di Donato\",\"username\":\"Mattiax\",\"email\":\"mattia@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Salvo\",\"surname\":\"Arancio Febbo\",\"username\":\"Salvox\",\"email\":\"salvo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Matteo\",\"surname\":\"Giorgi\",\"username\":\"Matteox\",\"email\":\"matteo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}]";

    public LoginController() {
    }

    public boolean verifyUsername(String nickname) throws JSONException {
        //query a mongo se l'user esiste o meno
        JSONArray array = new JSONArray(str);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("username").equals(nickname))
                return true;
            // System.out.println(object.getString("name"));
            //  System.out.println(object.getString("surname"));
            //  System.out.println(object.getString("email"));
        }
        return false;
    }

    public boolean verifyPassword(String password) throws JSONException {
        //query a mongo se l'user esiste o meno
        JSONArray array = new JSONArray(str);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("password").equals(password))
                return true;
            // System.out.println(object.getString("name"));
            //  System.out.println(object.getString("surname"));
            //  System.out.println(object.getString("email"));
        }
        return false;
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

    public String logIn(String username, String password) throws NoSuchAlgorithmException, JSONException {
        MessageDigest md;
        String pswHash;
        if (!verifyUsername(username))
            return "Existing username";

        //Hashing controll
        md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        if (!verifyPassword(pswHash))
            return "Wrong password";

        return verifyType(username); //return type of user(author o normal user)
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
        String login = logIn(username, password);
        actiontarget.setText(login);

        Parent user_scene;
        Session session = Session.getInstance();
        //TODO sessione settata con dati forzati, andranno recuperati tramite query al DB in base al username
        if (login.equals("author")) {
            session.setType(true);
            session.setLoggedAuthor("Mattia", "Di Donato", "Mattiax", "mattia@unipi.it", "2C87C8312E5F752A0E79660511567505");
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
            System.out.println("utente loggato: " + session.getLoggedAuthor().getNickname());
        } else if (login.equals("user")) {
            session.setType(false);
            session.setLoggedUser("Salvo", "Arancio Febbo", "Salvox", "salvo@unipi.it", "2C87C8312E5F752A0E79660511567505");
            user_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
        } else return;
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
