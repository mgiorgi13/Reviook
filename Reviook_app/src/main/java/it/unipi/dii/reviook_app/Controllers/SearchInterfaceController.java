package it.unipi.dii.reviook_app.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import it.unipi.dii.reviook_app.Data.Books;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


public class SearchInterfaceController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private CheckBox authorCheck;

    @FXML
    private CheckBox bookCheck;

    @FXML
    private JFXListView<String> bookList;

    @FXML
    private JFXListView<Users> usersList;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton profileButton;

    @FXML
    void initialize() {

    }

    @FXML
    void homeInterface(ActionEvent event) throws IOException {
        Session session = Session.getInstance();
        Parent homeInterface;
        if (session.getType())
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author-interface.fxml"));
        else
            homeInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user-interface.fxml"));
        Stage actual_stage = (Stage) homeButton.getScene().getWindow();
        actual_stage.setScene(new Scene(homeInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    void profileInterface() throws IOException {
        Session session = Session.getInstance();
        Parent userInterface;
        if (session.getType()) {
//            userInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author-interface.fxml"));
            String nickSelected = "Gianni bello bello";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author-interface.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
            controller.setNickname(nickSelected);
        } else {
            //userInterface = FXMLLoader.load(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user-interface.fxml"));
            String nickSelected = "Gianni bello bello";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/user-interface.fxml"));
            userInterface = (Parent) fxmlLoader.load();
            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
            controller.setNickname(nickSelected);
        }


        Stage actual_stage = (Stage) profileButton.getScene().getWindow();
        actual_stage.setScene(new Scene(userInterface));
        actual_stage.setResizable(false);
        actual_stage.show();
    }

    @FXML
    public void searchAction(ActionEvent actionEvent) {

        usersList.getItems().clear();
        bookList.getItems().clear();

        if (bookCheck.isSelected()) {
            //TODO implementare query per prendere lista oggetti dal DB

            usersList.setVisible(false);
            bookList.setVisible(true);

            ObservableList<String> booksList = FXCollections.observableArrayList();
            for (int i = 0; i < 30; i++) {
                booksList.add("Book to read " + i);
            }
            bookList.getItems().addAll(booksList);
            bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 ) {
                        try {
                            Parent bookInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/bookDetail-interface.fxml"));
                            bookInterface = (Parent) fxmlLoader.load();
                            Stage actual_stage = (Stage) profileButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(bookInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else if (authorCheck.isSelected()) {
            //TODO implementare query per prendere lista oggetti dal DB

            bookList.setVisible(false);
            usersList.setVisible(true);

            Users mattia = new Users("Mattia", "Di Donato", "Mattiax", "", "");
            Users salvo = new Users("Salvo", "Febbo", "Salvox", "", "");
            Users matteo = new Users("Matteo", "Giorgi", "Matteox", "", "");
            ObservableList<Users> listUser = FXCollections.observableArrayList();
            listUser.addAll(mattia, salvo, matteo);
            usersList.getItems().addAll(listUser);
            usersList.setCellFactory(new Callback<ListView<Users>, ListCell<Users>>() {
                @Override
                public ListCell<Users> call(ListView<Users> listView) {
                    return new ListCell<Users>() {
                        @Override
                        public void updateItem(Users item, boolean empty) {
                            super.updateItem(item, empty);
                            textProperty().unbind();
                            if (item != null)
                                setText(item.getNickname() + " " + item.getName());
                            else
                                setText(null);
                        }
                    };
                }
            });
            usersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2 /*&& (mouseEvent.getTarget() instanceof Text)*/) {
                        Users selectedCell = (Users) usersList.getSelectionModel().getSelectedItem();
                        try {
                            //sSystem.out.println(selectedCell.getNickname());
                            Parent userInterface;
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/unipi/dii/reviook_app/fxml/author-interface.fxml"));
                            userInterface = (Parent) fxmlLoader.load();
                            AuthorInterfaceController controller = fxmlLoader.<AuthorInterfaceController>getController();
                            controller.setNickname(selectedCell.getNickname());

                            Stage actual_stage = (Stage) profileButton.getScene().getWindow();
                            actual_stage.setScene(new Scene(userInterface));
                            actual_stage.setResizable(false);
                            actual_stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    @FXML
    public void selectBookCheckAction(ActionEvent actionEvent) {
        bookCheck.setSelected(true);
        authorCheck.setSelected(false);
    }

    @FXML
    public void selectAuthorCheckAction(ActionEvent actionEvent) {
        bookCheck.setSelected(false);
        authorCheck.setSelected(true);
    }


}
