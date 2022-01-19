package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class Book {
    private String isbn;
    private String language_code;
    private String asin;
    private Double average_rating;
    private String description;
    private Integer num_pages;
    private Integer publication_day;
    private Integer publication_month;
    private Integer publication_year;
    private String image_url;
    private String book_id;
    private Integer ratings_count;
    private String title;
    private ArrayList<String> authors;
    private ArrayList<String> genres;
    private ArrayList<Review> reviews;


    public Book(String isbn, String language_code, String asin, Double average_rating, String description, Integer num_pages, Integer publication_day, Integer publication_month, Integer publication_year, String image_url, String book_id, Integer ratings_count, String title, ArrayList<String> authors, ArrayList<String> genres, ArrayList<Review> reviews) {
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
        this.reviews = reviews;
    }

    public Book(String id, String title,String isbn,String description,ArrayList<String> genres, ArrayList<String> authors){
        this(isbn, "", "", 0.0, description, 0, 0, 0, 0, "", id , 0, title, authors, genres , new ArrayList());
    }

    public Book(String title,String book_id)
    {
        this( "", "", "", 0.0, "", 0, 0, 0, 0, "",  book_id, 0, title, new ArrayList(), new ArrayList() , new ArrayList());

    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(Double average_rating) {
        this.average_rating = average_rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public Integer getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(Integer ratings_count) {
        this.ratings_count = ratings_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Book{" +
                "book_id='" + book_id + '\'' +
                '}';
    }
}
