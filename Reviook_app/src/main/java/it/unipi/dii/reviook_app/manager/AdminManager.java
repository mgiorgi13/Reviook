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
import it.unipi.dii.reviook_app.entity.*;
import org.bson.Document;

import javax.print.Doc;
import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class AdminManager {
    private MongoDriver md;

    private static final String adminCollection = "admins";
    private static final String bookCollection = "books";
    private static final String reportsCollection = "reports";
    private static final String logsCollection = "logs";

    BookManager bookManager = new BookManager();
    UserManager userManager = new UserManager();

    public AdminManager() {
        this.md = MongoDriver.getInstance();
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
                            r.getString("book_id"),
                            "",
                            "",
                            r.getString("review_id"),
                            r.getString("review_text"),
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
                            r.getString("book_id"),
                            r.getString("title"),
                            r.getString("description"),
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

    public ArrayList<Log> loadLogs() {
        ArrayList<Log> logsList = new ArrayList<>();
        try {
            MongoCollection<Document> logs = md.getCollection(logsCollection);
            List<Document> results = logs.find().into(new ArrayList<>());
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
                                    l.getString("book_id"),
                                    "",
                                    "",
                                    l.getString("review_id"),
                                    l.getString("review_text"),
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
                                    l.getString("book_id"),
                                    l.getString("title"),
                                    l.getString("description"),
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

    public boolean addLog(Report report) {
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
                    .append("operation", "unreport")
                    .append("admin", "admin")
                    .append("type", report.getType())
                    .append("book_id", report.getBook_id())
                    .append("title", report.getTitle())
                    .append("description", report.getDescription())
                    .append("authors", authorsList)
                    .append("genres", report.getGenres());
            InsertOneResult res = logs.insertOne(newLog);
        } else if (report.getType().equals("review")) {
            Document newLog = new Document("id", UUID.randomUUID().toString())
                    .append("report_id", report.getReport_id())
                    .append("date", date)
                    .append("operation", "unreport")
                    .append("admin", "admin")
                    .append("type", report.getType())
                    .append("review_id", report.getReview_id())
                    .append("review_text", report.getReview_text())
                    .append("user_id", report.getUser_id())
                    .append("username", report.getUsername())
                    .append("book_id", report.getBook_id());
            InsertOneResult res = logs.insertOne(newLog);
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
                newBook.append("book_id", book.getBook_id());
                newBook.append("title", book.getTitle());
                newBook.append("description", book.getDescription());
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

    public boolean deleteReport(Report report) {
        MongoCollection<Document> reports = md.getCollection(reportsCollection);
        DeleteResult result = null;
        try {
            result = reports.deleteOne(eq("report_id", report.getReport_id()));
            addLog(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.wasAcknowledged();
        }
        return false;
    }
}
