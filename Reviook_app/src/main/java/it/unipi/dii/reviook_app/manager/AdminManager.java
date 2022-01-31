package it.unipi.dii.reviook_app.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.entity.Book;
import it.unipi.dii.reviook_app.entity.Report;
import it.unipi.dii.reviook_app.entity.Review;
import org.bson.Document;

import javax.xml.transform.Result;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class AdminManager {
    private MongoDriver md;

    private static final String adminCollection = "admins";
    private static final String bookCollection = "books";
    private static final String reportsCollection = "reports";

    BookManager bookManager = new BookManager();

    public AdminManager() {
        this.md = MongoDriver.getInstance();
    }

    public ArrayList<Report> loadReviewReported() {
        ArrayList<Report> reportedReview = new ArrayList<>();
        try{
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
                            r.getString("username")
                    ));
                }
            }
        }catch (Exception e){
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
            for (Document r : queryResults) {
                if (r.getString("type").equals("book")) {
                    reportedBook.add(new Report(
                            r.getString("report_id"),
                            r.getString("type"),
                            r.getString("book_id"),
                            r.getString("title"),
                            r.getString("description"),
                            "",
                            "",
                            "",
                            ""
                    ));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return reportedBook;
    }

    public boolean addNewAdmin(String username, String password) {
        InsertOneResult result = null;
        try {
            Document doc = new Document("username", username).append("password", password);
            result = md.getCollection(adminCollection).insertOne(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result != null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean reportBook(Book book) {
        MongoCollection<Document> reports = md.getCollection("reports");
        InsertOneResult result = null;
        try{
            if (!reports.find(and(eq("book_id", book.getBook_id()), eq("type", "book"))).iterator().hasNext()) {
                Document newBook = new Document();
                newBook.append("report_id", UUID.randomUUID().toString());
                newBook.append("type", "book");
                newBook.append("book_id", book.getBook_id());
                newBook.append("title", book.getTitle());
                newBook.append("description", book.getDescription());
                result = reports.insertOne(newBook);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result!=null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean reportReview(Review review, String book_id) {
        MongoCollection<Document> reports = md.getCollection("reports");
        UpdateResult result = null;

        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result != null)
            return result.wasAcknowledged();
        return false;
    }

    public boolean deleteReport(Report report) {
        MongoCollection<Document> reports = md.getCollection(reportsCollection);
        DeleteResult result = null;
        try {
            result = reports.deleteOne(eq("report_id", report.getReport_id()));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result != null)
            return result.wasAcknowledged();
        return false;
    }
}
