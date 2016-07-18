import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EddyJ on 7/13/16.
 */
public class Main {
    static int pageNumber = 1;
    static int pageLimit = 5;
    static int id = 0;
    static List<Restaurant> restaurants = new ArrayList<>();
    static Map<String, User> users = new HashMap<>();
    public static final String SESSION_USERNAME = "userName";

    public static void main(String[] args) {

        Spark.init();

        Spark.get("/", (request, response) -> {
            HashMap m = new HashMap();
            Session session = request.session();
            String userName = session.attribute(SESSION_USERNAME);

            List<Restaurant> userRestaurants = new ArrayList();
            List<Restaurant> totalRestaurants = new ArrayList();
            List<Restaurant> overLimitList = new ArrayList();

            for (Restaurant restaurant: restaurants){
                if(restaurant.getAuthor().equals(userName)){
                    userRestaurants.add(restaurant);
                }
                else{
                    totalRestaurants.add(restaurant);
                }
            }

            if(totalRestaurants.size() <= pageLimit){
                for (Restaurant restaurant : totalRestaurants){
                    overLimitList.add(restaurant);
                }
            }
            else{
                pageNumber++;

            }


            m.put("pageNumber", pageNumber);
            m.put("userRestaurants", userRestaurants);
            m.put("totalRestaurants", totalRestaurants);
            m.put("user", userName);

            return new ModelAndView(m, "home.html");
        }, new MustacheTemplateEngine());

        Spark.post("/create-user" , (request, response) -> {
            String name = request.queryParams("loginName");
            User user = users.get(name);

            if( user == null && !(users.containsKey(user))){
                user = new User( name);
                users.put(name, user);
            }

            Session session = request.session();
            session.attribute(SESSION_USERNAME, name);

            response.redirect("/");
            return "";
        });

        Spark.post("/logout" ,(request, response) -> {
            request.session().invalidate();
            response.redirect("/");
            return "";
        });

        Spark.post("/create-restaurant", (request, response) -> {
            Session session = request.session();
            String userName = session.attribute(SESSION_USERNAME);

            String name = request.queryParams("restaurantName");
            String menuType = request.queryParams("menuType");
            String address = request.queryParams("restaurantAddress");
            if(request.queryParams("restaurantRating").isEmpty()) {
                response.redirect("/");
            }
            String userRating = request.queryParams("restaurantRating");
            int rating = Integer.valueOf(userRating);
            if(request.queryParams().isEmpty()){
                response.redirect("/");
            }

            id++;

            Restaurant entry = new Restaurant(name, menuType, address, rating, userName, id);
            restaurants.add(entry);

            String url = "/";

            if (id >= 0){
                url += "?id="+id;
            }

            response.redirect(url);
            return "";
        });

        Spark.get("/edit", (request, response) -> {
            String restaurantId = request.queryParams("restaurantId");
            int id = Integer.valueOf(restaurantId);

            Map m = new HashMap();
            for(Restaurant restaurant: restaurants){
                if(restaurant.getRestaurantId()  == id){
                    m.put("userRestaurant", restaurant);
                }
            }
            return new ModelAndView(m, "edit.html");
        }, new MustacheTemplateEngine());

        Spark.post("/edit-restaurant", (request, response) -> {
            Map m = new HashMap();
            Session session = request.session();
            String userName = session.attribute(SESSION_USERNAME);

            String idString = request.queryParams("restaurantId");
            int restaurantId = Integer.valueOf(idString);
            String name = request.queryParams("restaurantName");
            String menuType = request.queryParams("menuType");
            String address = request.queryParams("restaurantAddress");
            String userRating = request.queryParams("restaurantRating");
            int rating = Integer.valueOf(userRating);
            if(request.queryParams().isEmpty()){
                response.redirect("/");
            }

            Restaurant editedRestaurant = null;
            for (Restaurant restaurant: restaurants){
                if (restaurant.getRestaurantId() == restaurantId) {
                    editedRestaurant = restaurant;
                }
            }
            restaurants.remove(editedRestaurant);

            Restaurant entry = new Restaurant(name, menuType, address, rating, userName, id);
            restaurants.add(entry);



            response.redirect("/");
            return "";
        });

        Spark.post("/delete", (request, response) -> {
            String idString = request.queryParams("restaurantId");
            int restaurantId = Integer.valueOf(idString);
            Restaurant deletedRestaurant = null;
            for(Restaurant restaurant : restaurants){
                if(restaurantId == restaurant.getRestaurantId()){
                    deletedRestaurant = restaurant;
                }
            }
            restaurants.remove(deletedRestaurant);

            response.redirect("/");
            return "";
        });

        Spark.get("/next" , (request, response) -> {
            

        });

    }
}
