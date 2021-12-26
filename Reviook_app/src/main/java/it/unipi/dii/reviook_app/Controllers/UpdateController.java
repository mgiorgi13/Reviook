package it.unipi.dii.reviook_app.Controllers;

import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateController {

    @FXML private Text actiontarget;

    @FXML private Button deleteButton, negateDelete, acceptDelete, updateButton;

    @FXML private TextField nameUpdate;

    @FXML private TextField surnameUpdate;

    @FXML private TextField emailUpdate;

    @FXML private TextField usernameUpdate;

    @FXML private TextField passwordUpdate;

    @FXML private TextField repeatPasswordUpdate;
    @FXML
    void updateAccountButton(ActionEvent event) throws IOException {
        actiontarget.setText("account updated");
    }
    @FXML
    void deleteAccountButton(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/securityDelete.fxml"));
        Stage actual_stage = (Stage) deleteButton.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.setTitle("Are you sure you want to delete your account?");
        actual_stage.show();
    }

    String str = "[{\"type\":\"author\",\"name\":\"Mattia\",\"surname\":\"Di Donato\",\"username\":\"Mattiax\",\"email\":\"mattia@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Salvo\",\"surname\":\"Arancio Febbo\",\"username\":\"Salvox\",\"email\":\"salvo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}," +
            "{\"type\":\"user\",\"name\":\"Matteo\",\"surname\":\"Giorgi\",\"username\":\"Matteox\",\"email\":\"matteo@unipi.it\",\"password\":\"2C87C8312E5F752A0E79660511567505\"}]";



    public boolean verifyUsername(String nickname) throws JSONException {
        //query a mongo se l'user esiste o meno
        // TODO verificare username db con quello dato dall'utente per l'update
        Session session = Session.getInstance();
        JSONArray array = new JSONArray(str);
        for(int i=0; i < array.length(); i++)
        {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("username").equals(nickname) && !session.getLogged().getNickname().equals(nickname))
                return true;
        }
        return false;
    }



    public boolean verifyEmail(String email) throws JSONException {
        // TODO verificare email db con quella data dall'utente per l'update
        Session session = Session.getInstance();
        JSONArray array = new JSONArray(str);
        for(int i=0; i < array.length(); i++)
        {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("email").equals(email) && !session.getLogged().getEmail().equals(email))
                return true;
        }
        return false;
    }


    public String SignIn(String name, String surname, String nickname,String email, String password, String repeatPsw){
        try {
            MessageDigest md;
            String pswHash;
            //convalid email
            Pattern p =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher mEmail = p.matcher(email);
            if(verifyUsername(nickname))
                return "Existing username";
            if (!mEmail.find())
                return "Invalid email";
            if(verifyEmail(email))
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
            pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Registered";
    }

    @FXML
    void updateButtonFun(ActionEvent event) throws IOException{
        String Name, surname,nickname,email, password,repeatPsw;
        Name = nameUpdate.getText();
        surname = surnameUpdate.getText();
        email = emailUpdate.getText();
        nickname = usernameUpdate.getText();
        password = passwordUpdate.getText();
        repeatPsw = repeatPasswordUpdate.getText();
        Session session = Session.getInstance();
        if (nameUpdate.getText().isEmpty())
            Name = session.getLogged().getName();
        if(surnameUpdate.getText().isEmpty())
            surname = session.getLogged().getSurname();
        if(usernameUpdate.getText().isEmpty())
            nickname =session.getLogged().getNickname();
        if(emailUpdate.getText().isEmpty())
            email = session.getLogged().getEmail();
        if(passwordUpdate.getText().isEmpty()) {
            actiontarget.setText("You must enter the new password");
            return;
        }


        String singIn= SignIn(Name,surname,nickname,email,password,repeatPsw);
        if (!singIn.equals("Registered")) {
            actiontarget.setText(singIn);
            return;
        }
        // TODO: elimina utente dal db

        // TODO: crea nuovo utente nel db
        session.setSession(null);
        session.setLogged(null);

        Parent login = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        if (!singIn.equals("Registered")) {
            actiontarget.setText(singIn);
            return;
        }
        Stage actual_stage = (Stage) updateButton.getScene().getWindow();
        actual_stage.setScene(new Scene(login));
        actual_stage.setResizable(false);
        actual_stage.setTitle("Create new account");
        actual_stage.show();



    }

    @FXML
    void acceptDeleteButton(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        session.setSession(null);
        session.setLogged(null);

        // TODO eliminare l'utente dal db
        Parent login = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) acceptDelete.getScene().getWindow();
        actual_stage.setScene(new Scene(login));
        actual_stage.setResizable(false);
        actual_stage.setTitle("Create new account");
        actual_stage.show();
    }

    @FXML
    void negateDeleteButton(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) negateDelete.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.setTitle("Are you sure you want to delete your account?");
        actual_stage.show();
    }
}
