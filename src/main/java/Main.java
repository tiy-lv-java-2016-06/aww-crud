import spark.Spark;
import spark.ModelAndView;
import spark.Session;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;

/**
 * Created by vasantia on 7/13/16.
 */
public class Main {

    public static final String SESSION_USER = "userName";
    static Map<String, User> users = new HashMap<>();
    static List<Bottle> allBottles = new ArrayList<>();

    public static void main(String[] args) {

        Spark.staticFileLocation("public");
        Spark.init();

        Spark.get("/", (request, response) -> {
            Map m = new HashMap();
            Session session = request.session();
            String name = session.attribute(SESSION_USER);
            User user = users.get(name);

            if(user == null){
                return new ModelAndView(m, "index.html");
            }
            else {
                m.put("user", user);
                return new ModelAndView(m, "index.html");
            }
        }, new MustacheTemplateEngine());

        Spark.get("/login", (request, response) -> {
            Map m = new HashMap();
            return new ModelAndView(m, "index.html");
        }, new MustacheTemplateEngine());

        Spark.post("/login", (request, response) -> {
            String name = request.queryParams("submitName");
            String password = request.queryParams("submitPassword");
            User user = users.get(name);

            if(user == null) {

                user = new User(name, password);
                users.put(name, user);
                Session session = request.session();
                session.attribute(SESSION_USER, name);
                response.redirect("/");
            }
            else if (user != null && request.queryParams("submitPassword").equals(user.getPassword())){
                Session session = request.session();
                session.attribute(SESSION_USER, name);
                response.redirect("/");
            }
            else {
                response.redirect("/login");
            }
            return "";
        });

        Spark.get("/create-bottle", (request, response) -> {
            Map m = new HashMap();
            return new ModelAndView(m, "index.html");
        }, new MustacheTemplateEngine());

        Spark.post("/create-bottle", ((request, response) -> {
            Session session = request.session();
            String name = session.attribute(SESSION_USER);
            User user = users.get(name);

            if(user==null){
                response.redirect("/");
                return "";
            }

            String bottleName = request.queryParams("bottleName");
            String bottleProducer = request.queryParams("bottleProducer");
            String bottleRegion = request.queryParams("bottleRegion");
            int bottleVintage = Integer.valueOf(request.queryParams("bottleVintage"));
            String bottleVariety = request.queryParams("bottleVariety");
            float bottleABV = Float.valueOf(request.queryParams("bottleABV"));
            Bottle bottle = new Bottle(bottleName, bottleProducer, bottleRegion, bottleVintage, bottleVariety, bottleABV);
            user.myBottles.add(bottle);
            allBottles.add(bottle);
            response.redirect("/");
            return "";
        }));

        Spark.post("/delete-bottle", (request, response) -> {
            Session session = request.session();
            String name = session.attribute(SESSION_USER);
            User user = users.get(name);

            String bottleName = request.queryParams("bottleName");

            for(Bottle bottle : user.myBottles){
                if(bottle.getName().equals(bottleName)){
                    user.myBottles.remove(bottle);
                    allBottles.remove(bottle);
                }
            }
            response.redirect("/");
            return "";
        });
        Spark.get("/edit-bottle", (request, response) -> {
            Map m = new HashMap();
            return new ModelAndView(m, "bottles.html");
        }, new MustacheTemplateEngine());

        Spark.post("/edit-bottle", (request, response) -> {
            Session session = request.session();
            String name = session.attribute(SESSION_USER);
            User user = users.get(name);

            String bottleName = request.queryParams("bottleName");
            String bottleProducer = request.queryParams("bottleProducer");
            String bottleRegion = request.queryParams("bottleRegion");
            int bottleVintage = Integer.valueOf(request.queryParams("bottleVintage"));
            String bottleVariety = request.queryParams("bottleVariety");
            float bottleABV = Float.valueOf(request.queryParams("bottleABV"));

            for(Bottle bottle : user.myBottles){
                if(bottle.getName().equals(bottleName)){
                    user.myBottles.remove(bottle);
                    user.myBottles.add(new Bottle(bottleName, bottleProducer, bottleRegion, bottleVintage, bottleVariety, bottleABV));
                    allBottles.remove(bottle);
                    allBottles.add(new Bottle(bottleName, bottleProducer, bottleRegion, bottleVintage, bottleVariety, bottleABV));
                }
            }
            response.redirect("/");
            return "";
        });

        Spark.post("/logout", (request, response) -> {
            request.session().invalidate();
            response.redirect("/");
            return "";
        });

    }
}
