package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import it.unipi.dii.reviook_app.components.ListReview;
import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Review;
import it.unipi.dii.reviook_app.entity.User;
import it.unipi.dii.reviook_app.manager.AdminManager;
import it.unipi.dii.reviook_app.manager.BookManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.util.Callback;


public class BookDetailController {

    @FXML
    public JFXButton deleteReviewButton;

    @FXML
    public JFXButton deleteBook;

    @FXML
    private ResourceBundle resources;

    @FXML
    private ImageView imageContainer;

    @FXML
    private URL location;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton addReviewButton;

    @FXML
    private JFXButton editReviewButton;

    @FXML
    private JFXButton reportButton;

    @FXML
    private ListView<Review> listView;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookCategories;

    @FXML
    private Text bookDescription, isbnKey, isbnValue, dataPubblication, languageCode, totalPage;

    @FXML
    private Text bookTitle;

    @FXML
    private Text ratingAVG;

    @FXML
    private HBox HBAuthor1, HBAuthor2, HBAuthor3, HBAuthor4, HBBook1, HBBook2, HBBook3, HBBook4;

    @FXML
    private Text suggestedAuthor1, suggestedAuthor2, suggestedAuthor3, suggestedAuthor4;

    @FXML
    private Text suggestedBook1, suggestedBook2, suggestedBook3, suggestedBook4;

    private ArrayList<Author> suggestedAuthors;
    private ArrayList<Book> suggestedBooks;

    Session session = Session.getInstance();

    private UserManager userManager = new UserManager();

    private String title, author, categories, description, img_url, book_id;

    private ArrayList<Review> reviewsList;

    private ObservableList<Review> observableList = FXCollections.observableArrayList();

    BookManager bookManager = new BookManager();
    AdminManager adminManager = new AdminManager();

    @FXML
    public void reportBookAction(ActionEvent actionEvent) {
        adminManager.ReportBook(new Book("", "", "", 0.0, description, 0, 0, 0, 0, "", book_id, 0, title, null, null, null));
    }

