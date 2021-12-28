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
    @FXML
    private Button editButtonUser;

    private String nickname;

    @FXML public void addfollow(ActionEvent event) throws IOException{

        Session session = Session.getInstance();

        if (follow.isSelected()){
            session.getLoggedUser().getInteractions().setFollow(usernameUser.getText());
            session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);

            // TODO aggiungere un follow al nostro author
            // TODO aggiungere un follower all'utente author
            // System.out.println(session.getLoggedUser().getInteractions().getFollow());
        }
        else {
            for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(usernameUser.getText())) {
                    session.getLoggedUser().getInteractions().getFollow().remove(i);
                    session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() - 1);
                }
            }
            //System.out.println(session.getLoggedUser().getInteractions().getFollow());
            //TODO rimuovere un follow al nostro author
            //TODO rimuovere un follower all'utente author
        }


    }
    public void setNickname(String nickname) {
        //TODO caricaListeLibri()

        this.nickname = nickname;
        usernameUser.setText(this.nickname);

        Session session = Session.getInstance();
        if (session.getLoggedUser().getNickname().equals(nickname) == false) {
            follow.setVisible(true);
            editButtonUser.setVisible(false);
        }
        System.out.println(session.getLoggedAuthor().getInteractions().getFollow().isEmpty());
        if (!session.getLoggedUser().getInteractions().getFollow().isEmpty()) {
            for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                System.out.println(session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname));
                if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname))
                    follow.setSelected(true);
            }
        }
    }



    public void initialize() {
        Session session = Session.getInstance();
        usernameUser.setText(session.getLoggedUser().getNickname());
        follow.setVisible(false);
        // TODO caricare da DB tutti i follow e follower , numero follow e numero followe su struttura user
    }

    @FXML
    void viewEditButtonUser(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) editButtonUser.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
}