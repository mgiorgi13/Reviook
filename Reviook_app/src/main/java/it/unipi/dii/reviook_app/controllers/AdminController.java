package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXCheckBox authorOption;

    @FXML
    private JFXCheckBox bookOption;

    @FXML
    private JFXButton deleteButtonList;

    @FXML
    private JFXButton deleteReviewButton;

    @FXML
    private JFXListView<?> reviewList;

    @FXML
    private JFXButton searchButton;

    @FXML
    private JFXButton logoutButton;

    @FXML
    private JFXListView<?> searchList;

    @FXML
    private JFXCheckBox userOption;

    @FXML
    void logoutActon(ActionEvent event) {

    }

    @FXML
    void deleteListElem(ActionEvent event) {

    }

    @FXML
    void deleteReviewAction(ActionEvent event) {

    }

    @FXML
    void searchAction(ActionEvent event) {

    }

}
