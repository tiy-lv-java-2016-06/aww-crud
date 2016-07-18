/**
 * Created by EddyJ on 7/13/16.
 */
public class Restaurant {
    private String name;
    private String menuType;
    private String address;
    private int rating;
    private String author;
    private int restaurantId = 0;

    public Restaurant(String name, String menuType, String address, int rating, String author, int restaurantId) {
        this.name = name;
        this.menuType = menuType;
        this.address = address;
        this.rating = rating;
        this.author = author;
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getMenuType() {
        return menuType;
    }

    public String getAddress() {
        return address;
    }

    public int getRating() {
        return rating;
    }


    public String getAuthor() {
        return author;
    }

    public int getRestaurantId() {
        return restaurantId;
    }
}
