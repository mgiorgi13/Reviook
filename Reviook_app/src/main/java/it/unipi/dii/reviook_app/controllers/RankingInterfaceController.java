package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Session;
import it.unipi.dii.reviook_app.entity.Genre;
import it.unipi.dii.reviook_app.entity.RankingObject;
import it.unipi.dii.reviook_app.manager.BookManager;
import it.unipi.dii.reviook_app.manager.SearchManager;
import it.unipi.dii.reviook_app.manager.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RankingInterfaceController {
    @FXML
    private JFXListView<String> yearBooksJFX;

    @FXML
    private JFXListView<String> desiredBooksJFX;

    @FXML
    private JFXListView<String> popularBooksJFX;

    @FXML
    private JFXListView<RankingObject> reliableUsersJFX;

    @FXML
    private Label labelError;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton profileButton;

    @FXML
    private ChoiceBox yearsFilter;


    private Session session = Session.getInstance();

    private ObservableList<String> availableChoices = FXCollections.observableArrayList();


    private ObservableList<String> yearBooksObs = FXCollections.observableArrayList();
    private ObservableList<String> desiredBooksObs = FXCollections.observableArrayList();
    private ObservableList<String> popularBooksObs = FXCollections.observableArrayList();
    private ObservableList<RankingObject> reliableUsersObs = FXCollections.observableArrayList();

    private SearchManager searchManager = new SearchManager();
    private BookManager bookManager = new BookManager();
    private UserManager userManager = new UserManager();

    private void clearLists() {
        yearBooksObs.clear();
        desiredBooksObs.clear();
        popularBooksObs.clear();
        reliableUsersObs.clear();
    }

    private void hideLists() {
        yearBooksJFX.setVisible(false);
        desiredBooksJFX.setVisible(false);
        popularBooksJFX.setVisible(false);
        reliableUsersJFX.setVisible(false);
    }

    @FXML
    public void annualPublications(ActionEvent actionEvent) {
        clearLists();
        hideLists();
        yearBooksObs.clear();
        yearBooksJFX.setVisible(true);

        String selectedChoice = yearsFilter.getSelectionModel().getSelectedItem() == null ? "" : yearsFilter.getSelectionModel().getSelectedItem().toString();
        if (selectedChoice.equals("")) {
            labelError.setText("You must enter the year ->");
        } else {
            labelError.setText("");
            ArrayList<Genre> array = bookManager.searchRankBook(Integer.valueOf(selectedChoice));

            String genres;
            ArrayList<String> genresList = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                if (i % 2 == 0) {
                    genres = /*"genres: " +*/ array.get(i).getType() + "\n\n    - total: " + array.get(i).getValue().intValue();
                    genresList.add(genres);
                }
            }
            yearBooksObs.addAll(genresList);
        }
    }

    @FXML
    public void mostReliableUsers(ActionEvent actionEvent) {
        clearLists();
        hideLists();
        reliableUsersObs.addAll(bookManager.rankReview());
        reliableUsersJFX.setVisible(true);
    }

    @FXML
    public void mostPopularBooks(ActionEvent actionEvent) {
        clearLists();
        hideLists();
        ArrayList<RankingObject> res = bookManager.topBooks("READ", 100);
        for (RankingObject b : res) {
//            popularBooksObs.add(b.getCount() + " users read : " + b.getName());
            popularBooksObs.add(
                    "Title: " +
                            b.getName() +
                            "\n\n   - " +
                            b.getCount() + " users read it"
            );
        }
        popularBooksJFX.setVisible(true);

    }

    @FXML
    public void mostDesiredBooks(ActionEvent actionEvent) {
        clearLists();
        hideLists();
        ArrayList<RankingObject> res = bookManager.topBooks("TO_READ", 100);
        for (RankingObject b : res) {
//            desiredBooksObs.add(b.getCount() + " users are interested in : " + b.getName());
            desiredBooksObs.add(
                    "Title: " +
                            b.getName() +
                            "\n\n   - " +
                            b.getCount() + " users are interested"
            );
        }
        desiredBooksJFX.setVisible(true);
    }

    @FXML
    void homeInterface(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;
        if (session.getIsAuthor()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
            homeInterface = (Parent) fxmlLoader.load();
            AuthorInterfaceController authorInterfaceController = fxmlLoader.getController();
            authorInterfaceController.setAuthor(session.getLoggedAuthor());
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
            homeInterface = (Parent) fxmlLoader.load();
            UserInterfaceController userInterfaceController = fxmlLoader.getController();
            userInterfaceController.setUser(session.getLoggedUser());
        }
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
        actual_stage.centerOnScreen();
    }

    @FXML
    void initialize() {
        availableChoices.addAll(searchManager.searchYears());
        yearsFilter.setItems(availableChoices);
        yearsFilter.setVisible(true);
        yearBooksJFX.setItems(yearBooksObs);
        reliableUsersJFX.setItems(reliableUsersObs);
        popularBooksJFX.setItems(popularBooksObs);
        desiredBooksJFX.setItems(desiredBooksObs);

    }
}
