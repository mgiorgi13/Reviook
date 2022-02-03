package it.unipi.dii.reviook_app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Genre;
import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.BookManager;
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
    private Text usernameUser, followerCount, followCount;

    @FXML
    private Text actionTarget;

    @FXML
    private JFXListView<String> listFollow, listFollower;

    @FXML
    private JFXListView<Book> listRead, listToRead;

    @FXML
    private CheckBox follow;

    @FXML
    private Button editButtonUser, logoutButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Text reviewCatValue1, reviewCatText1, reviewCatValue2, reviewCatText2, reviewCatValue3, reviewCatText3, reviewCatValue4, reviewCatText4, reviewCatValue5, reviewCatText5;

    @FXML
    private HBox Stat1, Stat2, Stat3, Stat4;

    @FXML
    private HBox HBAuthor1, HBAuthor2, HBAuthor3, HBAuthor4, HBUser1, HBUser2, HBUser3, HBUser4;

    @FXML
    private Text suggestedAuthor1, suggestedAuthor2, suggestedAuthor3, suggestedAuthor4;

    @FXML
    private Text suggestedUser1, suggestedUser2, suggestedUser3, suggestedUser4;

    @FXML
    private Button rankingButton;
    @FXML
    private JFXButton homeButton;

    private ArrayList<Genre> analytics;

    private String nickname;

    private ArrayList<Author> suggestedAuthors = new ArrayList<>();
    private ArrayList<User> suggestedUsers = new ArrayList<User>();

    private UserManager userManager = new UserManager();
    private SearchManager searchManager = new SearchManager();
    private BookManager bookManager = new BookManager();

    private Session session = Session.getInstance();

    private User visualizedUser = new User("");

    private ObservableList<Book> obsRead = FXCollections.observableArrayList();
    private ObservableList<Book> obsToRead = FXCollections.observableArrayList();
    private ObservableList<String> obsFollower = FXCollections.observableArrayList();
    private ObservableList<String> obsFollow = FXCollections.observableArrayList();


    @FXML
    void logoutActon(ActionEvent event) throws IOException {
        // TODO va invalidata la sessione
        session.clear();
        Parent loginInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) logoutButton.getScene().getWindow();
        actual_stage.setScene(new Scene(loginInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    @FXML
    public void addFollow(ActionEvent event) throws IOException {
        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                if(!userManager.following(session.getLoggedAuthor().getNickname(), "Author", usernameUser.getText(), "User"))
                    actionTarget.setText("Error: unable to add follow");
                else {
                    session.getLoggedAuthor().getInteractions().getFollow().add(usernameUser.getText());
                    session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);
                    visualizedUser.getInteractions().getFollower().add(session.getLoggedAuthor().getNickname());
                    visualizedUser.getInteractions().setNumberFollower(visualizedUser.getInteractions().getNumberFollower() + 1);
                }
            } else if (session.getLoggedUser() != null) {
                if(!userManager.following(session.getLoggedUser().getNickname(), "User", usernameUser.getText(), "User"))
                    actionTarget.setText("Error: unable to add follow");
                else {
                    session.getLoggedUser().getInteractions().getFollow().add(usernameUser.getText());
                    session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                    visualizedUser.getInteractions().getFollower().add(session.getLoggedUser().getNickname());
                    visualizedUser.getInteractions().setNumberFollower(visualizedUser.getInteractions().getNumberFollower() + 1);
                }
            }

        } else {
            if (session.getLoggedAuthor() != null) {
                if(!userManager.deleteFollowing(session.getLoggedAuthor().getNickname(), "Author", usernameUser.getText(), "User"))
                    actionTarget.setText("Error: unable to delete follow");
                else{
                    session.getLoggedAuthor().getInteractions().getFollow().remove(usernameUser.getText());
                    session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);
                    visualizedUser.getInteractions().getFollower().remove(session.getLoggedAuthor().getNickname());
                    visualizedUser.getInteractions().setNumberFollower(visualizedUser.getInteractions().getNumberFollower() - 1);
                }
            } else if (session.getLoggedUser() != null) {
                if(!userManager.deleteFollowing(session.getLoggedUser().getNickname(), "User", usernameUser.getText(), "User"))
                    actionTarget.setText("Error: unable to delete follow");
                else{
                    session.getLoggedUser().getInteractions().getFollow().remove(usernameUser.getText());
                    session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() - 1);
                    visualizedUser.getInteractions().getFollower().remove(session.getLoggedUser().getNickname());
                    visualizedUser.getInteractions().setNumberFollower(visualizedUser.getInteractions().getNumberFollower() - 1);
                }
            }
        }
    }

    private void viewReviewAnalytic() {
        Double previousValue = -1.0;
        String newGenre = "";
        ArrayList<Genre> genresReformatted = new ArrayList<>();

        analytics = visualizedUser.getStatistics();
        if (analytics == null) {
            //load analytics from db
            analytics = userManager.averageRatingCategoryUser(nickname);
            visualizedUser.setStatistics(analytics);
        }

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

    private String truckString(String input) {
        if (input.length() > 14) {
            return input.substring(0, 14);
        }
        return input;
    }

    private void setOnMouseClicked(HBox HbSuggestion, Integer index, String type) {
        HbSuggestion.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    if (type.equals("User")) {
                        User userSuggested = suggestedUsers.get(index);
                        System.out.println(userSuggested.getNickname());
                        try {
                            Parent userInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            UserInterfaceController userInterfaceController = fxmlLoader.getController();
                            userInterfaceController.setUser(userSuggested);
                            Stage actual_stage = (Stage) searchButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(userInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                            actual_stage.centerOnScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Author authorSuggested = suggestedAuthors.get(index);
                        System.out.println(authorSuggested.getNickname());
                        try {
                            Parent authorInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                            authorInterface = (Parent) fxmlLoader.load();
                            AuthorInterfaceController authorInterfaceController = fxmlLoader.getController();
                            authorInterfaceController.setAuthor(authorSuggested);
                            Stage actual_stage = (Stage) searchButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(authorInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                            actual_stage.centerOnScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    private void viewSuggestedAuthors() {
        suggestedAuthors = userManager.similarAuthors(nickname, "User");
        Collections.shuffle(suggestedAuthors);

        HBAuthor1.setVisible(false);
        HBAuthor2.setVisible(false);
        HBAuthor3.setVisible(false);
        HBAuthor4.setVisible(false);

        int size = suggestedAuthors.size();

        if (size >= 1) {
            HBAuthor1.setVisible(true);
            suggestedAuthor1.setText(truckString(suggestedAuthors.get(0).getNickname()));
            setOnMouseClicked(HBAuthor1, 0, "Author");
        }
        if (size >= 2) {
            HBAuthor2.setVisible(true);
            suggestedAuthor2.setText(truckString(suggestedAuthors.get(1).getNickname()));
            setOnMouseClicked(HBAuthor2, 1, "Author");
        }
        if (size >= 3) {
            HBAuthor3.setVisible(true);
            suggestedAuthor3.setText(truckString(suggestedAuthors.get(2).getNickname()));
            setOnMouseClicked(HBAuthor3, 2, "Author");
        }
        if (size >= 4) {
            HBAuthor4.setVisible(true);
            suggestedAuthor4.setText(truckString(suggestedAuthors.get(3).getNickname()));
            setOnMouseClicked(HBAuthor4, 3, "Author");
        }

    }

    private void viewSuggestedUsers() {
        suggestedUsers = userManager.similarUsers(nickname, "User");
        Collections.shuffle(suggestedUsers);
        HBUser1.setVisible(false);
        HBUser2.setVisible(false);
        HBUser3.setVisible(false);
        HBUser4.setVisible(false);

        int size = suggestedUsers.size();
        if (size >= 1) {
            HBUser1.setVisible(true);
            suggestedUser1.setText(truckString(suggestedUsers.get(0).getNickname()));
            setOnMouseClicked(HBUser1, 0, "User");
        }
        if (size >= 2) {
            HBUser2.setVisible(true);
            suggestedUser2.setText(truckString(suggestedUsers.get(1).getNickname()));
            setOnMouseClicked(HBUser2, 1, "User");
        }
        if (size >= 3) {
            HBUser3.setVisible(true);
            suggestedUser3.setText(truckString(suggestedUsers.get(2).getNickname()));
            setOnMouseClicked(HBUser3, 2, "User");
        }
        if (size >= 4) {
            HBUser4.setVisible(true);
            suggestedUser4.setText(truckString(suggestedUsers.get(3).getNickname()));
            setOnMouseClicked(HBUser4, 3, "User");
        }

    }

    public void setUser(User user) {
        //TODO caricaListeLibri()
        if (!session.getIsAuthor() && user.getNickname().equals(session.getLoggedUser().getNickname()))
            visualizedUser = session.getLoggedUser();
        else
            visualizedUser = user;

        this.nickname = visualizedUser.getNickname();
        usernameUser.setText(this.nickname);

        if (visualizedUser.getInteractions().getFollow().isEmpty() && visualizedUser.getInteractions().getFollower().isEmpty()) {
            viewFollow();
            viewFollower();
        } else {
            followCount.setText(String.valueOf(visualizedUser.getInteractions().getFollow().size()));
            followerCount.setText(String.valueOf(visualizedUser.getInteractions().getFollower().size()));
        }
        //load analytics

        viewReviewAnalytic();
        viewSuggestedAuthors();
        viewSuggestedUsers();

        rankingButton.setVisible(true);
        homeButton.setDisable(true);
        if (session.getLoggedAuthor() != null) {
            if (!session.getLoggedAuthor().getNickname().equals(nickname)) {
                follow.setVisible(true);
                editButtonUser.setVisible(false);
                rankingButton.setVisible(false);
                homeButton.setDisable(false);
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
                homeButton.setDisable(false);
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
        actual_stage.centerOnScreen();
    }

    @FXML
    void viewEditButtonUser(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/updateAccount.fxml"));
        Stage actual_stage = (Stage) editButtonUser.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    @FXML
    void viewFollow() {

        User user = visualizedUser;
        obsFollow.clear();
        List<String> Follow;
        if (visualizedUser.getInteractions().getFollow().isEmpty()) {
            Follow = userManager.loadRelationsFollowing("User", usernameUser.getText());
            user.getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                user.getInteractions().setFollow(Follow.get(i));
                obsFollow.add(Follow.get(i));
            }
            followCount.setText(String.valueOf(Follow.size()));
        } else {
            obsFollow.addAll(user.getInteractions().getFollow());
            followCount.setText(String.valueOf(user.getInteractions().getNumberFollow()));
        }
    }

    @FXML
    void viewFollower() {

        User user = visualizedUser;
        obsFollower.clear();
        List<String> Follower;
        if (visualizedUser.getInteractions().getFollower().isEmpty()) {
            Follower = userManager.loadRelationsFollower("User", usernameUser.getText());
            user.getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                user.getInteractions().setFollower(Follower.get(i));
                obsFollower.add(Follower.get(i));
            }
            followerCount.setText(String.valueOf(Follower.size()));
        } else {
            obsFollower.addAll(user.getInteractions().getFollower());
            followerCount.setText(String.valueOf(user.getInteractions().getNumberFollower()));
        }
    }

    @FXML
    void viewRead() {

        ArrayList<Book> read;
        obsRead.clear();
        if (visualizedUser.getBooks().getRead().isEmpty()) {
            read = userManager.loadRelationsBook("User", usernameUser.getText(), "READ");
            for (Book book : read) {
                visualizedUser.getBooks().addToSetRead(book);
                obsRead.add(book);
            }
        } else {
            obsRead.addAll(visualizedUser.getBooks().getRead());
        }
    }

    @FXML
    void viewToRead() {

        ArrayList<Book> toRead;
        obsToRead.clear();
        if (visualizedUser.getBooks().getToRead().isEmpty()) {
            toRead = userManager.loadRelationsBook("User", usernameUser.getText(), "TO_READ");
            for (Book book : toRead) {
                visualizedUser.getBooks().addToSetToRead(book);
                obsToRead.add(book);
            }
        } else {
            obsToRead.addAll(visualizedUser.getBooks().getToRead());
        }
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
        actual_stage.centerOnScreen();
    }

    public void setButtonConnection() {
        listToRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    Book selectedCell = (Book) listToRead.getSelectionModel().getSelectedItem();
                    Book allInfo = searchManager.searchIdBook(selectedCell.getBook_id());
                    if(allInfo != null){
                        try {
                            Parent bookInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                            bookInterface = (Parent) fxmlLoader.load();
                            BookDetailController bookController = fxmlLoader.getController();
                            bookController.setInfoBook(allInfo, false);
                            Stage actual_stage = (Stage) listToRead.getScene().getWindow();
                            actual_stage.setScene(new Scene(bookInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                            actual_stage.centerOnScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    if (session.getLoggedUser() != null) {
                        if(session.getLoggedUser().getNickname().equals(usernameUser.getText())) {
                            Book removeCell = (Book) listToRead.getSelectionModel().getSelectedItem();
                            bookManager.removeBookFromList(removeCell.getBook_id(), "TO_READ", usernameUser.getText(), "User");
                            listToRead.getItems().remove(removeCell);
                            session.getLoggedUser().getBooks().getToRead().remove(removeCell);
//                            for(int i = 0; i< visualizedUser.getBooks().getToRead().size();i++){
//                                if(visualizedUser.getBooks().getToRead().get(i).getBook_id().equals(removeCell.getBook_id())){
//                                    visualizedUser.getBooks().removeToRead();
//                                }
//                            }
                        }
                    }
                }
            }
        });
        listRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    Book selectedCell = (Book) listRead.getSelectionModel().getSelectedItem();
                    Book allInfo = searchManager.searchIdBook(selectedCell.getBook_id());
                    if(allInfo != null){
                        try {
                            Parent bookInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                            bookInterface = (Parent) fxmlLoader.load();
                            BookDetailController bookController = fxmlLoader.getController();
                            bookController.setInfoBook(allInfo, false);
                            Stage actual_stage = (Stage) listRead.getScene().getWindow();
                            actual_stage.setScene(new Scene(bookInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                            actual_stage.centerOnScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    if (session.getLoggedUser() != null) {
                        if(session.getLoggedUser().getNickname().equals(usernameUser.getText())) {
                            Book removeCell = (Book) listRead.getSelectionModel().getSelectedItem();
                            bookManager.removeBookFromList(removeCell.getBook_id(), "READ", usernameUser.getText(), "User");
                            listRead.getItems().remove(removeCell);
                            session.getLoggedUser().getBooks().getRead().remove(removeCell);
//                            for(int i = 0; i< visualizedUser.getBooks().getToRead().size();i++){
//                                if(visualizedUser.getBooks().getRead().get(i).getBook_id().equals(removeCell.getBook_id())){
//                                    visualizedUser.getBooks().removeRead();
//                                }
//                            }
                        }
                    }
                }
            }
        });
        listFollower.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listFollower.getSelectionModel().getSelectedItem();
                    System.out.println(selectedCell);
                    int result = userManager.verifyUsername(selectedCell, "", false);
                    if (result == -1 || result == 2)
                        return;
                    try {
                        Parent userInterface;
                        FXMLLoader fxmlLoader;
                        if (result == 1) {
                            Author author = new Author(selectedCell);
                            fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
                            controller.setAuthor(author);
                        } else {
                            User user = new User(selectedCell);
                            fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
                            controller.setUser(user);
                        }

                        Stage actual_stage = (Stage) listFollower.getScene().getWindow();
                        actual_stage.setScene(new Scene(userInterface));
                        actual_stage.setResizable(false);
                        actual_stage.show();
                        actual_stage.centerOnScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        listFollow.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listFollow.getSelectionModel().getSelectedItem();
                    System.out.println(selectedCell);
                    int result = userManager.verifyUsername(selectedCell, "", false);
                    if (result == -1 || result == 2)
                        return;
                    try {
                        Parent userInterface;
                        FXMLLoader fxmlLoader;
                        if (result == 1) {
                            Author author = new Author(selectedCell);
                            fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
                            controller.setAuthor(author);
                        } else {
                            User user = new User(selectedCell);
                            fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
                            controller.setUser(user);
                        }

                        Stage actual_stage = (Stage) listFollow.getScene().getWindow();
                        actual_stage.setScene(new Scene(userInterface));
                        actual_stage.setResizable(false);
                        actual_stage.show();
                        actual_stage.centerOnScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void homeButton(ActionEvent actionEvent) {
        try {
            Parent user_scene;
            Stage actual_stage = (Stage) homeButton.getScene().getWindow();
            if (session.getIsAuthor()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                user_scene = (Parent) fxmlLoader.load();
                AuthorInterfaceController authorInterfaceController = fxmlLoader.getController();
                authorInterfaceController.setAuthor(session.getLoggedAuthor());
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                user_scene = (Parent) fxmlLoader.load();
                UserInterfaceController userInterfaceController = fxmlLoader.getController();
                userInterfaceController.setUser(session.getLoggedUser());
                Parent root = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
            }
            actual_stage.setScene(new Scene(user_scene));
            actual_stage.setResizable(false);
            actual_stage.show();
            actual_stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        follow.setVisible(false);
        setButtonConnection();

        listRead.setItems(obsRead);
        listToRead.setItems(obsToRead);
        listFollow.setItems(obsFollow);
        listFollower.setItems(obsFollower);
    }
}