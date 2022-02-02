package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class Author extends User {
    private ArrayList<Book> published;

    public Author(String nickname) {
        super(nickname);
        this.published = new ArrayList<>();
    }

    public Author(String id, String name, String surname, String nickname, String email, String password, ArrayList<String> listReviewID, Integer follower_count) {
        super(id, name, surname, nickname, email, password, listReviewID, follower_count);
        this.published = new ArrayList<>();
    }

    public void setPublished(ArrayList<Book> published) {
        this.published = published;
    }

    public void addToSetPublished(Book published) {
        this.published.add(published);
    }

    public ArrayList<Book> getPublished() {
        return this.published;
    }

    public void addToPublished(Book writtenBook) {
        this.published.add(writtenBook);
    }
}
