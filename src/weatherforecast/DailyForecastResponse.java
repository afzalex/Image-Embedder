package weatherforecast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DailyForecastResponse {

    static private final String JSON_COD = "cod";
    static private final String JSON_MESSAGE = "message";
    static private final String JSON_CALCTIME = "dt";
    static private final String JSON_CALCTIME_TOTAL = "total";
    static private final String JSON_LIST = "list";
    static private final String JSON_URL = "url";
    static private final String JSON_CITY = "city";

    private final int code;
    private final String message;
    private final float calctime;
    private final String url;
    private final City city;
    private final List<DayForecast> forecasts;

    public DailyForecastResponse(JSONObject json) throws JSONException {

        /////////////////////////////////////////////////////////////////////////////////////////
        this.code = json.optInt(DailyForecastResponse.JSON_COD, Integer.MIN_VALUE);
        this.message = json.optString(DailyForecastResponse.JSON_MESSAGE);
        String calcTimeStr = json.optString(DailyForecastResponse.JSON_CALCTIME);
        float calcTimeTotal = Float.NaN;
        if (calcTimeStr.length() > 0) {
            try {
                calcTimeTotal = Float.valueOf(calcTimeStr);
            } catch (NumberFormatException nfe) { // So.. it's not a number.. let's see if we can still find it's value.
                String totalCalcTimeStr = getValueStrFromCalcTimePart(calcTimeStr, DailyForecastResponse.JSON_CALCTIME_TOTAL);
                if (totalCalcTimeStr != null) {
                    try {
                        calcTimeTotal = Float.valueOf(totalCalcTimeStr);
                    } catch (NumberFormatException nfe2) {
                        calcTimeTotal = Float.NaN;
                    }
                }
            }
        }
        this.calctime = calcTimeTotal;

        ////////////////////////////////////////////////////////////////////////////////////////
        this.url = json.optString(DailyForecastResponse.JSON_URL);
        JSONObject jsonCity = json.optJSONObject(DailyForecastResponse.JSON_CITY);
        if (jsonCity != null) {
            this.city = new City(jsonCity);
        } else {
            this.city = null;
        }
        JSONArray jsonForecasts = json.optJSONArray(DailyForecastResponse.JSON_LIST);
        if (jsonForecasts != null) {
            this.forecasts = new ArrayList<DayForecast>(jsonForecasts.length());
            for (int i = 0; i < jsonForecasts.length(); i++) {
                JSONObject jsonForecast = jsonForecasts.optJSONObject(i);
                this.forecasts.add(new DayForecast(jsonForecast));
            }
        } else {
            this.forecasts = Collections.emptyList();
        }
    }

    private String getValueStrFromCalcTimePart(final String calcTimeStr, final String part) {
        Pattern keyValuePattern = Pattern.compile(part + "\\s*=\\s*([\\d\\.]*)");
        Matcher matcher = keyValuePattern.matcher(calcTimeStr);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the calctime
     */
    public float getCalctime() {
        return calctime;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * @return the forecasts
     */
    public List<DayForecast> getForecasts() {
        return forecasts;
    }
}
