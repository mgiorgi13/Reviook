package it.unipi.dii.reviook_app.Data;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

public class Users {
    private String name;
    private String surname;
    private String nickname;
    private  String email;
    private String password;
    private Interaction interactions;
    private Books books;


    public Users() {
        this.name = null;
        this.surname = null;
        this.nickname = null;
        this.email = null;
        this.password =null;
        this.interactions = new Interaction();
        this.books = new Books();
    }
    public void SignIn(String name, String surname, String nickname,String email, String password){
        try {
            MessageDigest md;
            String pswHash;
            byte[] digest;
            this.name = name;
            this.surname = surname;
            this.nickname = nickname;
            this.email = email;
            this.password = password;

            //check if the password entered is at least 8
            //and maximum 20 characters long and contains at least one letter and at least one number:
            Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-zA-Z]).{8,20})");
            Matcher mpsw = pattern.matcher(password);
            if (!mpsw.find())
                System.out.println("Password entered is at least 8 and maximum 20 characters long and contains at least one letter and at least one number");
            System.out.println(mpsw.matches());

            //Password Hash
            md = MessageDigest.getInstance("MD5");
            //md.update(signUpPassword.getText().getBytes());
            md.update(password.getBytes());
            digest = md.digest();
            pswHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

            //convalid email
            Pattern p =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher mEmail = p.matcher(email);

            if (!mEmail.find())
                System.out.println("Invalid email");
            // else genera messaggio;

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void Login(){}
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public Interaction getInteractions() {
        return interactions;
    }

    public Books getBooks() {
        return books;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInteractions(Interaction interactions) {
        this.interactions = interactions;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

}
