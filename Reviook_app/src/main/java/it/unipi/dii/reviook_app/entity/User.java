package it.unipi.dii.reviook_app.entity;

import it.unipi.dii.reviook_app.manager.UserManager;
import javafx.fxml.FXML;
import java.util.List;


public class User {
    @FXML
    private String id;
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private Interaction interactions;
    //follower list
    //following list
    //to read list
    //read list
    //genres list
    //suggested books
    //suggested user
    private listBooks listBooks;

    UserManager userManager = new UserManager();

    public User(String id, String name, String surname, String nickname, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.interactions = new Interaction();
        this.listBooks = new listBooks();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Interaction getInteractions() {
        return interactions;
    }

    public listBooks getBooks() {
        return listBooks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInteractions(Interaction interactions) {
        this.interactions = interactions;
    }

    public void setBooks(listBooks books) {
        this.listBooks = books;
    }

    public int getFollowerCount() {
        List<String> followerList = userManager.loadRelationsFollower("User",this.nickname);
        return followerList.size();
    };

}