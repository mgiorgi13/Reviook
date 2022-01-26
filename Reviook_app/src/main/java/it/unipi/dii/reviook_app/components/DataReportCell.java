package it.unipi.dii.reviook_app.components;

import it.unipi.dii.reviook_app.entity.Report;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DataReportCell {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane pane;

    @FXML
    private Text title;

    @FXML
    private Text reviewText;

    @FXML
    private Text username;

    public DataReportCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/reportCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Report report) {
        if (report.getType().equals("book")) {
            title.setText(report.getTitle());
            reviewText.setVisible(false);
        } else if (report.getType().equals("review")) {
            if (report.getReview_text().length() > 150) {
                reviewText.setText(report.getReview_text().substring(0, Math.min(report.getReview_text().length(), 150)) + " ...");
            } else {
                reviewText.setText(report.getReview_text());
            }
            title.setVisible(false);
            username.setText(report.getUsername());
        }
    }

    public Pane getPane() {
        return pane;
    }

}
