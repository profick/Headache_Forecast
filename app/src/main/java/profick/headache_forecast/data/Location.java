package profick.headache_forecast.data;

import org.json.JSONObject;

/**
 * Created by Denis on 29.12.2015.
 */
public class Location implements JSONPopulator {
    String city;
    String country;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
    }
}
