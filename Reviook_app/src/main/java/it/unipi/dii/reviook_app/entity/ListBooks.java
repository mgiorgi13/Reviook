package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;

public class ListBooks {
    private ArrayList<Book> readed;
    private ArrayList<Book> toRead;


    public ListBooks() {
        this.readed = new ArrayList<>();
        this.toRead = new ArrayList<>();

    }

    public void listBooksClear() {
        readed.clear();
        toRead.clear();

    }
    public ArrayList<Book> getToRead(){
        return this.toRead;
    }
    public String setRead(String title, String book_id) {
        Book b = new Book(title, book_id);
        b.setTitle((readed.size() + 1) + ":" + b.getTitle());
        this.readed.add(b);
        return b.getTitle();
    }
    public ArrayList<Book> getReaded(){
        return this.readed;
    }

    public String getIdBookToRead(String title) {
        for (Book Books : this.toRead) {
            if (Books.getTitle().equals(title))
                return Books.getBook_id();
        }
        return null;
    }


    public String getIdBookRead(String title) {
        for (Book Books : this.readed) {
            if (Books.getTitle().equals(title))
                return Books.getBook_id();
        }
        return null;
    }

    public String setToRead(String title, String book_id) {
        Book b = new Book(title, book_id);
        b.setTitle((toRead.size() + 1) + ":" + b.getTitle());
        this.toRead.add(b);
        return b.getTitle();
    }





}
