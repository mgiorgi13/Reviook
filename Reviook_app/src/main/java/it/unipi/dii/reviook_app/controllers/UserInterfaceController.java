package it.unipi.dii.reviook_app.controllers;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Genre;
import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.SearchManager;
import it.unipi.dii.reviook_app.manager.UserManager;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private Text usernameUser, followersCount, followCount;

    @FXML
    private JFXListView<String> listFollow, listFollower, listRead, listToRead;

    @FXML
    private CheckBox follow;

    @FXML
    private Button editButtonUser;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Text reviewCatValue1, reviewCatText1, reviewCatValue2, reviewCatText2, reviewCatValue3, reviewCatText3, reviewCatValue4, reviewCatText4, reviewCatValue5, reviewCatText5;

    @FXML
    private HBox Stat1, Stat2, Stat3, Stat4;

    @FXML
    private HBox HBAuthor1,HBAuthor2,HBAuthor3,HBAuthor4,HBUser1,HBUser2,HBUser3,HBUser4;

    @FXML
    private Text suggestedAuthor1, suggestedAuthor2, suggestedAuthor3, suggestedAuthor4;

    @FXML
    private Text suggestedUser1, suggestedUser2, suggestedUser3, suggestedUser4;

    @FXML
    private Button rankingButton;

    private ArrayList<Genre> analytics;

    private String nickname;

    private UserManager userManager = new UserManager();
    private SearchManager searchManager = new SearchManager();

    private Session session = Session.getInstance();

    @FXML
    public void addFollow(ActionEvent event) throws IOException {
        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().setFollow(usernameUser.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);
                userManager.following(session.getLoggedAuthor().getNickname(), "Author", usernameUser.getText(), "User");
            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().setFollow(usernameUser.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                userManager.following(session.getLoggedUser().getNickname(), "User", usernameUser.getText(), "User");
            }
        } else {

            if (session.getLoggedAuthor() != null) {
                userManager.deleteFollowing(session.getLoggedAuthor().getNickname(), "Author", usernameUser.getText(), "User");
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(usernameUser.getText())) {
                        session.getLoggedAuthor().getInteractions().getFollow().remove(i);
                        session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);
                    }
                }
            } else if (session.getLoggedUser() != null) {
                userManager.deleteFollowing(session.getLoggedUser().getNickname(), "User", usernameUser.getText(), "User");
                for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(usernameUser.getText())) {
                        session.getLoggedUser().getInteractions().getFollow().remove(i);
                        session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() - 1);
                    }
                }
            }
        }
    }

    private void viewReviewAnalytic() {
        Double previousValue = -1.0;
        String newGenre = "";
        ArrayList<Genre> genresReformatted = new ArrayList<>();

        analytics = session.getCache().getAnalyticsExecuted().get(nickname + "user");
        if (analytics == null) {
            //load analytics from db
            analytics = userManager.averageRatingCategoryUser(nickname);
            session.getCache().getAnalyticsExecuted().put(nickname + "user", analytics);
        }
        System.out.println(session.getCache().getAnalyticsExecuted().get(nickname + "user"));

        for (int i = 0; i < analytics.size(); i++) {
            if (previousValue == -1.0 || analytics.get(i).getValue().equals(previousValue)) {
                newGenre = newGenre.concat(analytics.get(i).getType() + "\n");
            } else {
                genresReformatted.add(new Genre(newGenre, previousValue));
                newGenre = "";
                newGenre = newGenre.concat(analytics.get(i).getType() + "\n");
            }
            previousValue = analytics.get(i).getValue();
            if (i == analytics.size() - 1) {
                genresReformatted.add(new Genre(newGenre, previousValue));
            }
        }

        int size = genresReformatted.size();

        Stat1.setVisible(false);
        Stat2.setVisible(false);
        Stat3.setVisible(false);
        Stat4.setVisible(false);

        if (size >= 1) {
            Stat1.setVisible(true);
            reviewCatText1.setText(genresReformatted.get(0).getType());
            reviewCatValue1.setText(genresReformatted.get(0).getValue().toString());
        }
        if (size >= 2) {
            Stat2.setVisible(true);
            reviewCatText2.setText(genresReformatted.get(1).getType());
            reviewCatValue2.setText(genresReformatted.get(1).getValue().toString());
        }
        if (size >= 3) {
            Stat3.setVisible(true);
            reviewCatText3.setText(genresReformatted.get(2).getType());
            reviewCatValue3.setText(genresReformatted.get(2).getValue().toString());
        }
        if (size >= 4) {
            Stat4.setVisible(true);
            reviewCatText4.setText(genresReformatted.get(3).getType());
            reviewCatValue4.setText(genresReformatted.get(3).getValue().toString());
        }
    }

    private void viewSuggestedAuthors() {
        ArrayList<Author> suggestedAuthors = userManager.similarAuthors(nickname,"User");
        Collections.shuffle(suggestedAuthors);

        HBAuthor1.setVisible(false);
        HBAuthor2.setVisible(false);
        HBAuthor3.setVisible(false);
        HBAuthor4.setVisible(false);

        int size = suggestedAuthors.size();

        if(size >= 1){
            HBAuthor1.setVisible(true);
            suggestedAuthor1.setText(suggestedAuthors.get(0).getNickname().substring(0,20));
        }
        if(size >= 2){
            HBAuthor2.setVisible(true);
            suggestedAuthor2.setText(suggestedAuthors.get(1).getNickname().substring(0,20));
        }
        if(size >= 3){
            HBAuthor3.setVisible(true);
            suggestedAuthor3.setText(suggestedAuthors.get(2).getNickname().substring(0,20));
        }
        if(size >= 4){
            HBAuthor4.setVisible(true);
            suggestedAuthor4.setText((suggestedAuthors.get(3).getNickname()));
        }

    }

    private void viewSuggestedUsers() {
        ArrayList<User> suggestedUsers = userManager.similarUsers(nickname,"User");
        Collections.shuffle(suggestedUsers);

        HBUser1.setVisible(false);
        HBUser2.setVisible(false);
        HBUser3.setVisible(false);
        HBUser4.setVisible(false);

        int size = suggestedUsers.size();

        if(size >= 1){
            HBUser1.setVisible(true);
            suggestedUser1.setText(suggestedUsers.get(0).getNickname().substring(0,20));
        }
        if(size >= 2){
            HBUser2.setVisible(true);
            suggestedUser2.setText(suggestedUsers.get(1).getNickname().substring(0,20));
        }
        if(size >= 3){
            HBUser3.setVisible(true);
            suggestedUser3.setText(suggestedUsers.get(2).getNickname().substring(0,20));
        }
        if(size >= 4){
            HBUser4.setVisible(true);
            suggestedUser4.setText(suggestedUsers.get(3).getNickname().substring(0,20));
        }

    }

    public void setNickname(String nickname) {
        //TODO caricaListeLibri()
        this.nickname = nickname;
        usernameUser.setText(this.nickname);

        //load analytics
        viewReviewAnalytic();
        viewSuggestedAuthors();
        viewSuggestedUsers();

        //carico le info dello user
        viewFollow();
        viewFollower();
        rankingButton.setVisible(true);
        if (session.getLoggedAuthor() != null) {
            rankingButton.setVisible(false);
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
                rankingButton.setVisible(false);
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
            Follow = userManager.loadRelations("User", usernameUser.getText());
            session.getLoggedUser().getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                session.getLoggedUser().getInteractions().setFollow(Follow.get(i));
            }
            for (int i = 0; i < session.getLoggedUser().getInteractions().getNumberFollow(); i++)
                listFollows.add(session.getLoggedUser().getInteractions().getFollow().get(i));
            listFollow.getItems().addAll(listFollows);
        } else {
            User user = new User("", "", "", usernameUser.getText(), "", "", null, 0);
            user.getInteractions().delFollow();
            Follow = userManager.loadRelations("User", usernameUser.getText());
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
                    int result = userManager.verifyUsername(selectedCell, false);
                    if (result == -1)
                        return;
                    else
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
            Follower = userManager.loadRelationsFollower("User", usernameUser.getText());
            session.getLoggedUser().getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                session.getLoggedUser().getInteractions().setFollower(Follower.get(i));
            }

            for (int i = 0; i < session.getLoggedUser().getInteractions().getNumberFollower(); i++)
                listFollowers.add(session.getLoggedUser().getInteractions().getFollower().get(i));
            listFollower.getItems().addAll(listFollowers);
        } else {
            User users = new User("", "", "", usernameUser.getText(), "", "", null, 0);
            users.getInteractions().delFollower();
            Follower = userManager.loadRelationsFollower("User", usernameUser.getText());
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
                    int result = userManager.verifyUsername(selectedCell, false);
                    if (result == -1)
                        return;
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
    void viewRead() {
        if (session.getLoggedAuthor() != null)
            session.getLoggedAuthor().getBooks().listBooksClear();
        else
            session.getLoggedUser().getBooks().listBooksClear();
        String test = usernameUser.getText();
        listRead.getItems().clear();
        ArrayList<Book> read;

        read = userManager.loadRelationsBook("User", usernameUser.getText(), "READ");
        System.out.println(read);
        ObservableList<String> ListRead = FXCollections.observableArrayList();
        for (Book book : read) {
            if (session.getLoggedAuthor() != null)
                ListRead.add(session.getLoggedAuthor().getBooks().setRead(book.getTitle(), book.getBook_id()));
            else
                ListRead.add(session.getLoggedUser().getBooks().setRead(book.getTitle(), book.getBook_id()));
        }
        listRead.getItems().addAll(ListRead);
        listRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listRead.getSelectionModel().getSelectedItem();
                    String id_book;
                    if (session.getLoggedAuthor() != null) {
                        id_book = session.getLoggedAuthor().getBooks().getIdBookRead(selectedCell);
                    } else {
                        id_book = session.getLoggedUser().getBooks().getIdBookRead(selectedCell);
                    }
                    Book allInfo = searchManager.searchIdBook(id_book);
                    try {
                        Parent bookInterface;
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                        bookInterface = (Parent) fxmlLoader.load();
                        BookDetailController bookController = fxmlLoader.getController();
                        bookController.setInfoBook(allInfo);
                        Stage actual_stage = (Stage) listRead.getScene().getWindow();
                        actual_stage.setScene(new Scene(bookInterface));
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
    void viewToRead() {
        if (session.getLoggedAuthor() != null)
            session.getLoggedAuthor().getBooks().listBooksClear();
        else
            session.getLoggedUser().getBooks().listBooksClear();
        String test = usernameUser.getText();
        listToRead.getItems().clear();
        ArrayList<Book> toRead;

        toRead = userManager.loadRelationsBook("User", usernameUser.getText(), "TO_READ");

        ObservableList<String> ListToRead = FXCollections.observableArrayList();
        for (Book book : toRead) {
            if (session.getLoggedAuthor() != null)
                ListToRead.add(session.getLoggedAuthor().getBooks().setToRead(book.getTitle(), book.getBook_id()));
            else
                ListToRead.add(session.getLoggedUser().getBooks().setToRead(book.getTitle(), book.getBook_id()));
        }
        listToRead.getItems().addAll(ListToRead);

        listToRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listToRead.getSelectionModel().getSelectedItem();
                    String id_book;
                    if (session.getLoggedAuthor() != null) {
                        id_book = session.getLoggedAuthor().getBooks().getIdBookToRead(selectedCell);
                    } else {
                        id_book = session.getLoggedUser().getBooks().getIdBookToRead(selectedCell);
                    }
                    Book allInfo = searchManager.searchIdBook(id_book);
                    try {
                        Parent bookInterface;
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                        bookInterface = (Parent) fxmlLoader.load();
                        BookDetailController bookController = fxmlLoader.getController();
                        bookController.setInfoBook(allInfo);
                        Stage actual_stage = (Stage) listToRead.getScene().getWindow();
                        actual_stage.setScene(new Scene(bookInterface));
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
    void selectRanking(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;

        homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/ranking.fxml"));

        Stage actual_stage = (Stage) rankingButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    public void initialize() {
        follow.setVisible(false);

        if (session.getLoggedUser() != null) {
            usernameUser.setText(session.getLoggedUser().getNickname());
        }
        // TODO per mattia capire perche vengono chiamate anche qui e non solo sulla set_nickname()
        // TODO credo perche senno non si caricherebbero follower e follow count nella pagina dello logged user/author
        viewFollower();
        viewFollow();


    }
}
