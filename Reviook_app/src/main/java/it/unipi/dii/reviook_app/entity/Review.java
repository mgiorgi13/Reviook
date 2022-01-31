package it.unipi.dii.reviook_app.entity;

public class Review {
    private String date_added;
    private String review_id;
    private String date_update;
    private String user_id;
    private String username;
    private String rating;
    private String review_text;
    private Integer likes;
    private Boolean liked;

    public Review(String username, String date_added, String review_id, String date_update, Integer likes, String user_id, String rating, String review_text) {
        this.date_added = date_added;
        this.review_id = review_id;
        this.date_update = date_update;
        this.likes = likes;
        this.user_id = user_id;
        this.rating = rating;
        this.review_text = review_text;
        this.liked = false;
        this.username = username;
    }

    public void setLiked() {
        this.liked = true;
    }

    public void unSetLiked() {
        this.liked = false;
    }

    public Boolean getLiked() {
        return this.liked;
    }

    public String getDate_added() {
        return date_added;
    }

    public String date_addedProperty() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getReview_id() {
        return review_id;
    }

    public String review_idProperty() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getDate_update() {
        return date_update;
    }

    public String date_updateProperty() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer likesProperty() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return this.username;
    }

    public String user_idProperty() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRating() {
        return rating;
    }

    public String ratingProperty() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public String review_textProperty() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    @Override
    public String toString() {
        return "Review{" +
                "date_added='" + date_added + '\'' +
                ", review_id='" + review_id + '\'' +
                ", date_update='" + date_update + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", rating='" + rating + '\'' +
                ", review_text='" + review_text + '\'' +
                ", likes=" + likes +
                ", liked=" + liked +
                '}';
    }

}
