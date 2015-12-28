package profick.headache_forecast.data;

import org.json.JSONObject;

/**
 * Created by Denis on 28.12.2015.
 */
public class Astronomy implements JSONPopulator {
    private String sunrise;
    private String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    @Override
    public void populate(JSONObject data) {
        sunrise = data.optString("sunrise");
        sunset = data.optString("sunset");
    }
}
