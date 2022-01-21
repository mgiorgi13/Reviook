package it.unipi.dii.reviook_app.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Session;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Genre;
import it.unipi.dii.reviook_app.manager.SearchManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class RankingInterfaceController {
    @FXML
    private JFXListView<String> bookList;

    @FXML
    private Label labelError;

    @FXML
    private JFXListView<String> usersList;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton profileButton;

    @FXML
    private CheckBox booksRank;

    @FXML
    private CheckBox usersRank;

    @FXML
    private ChoiceBox yearsFilter;

    @FXML
    private TextField searchText;

    private Session session = Session.getInstance();

    private ObservableList<String> availableChoices = FXCollections.observableArrayList();


    private ObservableList<String> availableBook = FXCollections.observableArrayList();

    private SearchManager searchManager = new SearchManager();
    @FXML
    public void selectBooksRank(ActionEvent actionEvent) {
        usersList.getItems().clear();
        bookList.getItems().clear();
        usersRank.setSelected(false);
        if(booksRank.isSelected())
        {
            yearsFilter.setVisible(true);
        }
    }

    @FXML
    public void selectUsersRank(ActionEvent actionEvent) {
        usersList.getItems().clear();
        bookList.getItems().clear();
        booksRank.setSelected(false);
        if (usersRank.isSelected()) {
            yearsFilter.setVisible(false);
        }
    }

    @FXML
    public void searchAction(ActionEvent actionEvent) throws JSONException {
        usersList.getItems().clear();
        bookList.getItems().clear();
        availableBook.clear();
        usersList.setVisible(false);
        bookList.setVisible(true);
        if (booksRank.isSelected()) {
            String selectedChoice = yearsFilter.getSelectionModel().getSelectedItem() == null ? "" : yearsFilter.getSelectionModel().getSelectedItem().toString();
            if (selectedChoice.equals(""))
            {
                labelError.setText("You must enter the year");
                return;
            }
            else{
                labelError.setText("");
                JSONArray array = searchManager.searchRankBook(Integer.valueOf(selectedChoice));

                String genres;
                ArrayList<String> genresList = new ArrayList<>();
                for (int i = 0; i< array.length(); i++)
                {
                    if(i%2==0){
                        genres = "GENRES BOOK: "+array.get(i)+ "     TOTAL PUBLISHED: " + array.get(i+1);
                        genresList.add(genres);
                    }
                }
                availableBook.addAll(genresList);
                bookList.getItems().addAll(availableBook);

                return;
            }
        }
    }


    @FXML
    void homeInterface(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;
        if (session.getIsAuthor())
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
        else
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }


    @FXML
    public void profileInterface(ActionEvent actionEvent) throws IOException {
        Parent userInterface;
        if (session.getIsAuthor()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
            // controller.setNickname(nickSelected);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            UserInterfaceController controller = fxmlLoader.<UserInterfaceController>getController();
        }

        Stage actual_stage = (Stage) profileButton.getScene().getWindow();
        actual_stage.setScene(new Scene(userInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }
    @FXML
    void initialize() {
//        availableChoices.add(new Genre(""));
        availableChoices.addAll(searchManager.searchYears());
        yearsFilter.setItems(availableChoices);
        yearsFilter.setVisible(false);
    }
}
