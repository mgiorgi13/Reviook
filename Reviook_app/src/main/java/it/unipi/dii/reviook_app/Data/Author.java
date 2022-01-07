package it.unipi.dii.reviook_app.Data;

import java.util.ArrayList;
import java.util.List;

public class Author extends Users {
    private ArrayList<String> writtenBook;
    private ArrayList<String> writtenBookStatistic;
    private int likeNumber;

    public Author(String name, String surname, String nickname, String email, String password) {
        super(name, surname, nickname, email, password);
        this.writtenBook = new ArrayList<String>();
        this.writtenBookStatistic = new ArrayList<String>();
        this.likeNumber = 0;
    }

    public ArrayList<String> getWrittenBook() {
        return writtenBook;
    }

    public ArrayList<String> getWrittenBookStatistic() {
        return writtenBookStatistic;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setWrittenBook(String writtenBook) {
        this.writtenBook.add(writtenBook);
    }

    public void setWrittenBookStatistic(String writtenBookStatistic) {

        this.writtenBookStatistic.add(writtenBookStatistic);
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getFollowerCount() {
        List<String> followerList = userManager.loadRelationsFollower("Author", this.getNickname());
        return followerList.size();
    }

}
