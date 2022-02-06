package it.unipi.dii.reviook_app.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log extends Report {
    private String id;
    private String operation;
    private String admin;
    private Date date;


    public Log(String id, Date date, String operation, String admin, String report_id, String type, String isbn, String asin, String book_id, String title, String description, Integer num_pages, Integer publication_day, Integer publication_month, Integer publication_year, String image_url, String review_id, String review_text, String rating, String user_id, String username, ArrayList<Author> authors, ArrayList<String> genres) {
        super(report_id, type, isbn, asin, book_id, title, description, num_pages, publication_day, publication_month, publication_year, image_url, review_id, review_text, rating, user_id, username, authors, genres);
        this.operation = operation;
        this.admin = admin;
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy | HH:mm:ss ");
        String review_text;
        if (this.getReview_text().length() > 80) {
            review_text = this.getReview_text().substring(0, Math.min(this.getReview_text().length(), 80)) + " ...";
        } else {
            review_text = this.getReview_text();
        }
        return "operation: " + this.operation.toUpperCase() + " - " + this.getType().toUpperCase() +
                "\n\non date: " + sdf.format(this.date) +
                "\n\n" + (this.getType().equals("review") ? "review text:  " + review_text : "title:  " + this.getTitle()) +
                "\n\nby: " + this.admin;
    }
}
