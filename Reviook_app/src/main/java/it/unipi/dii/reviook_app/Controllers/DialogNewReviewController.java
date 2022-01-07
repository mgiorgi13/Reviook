package it.unipi.dii.reviook_app.Controllers;

import com.jfoenix.controls.JFXButton;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.Window;

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

    Session session = Session.getInstance();

    @FXML
    public void setBook_id(String id) {
        this.book_id = id;
    }

    @FXML

    public void exitDialogAction(ActionEvent actionEvent) {
        Stage actual_stage = (Stage) exitButton.getScene().getWindow();
        actual_stage.close();
    }

    @FXML
    public void addReviewAction(ActionEvent actionEvent) {
        String reviewText = newReviewText.getText();
        Integer ratingBook = bookStars.getValue();
        String book_id = this.book_id;
        if (session.getLoggedUser() != null) {
            String loggedUserID = session.getLoggedUser().getNickname();
            // query to mongo DB to insert new review
            System.out.println("book ID: " + book_id + " review Text: " + reviewText + " stars:" + ratingBook + " by " + loggedUserID);
        } else {
            String loggedAuthorID = session.getLoggedAuthor().getNickname();
            // query to mongo DB to insert new review
            System.out.println("book ID: " + book_id + " review Text: " + reviewText + " stars:" + ratingBook + " by " + loggedAuthorID);
        }
    }

    @FXML
    void initialize() {
        ObservableList<Integer> choice = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        bookStars.setItems(choice);
    }
}
