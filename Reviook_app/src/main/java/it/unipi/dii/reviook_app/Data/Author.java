package it.unipi.dii.reviook_app.Data;

import java.util.ArrayList;

public class Author extends Users {
    private ArrayList<String> writtenBook;
    private ArrayList<String> writtenBookStatisitc;
    private int likeNumber;

    public Author(String name, String surname, String password) {
        this.writtenBook = new ArrayList<String>();
        this.writtenBookStatisitc = new ArrayList<String>();
        this.likeNumber = 0;
    }

}
