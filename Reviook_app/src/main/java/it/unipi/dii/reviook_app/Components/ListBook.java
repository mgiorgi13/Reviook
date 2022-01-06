package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Book;
import javafx.scene.control.ListCell;

public class ListBook extends ListCell<Book> {
    @Override
    public void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);
        if (book != null) {
            DataBookCell dataBookCell = new DataBookCell();
            dataBookCell.setInfo(book);
            setGraphic(dataBookCell.getPane());
        }
    }
}