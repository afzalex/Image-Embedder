package googlegeoloc;

import org.json.JSONException;
import org.json.JSONObject;

public class Geometry {

    private GeologicalArea bounds;
    private GeoLocation location;
    private LocationType location_type;
    private GeologicalArea viewport;
    private boolean isLoadedSuccessfully = false;

    /**
     * Example : <br />
     * { "bounds" : { "northeast" : { "lat" : 34.36571, "lng" : -86.451988 },
     * "southwest" : { "lat" : 34.287805, "lng" : -86.56014379999999 } },
     * "location" : { "lat" : 34.31814970000001, "lng" : -86.4958219 },
     * "location_type" : "APPROXIMATE", "viewport" : { "northeast" : { "lat" :
     * 34.36571, "lng" : -86.451988 }, "southwest" : { "lat" : 34.287805, "lng"
     * : -86.56014379999999 } } }
     *
     * @param json
     */
    Geometry(JSONObject json) {
        boolean gotBounds, gotLocation, gotLocType, gotViewPort;
        gotBounds = gotLocation = gotLocType = gotViewPort = false;
        try {
            if (json.has("bounds")) {
                bounds = new GeologicalArea(json.getJSONObject("bounds"));
                gotBounds = true;
            }
        } catch (JSONException ex) {
        }
        try {
            if (json.has("location")) {
                location = new GeoLocation(json.getJSONObject("location"));
                gotLocation = true;
            }
        } catch (JSONException ex) {
        }
        try {
            if (json.has("location_type")) {
                location_type = LocationType.parse(json.getString("location_type"));
                gotLocType = true;
            }
        } catch (JSONException ex) {
        }
        try {
            if (json.has("viewport")) {
                viewport = new GeologicalArea(json.getJSONObject("viewport"));
                gotLocation = true;
            }
        } catch (JSONException ex) {
        }
        isLoadedSuccessfully = gotBounds && gotLocation && gotLocation && gotViewPort;
    }

    /**
     * @return the bounds
     */
    public GeologicalArea getBounds() {
        return bounds;
    }

    /**
     * @param bounds the bounds to set
     */
    public void setBounds(GeologicalArea bounds) {
        this.bounds = bounds;
    }

    /**
     * @return the location
     */
    public GeoLocation getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    /**
     * @return the location_type
     */
    public LocationType getLocation_type() {
        return location_type;
    }

    /**
     * @param location_type the location_type to set
     */
    public void setLocation_type(LocationType location_type) {
        this.location_type = location_type;
    }

    /**
     * @return the viewport
     */
    public GeologicalArea getViewport() {
        return viewport;
    }

    /**
     * @param viewport the viewport to set
     */
    public void setViewport(GeologicalArea viewport) {
        this.viewport = viewport;
    }

    /**
     * @return the isLoadedSuccessfully
     */
    public boolean isIsLoadedSuccessfully() {
        return isLoadedSuccessfully;
    }

    /**
     * @param isLoadedSuccessfully the isLoadedSuccessfully to set
     */
    public void setIsLoadedSuccessfully(boolean isLoadedSuccessfully) {
        this.isLoadedSuccessfully = isLoadedSuccessfully;
    }
}
