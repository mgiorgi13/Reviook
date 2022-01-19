package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class Cache {
    private ArrayList<Book> searchedBooks;
    private ArrayList<User> searchedUsers;
    private ArrayList<Author> searchedAuthors;
    private String searchedTitle;
    private String searchedGenres;
    private String searchType;

    public Cache() {
        this.searchedBooks = null;
        this.searchedUsers = null;
        this.searchedAuthors = null;
        this.searchedTitle = null;
        this.searchedGenres = null;
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
