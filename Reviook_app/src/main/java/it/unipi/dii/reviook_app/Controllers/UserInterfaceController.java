package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private Text usernameUser;

    @FXML
    private JFXListView<String> listFollow;

    @FXML
    private CheckBox follow;

    @FXML
    private Button editButtonUser;

    @FXML
    private JFXButton searchButton;

    private String nickname;

    @FXML
    public void addfollow(ActionEvent event) throws IOException {

        Session session = Session.getInstance();

        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().setFollow(usernameUser.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);

            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().setFollow(usernameUser.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);

            }

            // TODO aggiungere un follow al nostro author
            // TODO aggiungere un follower all'utente author
            // System.out.println(session.getLoggedUser().getInteractions().getFollow());
        } else {
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

        if (session.getLoggedAuthor() != null) {
            if (session.getLoggedAuthor().getNickname().equals(nickname) == false) {
                follow.setVisible(true);
                editButtonUser.setVisible(false);
            }
            if (!session.getLoggedAuthor().getInteractions().getFollow().isEmpty()) {
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    //System.out.println(session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname));
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(this.nickname))
                        follow.setSelected(true);
                }
            }
        } else if (session.getLoggedUser() != null) {
            if (session.getLoggedUser().getNickname().equals(nickname) == false) {
                follow.setVisible(true);
                editButtonUser.setVisible(false);
            }
            if (!session.getLoggedUser().getInteractions().getFollow().isEmpty()) {
                for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                    //System.out.println(session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname));
                    if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname))
                        follow.setSelected(true);
                }
            }
        }
        //System.out.println(session.getLoggedAuthor().getInteractions().getFollow().isEmpty());

    }

    public void initialize() {
        follow.setVisible(false);

        Session session = Session.getInstance();
        Random rand = new Random();


        if (session.getLoggedAuthor() != null) {
            usernameUser.setText(session.getLoggedAuthor().getNickname());
        } else if (session.getLoggedUser() != null) {
            usernameUser.setText(session.getLoggedUser().getNickname());
        }




    }

    @FXML
    void searchInterface(ActionEvent event) throws IOException {
        Parent searchInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/search.fxml"));
        Stage actual_stage = (Stage) searchButton.getScene().getWindow();
        actual_stage.setScene(new Scene(searchInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void viewEditButtonUser(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) editButtonUser.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void viewFollow () {
        listFollow.getItems().clear();
        Session session = Session.getInstance();
        ObservableList<String> listFollows = FXCollections.observableArrayList();
        System.out.println(session.getLoggedUser().getInteractions().getFollow());
        if (session.getLoggedAuthor() == null) {
            // if (!(getLoggedUser.getText()).equals(session.getLoggedUser().getNickname()))
            //TODO cerco nel db tutti i dati dell'utente e inserisco i follow(inserisco if su)

            for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++)
                listFollows.add(session.getLoggedUser().getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        }
        if (session.getLoggedAuthor() != null) {
            //TODO cerco nel db tutti i dati dell'autore e inserisco i follow(inserisco if su)

            // devo prednere i follow dell'utente
        }
    }
}

