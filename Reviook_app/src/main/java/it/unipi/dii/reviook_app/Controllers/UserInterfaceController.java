package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Data.Author;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private Text usernameUser, followersCount, followCount;

    @FXML
    private JFXListView<String> listFollow, listFollower,listReaded,listToRead;

    @FXML
    private CheckBox follow;

    @FXML
    private Button editButtonUser;

    @FXML
    private JFXButton searchButton;

    private String nickname;

    private UserManager userManagerNJ = new UserManager();

    private Session session = Session.getInstance();

    @FXML
    public void addfollow(ActionEvent event) throws IOException {

        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().setFollow(usernameUser.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);
                userManagerNJ.following(session.getLoggedAuthor().getNickname(), true, usernameUser.getText(), false);
            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().setFollow(usernameUser.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                userManagerNJ.following(session.getLoggedUser().getNickname(), false, usernameUser.getText(), false);
            }
        } else {

            if (session.getLoggedAuthor() != null) {
                userManagerNJ.deleteFollowing(session.getLoggedAuthor().getNickname(), true, usernameUser.getText(), false);
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(usernameUser.getText())) {
                        session.getLoggedAuthor().getInteractions().getFollow().remove(i);
                        session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);
                    }
                }
            } else if (session.getLoggedUser() != null) {
                userManagerNJ.deleteFollowing(session.getLoggedUser().getNickname(), false, usernameUser.getText(), false);
                for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(usernameUser.getText())) {
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
        usernameUser.setText(this.nickname);

        viewReaded();
        viewToRead();
        viewFollow();
        viewFollower();


        if (session.getLoggedAuthor() != null) {
            if (!session.getLoggedAuthor().getNickname().equals(nickname)) {
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
            if (!session.getLoggedUser().getNickname().equals(nickname)) {
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
    void viewFollow() {
        listFollow.getItems().clear();
        ObservableList<String> listFollows = FXCollections.observableArrayList();
        List<String> Follow;
        //I'm in my user profile
        if ((session.getLoggedUser() != null) && (session.getLoggedUser().getNickname().equals(usernameUser.getText()))) {
            session.getLoggedUser().getInteractions().delFollow();
            Follow = userManagerNJ.loadRelations("User", usernameUser.getText());
            session.getLoggedUser().getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                session.getLoggedUser().getInteractions().setFollow(Follow.get(i));
            }

            for (int i = 0; i < session.getLoggedUser().getInteractions().getNumberFollow(); i++)
                listFollows.add(session.getLoggedUser().getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        } else {
            Users user = new Users("", "", usernameUser.getText(), "", "");
            user.getInteractions().delFollow();
            Follow = userManagerNJ.loadRelations("User", usernameUser.getText());
            user.getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                user.getInteractions().setFollow(Follow.get(i));
            }

            for (int i = 0; i < user.getInteractions().getNumberFollow(); i++)
                listFollows.add(user.getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        }
        followCount.setText(String.valueOf(Follow.size()));
        listFollows.clear();

        listFollow.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listFollow.getSelectionModel().getSelectedItem();
                    int result = userManagerNJ.verifyUsername(selectedCell, false);
                    if(result == -1)
                        return;else
                        try {
                            Parent userInterface;
                            FXMLLoader fxmlLoader;
                            if (result == 1) {
                                fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                                userInterface = (Parent) fxmlLoader.load();
                                AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
                                controller.setNickname(selectedCell);
                            } else {
                                fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                                userInterface = (Parent) fxmlLoader.load();
                                UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
                                controller.setNickname(selectedCell);
                            }

                            Stage actual_stage = (Stage) listFollow.getScene().getWindow();
                            actual_stage.setScene(new Scene(userInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }

    @FXML
    void viewFollower() {
        listFollower.getItems().clear();
        ObservableList<String> listFollowers = FXCollections.observableArrayList();
        List<String> Follower;
        if ((session.getLoggedUser() != null) && (session.getLoggedUser().getNickname().equals(usernameUser.getText()))) {
            session.getLoggedUser().getInteractions().delFollower();
            Follower = userManagerNJ.loadRelationsFollower("User", usernameUser.getText());
            session.getLoggedUser().getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                session.getLoggedUser().getInteractions().setFollower(Follower.get(i));
            }

            for (int i = 0; i < session.getLoggedUser().getInteractions().getNumberFollower(); i++)
                listFollowers.add(session.getLoggedUser().getInteractions().getFollower().get(i));
            listFollower.getItems().addAll(listFollowers);
        } else {
            Users users = new Users("", "", usernameUser.getText(), "", "");
            users.getInteractions().delFollower();
            Follower = userManagerNJ.loadRelationsFollower("User", usernameUser.getText());
            users.getInteractions().setNumberFollower(Follower.size());

            for (int i = 0; i < Follower.size(); i++) {
                users.getInteractions().setFollower(Follower.get(i));
            }

            for (int i = 0; i < users.getInteractions().getNumberFollower(); i++)
                listFollowers.add(users.getInteractions().getFollower().get(i));
            listFollower.getItems().addAll(listFollowers);
        }
        followersCount.setText(String.valueOf(Follower.size()));
        listFollowers.clear();
        listFollower.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listFollower.getSelectionModel().getSelectedItem();
                    int result = userManagerNJ.verifyUsername(selectedCell, false);
                    if(result == -1)
                        return;
                    try {
                        Parent userInterface;
                        FXMLLoader fxmlLoader;
                        if (result == 1) {
                            fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
                            controller.setNickname(selectedCell);
                        }
                        else {
                            fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
                            controller.setNickname(selectedCell);
                        }

                        Stage actual_stage = (Stage) listFollower.getScene().getWindow();
                        actual_stage.setScene(new Scene(userInterface));
                        actual_stage.setResizable(false);
                        actual_stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    @FXML
    void viewReaded(){
        listReaded.getItems().clear();
        List<String> toRead;
        toRead = userManagerNJ.loadRelationsBook("User", usernameUser.getText(), "readed");
        ObservableList<String> listFollowers = FXCollections.observableArrayList();
        for (int i = 0; i < toRead.size(); i++)
            listFollowers.add(toRead.get(i));
        listReaded.getItems().addAll(listFollowers);
    }
    @FXML
    void viewToRead(){
        listToRead.getItems().clear();
        List<String> toRead;
        toRead = userManagerNJ.loadRelationsBook("User", usernameUser.getText(), "toRead");
        ObservableList<String> listFollowers = FXCollections.observableArrayList();
        for (int i = 0; i < toRead.size(); i++)
            listFollowers.add(toRead.get(i));
        listToRead.getItems().addAll(listFollowers);
    }
    public void initialize() {
        follow.setVisible(false);

        if (session.getLoggedUser() != null) {
            usernameUser.setText(session.getLoggedUser().getNickname());
        }
        viewReaded();
        viewToRead();
        viewFollower();
        viewFollow();
    }
}
