package it.unipi.dii.reviook_app.entity;

import it.unipi.dii.reviook_app.manager.UserManager;
import javafx.fxml.FXML;
import java.util.ArrayList;

public class User {
    @FXML
    private String id;
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private Interaction interactions;
    private ArrayList<String> listReviewID;
    private Integer follower_count;
    private ArrayList<Genre> statistics;
    private ListBooks listBooks;

    public User(String nickname){
        this.id = "";
        this.name = "";
        this.surname = "";
        this.nickname = nickname;
        this.email = "";
        this.password = "";
        this.interactions = new Interaction();
        this.listBooks = new ListBooks();
        this.listReviewID = new ArrayList<>();
        this.follower_count = 0;
    }

    public User(String id, String name, String surname, String nickname, String email, String password, ArrayList<String> listReviewID, Integer follower_count) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.interactions = new Interaction();
        this.listBooks = new ListBooks();
        this.listReviewID = listReviewID;
        this.follower_count = follower_count;
        this.statistics = null;
    }

    public ArrayList<Genre> getStatistics() {
        return statistics;
    }

    public void setStatistics(ArrayList<Genre> statistics) {
        this.statistics = statistics;
    }

    public ArrayList<String> getListReviewID() {
        return this.listReviewID;
    }

    public void setListReviewID(ArrayList<String> listReviewID) {
        this.listReviewID = listReviewID;
    }

    public void addReviewID(String id) {
        this.listReviewID.add(id);
    }

    public void removeReviewID(String id) {
        this.listReviewID.remove(id);
    }

    public String getId() {
        return this.id;
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

    public ListBooks getBooks() {
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

    public void setBooks(ListBooks books) {
        this.listBooks = books;
    }

    public int getFollowerCount() {
        return this.follower_count;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +  ",\n" +
                "name='" + name + '\'' + ",\n" +
                "surname='" + surname + '\'' + ",\n" +
                "nickname='" + nickname + '\'' + ",\n" +
                "email='" + email + '\'' + ",\n" +
                "password='" + password + '\'' + ",\n" +
                "interactions=" + interactions + ",\n" +
                "listReviewID=" + listReviewID + ",\n" +
                "follower_count=" + follower_count + ",\n" +
                "listBooks=" + listBooks + "\n" +
                '}';
    }
}