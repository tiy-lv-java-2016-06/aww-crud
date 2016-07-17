/**
 * Created by Nigel on 7/13/16.
 */
public class Car {
    /*
    * Make and model will be entered by the user. Platform and year will be options.
    */
    private String drivetrain;
    private String make;
    private String model;
    private int year;
    private String author;
    private int carId;

    public Car(String drivetrain, String make, String model, int year, int carId) {
        this.drivetrain = drivetrain;
        this.make = make;
        this.model = model;
        this.year = year;
        this.carId = carId;
    }

    public String getDrivetrain() {
        return drivetrain;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public int getCarId() {
        return carId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}