package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Review;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

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

    public DataReviewCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/listElem.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Review review) {
        previewText.setText(review.getReview_text());
        user.setText(review.getUser_id());
        like.setText(review.getN_votes());
        helpful.setText(review.getHelpful());
    }

    public Pane getPane() {
        return pane;
    }
}