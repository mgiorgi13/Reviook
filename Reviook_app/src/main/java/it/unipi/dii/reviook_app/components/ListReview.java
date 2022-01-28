package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Review;
import javafx.scene.control.ListCell;

public class ListReview extends ListCell<Review> {
    @Override
    public void updateItem(Review review, boolean empty) {
        super.updateItem(review, empty);
        if (review != null) {
            DataReviewCell dataReviewCell = new DataReviewCell();
            dataReviewCell.setInfo(review);
            setGraphic(dataReviewCell.getPane());
        } else {
            setGraphic(null);
        }
    }
}