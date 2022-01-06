package it.unipi.dii.reviook_app.Components;

import it.unipi.dii.reviook_app.Data.Book;
import it.unipi.dii.reviook_app.Data.Review;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataBookCell {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text authorsField;

    @FXML
    private Text likeCounter;

    @FXML
    private Pane pane;

    @FXML
    private Text reviewCounter;

    @FXML
    private Text titleField;

    public DataBookCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Book book) {
        titleField.setText(book.getTitle());
        authorsField.setText(book.getAuthors().get(0));
        likeCounter.setText(String.valueOf(book.getRatings_count()));
        reviewCounter.setText(String.valueOf(book.getReviews().size()));
    }

    public Pane getPane() {
        return pane;
    }
}
