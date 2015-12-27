package profick.headache_forecast.data;

import org.json.JSONObject;

/**
 * Created by Denis on 28.12.2015.
 */
public class Units implements JSONPopulator {

    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
