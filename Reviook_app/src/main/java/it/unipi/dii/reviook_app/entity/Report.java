package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class Report {
    private String report_id;
    private String type;
    private String book_id;
    private String title;
    private String description;
    private ArrayList<Author> authors;
    private ArrayList<String> genres;
    private String review_id;
    private String review_text;
    private String user_id;
    private String username;


    public Report(String report_id, String type, String book_id, String title, String description, String review_id, String review_text, String user_id, String username, ArrayList<Author> authors, ArrayList<String> genres) {
        this.report_id = report_id;
        this.type = type;
        this.book_id = book_id;
        this.title = title;
        this.description = description;
        this.review_id = review_id;
        this.review_text = review_text;
        this.user_id = user_id;
        this.username = username;
        this.authors = authors;
        this.genres = genres;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
