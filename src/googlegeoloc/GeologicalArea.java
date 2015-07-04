package googlegeoloc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * It represents the geological area in the terms of latitude and longitudes
 *
 * @author Afzalex
 */
public class GeologicalArea {

    private GeoLocation northeast;
    private GeoLocation southwest;

    /**
     * @param northeast north-east GeoLocation
     * @param southwest south-west GeoLocation
     */
    public GeologicalArea(GeoLocation northeast, GeoLocation southwest) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    /**
     * Example : <br />
     * { "northeast" : { "lat" : 34.36571, "lng" : -86.451988 }, "southwest" : {
     * "lat" : 34.287805, "lng" : -86.56014379999999 } }
     *
     * @param json
     * @throws JSONException
     */
    public GeologicalArea(JSONObject json) throws JSONException {
        if (json.has("northeast")) {
            northeast = new GeoLocation(json.getJSONObject("northeast"));
        }
        if (json.has("southwest")) {
            southwest = new GeoLocation(json.getJSONObject("southwest"));
        }
    }

    /**
     * @return the northeast
     */
    public GeoLocation getNortheast() {
        return northeast;
    }

    /**
     * @param northeast the northeast to set
     */
    public void setNortheast(GeoLocation northeast) {
        this.northeast = northeast;
    }

    /**
     * @return the southwest
     */
    public GeoLocation getSouthwest() {
        return southwest;
    }

    /**
     * @param southwest the southwest to set
     */
    public void setSouthwest(GeoLocation southwest) {
        this.southwest = southwest;
    }
}
