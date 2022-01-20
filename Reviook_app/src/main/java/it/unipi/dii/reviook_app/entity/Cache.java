package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;
import java.util.Date;

public class Cache {
    private ArrayList<Book> searchedBooks;
    private ArrayList<User> searchedUsers;
    private ArrayList<Author> searchedAuthors;
    private String searchedTitle;
    private String searchedGenres;
    private String searchType;
    private Date lastUpdate;
    private int count=0;

    public Cache() {
        this.searchedBooks = null;
        this.searchedUsers = null;
        this.searchedAuthors = null;
        this.searchedTitle = null;
        this.searchedGenres = null;
        this.lastUpdate = new Date();
    }

    public void ClearCache() {
        this.searchedAuthors = null;
        this.searchedBooks = null;
        this.searchedUsers = null;
        this.searchType = null;
        // TODO gestire anche gli altri campi
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public ArrayList<Book> getSearchedBooks() {
        return searchedBooks;
    }

    public void setSearchedBooks(ArrayList<Book> searchedBooks) {
        this.searchedBooks = searchedBooks;
    }

    public ArrayList<User> getSearchedUsers() {
        return searchedUsers;
    }

    public void setSearchedUsers(ArrayList<User> searchedUsers) {
        this.searchedUsers = searchedUsers;
    }

    public ArrayList<Author> getSearchedAuthors() {
        return searchedAuthors;
    }

    public void setSearchedAuthors(ArrayList<Author> searchedAuthors) {
        this.searchedAuthors = searchedAuthors;
    }

    public String getSearchedTitle() {
        return searchedTitle;
    }

    public void setSearchedTitle(String searchedTitle) {
        this.searchedTitle = searchedTitle;
    }

    public String getSearchedGenres() {
        return searchedGenres;
    }

    public void setSearchedGenres(String searchedGenres) {
        this.searchedGenres = searchedGenres;
    }
}
