package googlegeoloc;

import java.util.Arrays;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodeResult {

    public AddressComponent[] address_components;
    public String formattedAddress;
    public Geometry geometry;
    public AddressType[] types;

    public GeocodeResult(JSONObject json) throws JSONException {
        if (json.has("address_components")) {
            JSONArray jsonarr = json.getJSONArray("address_components");
            address_components = new AddressComponent[jsonarr.length()];
            for (int i = 0; i < jsonarr.length(); i++) {
                address_components[i] = new AddressComponent(jsonarr.getJSONObject(i));
            }
        }
        if (json.has("formatted_address")) {
            formattedAddress = json.getString("formatted_address");
        }
        if (json.has("geometry")) {
            geometry = new Geometry(json.getJSONObject("geometry"));
        }
        if (json.has("types")) {
            JSONArray jsonarr = json.getJSONArray("types");
            types = new AddressType[jsonarr.length()];
            for (int i = 0; i < jsonarr.length(); i++) {
                types[i] = AddressType.parse(jsonarr.getString(i));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        boolean rtrn = false;
        if (obj!= null && obj instanceof GeocodeResult) {
            GeocodeResult gr = (GeocodeResult) obj;
            rtrn = gr.formattedAddress.equals(formattedAddress);
            rtrn = rtrn && gr.geometry.equals(geometry);
            rtrn = rtrn && address_components.length == gr.address_components.length;
            for(int i = 0; rtrn && i < address_components.length ; i++){
                rtrn = rtrn && address_components[i].equals(gr.address_components[i]);
            }
            rtrn = rtrn && types.length == gr.types.length;
            for(int i = 0; rtrn && i < types.length; i++){
                rtrn = rtrn && types[i].equals(gr.types[i]);
            }
            return rtrn;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Arrays.deepHashCode(this.address_components);
        hash = 17 * hash + Objects.hashCode(this.formattedAddress);
        hash = 17 * hash + Objects.hashCode(this.geometry);
        hash = 17 * hash + Arrays.deepHashCode(this.types);
        return hash;
    }
}
