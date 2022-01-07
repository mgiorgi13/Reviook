
//package it.unipi.dii.reviook_app;
//
//import it.unipi.dii.reviook_app.Data.Author;
//import it.unipi.dii.reviook_app.Manager.UserManager;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import it.unipi.dii.reviook_app.Data.Users;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Scanner;
//import java.util.Scanner;
//public class fillDB {
//    public static int count = 0;
//    public static int writeCount = 0;
//    public static String target = "authorsDocument";
//    public static String newFileName = "Document";
//    public static void main(String[] args)
//    {
//        System.out.println("Enter Input : ");
//        Scanner scanner = new Scanner(System.in);
//        int a = scanner.nextInt();
//        if (a == 1)
//        {
//
//        }else{
//
//        }
//        JSONArray revList = new JSONArray();
//
//        //JSON parser object to parse read file
//        JSONParser jsonParser = new JSONParser();
//
//        System.out.println("Aspetta che apro il file...");
//
//        try {
//            FileReader reader = new FileReader(target+".json");
//            //Read JSON file
//            Object obj = jsonParser.parse(reader);
//            System.out.println("...aperto!");
//            JSONArray reviewList = (JSONArray) obj;
//
//            //Iterate over employee array
//            reviewList.forEach( rev -> parseReviewObject( (JSONObject) rev ) );
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
////        result.put("n_votes",n_votes);
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
//        UserManager us = new UserManager();
//        Faker faker = new Faker();
//        Author users = new Author("","",username,"","");
//        Neo4jDriver nd = Neo4jDriver.getInstance();
//        us.addNewUsers("Author",username);
//        if (count == 1) System.out.println("ok");
//        if (count == 829528) System.out.println("Fine");
//    }
//
//
//
//}
