package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class ListBooks {
    private ArrayList<Book> read;
    private ArrayList<Book> toRead;


    public ListBooks() {
        this.read = new ArrayList<>();
        this.toRead = new ArrayList<>();

    }

    public void listBooksClear() {
        read.clear();
        toRead.clear();
    }

    public ArrayList<Book> getToRead(){
        return this.toRead;
    }

    public ArrayList<Book> getRead(){
        return this.read;
    }

    public void addToSetRead(Book book) {
        this.read.add(book);
    }

    public void addToSetToRead(Book book) {
        this.toRead.add(book);
    }

    public void setRead(ArrayList<Book> read) {
        this.read = read;
    }

    public void setToRead(ArrayList<Book> toRead) {
        this.toRead = toRead;
    }

    @Override
    public String toString() {
        return "ListBooks{" +
                "read=" + read +
                ", toRead=" + toRead +
                '}';
    }
}
