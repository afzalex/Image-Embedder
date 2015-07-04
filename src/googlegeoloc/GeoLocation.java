package googlegeoloc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * It represents the location on glope in the terms of latitude and longitude.
 *
 * @author Afzalex
 */
public class GeoLocation {

    private double latitude;
    private double longitude;

    /**
     * @param lat latitude
     * @param lng longitude
     */
    public GeoLocation(double lat, double lng) {
        latitude = lat;
        longitude = lng;
    }

    /**
     * Example : <br />
     * { "lat" : 34.287805, "lng" : -86.56014379999999 }
     *
     * @param json
     * @throws JSONException
     */
    public GeoLocation(JSONObject json) throws JSONException {
        if (json.has("lat")) {
            latitude = json.getDouble("lat");
        }
        if (json.has("lng")) {
            longitude = json.getDouble("lng");
        }
    }

    @Override
    public String toString() {
        return String.format("GeoLocation[lat:%f, long:%f]", latitude, longitude);
    }

    
    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
