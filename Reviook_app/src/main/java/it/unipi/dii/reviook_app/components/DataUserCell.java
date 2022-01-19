package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataUserCell {

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

    public DataUserCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/userCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(User user) {
        nicknameField.setText(user.getNickname());
        nameField.setText(user.getName());
        followerCounter.setText(String.valueOf(user.getFollowerCount()));
    }

    public Pane getPane() {
        return pane;
    }
}
