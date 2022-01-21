package it.unipi.dii.reviook_app;

import it.unipi.dii.reviook_app.entity.Author;
import it.unipi.dii.reviook_app.entity.Cache;
import it.unipi.dii.reviook_app.entity.User;

import java.util.ArrayList;

public class Session {
    private static Session session = null;
    private Boolean isAuthor; // true : author , false : user
    private User loggedUser = null;
    private Author loggedAuthor = null;
    private static Cache cache = new Cache();

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        Session.session = session;
    }

    public void setCurrentLoggedUser(User logged) {
        this.loggedUser = logged;
    }

    public void setCurrentLoggedAuthor(Author logged) {
        this.loggedAuthor = logged;
    }

    private Session() {
    }

    public Cache getCache() {
        return cache;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }

    public void setIsAuthor(Boolean type) {
        if (session == null) {
            new RuntimeException("Session is not active.");
        } else
            this.isAuthor = type;
    }

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public void setLoggedUser(String id, String name, String surname, String nickname, String email, String password, ArrayList<String> listReviewID, Integer follower_count) {
        if (session == null) {
            new RuntimeException("Session is not active.");
        } else {
            session.loggedUser = new User(id, name, surname, nickname, email, password, listReviewID, follower_count);
        }
    }

    public void setLoggedAuthor(String id, String name, String surname, String nickname, String email, String password, ArrayList<String> listReviewID, Integer follower_count) {
        if (session == null) {
            new RuntimeException("Session is not active.");
        } else {
            session.loggedAuthor = new Author(id, name, surname, nickname, email, password, listReviewID, follower_count);
        }
    }

    public User getLoggedUser() {
        if (session == null) {
            throw new RuntimeException("Session is not active.");
        } else {
            return session.loggedUser;
        }
    }

    public Author getLoggedAuthor() {
        if (session == null) {
            throw new RuntimeException("Session is not active.");
        } else {
            return session.loggedAuthor;
        }
    }

}
