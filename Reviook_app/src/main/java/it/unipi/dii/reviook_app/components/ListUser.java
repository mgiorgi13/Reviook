package it.unipi.dii.reviook_app.components;

import javafx.scene.control.ListCell;
import it.unipi.dii.reviook_app.entity.User;

public class ListUser extends ListCell<User> {
    @Override
    public void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (user != null) {
            DataUserCell dataUserCell = new DataUserCell();
            dataUserCell.setInfo(user);
            setGraphic(dataUserCell.getPane());
        } else {
            setGraphic(null);
        }
    }
}
