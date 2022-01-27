package it.unipi.dii.reviook_app.entity;

import java.util.ArrayList;
import java.util.List;

public class Author extends User {
    private ArrayList<Book> writtenBook;
    private ArrayList<String> writtenBookStatistic;

    public Author(String nickname) {
        super(nickname);
        this.writtenBook = new ArrayList<>();
        this.writtenBookStatistic = new ArrayList<String>();
    }
    public Author(String id, String name, String surname, String nickname, String email, String password, ArrayList<String> listReviewID, Integer follower_count) {
        super(id, name, surname, nickname, email, password, listReviewID, follower_count);
        this.writtenBook = new ArrayList<>();
        this.writtenBookStatistic = new ArrayList<String>();
    }

    public ArrayList<Book> getWrittenBook() {
        return writtenBook;
    }

    public ArrayList<String> getWrittenBookStatistic() {
        return writtenBookStatistic;
    }

    public void setWrittenBook(Book writtenBook) {
        this.writtenBook.add(writtenBook);
    }

    public void setWrittenBookStatistic(String writtenBookStatistic) {
        this.writtenBookStatistic.add(writtenBookStatistic);
    }

    public String getIdBookPublished(String title) {
        for (Book Books : this.writtenBook) {
            if (Books.getTitle().equals(title))
                return Books.getBook_id();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Author{" +
                "writtenBook=" + writtenBook +
                ", writtenBookStatistic=" + writtenBookStatistic +
                ", userManager=" + userManager.toString() +
                '}';
    }
}
