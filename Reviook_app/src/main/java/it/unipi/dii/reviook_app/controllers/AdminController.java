package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import it.unipi.dii.reviook_app.Session;
import it.unipi.dii.reviook_app.components.*;
import it.unipi.dii.reviook_app.entity.*;
import it.unipi.dii.reviook_app.manager.AdminManager;
import it.unipi.dii.reviook_app.manager.BookManager;
import it.unipi.dii.reviook_app.manager.SearchManager;
import it.unipi.dii.reviook_app.manager.UserManager;
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
import javafx.scene.control.TextField;
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
    private JFXCheckBox reviewOption;

    @FXML
    private JFXButton deleteElemButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton logsButton;

    @FXML
    private JFXButton unReportButton;

    @FXML
    private Text actionTarget;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXCheckBox userOption;

    @FXML
    private JFXListView<Report> bookList;

    @FXML
    private JFXListView<Report> reviewList;

    @FXML
    private JFXListView<User> usersList;

    @FXML
    private JFXListView<Author> authorsList;

    @FXML
    private JFXListView<Log> logsList;

    @FXML
    private TextField usernameField;

    @FXML
    private Text nameTitle;

    @FXML
    private Text description;

    @FXML
    private Text reviewText;

    @FXML
    private Text follower;

    @FXML
    private Text username;

    @FXML
    private JFXButton addAdminButton;

    private String selectedBookID = null;

    private SearchManager searchManager = new SearchManager();
    private BookManager bookManager = new BookManager();
    private UserManager userManager = new UserManager();
    private AdminManager adminManager = new AdminManager();
    private Session session = Session.getInstance();

    ObservableList<Report> obsBooksList = FXCollections.observableArrayList();
    ObservableList<User> obsUserList = FXCollections.observableArrayList();
    ObservableList<Author> obsAuthorList = FXCollections.observableArrayList();
    ObservableList<Report> obsListReview = FXCollections.observableArrayList();
    ObservableList<Log> obsListLog = FXCollections.observableArrayList();

    @FXML
    void logsAction() {
        clearList();
        deleteElemButton.setDisable(true);
        unReportButton.setDisable(true);
        ArrayList<Log> list = adminManager.loadLogs();
        obsListLog.addAll(list);
        logsList.setVisible(true);
        bookList.setVisible(false);
        authorsList.setVisible(false);
        usersList.setVisible(false);
        reviewList.setVisible(false);
        logsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    Log selectedCell = (Log) logsList.getSelectionModel().getSelectedItem();
                    if (selectedCell != null) {
                        if (selectedCell.getType().equals("book")) {
                            nameTitle.setText(selectedCell.getTitle());
                            ArrayList<Author> authors = selectedCell.getAuthors();
                            ArrayList<String> authorsName = new ArrayList<>();
                            for (Author a : authors) {
                                authorsName.add(a.getName());
                            }
                            String author = String.join(", ", authorsName);
                            username.setText(author);
                            description.setText(selectedCell.getDescription());
                            follower.setText("-");
                            reviewText.setText("-");
                        } else if (selectedCell.getType().equals("review")) {
                            reviewText.setText(selectedCell.getReview_text());
                            username.setText(selectedCell.getUsername());
                            description.setText("-");
                            follower.setText("-");
                            nameTitle.setText("-");
                        }
                    }
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY && mouseEvent.getClickCount() == 2) {
                    Log selectedCell = (Log) logsList.getSelectionModel().getSelectedItem();
                    if (selectedCell == null) {
                        return;
                    }
                    if (selectedCell.getOperation().equals("delete")) {
                        if (adminManager.restoreLog(selectedCell)) {
                            // reported element restored with success
                            if (adminManager.deleteLog(selectedCell)) {
                                // selected log deleted with success
                                obsListLog.remove(selectedCell);
                            } else {
                                // can't delete selected log --- retry delete log
                                adminManager.deleteLog(selectedCell);
                                obsListLog.remove(selectedCell);
                                actionTarget.setText("Error: unable to restore selected element");
                            }
                        } else {
                            // can't restore reported elem
                            actionTarget.setText("Error: unable to restore selected element");
                        }
                    }
                }
            }
        });
        logsList.setCellFactory(new Callback<ListView<Log>, ListCell<Log>>() {
            @Override
            public ListCell<Log> call(ListView<Log> listView) {
                return new ListCell<Log>() {
                    @Override
                    public void updateItem(Log item, boolean empty) {
                        super.updateItem(item, empty);
                        textProperty().unbind();
                        if (item != null)
                            setText(item.toString());
                        else
                            setText(null);
                    }
                };
            }
        });
    }

    @FXML
    void logoutActon(ActionEvent event) throws IOException {
        session.clear();
        Parent loginInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/login.fxml"));
        Stage actual_stage = (Stage) logoutButton.getScene().getWindow();
        actual_stage.setScene(new Scene(loginInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    void resetRightDetail() {
        nameTitle.setText("-");
        username.setText("-");
        description.setText("-");
        follower.setText("-");
        reviewText.setText("-");
    }

    @FXML
    void deleteElemAction() {
        if (bookOption.isSelected()) {
            Report selectedBook = (Report) bookList.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                return;
            }
            Book bookForBackup = bookManager.getBookByID(selectedBook.getBook_id());
            if (bookManager.deleteBookMongo(bookForBackup)) {
                //book deleted from mongo
                if(bookManager.deleteBookN4J(bookForBackup)) {
                    // book deleted with success from NEO4J
                    if (adminManager.deleteReport(selectedBook, false)) {
                        // report deleted with success
                        obsBooksList.remove(selectedBook);
                        addCustomFactory("book");
                        resetRightDetail();
                    } else {
                        // can't delete report -- retry
                        if (adminManager.deleteReport(selectedBook, false)) {
                            // report deleted with success
                            obsBooksList.remove(selectedBook);
                            addCustomFactory("book");
                            resetRightDetail();
                        }
                    }
                }else {
                    bookManager.addBookMongo(bookForBackup);
                    actionTarget.setText("Error: unable to remove Book");
                }
            } else {
                actionTarget.setText("Error: unable to remove Book");
            }
        } else if (userOption.isSelected()) {
            User selectedUser = (User) usersList.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                return;
            }
            if (userManager.deleteUserMongo(selectedUser, "user")) {
                // user delete with success from MongoDB
                if (userManager.deleteUserN4J(selectedUser, "user")) {
                    // user delete with success from N4J
                    obsUserList.remove(selectedUser);
                    addCustomFactory("user");
                    resetRightDetail();
                } else {
                    // can't delete user from N4J
                    userManager.register(selectedUser, "user");
                    actionTarget.setText("Error: unable to delete user");
                }
            } else {
                // can't delete user form MongoDB
                actionTarget.setText("Error: unable to delete user");
            }
        } else if (authorOption.isSelected()) {
            Author selectedAuthor = (Author) authorsList.getSelectionModel().getSelectedItem();
            if (selectedAuthor == null) {
                return;
            }
            if (userManager.deleteUserMongo(selectedAuthor, "author")) {
                // author delete with success from MongoDB
                if (userManager.deleteUserN4J(selectedAuthor, "author")) {
                    // author delete with success from N4J
                    obsAuthorList.remove(selectedAuthor);
                    addCustomFactory("author");
                    resetRightDetail();
                } else {
                    // can't delete author from N4J
                    userManager.register(selectedAuthor, "author");
                    actionTarget.setText("Error: unable to delete author");
                }
            } else {
                // can't delete author form MongoDB
                actionTarget.setText("Error: unable to delete author");
            }
        } else if (reviewOption.isSelected()) {
            Report selectedReview = (Report) reviewList.getSelectionModel().getSelectedItem();
            if (selectedReview == null) {
                return;
            }
            if (bookManager.deleteReview(selectedReview.getReview_id(), selectedReview.getBook_id())) {
                // review removed with success
                if (adminManager.deleteReport(selectedReview, false)) {
                    // report review removed with success
                    obsListReview.remove(selectedReview);
                    addCustomFactory("review");
                    resetRightDetail();
                } else {
                    // can't remove report review -- retry
                    if (adminManager.deleteReport(selectedReview, false)) {
                        // report review removed with success
                        obsListReview.remove(selectedReview);
                        addCustomFactory("review");
                        resetRightDetail();
                    }
                }
            } else {
                // can't remove review
                actionTarget.setText("Error: unable to remove Review");
            }
        }
    }

    @FXML
    void addAdminAction(ActionEvent event) throws IOException {
        Parent newAdminInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/newAdmin.fxml"));
        Stage actual_stage = (Stage) addAdminButton.getScene().getWindow();
        actual_stage.setScene(new Scene(newAdminInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    @FXML
    void unReport() {
        if (bookOption.isSelected()) {
            Report selectedBook = (Report) bookList.getSelectionModel().getSelectedItem();
            if (adminManager.deleteReport(selectedBook, true)) {
                // reported book deleted with success
                obsBooksList.remove(selectedBook);
                addCustomFactory("book");
                resetRightDetail();
            } else {
                // can't delete reported book
                actionTarget.setText("Error: unable to remove reported book");
            }
        } else if (reviewOption.isSelected()) {
            Report selectedReview = (Report) reviewList.getSelectionModel().getSelectedItem();
            if (adminManager.deleteReport(selectedReview, true)) {
                // reported review deleted with success
                obsListReview.remove(selectedReview);
                addCustomFactory("review");
                resetRightDetail();
            } else {
                // can't delete reported review
                actionTarget.setText("Error: unable to remove reviewReport");
            }
        }
    }

    @FXML
    void searchAction() {
        clearList();
        resetRightDetail();
        deleteElemButton.setDisable(false);
        if (bookOption.isSelected()) {
            ArrayList<Report> list = adminManager.loadBookReported();
            obsBooksList.addAll(list);
            bookList.setVisible(true);
            authorsList.setVisible(false);
            usersList.setVisible(false);
            reviewList.setVisible(false);
            logsList.setVisible(false);
            addCustomFactory("book");
        } else if (userOption.isSelected()) {
            ArrayList<User> list = searchManager.searchUser(usernameField.getText());
            obsUserList.addAll(list);
            bookList.setVisible(false);
            authorsList.setVisible(false);
            usersList.setVisible(true);
            reviewList.setVisible(false);
            logsList.setVisible(false);
            addCustomFactory("user");
        } else if (authorOption.isSelected()) {
            ArrayList<Author> list = searchManager.searchAuthor(usernameField.getText());
            obsAuthorList.addAll(list);
            bookList.setVisible(false);
            authorsList.setVisible(true);
            usersList.setVisible(false);
            reviewList.setVisible(false);
            logsList.setVisible(false);
            addCustomFactory("author");
        } else if (reviewOption.isSelected()) {
            ArrayList<Report> listRev = adminManager.loadReviewReported();
            obsListReview.addAll(listRev);
            usersList.setVisible(false);
            authorsList.setVisible(false);
            bookList.setVisible(false);
            reviewList.setVisible(true);
            logsList.setVisible(false);
            addCustomFactory("review");
        }
    }

    private void addCustomFactory(String type) {
        if (type.equals("book")) {
            this.bookList.setCellFactory(new Callback<ListView<Report>, ListCell<Report>>() {
                @Override
                public ListCell<Report> call(ListView<Report> listView) {
                    return new ListReport();
                }
            });
            this.bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                        Report selectedBook = (Report) bookList.getSelectionModel().getSelectedItem();
                        if (selectedBook != null) {
                            selectedBookID = selectedBook.getBook_id();
                            nameTitle.setText(selectedBook.getTitle());
                            ArrayList<Author> authors = selectedBook.getAuthors();
                            ArrayList<String> authorsName = new ArrayList<>();
                            for (Author a : authors) {
                                authorsName.add(a.getName());
                            }
                            String author = String.join(", ", authorsName);
                            username.setText(author);
                            description.setText(selectedBook.getDescription());
                            follower.setText("-");
                            reviewText.setText("-");
                        }
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
                        if (selectedUser != null) {
                            nameTitle.setText(selectedUser.getName());
                            username.setText(selectedUser.getNickname());
                            follower.setText(((Integer) selectedUser.getFollowerCount()).toString());
                            description.setText("-");
                            reviewText.setText("-");
                        }
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
                        Author selectedAuthor = (Author) authorsList.getSelectionModel().getSelectedItem();
                        if (selectedAuthor != null) {
                            nameTitle.setText(selectedAuthor.getName());
                            username.setText(selectedAuthor.getNickname());
                            follower.setText(((Integer) selectedAuthor.getFollowerCount()).toString());
                            description.setText("-");
                            reviewText.setText("-");
                        }
                    }
                }
            });
        } else if (type.equals("review")) {
            reviewList.setCellFactory(new Callback<ListView<Report>, javafx.scene.control.ListCell<Report>>() {
                @Override
                public ListCell<Report> call(ListView<Report> listView) {
                    return new ListReport();
                }
            });
            this.reviewList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                        Report selectedRev = (Report) reviewList.getSelectionModel().getSelectedItem();
                        if (selectedRev != null) {
                            reviewText.setText(selectedRev.getReview_text());
                            username.setText(selectedRev.getUsername());
                            description.setText("-");
                            follower.setText("-");
                            nameTitle.setText("-");
                        }
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
        reviewOption.setSelected(false);
        unReportButton.setDisable(false);
    }

    @FXML
    public void selectAuthorCheckAction(ActionEvent actionEvent) {
        bookOption.setSelected(false);
        userOption.setSelected(false);
        authorOption.setSelected(true);
        reviewOption.setSelected(false);
        unReportButton.setDisable(true);
    }

    @FXML
    public void selectUserCheckAction(ActionEvent actionEvent) {
        bookOption.setSelected(false);
        authorOption.setSelected(false);
        userOption.setSelected(true);
        reviewOption.setSelected(false);
        unReportButton.setDisable(true);
    }

    @FXML
    public void selectReviewCheckAction(ActionEvent actionEvent) {
        bookOption.setSelected(false);
        authorOption.setSelected(false);
        userOption.setSelected(false);
        reviewOption.setSelected(true);
        unReportButton.setDisable(false);
    }

    private void clearList() {
        obsListReview.clear();
        obsBooksList.clear();
        obsUserList.clear();
        obsAuthorList.clear();
        obsListLog.clear();
    }

    @FXML
    void initialize() {
        clearList();
        reviewList.setItems(obsListReview);
        usersList.setItems(obsUserList);
        authorsList.setItems(obsAuthorList);
        bookList.setItems(obsBooksList);
        logsList.setItems(obsListLog);
        unReportButton.setDisable(true);
    }
}
