package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import it.unipi.dii.reviook_app.entity.Review;
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
    private Text dateUpdateField;

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
        ratingField.setText(selectedReview.getRating());
        authorReviewField.setText(selectedReview.getUsername());
        reviewText.setText(selectedReview.getReview_text());
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy | hh:mm:ss");
        try {
            dateUpdateField.setText(outputFormat.format(inputFormat.parse(selectedReview.getDate_update())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
    }
}
