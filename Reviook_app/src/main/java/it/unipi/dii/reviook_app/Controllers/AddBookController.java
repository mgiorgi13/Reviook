package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Data.Author;
import it.unipi.dii.reviook_app.Data.Books;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Manager.UserManager;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;



public class AddBookController {
    @FXML
    private JFXListView<Author> authorsList;

    @FXML
    private JFXListView<String>  generList, generTag;

    @FXML JFXButton backButton;

    @FXML
    private JFXListView<String> listTagged;

    @FXML
    private TextField authorTag, titleBook, ISBN;

    @FXML
    private TextArea description;

    @FXML
    private Text actiontarget;


    private Session session;
    private UserManager userManager = new UserManager();
    @FXML
    int contatoreUsername = 0;
    int contatoreGener = 0;
    public void searchAction(ActionEvent actionEvent) {
        authorsList.getItems().clear();
        generList.getItems().clear();
        generList.setVisible(false);
        authorsList.setVisible(true);

        ObservableList<Author> obsUserList = FXCollections.observableArrayList();
        obsUserList.addAll(userManager.searchAuthor(authorTag.getText()));
        authorsList.getItems().addAll(obsUserList);
        authorsList.setCellFactory(new Callback<ListView<Author>, ListCell<Author>>() {
            @Override
            public ListCell<Author> call(ListView<Author> listView) {
                return new ListCell<Author>() {
                    @Override
                    public void updateItem(Author item, boolean empty) {
                        super.updateItem(item, empty);
                        textProperty().unbind();
                        if (item != null)
                            setText(item.getNickname());
                        else
                            setText(null);
                    }
                };
            }
        });


        authorsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean check = false;
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    Author selectedCell = (Author) authorsList.getSelectionModel().getSelectedItem();

                    for(int i =0; i< contatoreUsername; i++)
                    {
                        if (listTagged.getItems().get(i).equals(selectedCell.getNickname()))
                        {
                            check = true;
                            break;
                        }
                        else check = false;
                    }
                    if (check)
                        return;
                    listTagged.getItems().addAll(selectedCell.getNickname());
                    contatoreUsername++;
                }
            }
        });
        listTagged.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String removeCell = (String) listTagged.getSelectionModel().getSelectedItem();


                    listTagged.getItems().remove(removeCell);
                    contatoreUsername --;
                }
            }
        });
    }
    @FXML
    public void searchActionGenres(ActionEvent actionEvent) {
        authorsList.getItems().clear();
        generList.getItems().clear();
        generList.setVisible(true);
        authorsList.setVisible(false);
        ObservableList<String> obsUserList = FXCollections.observableArrayList();
        obsUserList.addAll(userManager.searchGeners());
        generList.getItems().addAll(obsUserList);
        generList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String > call(ListView<String> listView) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        textProperty().unbind();
                        if (item != null)
                            setText(item);
                        else
                            setText(null);
                    }
                };
            }
        });


        generList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean check = false;
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String selectedCell = (String) generList.getSelectionModel().getSelectedItem();

                    for(int i =0; i< contatoreGener; i++)
                    {
                        if (generTag.getItems().get(i).equals(selectedCell))
                        {
                            check = true;
                            break;
                        }
                        else check = false;
                    }
                    if (check)
                        return;
                    generTag.getItems().addAll(selectedCell);
                    contatoreGener++;
                }
            }
        });
        generTag.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                    String removeCell = (String) generTag.getSelectionModel().getSelectedItem();
                    //System.out.println(selectedCell.getNickname());

                    generTag.getItems().remove(removeCell);
                    contatoreGener --;
                }
            }
        });
    }
    public  void backFunction() throws IOException {
        Parent userInterface;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author.fxml"));
        userInterface = (Parent) fxmlLoader.load();
        Stage actual_stage = (Stage) backButton.getScene().getWindow();
        actual_stage.setScene(new Scene(userInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    public void addBookFuncton() throws IOException {
        if (titleBook.getText().isEmpty())
        {
            actiontarget.setText("You must enter the title");
            return;
        }
        if (ISBN.getText().isEmpty())
        {
            actiontarget.setText("You must enter the ISBN");
            return;
        }
        if (generTag.getItems().isEmpty())
        {
            actiontarget.setText("You must enter the Genres");
            return;
        }

        String Title = titleBook.getText();
        String ISBN_ = ISBN.getText();
        String Description = description.getText();
        ArrayList<String> Genre=new ArrayList<String>((ObservableList)generTag.getItems());
        ArrayList<String> UsernameTagged=new ArrayList<String>((ObservableList)listTagged.getItems());

        if(userManager.verifyISBN(ISBN_)){
            actiontarget.setText("Existing ISBN");
            return;
        }
        userManager.addBook(  Title,  ISBN_,  Description,  Genre, UsernameTagged);
        actiontarget.setText("Congratulations you added a book!!");


        titleBook.clear();
        ISBN.clear();
        description.clear();
        generTag.getItems().clear();
        listTagged.getItems().clear();
        backFunction();
    }
}
