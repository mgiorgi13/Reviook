package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Author;
import javafx.scene.control.ListCell;

public class ListAuthor extends ListCell<Author> {
    @Override
    public void updateItem(Author author, boolean empty) {
        super.updateItem(author, empty);
        if (author != null) {
            DataAuthorCell dataAuthorCell = new DataAuthorCell();
            dataAuthorCell.setInfo(author);
            setGraphic(dataAuthorCell.getPane());
        } else {
            setGraphic(null);
        }
    }
}