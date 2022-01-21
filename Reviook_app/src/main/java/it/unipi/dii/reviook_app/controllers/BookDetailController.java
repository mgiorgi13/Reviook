package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import it.unipi.dii.reviook_app.components.ListReview;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Review;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.util.Callback;


public class BookDetailController {
    @FXML
    public JFXButton deleteReviewButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private ImageView imageContainer;

    @FXML
    private URL location;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton addReviewButton;

    @FXML
    private JFXButton editReviewButton;

    @FXML
    private ListView<Review> listView;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookCategories;

    @FXML
    private Text bookDescription;

    @FXML
    private Text bookTitle;

    @FXML
    private Text ratingAVG;

    Session session = Session.getInstance();

    private UserManager userManager = new UserManager();

    private String title, author, categories, description, img_url, book_id;

    private ArrayList<Review> reviewsList;

    private ObservableList<Review> observableList = FXCollections.observableArrayList();

    BookManager bookManager = new BookManager();

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

    @FXML
    void searchInterface(ActionEvent event) throws IOException {
        Parent searchInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/search.fxml"));
        Stage actual_stage = (Stage) searchButton.getScene().getWindow();
        actual_stage.setScene(new Scene(searchInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
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
                setListView();
            } else {
                session.getLoggedUser().addReviewID(selectedReview.getReview_id());
                bookManager.addLikeReview(selectedReview.getReview_id(), this.book_id);
                setListView();
            }
        } else if (session.getLoggedAuthor() != null) {
            if (selectedReview.getLiked()) {
                // I already liked it
                session.getLoggedAuthor().removeReviewID(selectedReview.getReview_id());
                bookManager.removeLikeReview(selectedReview.getReview_id(), this.book_id);
                setListView();
            } else {
                session.getLoggedAuthor().addReviewID(selectedReview.getReview_id());
                bookManager.addLikeReview(selectedReview.getReview_id(), this.book_id);
                setListView();
            }
        }

    }

    public void setInfoBook(Book bookSelected) {
        // BOOK TITLE
        this.title = bookSelected.getTitle();
        bookTitle.setText(this.title);
        // AUTHORS LIST
        ArrayList<String> authors = bookSelected.getAuthors();
        this.author = authors.get(0); // TODO per ora prendo solo il primo ma poi andranno elencati tutti
        bookAuthor.setText(this.author);
        // CATEGORIES LIST
        ArrayList<String> genres = bookSelected.getGenres();
        if (genres != null) {
            categories = genres.size() > 0 ? genres.get(0) : ""; // TODO per ora prendo solo il primo ma poi andranno elencati tutti
            bookCategories.setText(categories);
        }
        // BOOK DESCRIPTION
        this.description = bookSelected.getDescription();
        bookDescription.setText(this.description);
        // IMG BOOK
        this.img_url = bookSelected.getImage_url();
        if (this.img_url != null) {
            Image image = new Image(this.img_url);
            imageContainer.setImage(image);
        }
        // REVIEW LIST
        this.reviewsList = bookSelected.getReviews();
        setListView();
        // BOOK ID
        this.book_id = bookSelected.getBook_id();
    }

    @FXML
    void initialize() {
    }


}

