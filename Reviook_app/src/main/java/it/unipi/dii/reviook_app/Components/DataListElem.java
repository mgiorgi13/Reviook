package it.unipi.dii.reviook_app.Components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class DataListElem {

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

    public DataListElem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/listElem.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(String string) {
        previewText.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
        user.setText("user");
        like.setText("10");
        helpful.setText("10");
    }

    public Pane getPane() {
        return pane;
    }
}