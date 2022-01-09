package it.unipi.dii.reviook_app.Manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.TextSearchOptions;
import it.unipi.dii.reviook_app.Data.Author;
import it.unipi.dii.reviook_app.Data.Book;
import it.unipi.dii.reviook_app.Data.Review;
import it.unipi.dii.reviook_app.Data.Users;
import it.unipi.dii.reviook_app.MongoDriver;
import it.unipi.dii.reviook_app.Neo4jDriver;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.exclude;

public class SearchManager {
    private MongoDriver md;
    private Neo4jDriver nd;
    private it.unipi.dii.reviook_app.Session session = it.unipi.dii.reviook_app.Session.getInstance();

    private static final String usersCollection = "users";
    private static final String authorCollection = "authors";
    private static final String bookCollection = "amazonBooks";
    private static final String genreCollection = "geners";


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
                        r.get("date_added").toString(),
                        r.get("review_id").toString(),
                        r.get("date_updated").toString(),
                        r.get("n_votes").toString(),
                        r.get("user_id").toString(),
                        r.get("rating").toString(),
                        r.get("review_text").toString(),
                        r.get("helpful").toString()
                ));
            }
            for (Document a : authors) {
                authorsLis.add(a.getString("author_name"));
            }
            result = (new Book(

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
                        r.get("date_added").toString(),
                        r.get("review_id").toString(),
                        r.get("date_updated").toString(),
                        r.get("n_votes").toString(),
                        r.get("user_id").toString(),
                        r.get("rating").toString(),
                        r.get("review_text").toString(),
                        r.get("helpful").toString()
                ));
            }
            for (Document a : authors) {
                authorsLis.add(a.getString("author_name"));
            }


            //TODO inserisci nome dell'autore nel db
            //TODO migliorare se possibile il modo in cui si prelevano i campi embedded e array
//            for (Document a :
//                    authors) {
//                authorsLis.add(a.getString("author_id"));
//            }

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



    public ArrayList<String> searchBooksAuthor(String Username) {
        MongoCollection<Document> book = md.getCollection(bookCollection);
        List<Document> queryResults;
        if (Username.equals(""))
            queryResults = book.find().into(new ArrayList());
        else
            queryResults = book.find(in("authors.author_id", Username)).into(new ArrayList());
        ArrayList<String> result = new ArrayList<>();

        for (Document r :
                queryResults) {
            result.add(new String(r.get("title").toString()));
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

    public ArrayList<Users> searchUser(String Username) {
        MongoCollection<Document> user = md.getCollection(usersCollection);
        List<Document> queryResults;
        if (Username.equals(""))
            queryResults = user.find().into(new ArrayList());
        else
            queryResults = user.find(eq("username", Username)).into(new ArrayList());
        ArrayList<Users> result = new ArrayList<>();

        for (Document r :
                queryResults) {
            result.add(new Users(r.get("name").toString(), "", r.get("username").toString(), r.get("email").toString(), r.get("password").toString()));
        }
        return result;
    }

    public ArrayList<Author> searchAuthor(String Username) {
        MongoCollection<Document> author = md.getCollection(authorCollection);
        List<Document> queryResults;
        if (Username.equals(""))
            queryResults = author.find().into(new ArrayList());
        else
            queryResults = author.find(eq("username", Username)).into(new ArrayList());
        ArrayList<Author> result = new ArrayList<>();

        for (Document r :
                queryResults) {
            result.add(new Author(r.get("name").toString(), "", r.get("username").toString(), r.get("email").toString(), r.get("password").toString()));
        }
        return result;
    }

    public ArrayList<String> searchGeners() {
        MongoCollection<Document> geners = md.getCollection(genreCollection);
        List<Document> queryResults;

        queryResults = geners.find().into(new ArrayList());
        ArrayList<String> result = new ArrayList<>();

        for (Document r :
                queryResults) {
            result.add(new String(r.get("_id").toString()));
        }
        return result;
    }
}
