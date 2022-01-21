package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.Session;
import it.unipi.dii.reviook_app.entity.Review;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class DataReviewCell {

    @FXML
    private Text like;

    @FXML
    private Text helpful;

    @FXML
    private Pane pane;

    @FXML
    private Text previewText;

    @FXML
    private Text user;

    @FXML
    private Text ratingField;

    @FXML
    private FontAwesomeIconView likeIcon;

    Session session = Session.getInstance();

    public DataReviewCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/reviewCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Review review) {
        String reviewText = review.getReview_text();
        if (reviewText.length() > 350) {
            previewText.setText(review.getReview_text().substring(0, Math.min(review.getReview_text().length(), 350)) + " ...");
        } else {
            previewText.setText(reviewText);
        }
        user.setText(review.getUsername());
        like.setText(review.getLikes().toString());
//        helpful.setText(review.getHelpful());
        ratingField.setText(review.getRating().toString());
        if (session.getLoggedUser() != null) {
            ArrayList<String> listReviewID = session.getLoggedUser().getListReviewID();
            if (listReviewID.contains(review.getReview_id())) {
                likeIcon.setVisible(true);
                review.setLiked();
            } else {
                likeIcon.setVisible(false);
                review.unseLiked();
            }
        } else if (session.getLoggedAuthor() != null) {
            ArrayList<String> listReviewID = session.getLoggedAuthor().getListReviewID();
            if (listReviewID.contains(review.getReview_id())) {
                likeIcon.setVisible(true);
                review.setLiked();
            } else {
                likeIcon.setVisible(false);
                review.unseLiked();
            }
        }
    }

    public Pane getPane() {
        return pane;
    }
}