package it.unipi.dii.reviook_app.Manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.Data.Book;
import it.unipi.dii.reviook_app.Data.Review;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static org.neo4j.driver.Values.parameters;


public class BookManager {
    private MongoDriver md;
    private Neo4jDriver nd;
    private it.unipi.dii.reviook_app.Session session = it.unipi.dii.reviook_app.Session.getInstance();

    private static final String usersCollection = "users";
    private static final String authorCollection = "authors";
    private static final String bookCollection = "books";

    public BookManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }

    public void addBook(String title, String ISBN, String Description, ArrayList<String> Genre, ArrayList<DBObject> UsernameTagged) {
        String concat = ISBN + title + UsernameTagged;
        String id = UUID.nameUUIDFromBytes(concat.getBytes()).toString();

        //MONGO DB
        ArrayList<String> reviews = new ArrayList<String>();
        Document doc = new Document("image_url", "null")
                .append("num_pages", "")
                .append("isbn", ISBN)
                .append("description", Description)
                .append("average_rating", "")
                .append("book_id", id)
                .append("title", title)
                .append("language_code", "")
                .append("publication_month", "")
                .append("publication_year", "")
                .append("reviews", reviews)
                .append("genres", Genre)
                .append("asin", "")
                .append("publication_day", "")
                .append("ratings_count", "")
                .append("authors", UsernameTagged);

        md.getCollection(bookCollection).insertOne(doc);

        //N4J
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (ee: Book { book_id : $book_id, title: $ title})", parameters("book_id", id, "title", title));
                for (int i = 0; i < UsernameTagged.size(); i++) {
                    tx.run("MATCH (dd:Author),(ee: Book) WHERE dd.author_id = '" + UsernameTagged.get(i).get("author_id") + "' AND ee.book_id='" + id + "'" +
                            "CREATE (dd)-[:WROTE]->(ee)");
                }
                return null;
            });
        }
    }

    public void AddReviewToBook(String reviewText, Integer ratingBook, String book_id) {
        // TODO aggiornare il rating Book
        MongoCollection<Document> book = md.getCollection(bookCollection);
        Document newReview = new Document();
        String reviewID = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        newReview.append("date_added", date);
        newReview.append("date_updated", "");
        newReview.append("review_id", reviewID);
        newReview.append("n_votes", "0");
        newReview.append("rating", ratingBook);
        newReview.append("review_text", reviewText);
        newReview.append("helpful", "0");
        if (session.getLoggedUser() != null) {
            String loggedUserID = session.getLoggedUser().getNickname();
            newReview.append("user_id", loggedUserID);
        } else {
            String loggedAuthorID = session.getLoggedAuthor().getNickname();
            newReview.append("user_id", loggedAuthorID);
        }
        Bson getBook = eq("book_id", book_id);
        DBObject elem = new BasicDBObject("reviews", new BasicDBObject(newReview));
        DBObject insertReview = new BasicDBObject("$push", elem);
        book.updateOne(getBook, (Bson) insertReview);

        Book bookToUpdate = getBookByID(book_id);
        Double newRating = updateRating(bookToUpdate.getReviews());
        UpdateResult updateResult2 = book.updateOne(getBook, Updates.set("average_rating", newRating));
    }

    public void EditReview(String reviewText, Integer ratingBook, String book_id, String review_id) {
        // TODO aggiornare il rating Book
        MongoCollection<Document> books = md.getCollection(bookCollection);
        Bson getBook = eq("book_id", book_id);
        Bson getReview = eq("reviews.review_id", review_id);
        UpdateResult updateResult = books.updateOne(getReview, Updates.set("reviews.$.review_text", reviewText));
        UpdateResult updateResult2 = books.updateOne(getReview, Updates.set("reviews.$.rating", ratingBook));
        Book bookToUpdate = getBookByID(book_id);
        Double newRating = updateRating(bookToUpdate.getReviews());
        UpdateResult updateResult3 = books.updateOne(getBook, Updates.set("average_rating", newRating));
    }

    public void DeleteReview(String review_id, String book_id) {
        // TODO aggiornare il rating Book
        MongoCollection<Document> books = md.getCollection(bookCollection);
        Bson getBook = eq("book_id", book_id);
        //  Bson getReview = eq("reviews.review_id", review_id);
        UpdateResult updateResult = books.updateOne(getBook, Updates.pull("reviews", new Document("review_id", review_id)));
        Book bookToUpdate = getBookByID(book_id);
        Double newRating = updateRating(bookToUpdate.getReviews());
        UpdateResult updateResult2 = books.updateOne(getBook, Updates.set("average_rating", newRating));
    }

    public Book getBookByID(String book_id) {
        MongoCollection<Document> books = md.getCollection(bookCollection);
        Document book = books.find(eq("book_id", book_id)).iterator().next();

        ArrayList<String> authorsLis = new ArrayList<>();
        ArrayList<Review> reviewsList = new ArrayList<>();
        ArrayList<Document> authors = (ArrayList<Document>) book.get("authors");
        ArrayList<Document> reviews = (ArrayList<Document>) book.get("reviews");
        ArrayList<String> genres = (ArrayList<String>) book.get("genres");

        for (Document r : reviews) {
            reviewsList.add(new Review(
                    new SimpleStringProperty(r.get("date_added").toString()),
                    new SimpleStringProperty(r.getString("review_id")),
                    new SimpleStringProperty(r.get("date_updated").toString()),
                    new SimpleStringProperty(r.getString("n_votes")),
                    new SimpleStringProperty(r.getString("user_id")),
                    new SimpleStringProperty(r.get("rating").toString()),
                    new SimpleStringProperty(r.getString("review_text")),
                    new SimpleStringProperty(r.getString("helpful"))
            ));
        }
        for (Document a : authors) {
            authorsLis.add(a.getString("author_name"));
        }

        Book outputBook = new Book(
                book.getString("isbn"),
                book.getString("language_code"),
                book.getString("asin"),
                book.get("average_rating").toString().equals("") ? Double.valueOf(0) : Double.valueOf(book.get("average_rating").toString()),
                book.getString("description").toString(),
                book.getString("num_pages").equals("") ? 0 : Integer.valueOf(book.getString("num_pages")),
                book.getString("publication_day").equals("") ? 0 : Integer.valueOf(book.getString("publication_day")),
                book.getString("publication_month").equals("") ? 0 : Integer.valueOf(book.getString("publication_month")),
                book.getString("publication_year").equals("") ? 0 : Integer.valueOf(book.getString("publication_year")),
                book.getString("image_url"),
                book.getString("book_id"),
                book.getString("ratings_count").equals("") ? Integer.valueOf(0) : Integer.valueOf(book.getString("ratings_count")),
                book.getString("title"),
                authorsLis,
                genres,
                reviewsList
        );
        return outputBook;
    }

    public Double updateRating(ArrayList<Review> reviews) {
        Double ratingSum = 0.0;
        if (reviews.size() > 0) {
            for (Review r : reviews) {
                ratingSum += Double.parseDouble(r.getRating());
            }
            return ratingSum / reviews.size();
        } else {
            return ratingSum;
        }
    }
}
