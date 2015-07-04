package imageembedder;

import googlegeoloc.GeoLocation;
import googlegeoloc.GeocodeResult;
import googlegeoloc.GeocodeResults;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.bitpipeline.lib.owm.ForecastWeatherData;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.StatusWeatherData;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;
import weatherforecast.DailyForecastResponse;
import weatherforecast.DailyWeatherForecast;

public class Testing {

    public static void main(String... args) throws IOException, JSONException {
        //http://api.openweathermap.org/data/2.5/find?lat=30.092445&lon=78.283211&cnt=10&cluster=yes&APPID=5a4c1c16d3e982f7e39e6a3d9a6bd5e6
        fun4();
    }
    
    public static void fun4() throws JSONException, IOException{
        DailyWeatherForecast dwf = new DailyWeatherForecast();
        dwf.setOwmAPPID("5a4c1c16d3e982f7e39e6a3d9a6bd5e6");
        DailyForecastResponse forecast = dwf.forecast(30.092445f, 78.283211f, 5);
        
    }

    public static void fun2() throws IOException, JSONException {
        OwmClient oc = new OwmClient();
        oc.setAPPID("5a4c1c16d3e982f7e39e6a3d9a6bd5e6");
        Scanner sc = new Scanner(System.in);
        WeatherStatusResponse wsr = oc.currentWeatherAtCity(30.092446f, 78.283207f, 2);
        //WeatherStatusResponse wsr = oc.currentWeatherAtCity(-0.12574f, 51.50853f, 2);
        //WeatherStatusResponse wsr = oc.currentWeatherAtCity("london","gb");
        if (wsr.hasWeatherStatus()) {
            List<StatusWeatherData> wsl = wsr.getWeatherStatus();
            for (StatusWeatherData swd : wsl) {
                System.out.printf("<swd>\n");
                WeatherData.Main main = swd.getMain();
                float c = (float) (main.getTemp() / 62.95454545454545D);
                System.out.printf("%4sTemp : %s\n", "", c);
                System.out.printf("%4sMax  : %s\n", "", main.getTempMax());
                System.out.printf("%4sMin  : %s\n", "", main.getTempMin());
                System.out.printf("%4sName : %s\n", "", swd.getName());
                System.out.printf("%4sGeoloc   :%s:%s\n", "", swd.getCoord().getLatitude(), swd.getCoord().getLongitude());
                System.out.printf("%4sPrecipita: %s\n", "", swd.getPrecipitation());
                System.out.printf("%4sHumidity : %s\n", "", swd.getHumidity());
                System.out.printf("%4sRain     : %s\n", "", swd.getRain());
                System.out.printf("%4sWind     : %s\n", "", swd.getWindSpeed());

                List<WeatherData.WeatherCondition> wcs = swd.getWeatherConditions();
                for (WeatherData.WeatherCondition wc : wcs) {
                    System.out.printf("%4s<wc>\n", "");
                    System.out.printf("%8s%s\n", "", wc.getDescription());
                    System.out.printf("%8s%s\n", "", wc.getIconName());
                    System.out.printf("%4s</wc>\n", "");
                }
                System.out.printf("</swd>\n");
            }
        } else {
            System.out.println("Not has weather status");
        }
    }

    public static void fun3() throws JSONException, IOException {
        OwmClient oc = new OwmClient();
        oc.setAPPID("5a4c1c16d3e982f7e39e6a3d9a6bd5e6");
        WeatherForecastResponse fw = oc.forecastWeatherAtCity(30.092445f, 78.283211f, 5);
        ForecastWeatherData fwd = fw.getForecasts().get(0);
        System.out.println(new Date(fwd.getDateTime()*1000));
        System.out.println(fwd.getTemp());
        System.out.println(fwd.getWindSpeed());
    }

    public static void fun1() {

        OwmClient oc = new OwmClient();
        oc.setAPPID("5a4c1c16d3e982f7e39e6a3d9a6bd5e6");
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Query : ");
            String query = sc.nextLine();
            if (query.length() == 0) {
                break;
            }
            try {
                GeocodeResults geocodeResults = GeocodeResults.getGeocodeResults(query);
                for (GeocodeResult gr : geocodeResults.results) {
                    System.out.println("--------------------------------------------------");
                    System.out.println("   Location : " + gr.formattedAddress);
                    GeoLocation loc = gr.geometry.getLocation();
                    System.out.println("   Lat:" + loc.getLatitude() + "\tLng:" + loc.getLongitude());
                    WeatherStatusResponse wsr = oc.currentWeatherAtCity((float) loc.getLatitude(), (float) loc.getLongitude(), 3);
                    if (wsr.hasWeatherStatus()) {
                        List<StatusWeatherData> wsl = wsr.getWeatherStatus();
                        for (StatusWeatherData swd : wsl) {
                            System.out.println(swd.getStation());
                        }
                    } else {
                        System.out.println("Not has weather status");
                    }
                    System.out.println("__________________________________________________________\n\n");
                }
            } catch (Exception ex) {
                System.err.println("Error occured while receiving weather information : " + ex);
                ex.printStackTrace();
            }
        }
    }
}
