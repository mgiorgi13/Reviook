package it.unipi.dii.reviook_app.Controllers;

import com.jfoenix.controls.JFXButton;
import com.mongodb.MongoException;
import it.unipi.dii.reviook_app.Data.Book;
import it.unipi.dii.reviook_app.Data.Review;
import it.unipi.dii.reviook_app.Manager.BookManager;
import it.unipi.dii.reviook_app.Manager.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

public class DialogNewReviewController {
    @FXML
    public TextArea newReviewText;

    @FXML
    public JFXButton exitButton;

    @FXML
    public JFXButton addButton;

    @FXML
    private ChoiceBox<Integer> bookStars;

    private String book_id;

    private Boolean editMode = false;

    private Review reviewToEdit;

    private ObservableList<Review> previousList;
    private Text previousRatingAVG;
    private BookDetailController controll;

    UserManager userManager = new UserManager();
    BookManager bookManager = new BookManager();

    public DialogNewReviewController() {
    }

    @FXML
    public void setBook_id(String id, ObservableList<Review> list, Text ratingAVG) {
        this.previousList = list;
        this.previousRatingAVG = ratingAVG;
        this.book_id = id;
    }

    @FXML
    public void setEditReview(Review reviewToEdit) {
        this.editMode = true;
        this.reviewToEdit = reviewToEdit;
        newReviewText.setText(reviewToEdit.getReview_text());
        bookStars.setValue(Integer.parseInt(reviewToEdit.getRating()));
    }

    @FXML
    public void exitDialogAction(ActionEvent actionEvent) {
        Stage actual_stage = (Stage) exitButton.getScene().getWindow();
        actual_stage.close();
    }

    @FXML
    public void addReviewAction(ActionEvent actionEvent) throws IOException {
        String reviewText = newReviewText.getText();
        Integer ratingBook = bookStars.getValue() != null ? bookStars.getValue() : 0;
        String book_id = this.book_id;
        if (editMode) {
            try {
                bookManager.EditReview(reviewText, ratingBook, book_id, this.reviewToEdit.getReview_id());
            } catch (MongoException e) {
                e.printStackTrace();
            } finally {
                // TODO la query deve aggiornare il rating AVG
                Book book = bookManager.getBookByID(this.book_id); // query to update review
                this.previousList.setAll(book.getReviews());
                DecimalFormat df = new DecimalFormat("#.#");
                this.previousRatingAVG.setText(df.format(book.getAverage_rating()));
                Stage actual_stage = (Stage) addButton.getScene().getWindow();
                actual_stage.close();
            }
        } else {
            try {
                bookManager.AddReviewToBook(reviewText, ratingBook, book_id);
            } catch (MongoException e) {
                e.printStackTrace();
            } finally {
                // TODO la query deve aggiornare il rating AVG
                Book book = bookManager.getBookByID(this.book_id); // query to update review
                this.previousList.setAll(book.getReviews());
                DecimalFormat df = new DecimalFormat("#.#");
                this.previousRatingAVG.setText(df.format(book.getAverage_rating()));
                Stage actual_stage = (Stage) addButton.getScene().getWindow();
                actual_stage.close();
            }
        }

    }

    @FXML
    void initialize() {
        ObservableList<Integer> choice = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        bookStars.setItems(choice);
    }
}
