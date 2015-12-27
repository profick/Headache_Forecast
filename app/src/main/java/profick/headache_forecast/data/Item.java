package profick.headache_forecast.data;

import org.json.JSONObject;

/**
 * Created by Denis on 28.12.2015.
 */
public class Item implements JSONPopulator {

    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
