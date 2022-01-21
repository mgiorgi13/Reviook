package it.unipi.dii.reviook_app.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.TextSearchOptions;
import it.unipi.dii.reviook_app.entity.*;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public Book searchIdBook(String idBook)
    {
        MongoCollection<Document> books = md.getCollection(bookCollection);
        MongoCursor<Document> cursor;
        Book result = null;

        cursor = books.find(in("book_id", idBook)).iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            //System.out.println("documento->" + document);
            ArrayList<Document> authors;
            ArrayList<Document> reviews;
            ArrayList<String> genres;
            ArrayList<String> authorsLis = new ArrayList<>();
            ArrayList<Review> reviewsList = new ArrayList<>();

            authors = (ArrayList<Document>) document.get("authors");
            reviews = (ArrayList<Document>) document.get("reviews");
            genres = (ArrayList<String>) document.get("genres");
            for (Document r : reviews) {
                reviewsList.add(new Review(
                        new SimpleStringProperty(r.get("date_added").toString()),
                        new SimpleStringProperty(r.getString("review_id")),
                        new SimpleStringProperty(r.get("date_updated") == null ? "": r.get("date_updated").toString()),
                        new SimpleIntegerProperty(r.get("likes") == null ? r.getInteger("helpful") : r.getInteger("likes")),
                        new SimpleStringProperty(r.getString("user_id")),
                        new SimpleStringProperty(r.get("rating").toString()),
                        new SimpleStringProperty(r.getString("review_text"))
                ));
            }
            for (Document a : authors) {
                authorsLis.add(a.getString("author_name"));
            }
            result = (new Book(

                    document.get("isbn").toString(),
                    document.get("language_code").toString(),
                    document.get("asin").toString(),
                    document.getDouble("average_rating"),
                    document.get("description").toString(),
                    document.getInteger("num_pages"),
                    document.get("publication_day").toString().equals("") ? 0 : Integer.valueOf(document.get("publication_day").toString()),
                    document.get("publication_month").toString().equals("") ? 0 : Integer.valueOf(document.get("publication_month").toString()),
                    document.get("publication_year").toString().equals("") ? 0 : Integer.valueOf(document.get("publication_year").toString()),
                    document.get("image_url").toString(),
                    document.get("book_id").toString(),
                    document.getInteger("ratings_count"),
                    document.get("title").toString(),
                    authorsLis,
                    genres,
                    reviewsList
            ));
        }
        cursor.close();

        return result;

    }
    public ArrayList<Book> searchBooks(String searchField, String genre) {
        //TODO add support to genres, authors, review
        ArrayList<Document> authors;
        ArrayList<Document> reviews;
        ArrayList<String> genres;

        MongoCollection<Document> books = md.getCollection(bookCollection);
        MongoCursor<Document> cursor;
        ArrayList<Book> result = new ArrayList<>();

        boolean titleSearch = true;
        boolean genresSearch = true;

        if (searchField == null || searchField.equals(""))
            titleSearch = false;
        if (genre == null || genre.equals(""))
            genresSearch = false;

        Bson titleFilter;
        Bson genreFilter;

        //global research
        if (!titleSearch && !genresSearch)
            cursor = books.find().iterator();
            //search by title
        else if (titleSearch && !genresSearch) {
            titleFilter = text(searchField, new TextSearchOptions().caseSensitive(false));
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
            Document document = cursor.next();
            //System.out.println("documento->" + document);

            ArrayList<String> authorsLis = new ArrayList<>();
            ArrayList<Review> reviewsList = new ArrayList<>();

            authors = (ArrayList<Document>) document.get("authors");
            reviews = (ArrayList<Document>) document.get("reviews");
            genres = (ArrayList<String>) document.get("genres");

            for (Document r : reviews) {
                reviewsList.add(new Review(
                        new SimpleStringProperty(r.get("date_added").toString()),
                        new SimpleStringProperty(r.getString("review_id")),
                        new SimpleStringProperty(r.get("date_updated") == null ? "" : r.get("date_updated").toString()),
                        new SimpleIntegerProperty(r.get("likes") == null ? r.getInteger("helpful") : r.getInteger("likes")),
                        new SimpleStringProperty(r.getString("user_id")),
                        new SimpleStringProperty(r.get("rating").toString()),
                        new SimpleStringProperty(r.getString("review_text"))
                ));
            }
            for (Document a : authors) {
                authorsLis.add(a.getString("author_name"));
            }

            result.add(new Book(
                    document.get("isbn").toString(),
                    document.get("language_code").toString(),
                    document.get("asin").toString(),
                    document.get("average_rating").toString().equals("") ? Double.valueOf(0) : Double.valueOf(document.get("average_rating").toString()),
                    document.get("description").toString(),
                    document.get("num_pages").toString().equals("") ? 0 : Integer.valueOf(document.get("num_pages").toString()),
                    document.get("publication_day").toString().equals("") ? 0 : Integer.valueOf(document.get("publication_day").toString()),
                    document.get("publication_month").toString().equals("") ? 0 : Integer.valueOf(document.get("publication_month").toString()),
                    document.get("publication_year").toString().equals("") ? 0 : Integer.valueOf(document.get("publication_year").toString()),
                    document.get("image_url").toString(),
                    document.get("book_id").toString(),
                    document.get("ratings_count").toString().equals("") ? Integer.valueOf(0) : Integer.valueOf(document.get("ratings_count").toString()),
                    document.get("title").toString(),
                    authorsLis,
                    genres,
                    reviewsList
            ));
        }
        cursor.close();

        return result;
    }

    public ArrayList<String> searchBooksAuthor(String author_id) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        List<Document> queryResults;
        if (author_id.equals(""))
            queryResults = book.find().into(new ArrayList());
        else
            queryResults = book.find(in("authors.author_id", author_id)).into(new ArrayList());
        ArrayList<String> result = new ArrayList<>();

        //TODO MODIFICA BOOK_ID TO TITLE
        for (Document r :
                queryResults) {
            result.add(r.getString("title"));
        }
        return result;
    }

    public ArrayList<String> searchStatisticBooks(String Username) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        String queryResults;
        ArrayList<String> Genres = new ArrayList<String>();

        Bson match = match(in("author", Username));
        Bson unwind = unwind("$genres");
        Bson group = group("$genres", sum("counter", 1));
        Bson project = project(fields(computed("genre", "$_id"), include("counter"), exclude("_id")));


        try (MongoCursor<Document> result = book.aggregate(Arrays.asList(match, unwind, group, project)).iterator();) {

            while (result.hasNext()) {
                Document genre = result.next();
                Genres.add(genre.getString("genre") + ":" + genre.getInteger("counter"));
            }
        }
        return Genres;

    }

    public ArrayList<User> searchUser(String Username) {
        MongoCollection<Document> user = md.getCollection(usersCollection);
        List<Document> queryResults;
        //search on exact username
        if (Username.equals(""))
            queryResults = user.find().into(new ArrayList());
        else
            queryResults = user.find(eq("username", Username)).into(new ArrayList());
        ArrayList<User> result = new ArrayList<>();

        for (Document r :
                queryResults) {
            result.add(new User(r.getString("user_id"),r.get("name").toString(), "", r.get("username").toString(), r.get("email").toString(), r.get("password").toString()));
        }

        //search on name or surname
        if(!Username.equals("")){
            queryResults = user.find(text(Username, new TextSearchOptions().caseSensitive(false))).into(new ArrayList());
            User us;
            for (Document r :
                    queryResults) {
                us = new User(r.getString("user_id"),r.get("name").toString(), "", r.get("username").toString(), r.get("email").toString(), r.get("password").toString());
                if(!result.contains(us))
                    result.add(us);
            }
        }

        return result;
    }

    public ArrayList<Author> searchAuthor(String Username) {
        MongoCollection<Document> author = md.getCollection(authorCollection);
        List<Document> queryResults;
        //search on exact username
        if (Username.equals(""))
            queryResults = author.find().into(new ArrayList());
        else
            queryResults = author.find(eq("username", Username)).into(new ArrayList());
        ArrayList<Author> result = new ArrayList<>();

        for (Document r :
                queryResults) {
            result.add(new Author(r.getString("author_id"),r.get("name").toString(), "", r.get("username").toString(), r.get("email").toString(), r.get("password").toString()));
        }

        if(!Username.equals("")) {
            //search on name or surname
            queryResults = author.find(text(Username, new TextSearchOptions().caseSensitive(false))).into(new ArrayList());
            Author auth;
            for (Document r :
                    queryResults) {
                auth = new Author(r.getString("author_id"), r.get("name").toString(), "", r.get("username").toString(), r.get("email").toString(), r.get("password").toString());
                if (!result.contains(auth))
                    result.add(auth);
            }
        }
        return result;
    }

    public ArrayList<Genre> searchGenres() {
        MongoCollection<Document> genres = md.getCollection(genreCollection);
        MongoCursor<Document> cursor;
        ArrayList<Genre> result = new ArrayList<>();

        cursor = genres.find().iterator();

        while (cursor.hasNext()){
            result.add( new Genre(cursor.next().getString("_id")));
        }

        return result;
    }
    public JSONArray searchRankBook(Integer year){
        Date dt=new Date();

        MongoCollection<Document> bookGenres = md.getCollection(bookCollection);
        MongoCursor<Document> cursor;
        ArrayList<Book> total_years = new ArrayList<>();

        JSONArray genre = new JSONArray();
        Bson match = match(in("publication_year", year));
        Bson unwind = unwind("$genres");
        Bson group = group("$genres", sum("counter", 1));



        try (MongoCursor<Document> result = bookGenres.aggregate(Arrays.asList(match,unwind, group)).iterator();) {

            while (result.hasNext()) {
                Document y = result.next();
                total_years.add(new Book("",""));
                genre.put(y.getString("_id"));
                genre.put(y.getInteger("counter"));

            }


            System.out.println(genre.length());
        }

        return genre;
    }

    public ArrayList<RankingObject> rankReview()
    {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        ArrayList<RankingObject> array = new ArrayList<>();



        try (MongoCursor<Document> result = book.aggregate(Arrays.asList(new Document("$unwind",
                        new Document("path", "$reviews")
                                .append("includeArrayIndex", "string")
                                .append("preserveNullAndEmptyArrays", false)),
                new Document("$project",
                        new Document("reviews.username", 1L)
                                .append("reviews.likes",
                                        new Document("$ifNull", Arrays.asList("$reviews.likes", "$reviews.helpful")))),
                new Document("$group",
                        new Document("_id", "$reviews.username")
                                .append("reviews_number",
                                        new Document("$count",
                                                new Document()))
                                .append("average_likes",
                                        new Document("$avg", "$reviews.likes"))),
                new Document("$match",
                        new Document("reviews_number",
                                new Document("$gte", 200L))),
                new Document("$sort",
                        new Document("average_likes", -1L)),
                new Document("$limit", 100L))).iterator();) {

            while (result.hasNext()) {
                Document document = result.next();
                array.add(new RankingObject(document.getString("_id"),
                        document.getInteger("reviews_number"),
                        document.getDouble("average_likes")));
            }
        }
        return array;

    }

    public ArrayList<String> searchYears(){

        MongoCollection<Document> years = md.getCollection(bookCollection);
        MongoCursor<Document> cursor;
        ArrayList<String> total_years = new ArrayList<>();


        Bson match = match(and(gte("publication_year", 1900),lte("publication_year", 2022)));
        Bson group = group("$publication_year");
        Bson sort = sort(orderBy(descending("_id")));


        try (MongoCursor<Document> result = years.aggregate(Arrays.asList(match, group, sort)).iterator();) {

            while (result.hasNext()) {
                Document y = result.next();
                total_years.add(String.valueOf(y.getInteger("_id")));
            }
        }
        return total_years;

    }
}
