package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Review;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DataBookCell {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text authorsField;

    @FXML
    private Pane pane;

    @FXML
    private Text reviewCounter;

    @FXML
    private Text ratingCounter;

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
        String title = book.getTitle();
        if (title.length() > 65) {
            titleField.setText(book.getTitle().substring(0, Math.min(book.getTitle().length(), 65)) + " ...");
        } else {
            titleField.setText(title);
        }
//        titleField.setText(book.getTitle());
        if (book.getAuthors().size() > 0) {
            ArrayList<String> authorsName = new ArrayList<>();
            for (Author a : book.getAuthors()) {
                authorsName.add(a.getName());
            }
            String authors = String.join(", ", authorsName);
            authorsField.setText(authors);
//            authorsField.setText(book.getAuthors().get(0).getNickname());
        } else {
            authorsField.setText("");
        }
        reviewCounter.setText(String.valueOf(book.getReviews().size()));
        Float ratingSum = 0.0f;
        DecimalFormat df = new DecimalFormat("#.#");
        if (book.getReviews().size() > 0) {
            for (Review r : book.getReviews()) {
                ratingSum += Float.parseFloat(r.getRating());
                // System.out.println(r.getRating());
            }
            ratingCounter.setText(String.valueOf(df.format(ratingSum / book.getReviews().size())));
        } else {
            ratingCounter.setText(String.valueOf(df.format(ratingSum)));
        }

    }

    public Pane getPane() {
        return pane;
    }
}
