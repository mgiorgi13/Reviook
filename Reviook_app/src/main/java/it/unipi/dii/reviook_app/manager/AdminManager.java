package it.unipi.dii.reviook_app.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import it.unipi.dii.reviook_app.entity.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import javax.print.Doc;
import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.descending;
import static org.neo4j.driver.Values.parameters;

public class AdminManager {
    private MongoDriver md;
    private static Neo4jDriver nd;

    private static final String adminCollection = "admins";
    private static final String bookCollection = "books";
    private static final String reportsCollection = "reports";
    private static final String logsCollection = "logs";

    BookManager bookManager = new BookManager();
    UserManager userManager = new UserManager();

    public AdminManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }

    public ArrayList<Report> loadReviewReported() {
        ArrayList<Report> reportedReview = new ArrayList<>();
        try {
            MongoCollection<Document> reports = md.getCollection(reportsCollection);
            List<Document> queryResults;
            queryResults = reports.find().into(new ArrayList<>());
            for (Document r : queryResults) {
                if (r.getString("type").equals("review")) {
                    reportedReview.add(new Report(
                            r.getString("report_id"),
                            r.getString("type"),
                            "",
                            "",
                            r.getString("book_id"),
                            "",
                            "",
                            0,
                            0,
                            0,
                            0,
                            "",
                            r.getString("review_id"),
                            r.getString("review_text"),
                            r.get("rating").toString(),
                            r.getString("user_id"),
                            r.getString("username"),
                            null,
                            null
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportedReview;
    }

    public ArrayList<Report> loadBookReported() {
        ArrayList<Report> reportedBook = new ArrayList<>();
        try {
            MongoCollection<Document> reports = md.getCollection(reportsCollection);
            List<Document> queryResults;
            queryResults = reports.find().into(new ArrayList<>());
            ArrayList<Author> authorsLis = new ArrayList<>();
            for (Document r : queryResults) {
                if (r.getString("type").equals("book")) {
                    ArrayList<Document> authors = (ArrayList<Document>) r.get("authors");
                    ArrayList<String> genres = (ArrayList<String>) r.get("genres");
                    for (Document a : authors) {
                        Author author = new Author(
                                a.getString("author_id"),
                                a.getString("author_name"),
                                "",
                                a.getString("author_username"),
                                "",
                                "",
                                null,
                                0
                        );
                        authorsLis.add(author);
                    }
                    reportedBook.add(new Report(
                            r.getString("report_id"),
                            r.getString("type"),
                            r.getString("isbn"),
                            r.getString("asin"),
                            r.getString("book_id"),
                            r.getString("title"),
                            r.getString("description"),
                            (Integer) r.get("num_pages"),
                            (Integer) r.get("publication_day"),
                            (Integer) r.get("publication_month"),
                            (Integer) r.get("publication_year"),
                            r.getString("image_url"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            authorsLis,
                            genres
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportedBook;
    }

    public boolean restoreLog(Log log) {
        if (log.getType().equals("book")) {
            // add book
            ArrayList<DBObject> authorsObj = new ArrayList<>();
            for (Author a : log.getAuthors()) {
                DBObject author = new BasicDBObject();
                author.put("author_name", (String) a.getName());
                author.put("author_role", ""); // TODO da togliere
                author.put("author_id", (String) a.getId());
                authorsObj.add(author);
            }

            //TODO SISTEMARE CAMPI INSERITI SE NULL NON INSERIRE IL CAMPO
            //MONGO DB
            ArrayList<Review> reviews = new ArrayList<Review>();
            Document doc = new Document("language_code", "")
                    .append("description", log.getDescription())
                    .append("num_pages", log.getNum_pages())
                    .append("publication_day", log.getPublication_day())
                    .append("publication_month", log.getPublication_month())
                    .append("publication_year", log.getPublication_year())
                    .append("image_url", log.getImage_url())
                    .append("book_id", log.getBook_id())
                    .append("title", log.getTitle())
                    .append("average_rating", 0.0)
                    .append("ratings_count", 0)
                    .append("genres", log.getGenres())
                    .append("authors", authorsObj)
                    .append("reviews", reviews);
            if (log.getIsbn() != null) {
                doc.append("isbn", log.getIsbn());
            } else {
                doc.append("asin", log.getAsin());
            }

            md.getCollection(bookCollection).insertOne(doc);

            //N4J
            try (Session session = nd.getDriver().session()) {
                session.writeTransaction((TransactionWork<Void>) tx -> {
                    tx.run("CREATE (ee: Book { id : $id, title: $title})", parameters("id", log.getBook_id(), "title", log.getTitle()));
                    for (int i = 0; i < log.getAuthors().size(); i++) {
                        tx.run("MATCH (dd:Author),(ee: Book) WHERE dd.id = '" + log.getAuthors().get(i).getId() + "' AND ee.id='" + log.getBook_id() + "'" +
                                "CREATE (dd)-[:WROTE]->(ee)");
                    }
                    return null;
                });
            }
        } else if (log.getType().equals("review")) {
            // add review to book
            MongoCollection<Document> book = md.getCollection(bookCollection);
            Document newReview = new Document();
            LocalDateTime now = LocalDateTime.now();
            Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
//            newReview.append("date_added", date);
            newReview.append("date_updated", date);
            newReview.append("review_id", log.getReview_id());
            newReview.append("likes", 0);
            newReview.append("rating", log.getRating());
            newReview.append("review_text", log.getReview_text());
            newReview.append("user_id", log.getUser_id());
            newReview.append("username", log.getUsername());
            Bson getBook = eq("book_id", log.getBook_id());
            DBObject elem = new BasicDBObject("reviews", new BasicDBObject(newReview));
            DBObject insertReview = new BasicDBObject("$push", elem);
            book.updateOne(getBook, (Bson) insertReview);
            Book bookToUpdate = bookManager.getBookByID(log.getBook_id());
            if (bookToUpdate == null){
                return false;
            }
            Double newRating = bookManager.updateRating(bookToUpdate.getReviews());
            UpdateResult updateResult2 = book.updateOne(getBook, Updates.set("average_rating", newRating));
            if (userManager.verifyUsername(log.getUsername(), "", false) == 1) {
                userManager.readAdd("Author", log.getUsername(), log.getBook_id()); // restore relation on N4J
            } else if (userManager.verifyUsername(log.getUsername(), "", false) == 0) {
                userManager.readAdd("User", log.getUsername(), log.getBook_id()); // restore relation on N4J
            }
        }
        return true;
    }

    public void deleteLog(Log log) {
        MongoCollection<Document> reports = md.getCollection(logsCollection);
        reports.deleteOne(eq("id", log.getId()));
    }

    public ArrayList<Log> loadLogs() {
        ArrayList<Log> logsList = new ArrayList<>();
        try {
            MongoCollection<Document> logs = md.getCollection(logsCollection);
            List<Document> results = logs.find().sort(descending("date")).into(new ArrayList<>());
            for (Document l : results) {
                if (l.getString("type").equals("review")) {
                    logsList.add(
                            new Log(
                                    l.getString("id"),
                                    l.getDate("date"),
                                    l.getString("operation"),
                                    l.getString("admin"),
                                    l.getString("report_id"),
                                    l.getString("type"),
                                    "",
                                    "",
                                    l.getString("book_id"),
                                    "",
                                    "",
                                    0,
                                    0,
                                    0,
                                    0,
                                    "",
                                    l.getString("review_id"),
                                    l.getString("review_text"),
                                    l.getString("rating"),
                                    l.getString("user_id"),
                                    l.getString("username"),
                                    null,
                                    null
                            )
                    );
                } else if (l.getString("type").equals("book")) {
                    ArrayList<Author> authorsLis = new ArrayList<>();
                    ArrayList<Document> authors = (ArrayList<Document>) l.get("authors");
                    ArrayList<String> genres = (ArrayList<String>) l.get("genres");
                    for (Document a : authors) {
                        Author author = new Author(
                                a.getString("author_id"),
                                a.getString("author_name"),
                                "",
                                a.getString("author_username"),
                                "",
                                "",
                                null,
                                0
                        );
                        authorsLis.add(author);
                    }
                    logsList.add(
                            new Log(
                                    l.getString("id"),
                                    l.getDate("date"),
                                    l.getString("operation"),
                                    l.getString("admin"),
                                    l.getString("report_id"),
                                    l.getString("type"),
                                    l.getString("isbn"),
                                    l.getString("asin"),
                                    l.getString("book_id"),
                                    l.getString("title"),
                                    l.getString("description"),
                                    (Integer) l.get("num_pages"),
                                    (Integer) l.get("publication_day"),
                                    (Integer) l.get("publication_month"),
                                    (Integer) l.get("publication_year"),
                                    l.getString("image_url"),
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    authorsLis,
                                    genres
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logsList;
    }

    public boolean addLog(Report report, String operation) {
        MongoCollection<Document> logs = md.getCollection("logs");
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        if (report.getType().equals("book")) {
            ArrayList<DBObject> authorsList = new ArrayList<DBObject>();
            for (Author a : report.getAuthors()) {
                DBObject author = new BasicDBObject();
                author.put("author_name", (String) a.getName());
                author.put("author_username", (String) a.getNickname());
                author.put("author_id", (String) a.getId());
                authorsList.add(author);
            }
            Document newLog = new Document("id", UUID.randomUUID().toString())
                    .append("report_id", report.getReport_id())
                    .append("date", date)
                    .append("operation", operation)
                    .append("admin", "admin")
                    .append("type", report.getType())
                    .append("isbn", report.getIsbn() != null ? report.getIsbn() : "")
                    .append("asin", report.getAsin() != null ? report.getAsin() : "")
                    .append("num_pages", report.getNum_pages())
                    .append("publication_day", report.getPublication_day())
                    .append("publication_month", report.getPublication_month())
                    .append("publication_year", report.getPublication_year())
                    .append("image_url", report.getImage_url())
                    .append("book_id", report.getBook_id())
                    .append("title", report.getTitle())
                    .append("description", report.getDescription())
                    .append("authors", authorsList)
                    .append("genres", report.getGenres());
            InsertOneResult res = logs.insertOne(newLog);
            if (!res.wasAcknowledged()) {
                return false;
            }
        } else if (report.getType().equals("review")) {
            Document newLog = new Document("id", UUID.randomUUID().toString())
                    .append("report_id", report.getReport_id())
                    .append("date", date)
                    .append("operation", operation)
                    .append("admin", "admin")
                    .append("type", report.getType())
                    .append("review_id", report.getReview_id())
                    .append("review_text", report.getReview_text())
                    .append("rating", report.getRating())
                    .append("user_id", report.getUser_id())
                    .append("username", report.getUsername())
                    .append("book_id", report.getBook_id());
            InsertOneResult res = logs.insertOne(newLog);
            if (!res.wasAcknowledged()) {
                return false;
            }
        }
        return true;
    }

    public boolean addNewAdmin(String username, String password) {
        InsertOneResult result = null;
        try {
            Document doc = new Document("username", username).append("password", password);
            result = md.getCollection(adminCollection).insertOne(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean reportBook(Book book) {
        MongoCollection<Document> reports = md.getCollection("reports");
        InsertOneResult result = null;
        try (MongoCursor<Document> cursor = reports.find(and(eq("book_id", book.getBook_id()), eq("type", "book"))).iterator()) {
            if (!cursor.hasNext()) {
                ArrayList<DBObject> authorsList = new ArrayList<DBObject>();
                for (Author a : book.getAuthors()) {
                    DBObject author = new BasicDBObject();
                    author.put("author_name", (String) a.getName());
                    author.put("author_username", (String) a.getNickname());
                    author.put("author_id", (String) a.getId());
                    authorsList.add(author);
                }
                Document newBook = new Document();
                newBook.append("report_id", UUID.randomUUID().toString());
                newBook.append("type", "book");
                newBook.append("isbn", book.getIsbn() != null ? book.getIsbn() : "");
                newBook.append("asin", book.getAsin() != null ? book.getAsin() : "");
                newBook.append("book_id", book.getBook_id());
                newBook.append("title", book.getTitle());
                newBook.append("description", book.getDescription());
                newBook.append("num_pages", book.getNum_pages());
                newBook.append("publication_day", book.getPublication_day());
                newBook.append("publication_month", book.getPublication_month());
                newBook.append("publication_year", book.getPublication_year());
                newBook.append("image_url", book.getImage_url());
                newBook.append("genres", book.getGenres());
                newBook.append("authors", authorsList);
                result = reports.insertOne(newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean reportReview(Review review, String book_id) {
        MongoCollection<Document> reports = md.getCollection("reports");
        UpdateResult result = null;

        try (MongoCursor<Document> cursor = reports.find(and(eq("review_id", review.getReview_id()), eq("type", "review"))).iterator()) {
            if (!cursor.hasNext()) {
                Document newReview = new Document();
                newReview.append("report_id", UUID.randomUUID().toString());
                newReview.append("type", "review");
                newReview.append("review_id", review.getReview_id());
                newReview.append("review_text", review.getReview_text());
                newReview.append("rating", review.getRating());
                newReview.append("user_id", review.getUser_id());
                newReview.append("username", review.getUsername());
                newReview.append("book_id", book_id);
                reports.insertOne(newReview);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean deleteReport(Report report, Boolean unreport) {
        MongoCollection<Document> reports = md.getCollection(reportsCollection);
        DeleteResult result = null;
        try {
            result = reports.deleteOne(eq("report_id", report.getReport_id()));
            if (unreport) {
                if (addLog(report, "unreport")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (addLog(report, "delete")) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.wasAcknowledged();
        }
        return false;
    }


}
