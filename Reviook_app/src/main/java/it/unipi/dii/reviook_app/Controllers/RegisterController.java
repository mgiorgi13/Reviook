package it.unipi.dii.reviook_app.Controllers;

import it.unipi.dii.reviook_app.Data.Author;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Manager.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;


public class RegisterController {
    @FXML
    private Text actiontarget;

    @FXML
    private CheckBox CheckAuthor;

    @FXML
    private TextField singInName;

    @FXML
    private TextField singInSurname;

    @FXML
    private TextField singInEmail;

    @FXML
    private TextField singInUName;

    @FXML
    private TextField singInPassword;

    @FXML
    private TextField singInRepeatPsw;

    @FXML
    private Button loginButton;

    private UserManager userManager = new UserManager();

    // Create a new javax.json.JSONObject instance.
    String str = "[{\"type\":\"author\",\"name\":\"Mattia\",\"surname\":\"Di Donato\",\"username\":\"Mattiax\",\"email\":\"mattia@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Salvo\",\"surname\":\"Arancio Febbo\",\"username\":\"Salvox\",\"email\":\"salvo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Matteo\",\"surname\":\"Giorgi\",\"username\":\"Matteox\",\"email\":\"matteo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}]";


    public String SignIn(String name, String surname, String email, String nickname, String password, String repeatPsw) {
        try {
            MessageDigest md;
            String pswHash;
            //convalid email
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher mEmail = p.matcher(email);
            if (userManager.verifyUsername(nickname, true) != -1)
                return "Existing username";
            if (!mEmail.find())
                return "Invalid email";
            if (!userManager.verifyEmail(email))
                return "Existing e-mail";
            if (!password.equals(repeatPsw))
                return "Passwords must be the same";
            //check if the password entered is at least 8
            //and maximum 20 characters long and contains at least one letter and at least one number:
            Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-zA-Z]).{8,20})");
            Matcher mpsw = pattern.matcher(password);
            if (!mpsw.find())
                return "Password entered is at least 8 and maximum 20 characters long and contains at least one letter and at least one number";

            //Password Hash
            md = MessageDigest.getInstance("MD5");
            //md.update(signUpPassword.getText().getBytes());
            md.update(password.getBytes());
            byte[] digest = md.digest();
            this.password = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Registered";
    }

    Scanner myObj = new Scanner(System.in);
    String Name, surname, nickname, email, password, repeatPsw;

    @FXML
    protected void registerButton(ActionEvent event) throws IOException, InterruptedException {
        Name = singInName.getText();
        surname = singInSurname.getText();
        email = singInEmail.getText();
        nickname = singInUName.getText();
        password = singInPassword.getText();
        repeatPsw = singInRepeatPsw.getText();
        if (singInName.getText().isEmpty() || singInSurname.getText().isEmpty() || singInEmail.getText().isEmpty() || singInUName.getText().isEmpty() || singInPassword.getText().isEmpty() || singInRepeatPsw.getText().isEmpty()) {
            actiontarget.setText("You must fill in all fields");
            return;
        }

        String concat = nickname + email;
        String id = UUID.nameUUIDFromBytes(concat.getBytes()).toString();
        String singIn = SignIn(Name, surname, email, nickname, password, repeatPsw);
        if (!singIn.equals("Registered")) {
            actiontarget.setText(singIn);
            return;
        }
        if (CheckAuthor.isSelected()) {
            userManager.addNewUsers("Author", nickname, id);
            userManager.register(Name, surname, email, nickname, password, "Author",id);
        } else {
            userManager.addNewUsers("User", nickname,id);
            userManager.register(Name, surname, email, nickname, password, "User",id);
        }
        actiontarget.setText(singIn);
        Thread.sleep(1000);
        Parent login_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(login_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }


    @FXML
    void openLoginScene(ActionEvent event) throws IOException {
        Parent login_scene = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) loginButton.getScene().getWindow();
        actual_stage.setScene(new Scene(login_scene));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
}
