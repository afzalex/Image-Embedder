package weatherforecast;

import org.json.JSONObject;

public class DayTemperature {

    static private final String DAY_TEMP = "day";
    static private final String MIN_TEMP = "min";
    static private final String MAX_TEMP = "max";
    static private final String NIGHT_TEMP = "night";
    static private final String EVENING_TEMP = "eve";
    static private final String MORNING_TEMP = "morn";

    private final float dayTemp;
    private final float nightTemp;
    private final float eveningTemp;
    private final float morningTemp;
    private final float minTemp;
    private final float maxTemp;

    public DayTemperature(JSONObject json) {
        dayTemp = (float) json.optDouble(DAY_TEMP);
        minTemp = (float) json.optDouble(MIN_TEMP);
        maxTemp = (float) json.optDouble(MAX_TEMP);
        nightTemp = (float) json.optDouble(NIGHT_TEMP);
        eveningTemp = (float) json.optDouble(EVENING_TEMP);
        morningTemp = (float) json.optDouble(MORNING_TEMP);
    }

    /**
     * @return the dayTemp
     */
    public float getDayTemp() {
        return dayTemp;
    }

    /**
     * @return the nightTemp
     */
    public float getNightTemp() {
        return nightTemp;
    }

    /**
     * @return the eveningTemp
     */
    public float getEveningTemp() {
        return eveningTemp;
    }

    /**
     * @return the morningTemp
     */
    public float getMorningTemp() {
        return morningTemp;
    }

    /**
     * @return the minTemp
     */
    public float getMinTemp() {
        return minTemp;
    }

    /**
     * @return the maxTemp
     */
    public float getMaxTemp() {
        return maxTemp;
    }

}
