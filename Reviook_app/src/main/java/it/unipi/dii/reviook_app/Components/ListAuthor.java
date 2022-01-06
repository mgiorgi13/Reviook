package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Author;
import javafx.scene.control.ListCell;

public class ListAuthor extends ListCell<Author> {
    @Override
    public void updateItem(Author author, boolean empty) {
        super.updateItem(author, empty);
        if (author != null) {
            DataAuthorCell dataAuthorCell = new DataAuthorCell();
            dataAuthorCell.setInfo(author);
            setGraphic(dataAuthorCell.getPane());
        }
    }
}