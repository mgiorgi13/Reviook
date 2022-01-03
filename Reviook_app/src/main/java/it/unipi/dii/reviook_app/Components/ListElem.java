package it.unipi.dii.reviook_app.Components;

import javafx.scene.control.ListCell;

public class ListElem extends ListCell<String> {
    @Override
    public void updateItem(String string, boolean empty) {
        super.updateItem(string, empty);
        if (string != null) {
            DataListElem dataListElem = new DataListElem();
            dataListElem.setInfo(string);
            setGraphic(dataListElem.getPane());
        }
    }
}