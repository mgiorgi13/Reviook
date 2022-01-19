//
//package it.unipi.dii.reviook_app;
//
//import com.mongodb.client.MongoCollection;
//import it.unipi.dii.reviook_app.entity.Author;
//import it.unipi.dii.reviook_app.entity.Book;
//import it.unipi.dii.reviook_app.entity.Genre;
//import it.unipi.dii.reviook_app.entity.User;
//import it.unipi.dii.reviook_app.manager.BookManager;
//import it.unipi.dii.reviook_app.manager.SearchManager;
//import it.unipi.dii.reviook_app.manager.UserManager;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//
//
//import org.bson.Document;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.neo4j.driver.TransactionWork;
//
//import java.io.FileReader;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.Scanner;
//
//import static com.mongodb.client.model.Filters.in;
//import static org.neo4j.driver.Values.parameters;
//
//
//public class fillDB {
//    public static int count = 0;
//    public static int writeCount = 0;
//    public static String target = "authorsDocument";
//    public static String newFileName = "Document";
//    private static int ntread = 2000;
//    private static volatile Integer contatore = 0; // 28100
//    private static ArrayList<Author> author = new ArrayList();
//    private static ArrayList<User> user = new ArrayList();
//    private static ArrayList<Book> book = new ArrayList();
//    private static  Neo4jDriver nd = Neo4jDriver.getInstance();
//    private static  MongoDriver md = MongoDriver.getInstance();
//
//    public static void main(String[] args)
//
//    {
////        System.out.println("Enter Input : ");
////        Scanner scanner = new Scanner(System.in);
////        int a = scanner.nextInt();
////        if (a == 1)
////        {
////
////        }else{
////
////        }
////        JSONArray revList = new JSONArray();
////
////        //JSON parser object to parse read file
////        JSONParser jsonParser = new JSONParser();
////
////        System.out.println("Aspetta che apro il file...");
////
////        try {
////            FileReader reader = new FileReader(target+".json");
////            //Read JSON file
////            Object obj = jsonParser.parse(reader);
////            System.out.println("...aperto!");
////            JSONArray reviewList = (JSONArray) obj;
////
////            //Iterate over employee array
////            reviewList.forEach( rev -> parseReviewObject( (JSONObject) rev ) );
////
////
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        insertUsers();
//    }
//    private static void parseReviewObject(JSONObject review)
//    {
//        count++;
//        writeCount++;
//
////
//        String username = (String) review.get("username");
//
//
//
//
//
////        result.put("user_id",user_id);
////        result.put("book_id",book_id);
////        result.put("rating",rating);
////        result.put("review_text",review_text);
////        result.put("likes",likes);
//
//
//
//
//
//
//        appendJson(username);
//
//        if(writeCount >= 100000){
//            System.out.println(count);
//            writeCount = 0;
//        }
//    }
//
//    private static void appendJson(String username){
//        SearchManager us = new SearchManager();
//        ArrayList<User> users = new ArrayList();
//        users = us.searchUser("");
//
//        if (count == 1) System.out.println("ok");
//        if (count == 829528) System.out.println("Fine");
//    }
//
//    private static synchronized void addBooks(String id, String title) {
//        try (org.neo4j.driver.Session session = nd.getDriver().session()) {
//            session.writeTransaction((TransactionWork<Void>) tx -> {
//                tx.run("CREATE (ee: Book { id : $id, title: $title})", parameters("id", id, "title", title));
//                return null;
//            });
//        }
//    }
//
//    private static synchronized void addwrote(String author_nick,ArrayList<String> books) {
//        if(!books.isEmpty()) {
//            try (org.neo4j.driver.Session session = nd.getDriver().session()) {
//                session.writeTransaction((TransactionWork<Void>) tx -> {
//                    for (int i = 0; i < books.size(); i++) {
//                        tx.run("MATCH (dd:Author),(ee: Book) WHERE dd.username = '" + author_nick + "' AND ee.id='" + books.get(i) + "'" +
//                                "CREATE (dd)-[:WROTE]->(ee)");
//                    }
//                    return null;
//                });
//            }
//        }
//    }
//
//    public static synchronized ArrayList<String> searchBooksAuthor(String author_id) {
//        MongoCollection<Document> book = md.getCollection("books");
//        List<Document> queryResults;
//        if (author_id.equals(""))
//            queryResults = book.find().into(new ArrayList());
//        else
//            queryResults = book.find(in("authors.author_id", author_id)).into(new ArrayList());
//        ArrayList<String> result = new ArrayList<>();
//
//        for (Document r :
//                queryResults) {
//            result.add(r.getString("book_id"));
//        }
//        return result;
//    }
//
//
//    private static void insertUsers() {
//        SearchManager sm = new SearchManager();
//        UserManager um = new UserManager();
//        author = sm.searchAuthor("");
////        user = sm.searchUser("");
////        book = sm.searchBooks("","");
//
//        int size = book.size();
//
//        Runnable myRunnable = new Runnable(){
//        public void run(){
////            ArrayList<Author> copyA = author;
//            ArrayList<String> books;
//
//            while (true){
//                String id , name, nick , nick2,nick3,nick4;
//                String book1, book2, book3, book4, book5, book6;
//                int i = (int) (Math.random()* size);
//
//                synchronized (contatore) {
//                    synchronized (author) {
//                        if (contatore >= author.size()) { // user
//                            // System.out.println(Thread.currentThread().getId() + " : " + contatore);
//                            break;
//                        }
//                        //new author
//                        id = author.get(contatore).getId();
//                        name = author.get(contatore).getName();
//                        nick = author.get(contatore).getNickname();
////                        //new user
////                        id = user.get(contatore).getId();
////                        name = user.get(contatore).getName();
////                        nick = user.get(contatore).getNickname();
////                        //book
////                        id = book.get(contatore).getBook_id();
////                        name = book.get(contatore).getTitle();
//
////                        //toread read
////                        book1 = books.get(i).getBook_id();
////                        book2 = books.get((i+1)%size).getBook_id();
////                        book3 = books.get((i+2)%size).getBook_id();
////                        book4 = books.get((i+3)%size).getBook_id();
////                        book5 = books.get((i+4)%size).getBook_id();
////                        book6 = books.get((i+5)%size).getBook_id();
//
//                        contatore++;
//                    }
//                }
////                um.addNewUsers("Author",id, name, nick);
////                um.addNewUsers("User",id, name, nick);
////                addBooks(id,name);
//
////                nick2 = copyA.get(i).getNickname();
////                nick3 = copyA.get((i+1)% copyA.size()).getNickname();
////                nick4 = copyA.get((i+2)% copyA.size()).getNickname();
////
////                if(!nick.equals(nick2))
////                    um.following(nick, "User", nick2, "Author");
////                if(!nick.equals(nick3))
////                    um.following(nick, "User", nick3, "Author");
////                if(!nick.equals(nick4))
////                    um.following(nick, "User", nick4, "Author");
//
//                //add wrote relation
//                books = searchBooksAuthor(id);
////                System.out.println(nick);
////                System.out.println(books);
//                addwrote(nick,books);
//
////                //to read read
////
////                um.toReadAdd("User",nick,book1);
////                um.toReadAdd("User",nick,book2);
////                um.toReadAdd("User",nick,book3);
////
////                um.readAdd("User",nick,book4);
////                um.readAdd("User",nick,book5);
////                um.readAdd("User",nick,book6);
//
//
//
//                if (contatore % 100 == 0)
//                    System.out.println(Thread.currentThread().getId() + " : " + contatore);
//            }
//
//            System.out.println("-------->" + contatore);
//
//            }
//        };
//
//        try {
//            for (int i = 0; i < ntread; i++) {
//                Thread n = new Thread(myRunnable);
//                n.start();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        System.out.println(author.size() + "<--------------------");
//
//    }
//}
//
