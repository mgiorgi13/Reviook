package it.unipi.dii.reviook_app.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Report;
import it.unipi.dii.reviook_app.entity.Review;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class AdminManager {
    private MongoDriver md;

    private static final String adminCollection = "admins";
    private static final String bookCollection = "books";
    private static final String reportsCollection = "reports";

    BookManager bookManager = new BookManager();
    UserManager userManager = new UserManager();

    public AdminManager() {
        this.md = MongoDriver.getInstance();
    }

    public ArrayList<Report> loadReviewReported() {
        MongoCollection<Document> reports = md.getCollection(reportsCollection);
        List<Document> queryResults;
        queryResults = reports.find().into(new ArrayList<>());
        ArrayList<Report> reportedReview = new ArrayList<>();
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
        return reportedReview;
    }

    public ArrayList<Report> loadBookReported() {
        MongoCollection<Document> reports = md.getCollection(reportsCollection);
        List<Document> queryResults;
        queryResults = reports.find().into(new ArrayList<>());
        ArrayList<Report> reportedBook = new ArrayList<>();
        ArrayList<String> authorsLis = new ArrayList<>();
        for (Document r : queryResults) {
            if (r.getString("type").equals("book")) {
                ArrayList<Document> authors = (ArrayList<Document>) r.get("authors");
                ArrayList<String> genres = (ArrayList<String>) r.get("genres");
                for (Document a : authors) {
                    authorsLis.add(a.getString("author_name"));
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
                        genres,
                        authorsLis
                ));
            }
        }
        return reportedBook;
    }

    public void AddNewAdmin(String username, String password) {
        Document doc = new Document("username", username).append("password", password);
        md.getCollection(adminCollection).insertOne(doc);
    }

    public void ReportBook(Book book) {
        MongoCollection<Document> reports = md.getCollection("reports");
        if (!reports.find(and(eq("book_id", book.getBook_id()), eq("type", "book"))).iterator().hasNext()) {
            ArrayList<DBObject> authorsList = new ArrayList<DBObject>();
            for (int i = 0; i < book.getAuthors().size(); i++) {
                authorsList.add(userManager.paramAuthor(book.getAuthors().get(i)));
            }
            Document newBook = new Document();
            newBook.append("report_id", UUID.randomUUID().toString());
            newBook.append("type", "book");
            newBook.append("book_id", book.getBook_id());
            newBook.append("title", book.getTitle());
            newBook.append("description", book.getDescription());
            newBook.append("genres",book.getGenres());
            newBook.append("authors",authorsList);
            reports.insertOne(newBook);
        }
    }

    public void ReportReview(Review review, String book_id) {
        MongoCollection<Document> reports = md.getCollection("reports");
        if (!reports.find(and(eq("review_id", review.getReview_id()), eq("type", "review"))).iterator().hasNext()) {
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
    }

    public void DeleteReport(Report report) {
        MongoCollection<Document> reports = md.getCollection(reportsCollection);
        reports.deleteOne(eq("report_id", report.getReport_id()));
    }
}
