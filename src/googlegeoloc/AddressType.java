package googlegeoloc;

public enum AddressType {

    /**
     * indicates a precise street address.
     */
    STREET_ADDRESS("Street address", "streeet_address"),
    /**
     * indicates a named route (such as "US 101").
     */
    ROUTE("Route", "route"),
    /**
     * indicates a major intersection, usually of two major roads.
     */
    INTERSECTION("Intersection", "intersection"),
    /**
     * indicates a political entity. Usually, this type indicates a polygon of
     * some civil administration.
     */
    POLITICAL("Political entity", "political"),
    /**
     * indicates the national political entity, and is typically the highest
     * order type returned by the Geocoder.
     */
    COUNTRY("Country", "country"),
    /**
     * indicates a first-order civil entity below the country level. Within the
     * United States, these administrative levels are states. Not all nations
     * exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_1("State", "administrative_area_level_1"),
    /**
     * indicates a second-order civil entity below the country level. Within the
     * United States, these administrative levels are counties. Not all nations
     * exhibit these administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_2("Counties/District", "administrative_area_level_2"),
    /**
     * indicates a third-order civil entity below the country level. This type
     * indicates a minor civil division. Not all nations exhibit these
     * administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_3("Civil Division", "administrative_area_level_3"),
    /**
     * indicates a fourth-order civil entity below the country level. This type
     * indicates a minor civil division. Not all nations exhibit these
     * administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_4("Minor civil division", "administrative_area_level_4"),
    /**
     * indicates a fifth-order civil entity below the country level. This type
     * indicates a minor civil division. Not all nations exhibit these
     * administrative levels.
     */
    ADMINISTRATIVE_AREA_LEVEL_5("Minor civil division", "administrative_area_level_5"),
    /**
     * indicates a commonly-used alternative name for the entity.
     */
    COLLOQUIAL_AREA("Alternative name", "colloquial_area"),
    /**
     * indicates an incorporated city or town political entity.
     */
    LOCALITY("Locality", "locality"),
    /**
     * indicates a first-order civil entity below a locality. For some locations
     * may receive one of the additional types: sublocality_level_1 through to
     * sublocality_level_5. Each sublocality level is a civil entity. Larger
     * numbers indicate a smaller geographic area.
     */
    SUBLOCALITY("Sub-Locality", "sublocality"),
    SUBLOCALITY_LEVEL_1("Sub-Locality", "sublocality_level_1"),
    SUBLOCALITY_LEVEL_2("Sub-Locality", "sublocality_level_2"),
    SUBLOCALITY_LEVEL_3("Sub-Locality", "sublocality_level_3"),
    SUBLOCALITY_LEVEL_4("Sub-Locality", "sublocality_level_4"),
    SUBLOCALITY_LEVEL_5("Sub-Locality", "sublocality_level_5"),
    /**
     * indicates a named neighborhood.
     */
    NEIGHBORHOOD("Neighborhood", "neighborhood"),
    /**
     * indicates a named location, usually a building or collection of buildings
     * with a common name
     */
    PREMISE("Location", "premise"),
    /**
     * indicates a first-order entity below a named location, usually a singular
     * building within a collection of buildings with a common name.
     */
    SUBPREMISE("Sub location", "subpremise"),
    /**
     * indicates a postal code as used to address postal mail within the
     * country.
     */
    POSTAL_CODE("Postal code", "postal_code"),
    /**
     * indicates a prominent natural feature.
     */
    NEUTRAL_FEATURE("Natural feature", "natural_feature"),
    /**
     * indicates an airport.
     */
    AIRPORT("Airport", "airport"),
    /**
     * indicates a named park.
     */
    PARK("Park", "park");
    public final String type;
    public final String code;

    AddressType(String type, String code) {
        this.type = type;
        this.code = code;
    }
    
    public static AddressType parse(String addr){
        AddressType type = null;
        for(AddressType at : AddressType.values()){
            if(at.code.equals(addr)){
                type = at;
                break;
            }
        }
        return type;
    }
    
}
