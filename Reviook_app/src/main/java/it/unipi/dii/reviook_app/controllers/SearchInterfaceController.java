package it.unipi.dii.reviook_app.controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.components.ListAuthor;
import it.unipi.dii.reviook_app.components.ListBook;
import it.unipi.dii.reviook_app.components.ListUser;
import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;

import it.unipi.dii.reviook_app.entity.Genre;
import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.SearchManager;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SearchInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private CheckBox authorCheck;

    @FXML
    private CheckBox userCheck;

    @FXML
    private CheckBox bookCheck;

    @FXML
    private JFXListView<Book> bookList;

    @FXML
    private JFXListView<User> usersList;

    @FXML
    private JFXListView<Author> authorsList;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton profileButton;

    @FXML
    private TextField searchText;

    @FXML
    private ChoiceBox bookFilter;

    private SearchManager searchManager = new SearchManager();

    private ObservableList<Genre> availableChoices = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        availableChoices.add(new Genre(""));
        availableChoices.addAll(searchManager.searchGenres());
        bookFilter.setItems(availableChoices);
        bookFilter.setVisible(false);
    }

    @FXML
    void homeInterface(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;
        if (session.getIsAuthor())
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
        else
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void profileInterface() throws IOException {
        Session session = Session.getInstance();
        Parent userInterface;
        if (session.getIsAuthor()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
            // controller.setNickname(nickSelected);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
        }

        Stage actual_stage = (Stage) profileButton.getScene().getWindow();
        actual_stage.setScene(new Scene(userInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    public void searchAction(ActionEvent actionEvent) {
        //TODO formattare meglio i risultati
        usersList.getItems().clear();
        bookList.getItems().clear();
        authorsList.getItems().clear();

        if (bookCheck.isSelected()) {
            bookList.setVisible(true);
            authorsList.setVisible(false);
            usersList.setVisible(false);

            String selectedChoice = bookFilter.getSelectionModel().getSelectedItem() == null ? "" : bookFilter.getSelectionModel().getSelectedItem().toString();

            ObservableList<Book> obsBooksList = FXCollections.observableArrayList();
            obsBooksList.addAll(searchManager.searchBooks(searchText.getText(), selectedChoice));
            bookList.getItems().addAll(obsBooksList);
            bookList.setCellFactory(
                    new Callback<ListView<Book>, ListCell<Book>>() {
                        @Override
                        public ListCell<Book> call(ListView<Book> listView) {
                            return new ListBook();
                        }
                    });
            bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                        Book selectedCell = (Book) bookList.getSelectionModel().getSelectedItem();
                        try {
                            Parent bookInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                            bookInterface = (Parent) fxmlLoader.load();
                            BookDetailController bookController = fxmlLoader.getController();
                            bookController.setInfoBook(selectedCell);
                            Stage actual_stage = (Stage) profileButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(bookInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } else if (userCheck.isSelected()) {
            bookList.setVisible(false);
            authorsList.setVisible(false);
            usersList.setVisible(true);
            ObservableList<User> obsUserList = FXCollections.observableArrayList();
            obsUserList.addAll(searchManager.searchUser(searchText.getText()));
            usersList.getItems().addAll(obsUserList);
            usersList.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
                @Override
                public ListCell<User> call(ListView<User> listView) {
                    return new ListUser();
                }
            });
            usersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                        User selectedCell = (User) usersList.getSelectionModel().getSelectedItem();
                        try {
                            Parent userInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
                            controller.setNickname(selectedCell.getNickname());
                            Stage actual_stage = (Stage) profileButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(userInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else if (authorCheck.isSelected()) {
            bookList.setVisible(false);
            authorsList.setVisible(true);
            usersList.setVisible(false);
            ObservableList<Author> obsUserList = FXCollections.observableArrayList();
            obsUserList.addAll(searchManager.searchAuthor(searchText.getText()));
            authorsList.getItems().addAll(obsUserList);
            authorsList.setCellFactory(new Callback<ListView<Author>, ListCell<Author>>() {
                @Override
                public ListCell<Author> call(ListView<Author> listView) {
                    return new ListAuthor();
                }
            });
            authorsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                        Author selectedCell = (Author) authorsList.getSelectionModel().getSelectedItem();
                        try {
                            Parent userInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
                            controller.setNickname(selectedCell.getNickname());

                            Stage actual_stage = (Stage) profileButton.getScene().getWindow();
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
    }

    @FXML
    public void selectBookCheckAction(ActionEvent actionEvent) {
        bookCheck.setSelected(true);
        authorCheck.setSelected(false);
        userCheck.setSelected(false);
        bookFilter.setVisible(true);
    }

    @FXML
    public void selectAuthorCheckAction(ActionEvent actionEvent) {
        bookCheck.setSelected(false);
        userCheck.setSelected(false);
        authorCheck.setSelected(true);
        bookFilter.setVisible(false);
    }


    public void selectUserCheckAction(ActionEvent actionEvent) {
        bookCheck.setSelected(false);
        authorCheck.setSelected(false);
        userCheck.setSelected(true);
        bookFilter.setVisible(false);
    }
}
