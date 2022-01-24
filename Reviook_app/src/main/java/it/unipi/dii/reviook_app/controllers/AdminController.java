package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.mongodb.MongoException;
import it.unipi.dii.reviook_app.Session;
import it.unipi.dii.reviook_app.components.ListAuthor;
import it.unipi.dii.reviook_app.components.ListBook;
import it.unipi.dii.reviook_app.components.ListReview;
import it.unipi.dii.reviook_app.components.ListUser;
import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Review;
import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.BookManager;
import it.unipi.dii.reviook_app.manager.SearchManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AdminController {
    @FXML

    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXCheckBox authorOption;

    @FXML
    private JFXCheckBox bookOption;

    @FXML
    private JFXButton deleteButtonList;

    @FXML
    private JFXButton deleteReviewButton;

    @FXML
    private JFXListView<Review> reviewsListView;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXCheckBox userOption;

    @FXML
    private JFXListView<Book> bookList;

    @FXML
    private JFXListView<User> usersList;

    @FXML
    private JFXListView<Author> authorsList;

    @FXML
    private Text nameTitle;

    @FXML
    private Text rating;

    @FXML
    private Text description;

    @FXML
    private Text follower;

    @FXML
    private Text username;

    @FXML
    private Text categories;

    @FXML
    private JFXButton addAdminButton;

    private String selectedBookID = null;

    private SearchManager searchManager = new SearchManager();
    private BookManager bookManager = new BookManager();
    private Session session = Session.getInstance();

    @FXML
    void logoutActon(ActionEvent event) throws IOException {
        // TODO va invalidata la sessione
        session.clear();
        Parent loginInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) logoutButton.getScene().getWindow();
        actual_stage.setScene(new Scene(loginInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void deleteListElem(ActionEvent event) {
        if (bookOption.isSelected()) {
            Book selectedBook = (Book) bookList.getSelectionModel().getSelectedItem();
        } else if (userOption.isSelected()) {
            User selectedUser = (User) usersList.getSelectionModel().getSelectedItem();
        } else if (authorOption.isSelected()) {
            Author selectedAuthor = (Author) authorsList.getSelectionModel().getSelectedItem();
        }
    }
    
    @FXML
    void addAdminAction(ActionEvent event) throws IOException {
        Parent newAdminInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/newAdmin.fxml"));
        Stage actual_stage = (Stage) addAdminButton.getScene().getWindow();
        actual_stage.setScene(new Scene(newAdminInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void deleteReviewAction(ActionEvent event) {
        Review selectedReview = (Review) reviewsListView.getSelectionModel().getSelectedItem();
        if (selectedReview != null && selectedBookID != null) {
            bookManager.DeleteReview(selectedReview.getReview_id(), selectedBookID);
            Book book = bookManager.getBookByID(this.selectedBookID); // query to update review
            ObservableList<Review> obsListReview = FXCollections.observableArrayList();
            obsListReview.setAll(book.getReviews());
            this.reviewsListView.getItems().clear();
            this.reviewsListView.setItems(obsListReview);
        }
    }

    @FXML
    void searchAction(ActionEvent event) {
        //TODO formattare meglio i risultati
        usersList.getItems().clear();
        bookList.getItems().clear();
        authorsList.getItems().clear();
        reviewsListView.getItems().clear();
        if (bookOption.isSelected()) {
            bookList.setVisible(true);
            authorsList.setVisible(false);
            usersList.setVisible(false);
            ObservableList<Book> obsBooksList = FXCollections.observableArrayList();
            ArrayList<Book> list = searchManager.searchBooks("", "");
            obsBooksList.addAll(list);
            bookList.getItems().addAll(obsBooksList);
            addCustomFactory("book");
        } else if (userOption.isSelected()) {
            bookList.setVisible(false);
            authorsList.setVisible(false);
            usersList.setVisible(true);
            ObservableList<User> obsUserList = FXCollections.observableArrayList();
            ArrayList<User> list = searchManager.searchUser("");
            obsUserList.addAll(list);
            usersList.getItems().addAll(obsUserList);
            addCustomFactory("user");
        } else if (authorOption.isSelected()) {
            bookList.setVisible(false);
            authorsList.setVisible(true);
            usersList.setVisible(false);
            ObservableList<Author> obsUserList = FXCollections.observableArrayList();
            ArrayList<Author> list = searchManager.searchAuthor("");
            obsUserList.addAll(list);
            authorsList.getItems().addAll(obsUserList);
            addCustomFactory("author");
        }
    }

    private void addCustomFactory(String type) {
        if (type.equals("book")) {
            this.bookList.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
                @Override
                public ListCell<Book> call(ListView<Book> listView) {
                    return new ListBook();
                }
            });
            this.bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                        Book selectedBook = (Book) bookList.getSelectionModel().getSelectedItem();
                        selectedBookID = selectedBook.getBook_id();
                        nameTitle.setText(selectedBook.getTitle());
                        username.setText("-");
                        rating.setText(selectedBook.getAverage_rating().toString());
                        description.setText(selectedBook.getDescription());
                        categories.setText(String.join(", ", selectedBook.getGenres()));
                        follower.setText("-");
                        ObservableList<Review> obsListReview = FXCollections.observableArrayList();
                        obsListReview.setAll(selectedBook.getReviews());
                        reviewsListView.getItems().clear();
                        reviewsListView.setItems(obsListReview);
                        reviewsListView.setCellFactory(new Callback<ListView<Review>, javafx.scene.control.ListCell<Review>>() {
                            @Override
                            public ListCell<Review> call(ListView<Review> listView) {
                                return new ListReview();
                            }
                        });
                    }
                }
            });
        } else if (type.equals("user")) {
            this.usersList.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
                @Override
                public ListCell<User> call(ListView<User> listView) {
                    return new ListUser();
                }
            });
            this.usersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                        User selectedUser = (User) usersList.getSelectionModel().getSelectedItem();
                        nameTitle.setText(selectedUser.getName());
                        username.setText(selectedUser.getNickname());
                        follower.setText(((Integer) selectedUser.getFollowerCount()).toString());
                        description.setText("-");
                        categories.setText("-");
                        rating.setText("-");
                    }
                }
            });
        } else if (type.equals("author")) {
            this.authorsList.setCellFactory(new Callback<ListView<Author>, ListCell<Author>>() {
                @Override
                public ListCell<Author> call(ListView<Author> listView) {
                    return new ListAuthor();
                }
            });
            this.authorsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                        // va settata la parte destra della interfaccia
                        Author selectedAuthor = (Author) authorsList.getSelectionModel().getSelectedItem();
                        nameTitle.setText(selectedAuthor.getName());
                        username.setText(selectedAuthor.getNickname());
                        follower.setText(((Integer) selectedAuthor.getFollowerCount()).toString());
                        description.setText("-");
                        categories.setText("-");
                        rating.setText("-");
                    }
                }
            });
        }
    }

    @FXML
    public void selectBookCheckAction(ActionEvent actionEvent) {
        bookOption.setSelected(true);
        authorOption.setSelected(false);
        userOption.setSelected(false);
    }

    @FXML
    public void selectAuthorCheckAction(ActionEvent actionEvent) {
        bookOption.setSelected(false);
        userOption.setSelected(false);
        authorOption.setSelected(true);
    }

    @FXML
    public void selectUserCheckAction(ActionEvent actionEvent) {
        bookOption.setSelected(false);
        authorOption.setSelected(false);
        userOption.setSelected(true);
    }
}
