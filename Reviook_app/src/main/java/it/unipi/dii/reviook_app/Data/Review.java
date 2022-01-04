package it.unipi.dii.reviook_app.Data;

public class Review {
    private String date_added;
    private String review_id;
    private String date_update;
    private String n_votes;
    private String user_id;
    private String rating;

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }

    public String getN_votes() {
        return n_votes;
    }

    public void setN_votes(String n_votes) {
        this.n_votes = n_votes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getHelpful() {
        return helpful;
    }

    public void setHelpful(String helpful) {
        this.helpful = helpful;
    }

    private String review_text;
    private String helpful;

    public Review(String date_added, String review_id, String date_update, String n_votes, String user_id, String rating, String review_text, String helpful) {
        this.date_added = date_added;
        this.review_id = review_id;
        this.date_update = date_update;
        this.n_votes = n_votes;
        this.user_id = user_id;
        this.rating = rating;
        this.review_text = review_text;
        this.helpful = helpful;
    }

}
