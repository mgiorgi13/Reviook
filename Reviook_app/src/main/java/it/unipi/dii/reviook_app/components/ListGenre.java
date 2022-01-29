package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Genre;
import javafx.scene.control.ListCell;

public class ListGenre extends ListCell<Genre> {
    @Override
    public void updateItem(Genre genre, boolean empty) {
        super.updateItem(genre, empty);
        if (genre != null) {
            DataGenreCell dataGenreCell = new DataGenreCell();
            dataGenreCell.setInfo(genre);
            setGraphic(dataGenreCell.getPane());
        } else {
            setGraphic(null);
        }
    }
}
