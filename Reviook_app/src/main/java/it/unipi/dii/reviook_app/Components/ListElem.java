package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Review;
import javafx.scene.control.ListCell;

public class ListElem extends ListCell<Review> {
    @Override
    public void updateItem(Review review, boolean empty) {
        super.updateItem(review, empty);
        if (review != null) {
            DataListElem dataListElem = new DataListElem();
            dataListElem.setInfo(review);
            setGraphic(dataListElem.getPane());
        }
    }
}