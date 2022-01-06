package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.util.*;

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
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.*;
import java.util.stream.Collectors;

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
    private JFXListView listToRead, listPublished;

    @FXML
    private JFXListView listReaded;

    @FXML
    private Button editButtonAuthor,addButtonBook;

    @FXML
    private CheckBox follow;

    private String nickname;

    private Session session = Session.getInstance();

    private UserManager userManager = new UserManager();

    @FXML
    public void addButtonBookFuncton(ActionEvent event) throws IOException{
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/addBook.fxml"));
        Stage actual_stage = (Stage) addButtonBook.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
    @FXML
    public void addfollow(ActionEvent event) throws IOException {


        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().setFollow(usernameAuthor.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);

                userManager.following(session.getLoggedAuthor().getNickname(),true, usernameAuthor.getText() , true);

            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().setFollow(usernameAuthor.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                userManager.following(session.getLoggedUser().getNickname(),false, usernameAuthor.getText() , true);
            }
        } else {
            if (session.getLoggedAuthor() != null) {
                userManager.deleteFollowing(session.getLoggedAuthor().getNickname(),true, usernameAuthor.getText() , true);
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(usernameAuthor.getText())) {
                        session.getLoggedAuthor().getInteractions().getFollow().remove(i);
                        session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);
                    }
                }
            } else if (session.getLoggedUser() != null) {
                userManager.deleteFollowing(session.getLoggedUser().getNickname(),false, usernameAuthor.getText() , true);
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
        if (session.getLoggedAuthor()!=null && usernameAuthor.getText().equals(session.getLoggedAuthor().getNickname())){
            addButtonBook.setVisible(true);
        }
        else
        {
            addButtonBook.setVisible(false);
        }


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

        ObservableList<String> listFollows = FXCollections.observableArrayList();
        List<String> Follow;
        if ((session.getLoggedAuthor() != null) && (session.getLoggedAuthor().getNickname().equals(usernameAuthor.getText()))) {
            session.getLoggedAuthor().getInteractions().delFollow();
            Follow = userManager.loadRelations("Author", usernameAuthor.getText());
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
            Follow = userManager.loadRelations("Author", usernameAuthor.getText());
            author.getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                author.getInteractions().setFollow(Follow.get(i));
            }

            for (int i = 0; i < author.getInteractions().getNumberFollow(); i++)
                listFollows.add(author.getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        }
        followCount.setText(String.valueOf(Follow.size()));
        listFollows.clear();
        //    System.out.println(Follow.size()+ " "+session.getLoggedAuthor().getInteractions().getFollow()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }

    @FXML
    void viewFollower() {
        listFollower.getItems().clear();

        ObservableList<String> listFollowers = FXCollections.observableArrayList();
        List<String> Follower;
        if ((session.getLoggedAuthor() != null) && (session.getLoggedAuthor().getNickname().equals(usernameAuthor.getText()))) {
            session.getLoggedAuthor().getInteractions().delFollower();
            Follower = userManager.loadRelationsFollower("Author", usernameAuthor.getText());
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
            Follower = userManager.loadRelationsFollower("Author", usernameAuthor.getText());
            author.getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                author.getInteractions().setFollower(Follower.get(i));
            }

            for (int i = 0; i < author.getInteractions().getFollower().size(); i++)
                listFollowers.add(author.getInteractions().getFollower().get(i));
            listFollower.getItems().addAll(listFollowers);
        }
        followersCount.setText(String.valueOf(Follower.size()));
        listFollowers.clear();
        //  System.out.println(Follower.size()+ " "+session.getLoggedAuthor().getInteractions().getFollower()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }
    public void publishedFunction(){
        ObservableList<String> obsUserList = FXCollections.observableArrayList();
        obsUserList.addAll(userManager.searchBooksAuthor(usernameAuthor.getText()));
        ObservableList<String> statistic = FXCollections.observableArrayList();
        statistic.addAll(userManager.searchStatisticBooks(usernameAuthor.getText()));
        if (session.getLoggedAuthor()!=null && usernameAuthor.getText().equals(session.getLoggedAuthor().getNickname())) {
            for (int i = 0; i < obsUserList.size(); i++)
                session.getLoggedAuthor().setWrittenBook(obsUserList.get(i));
            for (int i = 0; i < statistic.size(); i++)
                session.getLoggedAuthor().setWrittenBookStatistic(statistic.get(i));
        }
        listPublished.getItems().addAll(obsUserList);
        String gener;
        Integer perc;
       for (int i=0;i<statistic.size();i++) {
           String[] split = statistic.get(i).split(":");
           gener = split[0];
           perc = Integer.valueOf(split[1]) * 100 /obsUserList.size();
           System.out.println(gener +" "+ perc);
       }




//        System.out.println(session.getLoggedAuthor().getWrittenBookStatisitc());
    }


    public void initialize() {
        follow.setVisible(false);
                //TODO chiedere a salvo perche ha levato queste cose
//        Session session = Session.getInstance();
//        Random rand = new Random();

        if (session.getLoggedAuthor() != null) {
            usernameAuthor.setText(session.getLoggedAuthor().getNickname());
        } /*else if (session.getLoggedUser() != null) {
            usernameAuthor.setText(session.getLoggedUser().getNickname());
        }*/
        viewFollow();
        viewFollower();
        System.out.println(usernameAuthor.getText());


        listToRead.getItems().add("Book to read 1");
        listToRead.getItems().add("Book to read 2");
        listToRead.getItems().add("Book to read 3");

        listReaded.getItems().add("Book readed 1");
        listReaded.getItems().add("Book readed 2");
        listReaded.getItems().add("Book readed 3");

    }

}
