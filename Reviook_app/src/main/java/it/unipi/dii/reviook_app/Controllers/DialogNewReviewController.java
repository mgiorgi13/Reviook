package it.unipi.dii.reviook_app.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DialogNewReviewController {
    @FXML
    public TextArea newReviewText;

    @FXML
    public JFXButton exitButton;

    @FXML
    public JFXButton addButton;

    @FXML
    public void exitDialogAction(ActionEvent actionEvent) {
        Stage actual_stage = (Stage) exitButton.getScene().getWindow();
        actual_stage.close();
    }

    @FXML
    public void addReviewAction(ActionEvent actionEvent) {

    }
}
