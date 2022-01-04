package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Data.Author;
import it.unipi.dii.reviook_app.Manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AuthorInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private Text usernameAuthor;

    @FXML
    private JFXButton button;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Text followCount;

    @FXML
    private Text followersCount;

    @FXML
    private Text likesCount;

    @FXML
    private JFXListView<String> listFollow, listFollower;

    @FXML
    private JFXListView listToRead;

    @FXML
    private JFXListView listReaded;

    @FXML
    private Button editButtonAuthor;

    @FXML
    private CheckBox follow;

    private String nickname;

    private UserManager userManagerNJ = new UserManager();

    @FXML
    public void addfollow(ActionEvent event) throws IOException {
        Session session = Session.getInstance();

        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().setFollow(usernameAuthor.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);
                userManagerNJ.following(session.getLoggedAuthor().getNickname(), true, usernameAuthor.getText(), true);

            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().setFollow(usernameAuthor.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                userManagerNJ.following(session.getLoggedUser().getNickname(), false, usernameAuthor.getText(), true);
            }
        } else {
            if (session.getLoggedAuthor() != null) {
                userManagerNJ.deleteFollowing(session.getLoggedAuthor().getNickname(), true, usernameAuthor.getText(), true);
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(usernameAuthor.getText())) {
                        session.getLoggedAuthor().getInteractions().getFollow().remove(i);
                        session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);
                    }
                }
            } else if (session.getLoggedUser() != null) {
                userManagerNJ.deleteFollowing(session.getLoggedUser().getNickname(), false, usernameAuthor.getText(), true);
                for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(usernameAuthor.getText())) {
                        session.getLoggedUser().getInteractions().getFollow().remove(i);
                        session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() - 1);
                    }
                }
            }
        }
    }

    public void setNickname(String nickname) {
        //TODO caricaListeLibri()

        this.nickname = nickname;
        usernameAuthor.setText(this.nickname);

        Session session = Session.getInstance();

        if (session.getLoggedAuthor() != null) {
            if (session.getLoggedAuthor().getNickname().equals(nickname) == false) {
                follow.setVisible(true);
                editButtonAuthor.setVisible(false);
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
                editButtonAuthor.setVisible(false);
            }
            if (!session.getLoggedUser().getInteractions().getFollow().isEmpty()) {
                for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                    // System.out.println(session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname));
                    if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname))
                        follow.setSelected(true);
                }
            }
        }
    }

    @FXML
    void viewEditButtonAuthor(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) editButtonAuthor.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
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
    void viewFollow() {
        listFollow.getItems().clear();
        Session session = Session.getInstance();
        ObservableList<String> listFollows = FXCollections.observableArrayList();
        List<String> Follow;
        if ((session.getLoggedAuthor() != null) && (session.getLoggedAuthor().getNickname().equals(usernameAuthor.getText()))) {
            session.getLoggedAuthor().getInteractions().delFollow();
            Follow = userManagerNJ.loadRelations("Author", usernameAuthor.getText());
            session.getLoggedAuthor().getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                session.getLoggedAuthor().getInteractions().setFollow(Follow.get(i));
            }

            for (int i = 0; i < session.getLoggedAuthor().getInteractions().getNumberFollow(); i++)
                listFollows.add(session.getLoggedAuthor().getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        } else {
            Author author = new Author("", "", usernameAuthor.getText(), "", "");
            author.getInteractions().delFollow();
            Follow = userManagerNJ.loadRelations("Author", usernameAuthor.getText());
            author.getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                author.getInteractions().setFollow(Follow.get(i));
            }

            for (int i = 0; i < author.getInteractions().getNumberFollow(); i++)
                listFollows.add(author.getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        }
        listFollows.clear();
        //    System.out.println(Follow.size()+ " "+session.getLoggedAuthor().getInteractions().getFollow()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }

    @FXML
    void viewFollower() {
        listFollower.getItems().clear();
        Session session = Session.getInstance();
        ObservableList<String> listFollowers = FXCollections.observableArrayList();
        List<String> Follower;
        if ((session.getLoggedAuthor() != null) && (session.getLoggedAuthor().getNickname().equals(usernameAuthor.getText()))) {
            session.getLoggedAuthor().getInteractions().delFollower();
            Follower = userManagerNJ.loadRelationsFollower("Author", usernameAuthor.getText());
            session.getLoggedAuthor().getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                session.getLoggedAuthor().getInteractions().setFollower(Follower.get(i));
            }

            for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollower().size(); i++)
                listFollowers.add(session.getLoggedAuthor().getInteractions().getFollower().get(i));
            listFollower.getItems().addAll(listFollowers);
        } else {
            Author author = new Author("", "", usernameAuthor.getText(), "", "");
            author.getInteractions().delFollower();
            Follower = userManagerNJ.loadRelationsFollower("Author", usernameAuthor.getText());
            author.getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                author.getInteractions().setFollower(Follower.get(i));
            }

            for (int i = 0; i < author.getInteractions().getFollower().size(); i++)
                listFollowers.add(author.getInteractions().getFollower().get(i));
            listFollower.getItems().addAll(listFollowers);
        }
        listFollowers.clear();
        //  System.out.println(Follower.size()+ " "+session.getLoggedAuthor().getInteractions().getFollower()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }

    public void initialize() {
        follow.setVisible(false);
        Session session = Session.getInstance();
        Random rand = new Random();
        if (session.getLoggedAuthor() != null) {
            usernameAuthor.setText(session.getLoggedAuthor().getNickname());
        } /*else if (session.getLoggedUser() != null) {
            usernameAuthor.setText(session.getLoggedUser().getNickname());
        }*/

        likesCount.setText(String.valueOf(rand.nextInt(9999)));
        followCount.setText(String.valueOf(rand.nextInt(9999)));
        followersCount.setText(String.valueOf(rand.nextInt(9999)));

        listToRead.getItems().add("Book to read 1");
        listToRead.getItems().add("Book to read 2");
        listToRead.getItems().add("Book to read 3");

        listReaded.getItems().add("Book readed 1");
        listReaded.getItems().add("Book readed 2");
        listReaded.getItems().add("Book readed 3");

    }

}
