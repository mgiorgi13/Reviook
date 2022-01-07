package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Author;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataAuthorCell {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text followerCounter;

    @FXML
    private Text nameField;

    @FXML
    private Text nicknameField;

    @FXML
    private Pane pane;

    public DataAuthorCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/authorCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Author author) {
        nicknameField.setText(author.getNickname());
        nameField.setText(author.getName());
        followerCounter.setText(String.valueOf(author.getFollowerCount()));
    }

    public Pane getPane() {
        return pane;
    }
}
