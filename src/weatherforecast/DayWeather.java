package weatherforecast;

import org.json.JSONObject;

public class DayWeather {

    public static final char DEGREE = 176;
    
    private static final String ID = "id";
    private static final String MAIN = "main";
    private static final String DESCRIPTION = "description";
    private static final String ICON = "icon";

    private final int id;
    private final String main;
    private final String description;
    private final String icon;

    public DayWeather(JSONObject json) {
        id = json.optInt(ID);
        main = json.optString(MAIN);
        description = json.optString(DESCRIPTION);
        icon = json.optString(ICON);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the main
     */
    public String getMain() {
        return main;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }
}
