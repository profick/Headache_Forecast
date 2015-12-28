package profick.headache_forecast.data;

import org.json.JSONObject;

/**
 * Created by Denis on 28.12.2015.
 */
public class Channel implements JSONPopulator {

    private Units units;
    private Item item;
    private Atmosphere atmosphere;
    private Astronomy astronomy;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        astronomy = new Astronomy();
        astronomy.populate(data.optJSONObject("astronomy"));
    }
}
