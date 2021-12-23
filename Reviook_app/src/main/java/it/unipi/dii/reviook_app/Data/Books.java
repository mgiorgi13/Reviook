package it.unipi.dii.reviook_app.Data;

import java.util.ArrayList;

public class Books {
    private ArrayList<String> readed;
    private ArrayList<String> toRead;
    private ArrayList<String> statistic;

    public Books(){
        this.readed = new ArrayList<String>();
        this.toRead =  new ArrayList<String>();
        this.statistic = new ArrayList<String>();
    }
}
