import java.util.UUID;

/**
 * Created by vasantia on 7/14/16.
 */
public class Bottle {

    private String name;
    private String producer;
    private String region;
    private int vintage;
    private String variety;
    private float abv;

    public Bottle(String name, String producer, String region, int vintage, String variety, float abv) {
        this.name = name;
        this.producer = producer;
        this.region = region;
        this.vintage = vintage;
        this.variety = variety;
        this.abv = abv;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public String getRegion() {
        return region;
    }

    public int getVintage() {
        return vintage;
    }

    public String getVariety() {
        return variety;
    }

    public float getAbv() {
        return abv;
    }
}
