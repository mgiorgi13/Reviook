package it.unipi.dii.reviook_app.Controllers;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

import it.unipi.dii.reviook_app.Data.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PreviewReviewController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text authorReviewField;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Text ratingField;

    @FXML
    private Text reviewText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void closeDialogAction(ActionEvent event) {
        Stage actual_stage = (Stage) closeButton.getScene().getWindow();
        actual_stage.close();
    }

    public void setInfoReview(Review selectedReview) {
//        scrollPane.
        ratingField.setText(selectedReview.getRating());
        authorReviewField.setText(selectedReview.getUser_id());
        reviewText.setText(selectedReview.getReview_text());
    }

    @FXML
    void initialize() {

    }
}
