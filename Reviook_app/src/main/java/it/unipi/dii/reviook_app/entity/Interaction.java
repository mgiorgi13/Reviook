package it.unipi.dii.reviook_app.entity;

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

    public ArrayList<String> getFollow() {
        return follow;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public int getNumberFollow() {
        return numberFollow;
    }

    public int getNumberFollower() {
        return numberFollower;
    }

    public int getNumberUseful() {
        return numberUseful;
    }

    public void setFollow(String follow) {
        this.follow.add(follow);
    }

    public void setFollower(String follower) {
        this.follower.add(follower);
    }
    public void delFollower() {
        this.follower.clear();
        this.numberFollower = 0;
    }
    public void delFollow() {
        this.follow.clear();
        this.numberFollow=0;
    }
    public void setNumberFollow(int numberFollow) {
        this.numberFollow = numberFollow;
    }

    public void setNumberFollower(int numberFollower) {
        this.numberFollower = numberFollower;
    }

    public void setNumberUseful(int numberUseful) {
        this.numberUseful = numberUseful;
    }
}
