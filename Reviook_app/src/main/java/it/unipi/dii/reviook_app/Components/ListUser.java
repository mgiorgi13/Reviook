package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Book;
import javafx.scene.control.ListCell;
import it.unipi.dii.reviook_app.Data.Users;

public class ListUser extends ListCell<Users> {
    @Override
    public void updateItem(Users user, boolean empty) {
        super.updateItem(user, empty);
        if (user != null) {
            DataUserCell dataUserCell = new DataUserCell();
            dataUserCell.setInfo(user);
            setGraphic(dataUserCell.getPane());
        }
    }
}
