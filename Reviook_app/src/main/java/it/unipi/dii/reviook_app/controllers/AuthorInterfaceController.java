package it.unipi.dii.reviook_app.controllers;

import java.io.IOException;
import java.util.*;

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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    private JFXButton homeButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Button rankingButton;

    @FXML
    private Text followCount;

    @FXML
    private Text followerCount;

    @FXML
    private JFXListView<String> listFollow, listFollower;

    @FXML
    private JFXListView<Book> listToRead, listRead, listPublished;

    @FXML
    private Button editButtonAuthor, addButtonBook, logoutButton;

    @FXML
    private CheckBox follow;

    @FXML
    private Text bookCatValue1, bookCatText1, bookCatValue2, bookCatText2, bookCatValue3, bookCatText3, bookCatValue4, bookCatText4, bookCatValue5, bookCatText5;

    @FXML
    private HBox Stat1, Stat2, Stat3, Stat4;

    @FXML
    private HBox HBAuthor1, HBAuthor2, HBAuthor3, HBAuthor4, HBUser1, HBUser2, HBUser3, HBUser4;

    @FXML
    private Text suggestedAuthor1, suggestedAuthor2, suggestedAuthor3, suggestedAuthor4;

    @FXML
    private Text suggestedUser1, suggestedUser2, suggestedUser3, suggestedUser4;

    private String nickname;

    private ArrayList<Author> suggestedAuthors;
    private ArrayList<User> suggestedUsers;

    private Session session = Session.getInstance();

    private ArrayList<Genre> analytics;

    private UserManager userManager = new UserManager();
    private SearchManager searchManager = new SearchManager();
    private Author visualizedAuthor = new Author("");

    private ObservableList<Book> obsPublished = FXCollections.observableArrayList();
    private ObservableList<Book> obsToRead = FXCollections.observableArrayList();
    private ObservableList<Book> obsRead = FXCollections.observableArrayList();
    private ObservableList<String> obsFollower = FXCollections.observableArrayList();
    private ObservableList<String> obsFollow = FXCollections.observableArrayList();

    @FXML
    public void addButtonBookFunction(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/addBook.fxml"));
        Stage actual_stage = (Stage) addButtonBook.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    @FXML
    public void addFollow(ActionEvent event) throws IOException {
        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().getFollow().add(usernameAuthor.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);
                userManager.following(session.getLoggedAuthor().getNickname(), "Author", usernameAuthor.getText(), "Author");
                visualizedAuthor.getInteractions().getFollower().add(session.getLoggedAuthor().getNickname());
                visualizedAuthor.getInteractions().setNumberFollower(visualizedAuthor.getInteractions().getNumberFollower() + 1);
            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().getFollow().add(usernameAuthor.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                userManager.following(session.getLoggedUser().getNickname(), "User", usernameAuthor.getText(), "Author");

                visualizedAuthor.getInteractions().getFollower().add(session.getLoggedUser().getNickname());
                visualizedAuthor.getInteractions().setNumberFollower(visualizedAuthor.getInteractions().getNumberFollower() + 1);
            }
        } else {
            if (session.getLoggedAuthor() != null) {
                userManager.deleteFollowing(session.getLoggedAuthor().getNickname(), "Author", usernameAuthor.getText(), "Author");
                session.getLoggedAuthor().getInteractions().getFollow().remove(usernameAuthor.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);

                visualizedAuthor.getInteractions().getFollower().remove(session.getLoggedAuthor().getNickname());
                visualizedAuthor.getInteractions().setNumberFollower(visualizedAuthor.getInteractions().getNumberFollower() - 1);
            } else if (session.getLoggedUser() != null) {
                userManager.deleteFollowing(session.getLoggedUser().getNickname(), "User", usernameAuthor.getText(), "Author");
                session.getLoggedUser().getInteractions().getFollow().remove(usernameAuthor.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() - 1);

                visualizedAuthor.getInteractions().getFollower().remove(session.getLoggedUser().getNickname());
                visualizedAuthor.getInteractions().setNumberFollower(visualizedAuthor.getInteractions().getNumberFollower() - 1);
            }
        }
    }

    private String truckString(String input) {
        if (input.length() > 14) {
            return input.substring(0, 14);
        }
        return input;
    }

    private void viewBookAnalytic() {
        Double previousValue = -1.0;
        String newGenre = "";
        ArrayList<Genre> genresReformatted = new ArrayList<>();

        analytics = visualizedAuthor.getStatistics();
        if (analytics == null) {
            //load analytics from db
            analytics = userManager.averageRatingCategoryAuthor(nickname);
            visualizedAuthor.setStatistics(analytics);
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
            bookCatText1.setText(genresReformatted.get(0).getType());
            bookCatValue1.setText(genresReformatted.get(0).getValue().toString());
        }
        if (size >= 2) {
            Stat2.setVisible(true);
            bookCatText2.setText(genresReformatted.get(1).getType());
            bookCatValue2.setText(genresReformatted.get(1).getValue().toString());
        }
        if (size >= 3) {
            Stat3.setVisible(true);
            bookCatText3.setText(genresReformatted.get(2).getType());
            bookCatValue3.setText(genresReformatted.get(2).getValue().toString());
        }
        if (size >= 4) {
            Stat4.setVisible(true);
            bookCatText4.setText(genresReformatted.get(3).getType());
            bookCatValue4.setText(genresReformatted.get(3).getValue().toString());
        }
    }

    private void setOnMouseClicked(HBox HbSuggestion, Integer index, String type) {
        HbSuggestion.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    if (type.equals("User")) {
                        User userSuggested = suggestedUsers.get(index);
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
        suggestedAuthors = userManager.similarAuthors(nickname, "Author");
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
        suggestedUsers = userManager.similarUsers(nickname, "Author");
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

    public void setAuthor(Author author) {
        if (session.getIsAuthor() && author.getNickname().equals(session.getLoggedAuthor().getNickname()))
            visualizedAuthor = session.getLoggedAuthor();
        else
            visualizedAuthor = author;

        this.nickname = visualizedAuthor.getNickname();
        usernameAuthor.setText(this.nickname);

        //set analytics result
        viewBookAnalytic();
        viewSuggestedAuthors();
        viewSuggestedUsers();

        if (visualizedAuthor.getInteractions().getFollow().isEmpty() && visualizedAuthor.getInteractions().getFollower().isEmpty()) {
            viewFollow();
            viewFollower();
        } else {
            followCount.setText(String.valueOf(visualizedAuthor.getInteractions().getFollow().size()));
            followerCount.setText(String.valueOf(visualizedAuthor.getInteractions().getFollower().size()));
        }
        rankingButton.setVisible(false);
        if (session.getLoggedAuthor() != null && usernameAuthor.getText().equals(session.getLoggedAuthor().getNickname())) {
            addButtonBook.setVisible(true);
            rankingButton.setVisible(true);
            homeButton.setDisable(true);
        } else {
            addButtonBook.setVisible(false);
            rankingButton.setVisible(false);
            homeButton.setDisable(false);
        }
        // I'm author
        if (session.getLoggedAuthor() != null) {
            //other author
            if (!session.getLoggedAuthor().getNickname().equals(nickname)) {
                follow.setVisible(true);
                editButtonAuthor.setVisible(false);
            }
            //check if I follow already that author
            if (!session.getLoggedAuthor().getInteractions().getFollow().isEmpty()) {
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    //System.out.println(session.getLoggedUser().getInteractions().getFollow().get(i).equals(this.nickname));
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(this.nickname))
                        follow.setSelected(true);
                }
            }
        }
        // I'm user
        else if (session.getLoggedUser() != null) {
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
        actual_stage.centerOnScreen();
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
    void viewFollow() {
        Author author = visualizedAuthor;
        obsFollow.clear();
        List<String> Follow;
        if (visualizedAuthor.getInteractions().getFollow().isEmpty()) {
            Follow = userManager.loadRelations("Author", usernameAuthor.getText());
            author.getInteractions().setNumberFollow(Follow.size());
            for (int i = 0; i < Follow.size(); i++) {
                author.getInteractions().setFollow(Follow.get(i));
                obsFollow.add(Follow.get(i));
            }
            followCount.setText(String.valueOf(Follow.size()));
        } else {
            obsFollow.addAll(author.getInteractions().getFollow());
            followCount.setText(String.valueOf(author.getInteractions().getNumberFollow()));
        }
    }

    @FXML
    void viewRead() {
        ArrayList<Book> read;
        obsRead.clear();
        if (visualizedAuthor.getBooks().getRead().isEmpty()) {
            read = userManager.loadRelationsBook("Author", usernameAuthor.getText(), "READ");
            for (Book book : read) {
                visualizedAuthor.getBooks().addToSetRead(book);
                obsRead.add(book);
            }
        } else {
            for (Book book : visualizedAuthor.getBooks().getRead()) {
                obsRead.add(book);
            }
        }
    }

    @FXML
    void viewToRead() {
        ArrayList<Book> toRead;
        obsToRead.clear();
        if (visualizedAuthor.getBooks().getToRead().isEmpty()) {
            toRead = userManager.loadRelationsBook("Author", usernameAuthor.getText(), "TO_READ");
            for (Book book : toRead) {
                visualizedAuthor.getBooks().addToSetToRead(book);
                obsToRead.add(book);
            }
        } else {
            obsToRead.addAll(visualizedAuthor.getBooks().getToRead());
        }
    }

    public void viewPublished() {
//        if (session.getLoggedAuthor() != null)
//            session.getLoggedAuthor().getBooks().listBooksClear();
//        else
//            session.getLoggedUser().getBooks().listBooksClear();
        // TODO recuperare id  autore e fare la ricerca con quello

        ArrayList<Book> published;
        obsPublished.clear();
        if (visualizedAuthor.getPublished().isEmpty()) {
            published = userManager.loadRelationsBook("Author", usernameAuthor.getText(), "WROTE");
            for (Book book : published) {
                visualizedAuthor.addToSetPublished(book);
                obsPublished.add(book);
            }
        } else {
            obsPublished.addAll(visualizedAuthor.getPublished());
        }
    }

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
    void viewFollower() {
        Author author = visualizedAuthor;
        obsFollower.clear();
        List<String> Follower;
        if (visualizedAuthor.getInteractions().getFollower().isEmpty()) {
            Follower = userManager.loadRelationsFollower("Author", usernameAuthor.getText());
            author.getInteractions().setNumberFollower(Follower.size());
            for (int i = 0; i < Follower.size(); i++) {
                author.getInteractions().setFollower(Follower.get(i));
                obsFollower.add(Follower.get(i));
            }
            followerCount.setText(String.valueOf(Follower.size()));
        } else {
            obsFollower.addAll(author.getInteractions().getFollower());
            followerCount.setText(String.valueOf(author.getInteractions().getNumberFollower()));
        }
        //  System.out.println(Follower.size()+ " "+session.getLoggedAuthor().getInteractions().getFollower()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }

    @FXML
    void selectRanking(ActionEvent event) throws IOException {
        Parent homeInterface;
        homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/ranking.fxml"));
        Stage actual_stage = (Stage) rankingButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    public void setButtonConnection() {
        listRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    Book selectedCell = (Book) listRead.getSelectionModel().getSelectedItem();
                    if (selectedCell == null) {
                        return;
                    }
                    Book allInfo = searchManager.searchIdBook(selectedCell.getBook_id());
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
        });
        listToRead.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    Book selectedCell = (Book) listToRead.getSelectionModel().getSelectedItem();
                    if (selectedCell == null) {
                        return;
                    }
                    Book allInfo = searchManager.searchIdBook(selectedCell.getBook_id());
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
        });
        listFollower.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listFollower.getSelectionModel().getSelectedItem();
                    if (selectedCell == null) {
                        return;
                    }
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
        listPublished.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    Book selectedCell = (Book) listPublished.getSelectionModel().getSelectedItem();
                    if (selectedCell == null) {
                        return;
                    }
                    Book allInfo = searchManager.searchIdBook(selectedCell.getBook_id());
                    try {
                        Parent bookInterface;
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                        bookInterface = (Parent) fxmlLoader.load();
                        BookDetailController bookController = fxmlLoader.getController();
                        bookController.setInfoBook(allInfo, false);
                        Stage actual_stage = (Stage) listPublished.getScene().getWindow();
                        actual_stage.setScene(new Scene(bookInterface));
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
                    if (selectedCell == null) {
                        return;
                    }
                    //check if author or user
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

    @FXML
    private void homeButton() {
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

        listPublished.setItems(obsPublished);
        listToRead.setItems(obsToRead);
        listRead.setItems(obsRead);
        listFollow.setItems(obsFollow);
        listFollower.setItems(obsFollower);

    }

}