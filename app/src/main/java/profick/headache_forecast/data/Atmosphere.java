package profick.headache_forecast.data;

import org.json.JSONObject;

/**
 * Created by Denis on 28.12.2015.
 */
public class Atmosphere implements JSONPopulator {

    private int rising;
    private double pressure;

    public int getRising() {
        return rising;
    }

    public double getPressure() {
        return pressure;
    }

    @Override
    public void populate(JSONObject data) {
        rising = data.optInt("rising");
        pressure = data.optDouble("pressure");
    }
}
