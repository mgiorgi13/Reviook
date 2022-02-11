package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class Report {
    private String report_id;
    private String type;
    private String isbn;
    private String asin;
    private String book_id;
    private String title;
    private String description;
    private Integer num_pages;
    private Integer publication_day;
    private Integer publication_month;
    private Integer publication_year;
    private String image_url;
    private ArrayList<Author> authors;
    private ArrayList<String> genres;
    private String review_id;
    private String review_text;
    private String rating;
    private String user_id;
    private String username;

    public Report(String report_id, String type, String isbn, String asin, String book_id, String title, String description, Integer num_pages, Integer publication_day, Integer publication_month, Integer publication_year, String image_url, String review_id, String review_text, String rating, String user_id, String username, ArrayList<Author> authors, ArrayList<String> genres) {
        this.report_id = report_id;
        this.type = type;

        this.isbn=isbn;
        this.asin=asin;
        this.book_id = book_id;
        this.title = title;
        this.description = description;
        this.num_pages = num_pages;
        this.publication_day = publication_day;
        this.publication_month = publication_month;
        this.publication_year = publication_year;
        this.image_url = image_url;

        this.review_id = review_id;
        this.review_text = review_text;
        this.rating = rating;
        this.user_id = user_id;
        this.username = username;
        this.authors = authors;
        this.genres = genres;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Integer getNum_pages() {
        return num_pages;
    }

    public void setNum_pages(Integer num_pages) {
        this.num_pages = num_pages;
    }

    public Integer getPublication_day() {
        return publication_day;
    }

    public void setPublication_day(Integer publication_day) {
        this.publication_day = publication_day;
    }

    public Integer getPublication_month() {
        return publication_month;
    }

    public void setPublication_month(Integer publication_month) {
        this.publication_month = publication_month;
    }

    public Integer getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(Integer publication_year) {
        this.publication_year = publication_year;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
