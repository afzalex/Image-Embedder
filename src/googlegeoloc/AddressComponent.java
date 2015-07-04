package googlegeoloc;

import java.util.Arrays;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddressComponent {

    private String longName;
    private String shortName;
    private AddressType type;
    private AddressType[] types;

    /**
     * Example : <br />
     * {
     * "long_name" : "India", "short_name" : "IN", "types" : [ "country",
     * "political" ] }
     *
     * @param json
     * @throws JSONException
     */
    AddressComponent(JSONObject json) throws JSONException {
        longName = json.getString("long_name");
        shortName = json.getString("short_name");
        JSONArray jsonarr = json.getJSONArray("types");
        types = new AddressType[jsonarr.length()];
        for (int i = 0; i < jsonarr.length(); i++) {
            types[i] = AddressType.parse(jsonarr.getString(i));
        }
        loop:
        for (int i = 0; i < jsonarr.length(); i++) {
            String typ = jsonarr.getString(i);
            if (typ.equals(AddressType.COUNTRY.code)) {
                type = AddressType.COUNTRY;
                break;
            } else if (typ.equals(AddressType.ADMINISTRATIVE_AREA_LEVEL_1.code)) {
                type = AddressType.ADMINISTRATIVE_AREA_LEVEL_1;
                break;
            } else if (typ.equals(AddressType.ADMINISTRATIVE_AREA_LEVEL_2.code)) {
                type = AddressType.ADMINISTRATIVE_AREA_LEVEL_2;
                break;
            } else if (typ.equals(AddressType.LOCALITY.code)) {
                type = AddressType.LOCALITY;
                break;
            } else if (typ.equals(AddressType.STREET_ADDRESS)) {
                type = AddressType.STREET_ADDRESS;
                break;
            } else if (typ.equals(AddressType.POSTAL_CODE)) {
                type = AddressType.POSTAL_CODE;
                break;
            }
        }
        if (type == null && jsonarr.length() > 0) {
            type = AddressType.parse(jsonarr.getString(0));
        }
    }

    /**
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @return the type
     */
    public AddressType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        boolean rtrn = false;
        if (obj != null && obj instanceof AddressComponent) {
            AddressComponent ac = (AddressComponent) obj;
            rtrn = longName != null && longName.equals(ac.longName);
            rtrn = rtrn && shortName != null && shortName.equals(ac.shortName);
            rtrn = rtrn && type != null && type.equals(ac.type);
            rtrn = rtrn && types!= null && types.length == ac.types.length;
            for(int i = 0;rtrn && i < types.length; i++){
                rtrn = rtrn && types[i].equals(ac.types[i]);
            }
        }
        return rtrn;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.longName);
        hash = 59 * hash + Objects.hashCode(this.shortName);
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Arrays.deepHashCode(this.types);
        return hash;
    }
}
