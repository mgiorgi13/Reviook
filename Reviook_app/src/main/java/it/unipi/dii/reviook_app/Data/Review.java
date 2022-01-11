package it.unipi.dii.reviook_app.Data;

import javafx.beans.property.SimpleStringProperty;

public class Review {
    private SimpleStringProperty date_added;
    private SimpleStringProperty review_id;
    private SimpleStringProperty date_update;
    private SimpleStringProperty user_id;
    private SimpleStringProperty rating;
    private SimpleStringProperty review_text;
    private SimpleStringProperty helpful;
    private SimpleStringProperty n_votes;

    public Review(SimpleStringProperty date_added, SimpleStringProperty review_id, SimpleStringProperty date_update, SimpleStringProperty n_votes, SimpleStringProperty user_id, SimpleStringProperty rating, SimpleStringProperty review_text, SimpleStringProperty helpful) {
        this.date_added = date_added;
        this.review_id = review_id;
        this.date_update = date_update;
        this.n_votes = n_votes;
        this.user_id = user_id;
        this.rating = rating;
        this.review_text = review_text;
        this.helpful = helpful;
    }

    public String getDate_added() {
        return date_added.get();
    }

    public SimpleStringProperty date_addedProperty() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added.set(date_added);
    }

    public String getReview_id() {
        return review_id.get();
    }

    public SimpleStringProperty review_idProperty() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id.set(review_id);
    }

    public String getDate_update() {
        return date_update.get();
    }

    public SimpleStringProperty date_updateProperty() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update.set(date_update);
    }

    public String getN_votes() {
        return n_votes.get();
    }

    public SimpleStringProperty n_votesProperty() {
        return n_votes;
    }

    public void setN_votes(String n_votes) {
        this.n_votes.set(n_votes);
    }

    public String getUser_id() {
        return user_id.get();
    }

    public SimpleStringProperty user_idProperty() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id.set(user_id);
    }

    public String getRating() {
        return rating.get();
    }

    public SimpleStringProperty ratingProperty() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating.set(rating);
    }

    public String getReview_text() {
        return review_text.get();
    }

    public SimpleStringProperty review_textProperty() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text.set(review_text);
    }

    public String getHelpful() {
        return helpful.get();
    }

    public SimpleStringProperty helpfulProperty() {
        return helpful;
    }

    public void setHelpful(String helpful) {
        this.helpful.set(helpful);
    }


}
