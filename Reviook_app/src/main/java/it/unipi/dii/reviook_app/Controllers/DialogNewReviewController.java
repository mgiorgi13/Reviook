package it.unipi.dii.reviook_app.Controllers;

import com.jfoenix.controls.JFXButton;
import com.mongodb.MongoException;
import it.unipi.dii.reviook_app.Data.Book;
import it.unipi.dii.reviook_app.Data.Review;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Manager.BookManager;
import it.unipi.dii.reviook_app.Manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    UserManager userManager = new UserManager();
    BookManager bookManager = new BookManager();

    @FXML
    public void setBook_id(String id) {
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
                // TODO deve partire la query per aggiornaere il book detail
                Book book = bookManager.getBookByID(this.book_id); // query to update review
                Parent bookInterface;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
                bookInterface = (Parent) fxmlLoader.load();
                BookDetailController bookController = fxmlLoader.getController();
                bookController.setInfoBook(book); // DOVERBBE FARE LA RELOAD DELLE INFO BOOK
                bookController.clearlist();
                Stage actual_stage = (Stage) addButton.getScene().getWindow();
                actual_stage.close();
            }
        } else {
            try {
                bookManager.AddReviewToBook(reviewText, ratingBook, book_id);
            } catch (MongoException e) {
                e.printStackTrace();
            } finally {
                // TODO deve partire la query per aggiornaere il book detail
//                Parent bookInterface;
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail.fxml"));
//                bookInterface = (Parent) fxmlLoader.load();
//                BookDetailController bookController = fxmlLoader.getController();
//                bookController.setInfoBook(selectedCell);

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
