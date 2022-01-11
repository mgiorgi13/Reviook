package it.unipi.dii.reviook_app.Data;

import java.util.ArrayList;

public class listBooks {
    private ArrayList<Book> readed;
    private ArrayList<Book> toRead;
    private ArrayList<Book> statistic;

    public listBooks(){
        this.readed = new ArrayList<Book>();
        this.toRead =  new ArrayList<Book>();
        this.statistic = new ArrayList<Book>();
    }
    public void listBooksClear()
    {
        readed.clear();
        toRead.clear();
        statistic.clear();
    }

    public void setRead(String title, String book_id ) {
        this.readed.add(new Book(title,book_id)) ;
    }

    public String getIdBookToRead(String title)
    {
        for( Book Books: this.toRead) {

            if (Books.getTitle().equals(title))
                return Books.getBook_id();
        }
        return null;
    }
    public String getIdBookRead(String title)
    {
        for( Book Books: this.readed) {
            if (Books.getTitle().equals(title))
                return Books.getBook_id();
        }
        return null;
    }

    public void setToRead(String title, String book_id ) {
        this.toRead.add(new Book(title,book_id)) ;
    }

}
