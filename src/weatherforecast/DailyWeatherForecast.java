package weatherforecast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import resources.ResourceHelper;

public class DailyWeatherForecast {
    
    private String baseOwmUrl = "http://api.openweathermap.org/data/2.5/";
    private String owmAPPID = null;
    static private final String APPID_HEADER = "x-api-key";
    private HttpClient httpClient;
    private static String UNITS = "units=metric";
    
    public DailyWeatherForecast() {
        httpClient = new DefaultHttpClient();
    }
    
    public DailyForecastResponse forecast(float latitude, float longitude, int days) throws JSONException, IOException {
        String subUrl = String.format(Locale.ROOT, "forecast/daily?lat=%f&lon=%f&cnt=%d&cluster=yes&units=metric&APPID=%s",
                Float.valueOf(latitude), Float.valueOf(longitude), Integer.valueOf(days), this.owmAPPID);
        JSONObject response = doQuery(subUrl);
        return new DailyForecastResponse(response);
    }
    
    public DailyForecastResponse sampleForecast() throws JSONException {
        StringWriter sw = new StringWriter();
        JSONObject response;
        try (Scanner sc = new Scanner(new File((ResourceHelper.getResourceURL("sample_forecast.json").getFile())))) {
            int r;
            while (sc.hasNextLine()) {
                sw.append(sc.nextLine());
            }
            response = new JSONObject(sw.toString());
        } catch (Exception ex) {
            ResourceHelper.errLog("DailyWeatherForecast > sampleForecast() > Error : " + ex);
            response = null;
        }
        return new DailyForecastResponse(response);
    }
    
    private JSONObject doQuery(String subUrl) throws JSONException, IOException {
        String responseBody = null;
        HttpGet httpget = new HttpGet(this.baseOwmUrl + subUrl);
        //System.out.println(httpget.getURI().toString());
        if (this.owmAPPID != null) {
            httpget.addHeader(DailyWeatherForecast.APPID_HEADER, this.owmAPPID);
        }
        
        HttpResponse response = this.httpClient.execute(httpget);
        InputStream contentStream = null;
        try {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null) {
                throw new IOException(
                        String.format("Unable to get a response from OWM server"));
            }
            int statusCode = statusLine.getStatusCode();
            if (statusCode < 200 && statusCode >= 300) {
                throw new IOException(
                        String.format("OWM server responded with status code %d: %s", statusCode, statusLine));
            }
            /* Read the response content */
            HttpEntity responseEntity = response.getEntity();
            contentStream = responseEntity.getContent();
            Reader isReader = new InputStreamReader(contentStream);
            int contentSize = (int) responseEntity.getContentLength();
            if (contentSize < 0) {
                contentSize = 8 * 1024;
            }
            StringWriter strWriter = new StringWriter(contentSize);
            char[] buffer = new char[8 * 1024];
            int n = 0;
            while ((n = isReader.read(buffer)) != -1) {
                strWriter.write(buffer, 0, n);
            }
            responseBody = strWriter.toString();
            contentStream.close();
        } catch (IOException e) {
            throw e;
        } catch (RuntimeException re) {
            httpget.abort();
            throw re;
        } finally {
            if (contentStream != null) {
                contentStream.close();
            }
        }
        return new JSONObject(responseBody);
    }

    /**
     * @return the owmAPPID
     */
    public String getOwmAPPID() {
        return owmAPPID;
    }

    /**
     * @param owmAPPID the owmAPPID to set
     */
    public void setOwmAPPID(String owmAPPID) {
        this.owmAPPID = owmAPPID;
    }
}
