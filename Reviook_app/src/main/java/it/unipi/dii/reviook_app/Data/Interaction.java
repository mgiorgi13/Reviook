package it.unipi.dii.reviook_app.Data;

import java.util.ArrayList;

public class Interaction {
    private ArrayList<String> follow;
    private ArrayList<String> follower;
    private int numberFollow;
    private int numberFollower;
    private int numberUseful;

    public Interaction(){
        this.follow = new ArrayList<String>();
        this.follower = new ArrayList<String>();
        this.numberFollow = 0;
        this.numberFollower = 0;
        this.numberUseful = 0;
    }
}
