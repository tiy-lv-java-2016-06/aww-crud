import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vasantia on 7/14/16.
 */
public class User {

    private String name;
    private String password;
    List<Bottle> myBottles = new ArrayList<>();

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Bottle> getMyBottles() {
        return myBottles;
    }
}