    public void setListView() {
        this.observableList.setAll(this.reviewsList);
        listView.setItems(this.observableList);
        listView.setCellFactory(new Callback<ListView<Review>, javafx.scene.control.ListCell<Review>>() {
            @Override
            public ListCell<Review> call(ListView<Review> listView) {
                return new ListReview();
            }
        });
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    Review selectedCell = (Review) listView.getSelectionModel().getSelectedItem();
                    try {
                        Parent dialogReview;
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/previewReview.fxml"));
                        dialogReview = (Parent) fxmlLoader.load();
                        PreviewReviewController prevRevContr = fxmlLoader.getController();
                        prevRevContr.setInfoReview(selectedCell);
                        Stage dialogNewReviewStage = new Stage();
                        Scene dialogScene = new Scene(dialogReview);
                        dialogNewReviewStage.setScene(dialogScene);
                        dialogNewReviewStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Float ratingSum = 0.0f;
        DecimalFormat df = new DecimalFormat("#.#");
        if (listView.getItems().size() > 0) {
            for (Review r : listView.getItems()) {
                ratingSum += Float.parseFloat(r.getRating());
            }
            ratingAVG.setText(String.valueOf(df.format(ratingSum / listView.getItems().size())));
        } else {
            ratingAVG.setText(df.format(ratingSum));
        }
    }

    private void setOnMouseClicked(HBox HbSuggestion, Integer index, String type) {
        HbSuggestion.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    if (type.equals("Book")) {
                        Book bookSuggested = suggestedBooks.get(index);

                        try {
                            Parent bookInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                            bookInterface = (Parent) fxmlLoader.load();
                            BookDetailController bookDetailController = fxmlLoader.getController();
                            bookDetailController.setInfoBook(bookSuggested, true);
                            Stage actual_stage = (Stage) searchButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(bookInterface));
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
    void homeAction(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;
        if (session.getIsAuthor()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
            homeInterface = (Parent) fxmlLoader.load();
            AuthorInterfaceController authorInterfaceController = fxmlLoader.getController();
            authorInterfaceController.setAuthor(session.getLoggedAuthor());
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
            homeInterface = (Parent) fxmlLoader.load();
            UserInterfaceController userInterfaceController = fxmlLoader.getController();
            userInterfaceController.setUser(session.getLoggedUser());
        }
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    @FXML
    void toRead(ActionEvent event) throws IOException {
        String bookTitleText = bookTitle.getText();
        if (session.getLoggedAuthor() != null)
            userManager.toReadAdd("Author", session.getLoggedAuthor().getNickname(), this.book_id);
        else
            userManager.toReadAdd("User", session.getLoggedUser().getNickname(), this.book_id);
    }

    @FXML
    void readed(ActionEvent event) throws IOException {
        String bookTitleText = bookTitle.getText();
        if (session.getLoggedAuthor() != null)
            userManager.readAdd("Author", session.getLoggedAuthor().getNickname(), this.book_id);
        else
            userManager.readAdd("User", session.getLoggedUser().getNickname(), this.book_id);
    }

    @FXML
    public void addReviewAction(ActionEvent actionEvent) throws IOException {
        Stage dialogNewReviewStage = new Stage();
        Parent dialogInterface;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/dialogNewReview.fxml"));
        dialogInterface = (Parent) fxmlLoader.load();
        DialogNewReviewController controller = fxmlLoader.getController();
        controller.setBook_id(this.book_id, this.observableList, this.ratingAVG);
        Scene dialogScene = new Scene(dialogInterface);
        dialogNewReviewStage.setScene(dialogScene);
        dialogNewReviewStage.show();
    }

    @FXML
    public void editReviewAction(ActionEvent actionEvent) throws IOException {
        Review selectedReview = (Review) listView.getSelectionModel().getSelectedItem();
        if (selectedReview == null) {
            return;
        }
        if (session.getLoggedUser() != null && !selectedReview.getUser_id().equals(session.getLoggedUser().getNickname())) {
            return;
        }
        if (session.getLoggedAuthor() != null && !selectedReview.getUser_id().equals(session.getLoggedAuthor().getNickname())) {
            return;
        }
        Stage dialogNewReviewStage = new Stage();
        Parent dialogInterface;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/dialogNewReview.fxml"));
        dialogInterface = (Parent) fxmlLoader.load();
        DialogNewReviewController controller = fxmlLoader.getController();
        controller.setBook_id(this.book_id, this.observableList, this.ratingAVG);
        controller.setEditReview(selectedReview); //set to edit review fields
        Scene dialogScene = new Scene(dialogInterface);
        dialogNewReviewStage.setScene(dialogScene);
        dialogNewReviewStage.show();
    }

    @FXML
    public void deleteReviewAction(ActionEvent actionEvent) {
        Review selectedReview = (Review) listView.getSelectionModel().getSelectedItem();
        if (selectedReview == null) {
            return;
        }
        if (session.getLoggedUser() != null && !selectedReview.getUser_id().equals(session.getLoggedUser().getNickname())) {
            return;
        }
        if (session.getLoggedAuthor() != null && !selectedReview.getUser_id().equals(session.getLoggedAuthor().getNickname())) {
            return;
        }
        try {
            bookManager.DeleteReview(selectedReview.getReview_id(), this.book_id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Book book = bookManager.getBookByID(this.book_id); // query to update review
            this.reviewsList = book.getReviews();
            setListView();
            DecimalFormat df = new DecimalFormat("#.#");
            this.ratingAVG.setText(df.format(book.getAverage_rating()));
        }
    }

    @FXML
    public void putLikeAction() {
        Review selectedReview = (Review) listView.getSelectionModel().getSelectedItem();
        if (selectedReview == null) {
            return;
        }
        if (session.getLoggedUser() != null) {
            if (selectedReview.getLiked()) {
                // I already liked it
                session.getLoggedUser().removeReviewID(selectedReview.getReview_id());
                bookManager.removeLikeReview(selectedReview.getReview_id(), this.book_id);
            } else {
                session.getLoggedUser().addReviewID(selectedReview.getReview_id());
                bookManager.addLikeReview(selectedReview.getReview_id(), this.book_id);
            }
        } else if (session.getLoggedAuthor() != null) {
            if (selectedReview.getLiked()) {
                // I already liked it
                session.getLoggedAuthor().removeReviewID(selectedReview.getReview_id());
                bookManager.removeLikeReview(selectedReview.getReview_id(), this.book_id);
            } else {
                session.getLoggedAuthor().addReviewID(selectedReview.getReview_id());
                bookManager.addLikeReview(selectedReview.getReview_id(), this.book_id);
            }
        }
        setListView();
    }

    private String truckString(String input) {
        if (input.length() > 24) {
            return input.substring(0, 24);
        }
        return input;
    }

    private void viewSuggestedAuthors(String book_id) {
        suggestedAuthors = bookManager.similarAuthors(book_id);
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

    private void viewSuggestedBooks(String book_id) {
        suggestedBooks = bookManager.similarBooks(book_id);
        Collections.shuffle(suggestedBooks);

        HBBook1.setVisible(false);
        HBBook2.setVisible(false);
        HBBook3.setVisible(false);
        HBBook4.setVisible(false);

        int size = suggestedBooks.size();

        if (size >= 1) {
            HBBook1.setVisible(true);
            suggestedBook1.setText(truckString(suggestedBooks.get(0).getTitle()));
            setOnMouseClicked(HBBook1, 0, "Book");
        }
        if (size >= 2) {
            HBBook2.setVisible(true);
            suggestedBook2.setText(truckString(suggestedBooks.get(1).getTitle()));
            setOnMouseClicked(HBBook2, 1, "Book");
        }
        if (size >= 3) {
            HBBook3.setVisible(true);
            suggestedBook3.setText(truckString(suggestedBooks.get(2).getTitle()));
            setOnMouseClicked(HBBook3, 2, "Book");
        }
        if (size >= 4) {
            HBBook4.setVisible(true);
            suggestedBook4.setText(truckString(suggestedBooks.get(3).getTitle()));
            setOnMouseClicked(HBBook4, 3, "Book");
        }

    }

    public void setInfoBook(Book bookSelected, boolean fromSuggestion) {
        if (fromSuggestion) {
            bookSelected = bookManager.getBookByID(bookSelected.getBook_id());
        }

        //book&author suggestion
        viewSuggestedBooks(bookSelected.getBook_id());
        viewSuggestedAuthors(bookSelected.getBook_id());

        if (bookSelected.getIsbn() != null) {
            isbnKey.setText("ISBN");
            isbnValue.setText(bookSelected.getIsbn());
        } else if (bookSelected.getAsin() != null) {
            isbnKey.setText("ASIN");
            isbnValue.setText(bookSelected.getAsin());
        } else isbnValue.setText("-");
        if (bookSelected.getNum_pages() != null) {
            totalPage.setText(String.valueOf(bookSelected.getNum_pages()));
        } else totalPage.setText("-");
        if (bookSelected.getPublication_month() != null && bookSelected.getPublication_day() != null && bookSelected.getPublication_year() != null) {
            dataPubblication.setText(String.valueOf(bookSelected.getPublication_day()) + "/" + String.valueOf(bookSelected.getPublication_month()) + "/" + String.valueOf(bookSelected.getPublication_year()));
        } else dataPubblication.setText("-");
        if (bookSelected.getLanguage_code() != null) {
            languageCode.setText(String.valueOf(bookSelected.getLanguage_code()));
        } else languageCode.setText("-");

        deleteBook.setVisible(false);
        if (session.getLoggedAuthor() != null) {
            if (BookManager.foundMyBook(bookSelected.getBook_id(), session.getLoggedAuthor().getId()))
                deleteBook.setVisible(true);
        }
        // BOOK TITLE
        this.title = bookSelected.getTitle();
        bookTitle.setText(this.title);
        // AUTHORS LIST
        ArrayList<String> authors = bookSelected.getAuthors();
        this.author = String.join(", ", authors);
        bookAuthor.setText(this.author);
        // CATEGORIES LIST
        ArrayList<String> genres = bookSelected.getGenres();
        if (genres != null) {
            categories = String.join(", ", genres);
            bookCategories.setText(categories);
        }
        // BOOK DESCRIPTION
        this.description = bookSelected.getDescription();
        bookDescription.setText(this.description);
        // IMG BOOK
        this.img_url = bookSelected.getImage_url();
        try {
            Image image = new Image(this.img_url);
            imageContainer.setImage(image);
        } catch (Exception e) {
        }

        // REVIEW LIST
        this.reviewsList = bookSelected.getReviews();
        setListView();
        // BOOK ID
        this.book_id = bookSelected.getBook_id();
    }

    @FXML
    void deleteBookFun(ActionEvent event) throws IOException {
        if (BookManager.deleteBook(book_id)) {
            Parent userInterface;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            Stage actual_stage = (Stage) deleteBook.getScene().getWindow();
            actual_stage.setScene(new Scene(userInterface));
            actual_stage.setResizable(false);
            actual_stage.show();
            actual_stage.centerOnScreen();
        }
        return;
    }

    @FXML
    void reportAction() {
        Review selectedReview = (Review) listView.getSelectionModel().getSelectedItem();
        adminManager.ReportReview(selectedReview, book_id);
    }


}

