package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;
import java.util.List;

public class Author extends User {
    private ArrayList<Book> writtenBook;
    private ArrayList<String> writtenBookStatistic;
    private int likeNumber;

    public Author(String id, String name, String surname, String nickname, String email, String password) {
        super(id, name, surname, nickname, email, password);
        this.writtenBook = new ArrayList<>();
        this.writtenBookStatistic = new ArrayList<String>();
        this.likeNumber = 0;
    }

    public ArrayList<Book> getWrittenBook() {
        return writtenBook;
    }

    public ArrayList<String> getWrittenBookStatistic() {
        return writtenBookStatistic;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setWrittenBook(Book writtenBook) {
        this.writtenBook.add(writtenBook);
    }

    public void setWrittenBookStatistic(String writtenBookStatistic) {

        this.writtenBookStatistic.add(writtenBookStatistic);
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }
//todo aggiungere getfollowing
    public int getFollowerCount() {
        List<String> followerList = userManager.loadRelationsFollower("Author", this.getNickname());
        return followerList.size();
    }


     public String getIdBookPublished(String title)
    {
        for( Book Books: this.writtenBook) {

            if (Books.getTitle().equals(title))
                return Books.getBook_id();
        }
        return null;
    }

}
