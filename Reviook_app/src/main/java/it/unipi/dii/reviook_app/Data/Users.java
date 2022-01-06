package it.unipi.dii.reviook_app.Data;
import it.unipi.dii.reviook_app.Neo4jDriver;
import javafx.fxml.FXML;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

public class Users {
    @FXML
    private String name;
    private String surname;
    private String nickname;
    private String email;
    private String password;
    private Interaction interactions;
    private Books books;


    public Users(String name, String surname, String nickname,String email, String password) {
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.email = email;
        this.password =password;
        this.interactions = new Interaction();
        this.books = new Books();
    }

    public void Login(){}
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public Interaction getInteractions() {
        return interactions;
    }

    public Books getBooks() {
        return books;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInteractions(Interaction interactions) {
        this.interactions = interactions;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

}
