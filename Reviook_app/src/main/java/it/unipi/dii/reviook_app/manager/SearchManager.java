package it.unipi.dii.reviook_app.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.TextSearchOptions;
import it.unipi.dii.reviook_app.entity.*;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Sorts.orderBy;

public class SearchManager {
    private MongoDriver md;
    private Neo4jDriver nd;
    private it.unipi.dii.reviook_app.Session session = it.unipi.dii.reviook_app.Session.getInstance();

    private static final String usersCollection = "users";
    private static final String authorCollection = "authors";
    private static final String bookCollection = "books";
    private static final String genreCollection = "genres";


    public SearchManager() {
        this.md = MongoDriver.getInstance();
        this.nd = Neo4jDriver.getInstance();
    }

    public Book searchIdBook(String idBook) {
        MongoCollection<Document> books = md.getCollection(bookCollection);
        Book result = null;

        try (MongoCursor<Document> cursor = books.find(in("book_id", idBook)).iterator()) {

            while (cursor.hasNext()) {
                Document book = cursor.next();
                //System.out.println("documento->" + document);
                ArrayList<Document> authors = (ArrayList<Document>) book.get("authors");
                ArrayList<Document> reviews = (ArrayList<Document>) book.get("reviews");
                ArrayList<String> genres = (ArrayList<String>) book.get("genres");
                ArrayList<Author> authorsLis = new ArrayList<>();
                ArrayList<Review> reviewsList = new ArrayList<>();


                for (Document r : reviews) {
                    String date;
                    if(r.get("date_updated") == null) {
                        if (r.get("date_added") == null) {
                            date = null;
                        }else{
                            date = r.get("date_added").toString();
                        }
                    }else {
                        date = r.get("date_updated").toString();
                    }
                    reviewsList.add(new Review(
                            r.getString("username"),
                            r.getString("review_id"),
                            date,
                            r.get("likes") == null ? r.getInteger("helpful") : r.getInteger("likes"),
                            r.getString("user_id"),
                            r.get("rating").toString(),
                            r.getString("review_text")
                    ));
                }
                for (Document a : authors) {
                    Author author = new Author(
                            a.getString("author_id"),
                            a.getString("author_name"),
                            "",
                            "",
                            "",
                            null,
                            0
                    );
                    authorsLis.add(author);
//            authorsLis.add(a.getString("author_name"));
                }
                result = (new Book(
                        book.get("isbn") == null ? null : book.getString("isbn"),
                        book.get("language_code") == null ? null : book.getString("language_code"),
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
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public ArrayList<Book> searchBooks(String searchField, String genre) {

        MongoCollection<Document> books = md.getCollection(bookCollection);
        MongoCursor<Document> cursor = null;
        ArrayList<Book> result = new ArrayList<>();

        boolean titleSearch = true;
        boolean genresSearch = true;

        if (searchField == null || searchField.equals(""))
            titleSearch = false;
        if (genre == null || genre.equals(""))
            genresSearch = false;

        Bson titleFilter;
        Bson genreFilter;

        try {
            //global research
            if (!titleSearch && !genresSearch)
                cursor = books.find().iterator();
                //search by title
            else if (titleSearch && !genresSearch) {
                titleFilter = text("\"" + searchField + "\"", new TextSearchOptions().caseSensitive(false));
                cursor = books.find(titleFilter).iterator();
            }
            //search by genre
            else if (!titleSearch && genresSearch) {
                genreFilter = in("genres", genre);
                cursor = books.find(genreFilter).iterator();
            }
            //search by title & genre
            else {
                titleFilter = match(text(searchField, new TextSearchOptions().caseSensitive(false)));
                genreFilter = match(in("genres", genre));
                cursor = books.aggregate(Arrays.asList(titleFilter, genreFilter)).iterator();
            }

            while (cursor.hasNext()) {
                Document book = cursor.next();
                //System.out.println("documento->" + document);

                ArrayList<Author> authorsLis = new ArrayList<>();
                ArrayList<Review> reviewsList = new ArrayList<>();
                ArrayList<Document> authors = (ArrayList<Document>) book.get("authors");
                ArrayList<Document> reviews = (ArrayList<Document>) book.get("reviews");
                ArrayList<String> genres = (ArrayList<String>) book.get("genres");

                for (Document r : reviews) {
                    String date;
                    if(r.get("date_updated") == null) {
                        if (r.get("date_added") == null) {
                            date = null;
                        }else{
                            date = r.get("date_added").toString();
                        }
                    }else {
                        date = r.get("date_updated").toString();
                    }
                    reviewsList.add(new Review(
                            r.getString("username"),
                            r.getString("review_id"),
                            date,
                            r.get("likes") == null ? r.getInteger("helpful") : r.getInteger("likes"),
                            r.getString("user_id"),
                            r.get("rating").toString(),
                            r.getString("review_text")
                    ));
                }
                for (Document a : authors) {
                    Author author = new Author(
                            a.getString("author_id"),
                            a.getString("author_name"),
                            "",
                            "",
                            "",
                            null,
                            0
                    );
                    authorsLis.add(author);
                    //authorsLis.add(a.getString("author_name"));
                }

                result.add(new Book(
                        book.get("isbn") == null ? null : book.getString("isbn"),
                        book.get("language_code") == null ? null : book.getString("language_code"),
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
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return result;
    }

    public ArrayList<User> searchUser(String Username) {
        MongoCollection<Document> user = md.getCollection(usersCollection);
        List<Document> queryResults;
        ArrayList<User> result = new ArrayList<>();
        //search on exact username
        try {
            if (Username.equals("")) {
                queryResults = user.find().into(new ArrayList());
            } else {
                queryResults = user.find(eq("username", Username)).into(new ArrayList());
            }

            for (Document r : queryResults) {
                ArrayList<String> listReviewID = (ArrayList<String>) r.get("liked_review");
                result.add(new User(r.getString("user_id"), r.get("name").toString(), r.get("username").toString(), r.get("email").toString(), r.get("password").toString(), listReviewID, (Integer) r.get("follower_count")));
            }

            //search on name or surname
            if (!Username.equals("")) {
                queryResults = user.find(text("\"" + Username + "\"", new TextSearchOptions().caseSensitive(false))).into(new ArrayList());
                User us;
                for (Document r : queryResults) {
                    ArrayList<String> listReviewID = (ArrayList<String>) r.get("liked_review");
                    us = new User(r.getString("user_id"), r.get("name").toString(), r.get("username").toString(), r.get("email").toString(), r.get("password").toString(), listReviewID, (Integer) r.get("follower_count"));
                    if (!result.contains(us))
                        result.add(us);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Author> searchAuthor(String Username) {
        MongoCollection<Document> author = md.getCollection(authorCollection);
        List<Document> queryResults;
        ArrayList<Author> result = new ArrayList<>();

        //search on exact username
        try {
            if (Username.equals("")) {
                queryResults = author.find().into(new ArrayList());
            } else {
                queryResults = author.find(eq("username", Username)).into(new ArrayList());
            }

            for (Document r : queryResults) {
                ArrayList<String> listReviewID = (ArrayList<String>) r.get("liked_review");
                result.add(new Author(r.getString("author_id"), r.get("name").toString(), r.get("username").toString(), r.get("email").toString(), r.get("password").toString(), listReviewID, (Integer) r.get("follower_count")));
            }

            if (!Username.equals("")) {
                //search on name or surname
                queryResults = author.find(text("\"" + Username + "\"", new TextSearchOptions().caseSensitive(false))).into(new ArrayList());
                Author auth;
                for (Document r : queryResults) {
                    ArrayList<String> listReviewID = (ArrayList<String>) r.get("liked_review");
                    auth = new Author(r.getString("author_id"), r.get("name").toString(), r.get("username").toString(), r.get("email").toString(), r.get("password").toString(), listReviewID, (Integer) r.get("follower_count"));
                    if (!result.contains(auth))
                        result.add(auth);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Genre> searchGenres() {
        MongoCollection<Document> genres = md.getCollection(genreCollection);
        ArrayList<Genre> result = new ArrayList<>();

        try (MongoCursor<Document> cursor = genres.find().iterator()) {

            while (cursor.hasNext()) {
                result.add(new Genre(cursor.next().getString("_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> searchLanguageCode() {
        MongoCollection<Document> languageCode = md.getCollection(bookCollection);
        ArrayList<String> result = new ArrayList<>();
        Bson group = group("$language_code", sum("counter", 1));

        try (MongoCursor<Document> cursor = languageCode.aggregate(Arrays.asList(group)).iterator();) {
            while (cursor.hasNext()) {
                result.add(cursor.next().getString("_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> searchYears() {
        MongoCollection<Document> years = md.getCollection(bookCollection);
        ArrayList<String> total_years = new ArrayList<>();
        Bson match = match(and(gte("publication_year", 1900), lte("publication_year", 2022)));
        Bson group = group("$publication_year");
        Bson sort = sort(orderBy(descending("_id")));

        try (MongoCursor<Document> cursor = years.aggregate(Arrays.asList(match, group, sort)).iterator()) {

            while (cursor.hasNext()) {
                Document y = cursor.next();
                total_years.add(String.valueOf(y.getInteger("_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total_years;

    }
}
