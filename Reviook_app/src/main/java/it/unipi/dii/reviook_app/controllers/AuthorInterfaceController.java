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
    private JFXButton searchButton;

    @FXML
    private Button rankingButton;

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
    private JFXListView listRead;

    @FXML
    private Button editButtonAuthor, addButtonBook;

    @FXML
    private CheckBox follow;

    @FXML
    private Text bookCatValue1, bookCatText1, bookCatValue2, bookCatText2, bookCatValue3, bookCatText3, bookCatValue4, bookCatText4, bookCatValue5, bookCatText5;

    @FXML
    private HBox Stat1,Stat2,Stat3,Stat4;

    @FXML
    private HBox HBAuthor1,HBAuthor2,HBAuthor3,HBAuthor4,HBUser1,HBUser2,HBUser3,HBUser4;

    @FXML
    private Text suggestedAuthor1, suggestedAuthor2, suggestedAuthor3, suggestedAuthor4;

    @FXML
    private Text suggestedUser1, suggestedUser2, suggestedUser3, suggestedUser4;

    private String nickname;

    private Session session = Session.getInstance();

    private ArrayList<Genre> analytics;

    private UserManager userManager = new UserManager();
    private SearchManager searchManager = new SearchManager();

    @FXML
    public void addButtonBookFunction(ActionEvent event) throws IOException {
        Parent updateInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/addBook.fxml"));
        Stage actual_stage = (Stage) addButtonBook.getScene().getWindow();
        actual_stage.setScene(new Scene(updateInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    public void addFollow(ActionEvent event) throws IOException {
        if (follow.isSelected()) {
            if (session.getLoggedAuthor() != null) {
                session.getLoggedAuthor().getInteractions().setFollow(usernameAuthor.getText());
                session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() + 1);

                userManager.following(session.getLoggedAuthor().getNickname(), "Author", usernameAuthor.getText(), "Author");

            } else if (session.getLoggedUser() != null) {
                session.getLoggedUser().getInteractions().setFollow(usernameAuthor.getText());
                session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() + 1);
                userManager.following(session.getLoggedUser().getNickname(), "User", usernameAuthor.getText(), "Author");
            }
        } else {
            if (session.getLoggedAuthor() != null) {
                userManager.deleteFollowing(session.getLoggedAuthor().getNickname(), "Author", usernameAuthor.getText(), "Author");
                for (int i = 0; i < session.getLoggedAuthor().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedAuthor().getInteractions().getFollow().get(i).equals(usernameAuthor.getText())) {
                        session.getLoggedAuthor().getInteractions().getFollow().remove(i);
                        session.getLoggedAuthor().getInteractions().setNumberFollow(session.getLoggedAuthor().getInteractions().getNumberFollow() - 1);
                    }
                }
            } else if (session.getLoggedUser() != null) {
                userManager.deleteFollowing(session.getLoggedUser().getNickname(), "User", usernameAuthor.getText(), "Author");
                for (int i = 0; i < session.getLoggedUser().getInteractions().getFollow().size(); i++) {
                    if (session.getLoggedUser().getInteractions().getFollow().get(i).equals(usernameAuthor.getText())) {
                        session.getLoggedUser().getInteractions().getFollow().remove(i);
                        session.getLoggedUser().getInteractions().setNumberFollow(session.getLoggedUser().getInteractions().getNumberFollow() - 1);
                    }
                }
            }
        }
    }

    private void viewBookAnalytic(){
        Double previousValue = -1.0;
        String newGenre = "";
        ArrayList<Genre> genresReformatted = new ArrayList<>();

        analytics = session.getCache().getAnalyticsExecuted().get(nickname+"author");
        if(analytics == null) {
            //load analytics from db
            analytics = userManager.averageRatingCategoryAuthor(nickname);
            session.getCache().getAnalyticsExecuted().put(nickname+"author", analytics);
        }

        for(int i = 0; i < analytics.size(); i ++){
            if(previousValue == -1.0 || analytics.get(i).getValue().equals(previousValue)){
                newGenre =  newGenre.concat(analytics.get(i).getType() + "\n");
            }else{
                genresReformatted.add(new Genre(newGenre,previousValue));
                newGenre = "";
                newGenre = newGenre.concat(analytics.get(i).getType() + "\n");
            }
            previousValue = analytics.get(i).getValue();
            if(i == analytics.size() - 1){
                genresReformatted.add(new Genre(newGenre,previousValue));
            }
        }

        int size = genresReformatted.size();

        Stat1.setVisible(false);
        Stat2.setVisible(false);
        Stat3.setVisible(false);
        Stat4.setVisible(false);

        if(size >= 1){
            Stat1.setVisible(true);
            bookCatText1.setText(genresReformatted.get(0).getType());
            bookCatValue1.setText(genresReformatted.get(0).getValue().toString().substring(0,20));
        }
        if(size >= 2){
            Stat2.setVisible(true);
            bookCatText2.setText(genresReformatted.get(1).getType());
            bookCatValue2.setText(genresReformatted.get(1).getValue().toString().substring(0,20));
        }
        if(size >= 3){
            Stat3.setVisible(true);
            bookCatText3.setText(genresReformatted.get(2).getType());
            bookCatValue3.setText(genresReformatted.get(2).getValue().toString().substring(0,20));
        }
        if(size >= 4){
            Stat4.setVisible(true);
            bookCatText4.setText(genresReformatted.get(3).getType());
            bookCatValue4.setText(genresReformatted.get(3).getValue().toString().substring(0,20));
        }
    }

    private void viewSuggestedAuthors() {
        ArrayList<Author> suggestedAuthors = userManager.similarAuthors(nickname,"Author");
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
            suggestedAuthor4.setText(suggestedAuthors.get(3).getNickname().substring(0,20));
        }

    }

    private void viewSuggestedUsers() {
        ArrayList<User> suggestedUsers = userManager.similarUsers(nickname,"Author");
        Collections.shuffle(suggestedUsers);

        HBUser1.setVisible(false);
        HBUser2.setVisible(false);
        HBUser3.setVisible(false);
        HBUser4.setVisible(false);

        int size = suggestedUsers.size();

        if(size >= 1){
            HBUser1.setVisible(true);
            suggestedUser1.setText(suggestedUsers.get(0).getNickname());
        }
        if(size >= 2){
            HBUser2.setVisible(true);
            suggestedUser2.setText(suggestedUsers.get(1).getNickname());
        }
        if(size >= 3){
            HBUser3.setVisible(true);
            suggestedUser3.setText(suggestedUsers.get(2).getNickname());
        }
        if(size >= 4){
            HBUser4.setVisible(true);
            suggestedUser4.setText(suggestedUsers.get(3).getNickname());
        }

    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        usernameAuthor.setText(this.nickname);

        //set analytics result
        viewBookAnalytic();
        viewSuggestedAuthors();
        viewSuggestedUsers();

        viewFollow();
        viewFollower();
        rankingButton.setVisible(false);
        if (session.getLoggedAuthor() != null && usernameAuthor.getText().equals(session.getLoggedAuthor().getNickname())) {
            addButtonBook.setVisible(true);
            rankingButton.setVisible(true);
        } else {
            addButtonBook.setVisible(false);
            rankingButton.setVisible(false);
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
            Author author = new Author("", "", "", usernameAuthor.getText(), "", "", null, 0);
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
        //    System.out.println(Follow.size()+ " "+session.getLoggedAuthor().getInteractions().getFollow()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }

    @FXML
    void viewRead() {
        if (session.getLoggedAuthor() != null)
            session.getLoggedAuthor().getBooks().listBooksClear();
        else
            session.getLoggedUser().getBooks().listBooksClear();
        String test = usernameAuthor.getText();
        listRead.getItems().clear();
        ArrayList<Book> read;

        read = userManager.loadRelationsBook("Author", usernameAuthor.getText(), "READ");
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

        listToRead.getItems().clear();
        ArrayList<Book> toRead;

        toRead = userManager.loadRelationsBook("Author", usernameAuthor.getText(), "TO_READ");

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
            Author author = new Author("", "", "", usernameAuthor.getText(), "", "", null, 0);
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
        //  System.out.println(Follower.size()+ " "+session.getLoggedAuthor().getInteractions().getFollower()+" "+ session.getLoggedAuthor().getInteractions().getNumberFollower());
    }

    public void publishedFunction() {
        listPublished.getItems().clear();
        if (session.getLoggedAuthor() != null)
            session.getLoggedAuthor().getBooks().listBooksClear();
        else
            session.getLoggedUser().getBooks().listBooksClear();
        // TODO recuperare id  autore e fare la ricerca con quello
        ArrayList<Book> published;

        published = userManager.loadRelationsBook("Author", usernameAuthor.getText(), "WROTE");

        ObservableList<String> ListPublished = FXCollections.observableArrayList();
        for (Book book : published) {
            if (session.getLoggedAuthor() != null) {
                ListPublished.add(session.getLoggedAuthor().getBooks().setPublished(book.getTitle(), book.getBook_id()));
            } else
                ListPublished.add(session.getLoggedUser().getBooks().setPublished(book.getTitle(), book.getBook_id()));
        }
        listPublished.getItems().addAll(ListPublished);
        listPublished.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) listPublished.getSelectionModel().getSelectedItem();
                    String id_book;
                    if (session.getLoggedAuthor() != null) {
                        id_book = session.getLoggedAuthor().getBooks().getIdBookPublished(selectedCell);
                    } else {
                        id_book = session.getLoggedUser().getBooks().getIdBookPublished(selectedCell);
                    }
                    Book allInfo = searchManager.searchIdBook(id_book);
                    try {
                        Parent bookInterface;
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                        bookInterface = (Parent) fxmlLoader.load();
                        BookDetailController bookController = fxmlLoader.getController();
                        bookController.setInfoBook(allInfo);
                        Stage actual_stage = (Stage) listPublished.getScene().getWindow();
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
        Parent homeInterface;

        homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/ranking.fxml"));

        Stage actual_stage = (Stage) rankingButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    public void initialize() {
        follow.setVisible(false);

        if (session.getLoggedAuthor() != null) {
            usernameAuthor.setText(session.getLoggedAuthor().getNickname());
        } /*else if (session.getLoggedUser() != null) {
            usernameAuthor.setText(session.getLoggedUser().getNickname());
        }*/

        viewFollow();
        viewFollower();
    }

}
