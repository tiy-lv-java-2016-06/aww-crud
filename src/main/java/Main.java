import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

/**
 * Created by Nigel on 7/13/16.
 */
public class Main {
    public static final String SESSION_USERNAME = "userName";
    public static final int ITEM_TOTAL = 5;
    public static final String ALL_USER_CARS = "allUsersCars";
    public static int idCounter = 0;
    public static int page = 1;

    static Map<String, User> users = new HashMap<>();
    static List<Car> carsListOfAllUsers = new ArrayList<>();

    public static void main(String[] args) {

        Spark.init();

        /**
         * Requests to the server
         */
        Spark.get("/", (request, response) -> {

            boolean viewNext = false;
            boolean viewPrevious = false;
            int nextPage = page+1;
            int previousPage = page-1;

            Session session = request.session();
            Map templateData = new HashMap();

            String userName = session.attribute(SESSION_USERNAME);

            List<Car> userCars = new ArrayList<>();
            List<Car> othersCars = new ArrayList<>();
            List<Car> carsPage = new ArrayList<>();

            /*
             * Takes the cars that the current user created out of the list of all cars ever created. All other cars go into a different list.
             */
            for (Car car : carsListOfAllUsers){
                if (car.getAuthor().equals(userName)){
                    userCars.add(car);
                }
                else{
                    othersCars.add(car);
                }
            }

            //allows pagination over the list of cars that were created by users not currently logged in.
            if (othersCars.size()<=5){
                for (Car car : othersCars){
                    carsPage.add(car);
                }
            }else {
                viewNext = true;
                int maxPageNum = othersCars.size()/ITEM_TOTAL;
                String pageString = request.queryParams("page");
                if (pageString != null) {
                    int newPageValue = Integer.valueOf(pageString);
                    //make sure the page is not bigger than the number of cars in the list and never less than 1.
                    if (newPageValue  < 1){
                        newPageValue  = 1;
                    }else if (newPageValue  > maxPageNum){
                        newPageValue = maxPageNum;
                        if (othersCars.size() % ITEM_TOTAL > 0){
                            newPageValue++;
                            viewPrevious = true;
                        }
                    }
                    page = newPageValue;
                }
                int maxIndex = (page * ITEM_TOTAL - 1), minIndex = (maxIndex - ITEM_TOTAL + 1);
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (i < othersCars.size()) {
                        carsPage.add(othersCars.get(i));
                    }
                }
            }

            templateData.put("currentPage", carsPage);
            templateData.put("userName", userName);
            templateData.put("userCars", userCars);
            templateData.put("pageId", page);
            templateData.put("previous", previousPage);
            templateData.put("next", nextPage);
            templateData.put("viewNext", viewNext);
            templateData.put("viewPrevious", viewPrevious);


            return new ModelAndView(templateData, "index.html");
        }, new MustacheTemplateEngine());

        Spark.get("/edit" , ((request, response) -> {
            Map templateData = new HashMap();
            String carIdString = request.queryParams("carId");
            int carId = Integer.valueOf(carIdString);

            for (Car car : carsListOfAllUsers){
                if (car.getCarId() == carId){
                    templateData.put("editCar", car);
                }
            }

            return new ModelAndView(templateData, "edit.html");
        }));

        /**
         * Posts to the browser after an action "/login". User creation.
         */
        Spark.post("/login", (request, response) -> {

            String userName = request.queryParams("userName");
            User user = users.get(userName);

            if (user == null){
                user = new User(userName);
            }

            Session session = request.session();
            session.attribute(SESSION_USERNAME, userName);

            response.redirect("/");

            return "";
        });

        /**
         * Ends the current user's session.
         */
        Spark.post("/logout", (request, response) -> {

            request.session().invalidate();
            response.redirect("/");

            return "";
        });

        Spark.post("/create-car", (request, response) -> {

            Session session = request.session();
            String name = session.attribute(SESSION_USERNAME);
            User user = users.get(name);

            int year = Integer.valueOf(request.queryParams("year"));
            String make = request.queryParams("make");
            String model = request.queryParams("model");
            String drivetrain = request.queryParams("drivetrain");

            //String url = "/";

            /*if (userId >= 0){
                url += "?userId="+userId;
            }*/
            idCounter++;

            Car newCar = new Car(drivetrain, make, model, year, idCounter);
            newCar.setAuthor(name);
            carsListOfAllUsers.add(newCar);

            response.redirect("/");
            return "";
        });

        Spark.post("/edit", ((request, response) -> {
            int year = Integer.valueOf(request.queryParams("year"));
            String make = request.queryParams("make");
            String model = request.queryParams("model");
            String drivetrain = request.queryParams("drivetrain");
            String carIdString = request.queryParams("carId");
            int carId = Integer.valueOf(carIdString);

            for (Car car : carsListOfAllUsers){
                if (carId == car.getCarId()){
                    carsListOfAllUsers.remove(car);
                }
            }

            Car editedCar = new Car(drivetrain, make, model, year, carId);
            carsListOfAllUsers.add(editedCar);

            response.redirect("/");
            return "";
        }));

        Spark.post("/delete", ((request, response) -> {
            String carIdString = request.queryParams("carId");
            int carId = Integer.valueOf(carIdString);

            for (Car car : carsListOfAllUsers){
                if (carId == car.getCarId()){
                    carsListOfAllUsers.remove(car);
                }
            }

            response.redirect("/");

            return "";
        }));
    }
}