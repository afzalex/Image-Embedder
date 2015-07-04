package googlegeoloc;

public enum ResponseStatus {

    /**
     * indicates that no errors occurred; the address was successfully parsed
     * and at least one geocode was returned.
     */
    OK("The address was successfully parsed"),
    /**
     * indicates that the geocode was successful but returned no results. This
     * may occur if the geocoder was passed a non-existent address.
     */
    ZERO_RESULTS("The geocode was successful but returned no results"),
    /**
     * indicates that you are over your quota.
     */
    OVER_QUERY_LIMIT("You are over your quota"),
    /**
     * indicates that your request was denied.
     */
    REQUEST_DENIED("Your request was denied"),
    /**
     * generally indicates that the query (address, components or latlng) is
     * missing.
     */
    INVALID_REQUEST("The query (address, components or latlng) is missing"),
    /**
     * indicates that the request could not be processed due to a server error.
     * The request may succeed if you try again.
     */
    UKNOWN_ERROR("The request could not be processed due to a server error");

    public final String message;

    private ResponseStatus(String message) {
        this.message = message;
    }

    public static ResponseStatus parse(String status) {
        ResponseStatus resp = null;
        for (ResponseStatus rs : ResponseStatus.values()) {
            if (status.equalsIgnoreCase(rs.toString())) {
                resp = rs;
                break;
            }
        }
        return resp;
    }
}
