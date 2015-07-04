package googlegeoloc;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodeResults {

    public ResponseStatus status;
    public GeocodeResult[] results;

    public GeocodeResults(JSONObject json) throws JSONException {
        if (json.has("status")) {
            status = ResponseStatus.parse(json.getString("status"));
        }
        if (json.has("results")) {
            JSONArray jsonarr = json.getJSONArray("results");
            results = new GeocodeResult[jsonarr.length()];
            for (int i = 0; i < jsonarr.length(); i++) {
                results[i] = new GeocodeResult(jsonarr.getJSONObject(i));
            }
        }
    }

    public static GeocodeResults getGeocodeResults(String query) throws Exception {
        String GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
        URL url = new URL(GEOCODE_API_URL + "?address=" + URLEncoder.encode(query, "UTF-8"));
        Scanner sc = new Scanner((InputStream) url.getContent());
        String jsonString = "";
        while (sc.hasNextLine()) {
            jsonString += sc.nextLine();
        }
        JSONObject json = new JSONObject(jsonString);
        return new GeocodeResults(json);
    }

    public static void main(String... args) throws Exception {
        GeocodeResults res;
        res = getGeocodeResults("Avas Vikas, Rishikesh, Uttarakhand");
        for(GeocodeResult r : res.results){
            System.out.println(r.formattedAddress);
            System.out.println(r.geometry.getLocation());
            for(AddressComponent ac : r.address_components){
                System.out.println(ac.getType() + " : " +ac.getLongName());
            }
        }
    }
}
