package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Genre;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class DataGenreCell {

    @FXML
    private Text genre;

    @FXML
    private Pane pane;

    public DataGenreCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/genreCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Genre gen) {
        genre.setText(gen.getType());
    }

    public Pane getPane() {
        return pane;
    }
}
