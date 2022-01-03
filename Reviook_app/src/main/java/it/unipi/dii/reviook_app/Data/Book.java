package it.unipi.dii.reviook_app.Data;

import javafx.collections.ObservableArray;

import java.util.ArrayList;
import java.util.Collections;

public class Book {
    private String isbn;
    private String language_code;
    private String asin;
    private Float average_rating;
    private String description;
    private Integer num_pages;
    private Integer publication_day;
    private Integer publication_month;
    private Integer publication_year;
    private String image_url;
    private String book_id;
    private Integer ratings_count;
    private String title;
    private ArrayList<Author> authors;
    private ArrayList<String> genres;
// private ArrayList<Review> genres;


    public Book(String isbn, String language_code, String asin, Float average_rating, String description, Integer num_pages, Integer publication_day, Integer publication_month, Integer publication_year, String image_url, String book_id, Integer ratings_count, String title, ArrayList<Author> authors, ArrayList<String> genres) {
        this.isbn = isbn;
        this.language_code = language_code;
        this.asin = asin;
        this.average_rating = average_rating;
        this.description = description;
        this.num_pages = num_pages;
        this.publication_day = publication_day;
        this.publication_month = publication_month;
        this.publication_year = publication_year;
        this.image_url = image_url;
        this.book_id = book_id;
        this.ratings_count = ratings_count;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
