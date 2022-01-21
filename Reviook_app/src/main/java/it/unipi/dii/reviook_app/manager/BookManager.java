package it.unipi.dii.reviook_app.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Review;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;
import java.util.UUID;
import java.util.ArrayList;
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

    public boolean verifyISBN(String ISBN) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        try (MongoCursor<Document> cursor = book.find(eq("isbn", ISBN)).iterator()) {
            while (cursor.hasNext()) {
                return true;
            }
        }
        return false;
    }

    public void addBook(String id, String title, String ISBN, String Description, ArrayList<String> Genre, ArrayList<DBObject> UsernameTagged) {
        //TODO controllare se il formato dei campi inseriti corrisponde a quelli di mongo
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        //TODO SISTEMARE CAMPI INSERITI SE NULL NON INSERIRE IL CAMPO
        //MONGO DB
        ArrayList<String> reviews = new ArrayList<String>();
        Document doc = new Document("image_url", "")
                .append("num_pages", 0)
                .append("isbn", ISBN)
                .append("description", Description)
                .append("average_rating", "")
                .append("book_id", id)
                .append("title", title)
                .append("language_code", "")
                .append("publication_month", calendar.get(Calendar.MONTH))
                .append("publication_year", calendar.get(Calendar.YEAR))
                .append("reviews", reviews)
                .append("genres", Genre)
                .append("asin", "")
                .append("publication_day", calendar.get(Calendar.DAY_OF_MONTH))
                .append("ratings_count", 0.0)
                .append("authors", UsernameTagged);

        md.getCollection(bookCollection).insertOne(doc);

        //N4J
        try (Session session = nd.getDriver().session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run("CREATE (ee: Book { id : $id, title: $title})", parameters("id", id, "title", title));
                for (int i = 0; i < UsernameTagged.size(); i++) {
                    tx.run("MATCH (dd:Author),(ee: Book) WHERE dd.id = '" + UsernameTagged.get(i).get("author_id") + "' AND ee.id='" + id + "'" +
                            "CREATE (dd)-[:WROTE]->(ee)");
                }
                return null;
            });
        }
    }

    public void AddReviewToBook(String reviewText, Integer ratingBook, String book_id) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        Document newReview = new Document();
        String reviewID = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        newReview.append("date_added", date);
        newReview.append("date_updated", date);
        newReview.append("review_id", reviewID);
        newReview.append("likes", 0);
        newReview.append("rating", ratingBook);
        newReview.append("review_text", reviewText);
        if (session.getLoggedUser() != null) {
            newReview.append("user_id", session.getLoggedUser().getId());
            newReview.append("username", session.getLoggedUser().getNickname());
        } else {
            newReview.append("user_id", session.getLoggedAuthor().getId());
            newReview.append("username", session.getLoggedAuthor().getNickname());
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
        MongoCollection<Document> books = md.getCollection(bookCollection);
        Bson getBook = eq("book_id", book_id);
        //  Bson getReview = eq("reviews.review_id", review_id);
        UpdateResult updateResult = books.updateOne(getBook, Updates.pull("reviews", new Document("review_id", review_id)));
        Book bookToUpdate = getBookByID(book_id);
        Double newRating = updateRating(bookToUpdate.getReviews());
        UpdateResult updateResult2 = books.updateOne(getBook, Updates.set("average_rating", newRating));
        removeLikeReview(review_id, book_id); // TODO funziona solo per i like miei a mie review, altrimenti non funziona
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
                        r.getString("username"),
                        r.get("date_added").toString(),
                        r.getString("review_id"),
                        r.get("date_updated") == null ? "" : r.get("date_updated").toString(),
                        r.get("likes") == null ? r.getInteger("helpful") : r.getInteger("likes"),
                        r.getString("user_id"),
                        r.get("rating").toString(),
                        r.getString("review_text")
                ));
            }
        for (Document a : authors) {
            authorsLis.add(a.getString("author_name"));
        }

        Book outputBook = new Book(
                book.get("isbn") == null ? null : book.getString("isbn"),
                book.get("language_code")  == null ? null : book.getString("language_code"),
                book.get("asin") == null ? null : book.getString("asin"),
                book.get("average_rating").toString().equals("") ? Double.valueOf(0) : Double.valueOf(book.get("average_rating").toString()),
                book.get("description") == null ? null : book.getString("description"),
                book.get("num_pages") == null ? null : book.getInteger("num_pages"),
                book.get("publication_day") == null ? null : book.getInteger("publication_day"),
                book.get("publication_month") == null ? null : book.getInteger("publication_month"),
                book.get("publication_year") == null ? null : book.getInteger("publication_year"),
                book.get("image_url") == null ? null : book.getString("image_url"),
                book.getString("book_id"),
                book.getInteger("ratings_count"),
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

    public void addLikeReview(String reviewID, String book_id) {
        if (session.getLoggedAuthor() != null) {
            MongoCollection<Document> authors = md.getCollection(authorCollection);
            Bson getAuthor = eq("author_id", session.getLoggedAuthor().getId());
            DBObject elem = new BasicDBObject("liked_review", reviewID);
            DBObject insertRevID = new BasicDBObject("$push", elem);
            authors.updateOne(getAuthor, (Bson) insertRevID);
            //increment like review counter
            MongoCollection<Document> books = md.getCollection(bookCollection);
            Bson getBook = eq("book_id", book_id);
            Bson getReview = eq("reviews.review_id", reviewID);
            UpdateResult updateResult = books.updateOne(getReview, Updates.inc("reviews.$.likes", 1));
        } else if (session.getLoggedUser() != null) {
            MongoCollection<Document> users = md.getCollection(usersCollection);
            Bson getUser = eq("user_id", session.getLoggedUser().getId());
            DBObject elem = new BasicDBObject("liked_review", reviewID);
            DBObject insertRevID = new BasicDBObject("$push", elem);
            users.updateOne(getUser, (Bson) insertRevID);
            //increment like review counter
            MongoCollection<Document> books = md.getCollection(bookCollection);
            Bson getBook = eq("book_id", book_id);
            Bson getReview = eq("reviews.review_id", reviewID);
            UpdateResult updateResult = books.updateOne(getReview, Updates.inc("reviews.$.likes", 1));
        }

    }

    public void removeLikeReview(String reviewID, String book_id) {
        if (session.getLoggedAuthor() != null) {
            MongoCollection<Document> authors = md.getCollection(authorCollection);
            Bson getAuthor = eq("author_id", session.getLoggedAuthor().getId());
            authors.updateOne(getAuthor, Updates.pull("liked_review", reviewID));
            //decrement like review counter
            MongoCollection<Document> books = md.getCollection(bookCollection);
            Bson getBook = eq("book_id", book_id);
            Bson getReview = eq("reviews.review_id", reviewID);
            UpdateResult updateResult = books.updateOne(getReview, Updates.inc("reviews.$.likes", -1));
        } else if (session.getLoggedUser() != null) {
            MongoCollection<Document> users = md.getCollection(usersCollection);
            Bson getUser = eq("user_id", session.getLoggedUser().getId());
            users.updateOne(getUser, Updates.pull("liked_review", reviewID));
            //increment like review counter
            MongoCollection<Document> books = md.getCollection(bookCollection);
            Bson getBook = eq("book_id", book_id);
            Bson getReview = eq("reviews.review_id", reviewID);
            UpdateResult updateResult = books.updateOne(getReview, Updates.inc("reviews.$.likes", -1));
        }

    }
}
