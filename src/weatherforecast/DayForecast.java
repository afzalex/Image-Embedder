package weatherforecast;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DayForecast {

    static private final String DT = "dt";
    static private final String TEMP = "temp";
    static private final String PRESSURE = "pressure";
    static private final String HUMIDITY = "humidity";
    static private final String WEATHER = "weather";
    static private final String WIND_SPEED = "speed";
    static private final String WIND_DIRECTION = "deg";

    private long dateTime = Long.MIN_VALUE;
    private final DayTemperature temp;
    private final float pressure;
    private final int humidity;
    private final float windSpeed;
    private final int windDirection;
    private final List<DayWeather> weather;

    public DayForecast(JSONObject json) throws JSONException {
        this.dateTime = json.optLong(DT, Long.MIN_VALUE);
        this.temp = new DayTemperature(json.optJSONObject(TEMP));
        this.pressure = (float) json.optDouble(PRESSURE);
        this.humidity = (int) json.optInt(HUMIDITY);
        JSONArray weatherArray = json.optJSONArray(WEATHER);
        weather = new ArrayList<>();
        for (int i = 0; i < weatherArray.length(); i++) {
            weather.add(new DayWeather(weatherArray.getJSONObject(i)));
        }
        this.windSpeed = (float) json.optDouble(WIND_SPEED);
        this.windDirection = json.optInt(WIND_DIRECTION);
    }

    /**
     * @return the dateTime
     */
    public long getDateTime() {
        return dateTime;
    }

    /**
     * @return the temp
     */
    public DayTemperature getTemp() {
        return temp;
    }

    /**
     * @return the pressure
     */
    public float getPressure() {
        return pressure;
    }

    /**
     * @return the humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * @return the windSpeed
     */
    public float getWindSpeed() {
        return windSpeed;
    }

    /**
     * @return the windDirection
     */
    public int getWindDirection() {
        return windDirection;
    }

    /**
     * @return the weather
     */
    public List<DayWeather> getWeather() {
        return weather;
    }
}
