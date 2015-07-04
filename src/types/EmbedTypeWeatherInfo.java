package types;

import googlegeoloc.GeoLocation;
import googlegeoloc.GeocodeResult;
import googlegeoloc.GeocodeResults;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.ResourceHelper;
import weatherforecast.DailyForecastResponse;
import weatherforecast.DailyWeatherForecast;
import weatherforecast.DayForecast;
import weatherforecast.DayTemperature;
import weatherforecast.DayWeather;

public class EmbedTypeWeatherInfo extends EmbedType {

    private String location = "";
    private String address = "";
    private GeocodeResult geocodeResult;
    private float latitude = -1;
    private float longitude = -1;
    private boolean locationIncluded;
    private boolean iconIncluded;
    private boolean descriptionIncluded;
    private boolean temperatureIncluded;
    private boolean isFahrennheit;
    private boolean humidityIncluded;
    private boolean windSpeedIncluded;
    private boolean forecastIncluded;
    private boolean isRectangular;
    private Color textColor;
    private Color backgroundColor;

    public EmbedTypeWeatherInfo() {
        super(EmbedType.Name.WeatherInfo);
    }

    public void retrieveLocation() throws Exception {
        GeocodeResults results = GeocodeResults.getGeocodeResults(location);
        if (results != null && results.results != null && results.results.length > 0) {
            GeocodeResult result = results.results[0];
            GeoLocation loc = result.geometry.getLocation();
            latitude = (float) loc.getLatitude();
            longitude = (float) loc.getLongitude();
            address = result.formattedAddress;
            geocodeResult = result;
        }
    }

    @Override
    public Image createNewImage(int parentWidth, int parentHeight) {
        Image image = createEmptyImage(parentWidth, parentHeight);
        try {
            File temp = ResourceHelper.getResource("APPDATA_DIRECTORY");
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            float opacity = (float) getOpacity() / 100;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            // Create image
            if (geocodeResult == null) {
                retrieveLocation();
            }
            DailyWeatherForecast dwf = new DailyWeatherForecast();
            DailyForecastResponse resp = dwf.forecast(latitude, longitude, 6);
            DayForecast currDay = resp.getForecasts().get(0);
            DayWeather currWeather = currDay.getWeather().get(0);

            // Create image
            {
                int py = 0, ny = 10, gap = 2, pad = 10, nw = 10;
                int w = 300;
                int h = pad + (locationIncluded ? 27 : 0)
                        + ((iconIncluded || descriptionIncluded) ? 32 : 0)
                        + ((temperatureIncluded || windSpeedIncluded || humidityIncluded) ? 34 : 0)
                        + (forecastIncluded ? 40 : 0) + pad;
                Image tempImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) tempImage.getGraphics();
                Rectangle2D rect;
                FontRenderContext frc = g.getFontRenderContext();
                Font addressFont = new Font("Ebrima", Font.PLAIN, 18);
                Font normFont = new Font("Ebrima", Font.PLAIN, 18);
                Font smallFont = new Font("Ebrima", Font.PLAIN, 10);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color txtClr = Color.BLACK;
                Color addrBarClr = Color.GRAY;
                Color col1 = Color.BLACK;
                Color col2 = Color.GRAY;
                Color col3 = Color.WHITE;

                if (isRectangular) {
                    g.setPaint(new GradientPaint(new Point(0, 0), col2, new Point(0, height), backgroundColor));
                    g.fillRoundRect(0, 0, w, h, 20, 20);
                }

                if (locationIncluded) {
                    g.setFont(addressFont);
                    rect = addressFont.getStringBounds(location, frc);
                    g.setPaint(new GradientPaint(new Point(pad, ny), col2, new Point(pad, (int) (ny + rect.getHeight())), col3));
                    g.fillRoundRect(pad, ny, w - pad * 2, (int) rect.getHeight(), 10, 10);
                    g.setColor(txtClr);
                    g.drawString(location, (int) ((w - rect.getWidth()) / 2), (int) (ny - rect.getY()));
                    py = ny;
                    ny += rect.getHeight() + gap;
                }

                if (iconIncluded || descriptionIncluded) {

                    g.setPaint(new GradientPaint(new Point(pad, ny), col2, new Point(pad, ny + 30), col3));
                    g.fillRoundRect(pad, ny, w - pad * 2, 30, 10, 10);

                    if (iconIncluded) {
                        String iconStr = currWeather.getIcon();
                        File iconFile = new File(temp, iconStr + "_info.png");
                        Image icon = ResourceHelper.getImageObject(iconFile);
                        if (icon == null) {
                            icon = ResourceHelper.getImageObject(new URL("http://openweathermap.org/img/w/" + iconStr + ".png"));
                            ImageIO.write((RenderedImage) icon, "png", iconFile);
                        }
                        g.drawImage(icon, pad, ny - 9, null);
                    }

                    if (isDescriptionIncluded()) {
                        g.setFont(normFont);
                        g.setColor(txtClr);
                        rect = normFont.getStringBounds(currWeather.getDescription(), frc);
                        g.drawString(currWeather.getDescription(),
                                (int) (pad + 20 + (w - pad - 20 - rect.getWidth()) / 2),
                                (int) (ny - rect.getY()));
                    }

                    py = ny;
                    ny += 30 + gap;
                }

                if (temperatureIncluded || windSpeedIncluded || humidityIncluded) {
                    g.setPaint(new GradientPaint(new Point(pad, ny), col2, new Point(pad, ny + 32), col3));
                    g.fillRoundRect(pad, ny, w - pad * 2, 32, 10, 10);
                    g.setColor(txtClr);

                    int x = pad + gap + 5;
                    if (temperatureIncluded) {
                        DayTemperature dTemp = currDay.getTemp();
                        String min = String.format("%.1f%cC", dTemp.getMinTemp(), DayWeather.DEGREE);
                        rect = normFont.getStringBounds(min, frc);
                        g.setFont(normFont);
                        g.drawString(min, x, (int) (ny - rect.getY()));
                        g.setFont(smallFont);
                        g.drawString("min", x + 5, (int) (ny + 5 + rect.getHeight()));
                        x += gap + 60;
                        String max = String.format("%.1f%cC", dTemp.getMaxTemp(), DayWeather.DEGREE);
                        rect = normFont.getStringBounds(max, frc);
                        g.setFont(normFont);
                        g.drawString(max, x, (int) (ny - rect.getY()));
                        g.setFont(smallFont);
                        g.drawString("max", x + 5, (int) (ny + 5 + rect.getHeight()));
                        x += gap + 60;
                    }

                    if (windSpeedIncluded) {
                        String speed = String.format("%2.1fm/s", currDay.getWindSpeed());
                        rect = normFont.getStringBounds(speed, frc);
                        g.setFont(normFont);
                        g.drawString(speed, x + 5, (int) (ny - rect.getY()));
                        g.setFont(smallFont);
                        g.drawString("wind", x + 15, (int) (ny + 5 + rect.getHeight()));
                        x += gap + 80;
                    }

                    if (humidityIncluded) {
                        String humidity = String.format("%2d%%", currDay.getHumidity());
                        rect = normFont.getStringBounds(humidity, frc);
                        g.setFont(normFont);
                        g.drawString(humidity, x + 5, (int) (ny - rect.getY()));
                        g.setFont(smallFont);
                        g.drawString("humidity", x + 5, (int) (ny + 5 + rect.getHeight()));
                    }
                    py = ny;
                    ny += 32 + gap;
                }

                if (forecastIncluded) {
                    g.setPaint(new GradientPaint(new Point(pad, ny), col2, new Point(pad, ny + 40), col3));
                    g.fillRoundRect(pad, ny, w - pad * 2, 40, 10, 10);
                    g.setColor(txtClr);
                    g.setFont(smallFont);
                    List<DayForecast> forecasts = resp.getForecasts();
                    int x = pad;
                    for (int i = 1; i < 5; i++) {
                        DayWeather weather = forecasts.get(i).getWeather().get(0);
                        String iconStr = weather.getIcon();
                        File iconFile = new File(temp, iconStr + "_info.png");
                        Image icon = ResourceHelper.getImageObject(iconFile);
                        if (icon == null) {
                            icon = ResourceHelper.getImageObject(new URL("http://openweathermap.org/img/w/" + iconStr + ".png"));
                            ImageIO.write((RenderedImage) icon, "png", iconFile);
                        }
                        g.drawImage(icon, x, ny - 9, null);
                        g.drawString(weather.getMain(), x + 10, ny + 35);
                        x += 70;
                    }
                }

                g.dispose();

                float s1 = (float) width / w;
                float s2 = (float) height / h;
                float s = s1 < s2 ? s1 : s2;
                int newWidth = (int) (s * w);
                tempImage = tempImage.getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
                w = tempImage.getWidth(null);
                h = tempImage.getHeight(null);
                int locx, locy;
                locx = (width - w) / 2;
                locy = (height - h) / 2;
                g2d.drawImage(tempImage, locx, locy, null);
            }

            g2d.dispose();
        } catch (Exception ex) {
            ResourceHelper.errLog("EmbedTypeWeatherInfo > createNewImage(...) > Error : " + ex);
        }
        embedTypeImage = image;
        return image;
    }

    @Override
    public Image createSampleImage(int parentWidth, int parentHeight) {
        if (embedTypeImage == null) {
            createNewImage(parentWidth, parentHeight);
        }
        return embedTypeImage;
    }

    @Override
    public final void reset() {
        super.resetCommonProperties();
        location = address = "";
        latitude = longitude = -1;
        isFahrennheit = true;
        locationIncluded = iconIncluded = descriptionIncluded = temperatureIncluded
                = humidityIncluded = windSpeedIncluded
                = forecastIncluded = isRectangular = false;
        textColor = Color.BLACK;
        backgroundColor = Color.GRAY;

    }

    @Override
    public void resetOnlyMainProperties(EmbedType embedType) {
            if (embedType.getName().equals(getName())) {
                EmbedTypeWeatherInfo etwi = (EmbedTypeWeatherInfo) embedType;
                location = etwi.location;
                address = etwi.address;
                latitude = etwi.latitude;
                longitude = etwi.longitude;
                locationIncluded = etwi.locationIncluded;
                iconIncluded = etwi.iconIncluded;
                descriptionIncluded = etwi.descriptionIncluded;
                temperatureIncluded = etwi.temperatureIncluded;
                isFahrennheit = etwi.isFahrennheit;
                humidityIncluded = etwi.humidityIncluded;
                windSpeedIncluded = etwi.windSpeedIncluded;
                forecastIncluded = etwi.forecastIncluded;
                isRectangular = etwi.isRectangular;
                textColor = etwi.textColor;
                backgroundColor = etwi.backgroundColor;

            }
    }

    @Override
    public boolean equals(Object obj) {
        boolean rtrn = false;
        if (obj != null && super.equals(obj)) {
            if (obj instanceof EmbedTypeWeatherInfo) {
                EmbedTypeWeatherInfo etwi = (EmbedTypeWeatherInfo) obj;
                rtrn = location != null && location.equals(etwi.location);
                rtrn = rtrn && latitude == etwi.latitude;
                rtrn = rtrn && longitude == etwi.longitude;
                rtrn = rtrn && locationIncluded == etwi.locationIncluded;
                rtrn = rtrn && iconIncluded == etwi.iconIncluded;
                rtrn = rtrn && descriptionIncluded == etwi.descriptionIncluded;
                rtrn = rtrn && temperatureIncluded == etwi.temperatureIncluded;
                rtrn = rtrn && isFahrennheit == etwi.isFahrennheit;
                rtrn = rtrn && humidityIncluded == etwi.humidityIncluded;
                rtrn = rtrn && windSpeedIncluded == etwi.windSpeedIncluded;
                rtrn = rtrn && forecastIncluded == etwi.forecastIncluded;
                rtrn = rtrn && isRectangular == etwi.isRectangular;
                rtrn = rtrn && (textColor == null && etwi.textColor == null)
                        || textColor.equals(etwi.textColor);
                rtrn = rtrn && (textColor == null && etwi.textColor == null)
                        || backgroundColor.equals(etwi.backgroundColor);
                rtrn = rtrn && address != null && address.equals(etwi.address);
                rtrn = rtrn && (geocodeResult == null && etwi.geocodeResult == null)
                        || (geocodeResult.equals(etwi.geocodeResult));
            }
        }
        return rtrn;
    }

    @Override
    protected void writeXMLElementInnerData(XMLStreamWriter xsw) throws XMLStreamException {

        xsw.writeEmptyElement("location");                                      //<location ... />
        xsw.writeAttribute("value", location);

        xsw.writeEmptyElement("address");                                       //<address ... />
        xsw.writeAttribute("value", address);

        xsw.writeEmptyElement("latitude");                                      //<latitude ... />
        xsw.writeAttribute("value", Float.toString(latitude));

        xsw.writeEmptyElement("longitude");                                     //<longitude ... />
        xsw.writeAttribute("value", Float.toString(longitude));

        xsw.writeEmptyElement("location_included");                             //<location_included ... />
        xsw.writeAttribute("value", Boolean.toString(locationIncluded));

        xsw.writeEmptyElement("icon_included");                                 //<icon_included ... />
        xsw.writeAttribute("value", Boolean.toString(iconIncluded));

        xsw.writeEmptyElement("description_included");                          //<description_included ... />
        xsw.writeAttribute("value", Boolean.toString(descriptionIncluded));

        xsw.writeEmptyElement("temperature_included");                          //<temperature_included ... />
        xsw.writeAttribute("value", Boolean.toString(temperatureIncluded));

        xsw.writeEmptyElement("is_fahrenheit");                                 //<is_fahrenheit ... />
        xsw.writeAttribute("value", Boolean.toString(isFahrennheit));

        xsw.writeEmptyElement("humidity_included");                             //<humidity_included ... />
        xsw.writeAttribute("value", Boolean.toString(humidityIncluded));

        xsw.writeEmptyElement("precipitation_included");                        //<precipitation_included ... />
        xsw.writeAttribute("value", Boolean.toString(windSpeedIncluded));

        xsw.writeEmptyElement("forecast_included");                             //<forecast_included ... />
        xsw.writeAttribute("value", Boolean.toString(forecastIncluded));

        xsw.writeEmptyElement("is_rectangular");                                //<is_rectangular ... />
        xsw.writeAttribute("value", Boolean.toString(isRectangular));

        xsw.writeEmptyElement("text_color");                                    //<text_color ... />
        xsw.writeAttribute("value", Integer.toString(textColor.getRGB()));

        xsw.writeEmptyElement("background_color");                              // <background_color ... />
        xsw.writeAttribute("value", Integer.toString(backgroundColor.getRGB()));
    }

    @Override
    protected void readXMLElementInnerData(Node node) {
        Node n;
        NamedNodeMap nnm;
        String nm;

        NodeList nodes = node.getChildNodes();
        int l = nodes.getLength();

        for (int i = 0; i < l; i++) {
            n = nodes.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    nm = n.getNodeName();
                    nnm = n.getAttributes();
                    switch (nm) {
                        case "location":
                            location = nnm.getNamedItem("value").getNodeValue();
                            break;
                        case "address":
                            address = nnm.getNamedItem("value").getNodeValue();
                            break;
                        case "latitude":
                            latitude = Float.parseFloat(nnm.getNamedItem("value")
                                    .getNodeValue());
                            break;
                        case "longitude":
                            longitude = Float.parseFloat(nnm.getNamedItem("value")
                                    .getNodeValue());
                            break;
                        case "location_included":
                            locationIncluded = Boolean.parseBoolean(nnm.getNamedItem("value")
                                    .getNodeValue());
                            break;
                        case "icon_included":
                            iconIncluded = Boolean.parseBoolean(nnm
                                    .getNamedItem("value") .getNodeValue());
                            break;
                        case "description_included":
                            descriptionIncluded = Boolean.parseBoolean(nnm
                                    .getNamedItem("value").getNodeValue());
                            break;
                        case "temperature_included":
                            temperatureIncluded = Boolean.parseBoolean(nnm
                                    .getNamedItem("value").getNodeValue());
                            break;
                        case "is_fahrenheit":
                            isFahrennheit = Boolean.parseBoolean(nnm.getNamedItem("value")
                                    .getNodeValue());
                            break;
                        case "humidity_included":
                            humidityIncluded = Boolean.parseBoolean(nnm
                                    .getNamedItem("value").getNodeValue());
                            break;
                        case "precipitation_included":
                            windSpeedIncluded = Boolean.parseBoolean(nnm
                                    .getNamedItem("value").getNodeValue());
                            break;
                        case "forecast_included":
                            forecastIncluded = Boolean.parseBoolean(nnm
                                    .getNamedItem("value").getNodeValue());
                            break;
                        case "is_rectangular":
                            isRectangular = Boolean.parseBoolean(nnm
                                    .getNamedItem("value").getNodeValue());
                            break;
                        case "text_color":
                            textColor = new Color(Integer.parseInt(nnm
                                    .getNamedItem("value").getNodeValue()));
                            break;
                        case "background_color":
                            backgroundColor = new Color(Integer.parseInt(nnm
                                    .getNamedItem("value").getNodeValue()));
                            break;
                    }
                } catch (Exception ex) {
                    ResourceHelper.errLog("EmbedTypeWeatherInfo > readXMLElementInnerData(Node) > Error : " + ex);
                }
            }
        }
    }

    @Override
    public EmbedType createNewEmbedType() {
        EmbedTypeWeatherInfo embedType = new EmbedTypeWeatherInfo();
        embedType.resetFromEmbedType(this);
        return embedType;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the includeLocation
     */
    public boolean isLocationIncluded() {
        return locationIncluded;
    }

    /**
     * @param locationIncluded the includeLocation to set
     */
    public void setLocationIncluded(boolean locationIncluded) {
        this.locationIncluded = locationIncluded;
    }

    /**
     * @return the includeIcon
     */
    public boolean isIconIncluded() {
        return iconIncluded;
    }

    /**
     * @param includeIcon the includeIcon to set
     */
    public void setIconIncluded(boolean includeIcon) {
        this.iconIncluded = includeIcon;
    }

    /**
     * @return the isFerenheit
     */
    public boolean isIsFahrennheit() {
        return isFahrennheit;
    }

    /**
     * @param isFahrennheit the isFerenheit to set
     */
    public void setIsFahrennheit(boolean isFahrennheit) {
        this.isFahrennheit = isFahrennheit;
    }

    /**
     * @return the includeHumidity
     */
    public boolean isHumidityIncluded() {
        return humidityIncluded;
    }

    /**
     * @param humidityIncluded the includeHumidity to set
     */
    public void setHumidityIncluded(boolean humidityIncluded) {
        this.humidityIncluded = humidityIncluded;
    }

    /**
     * @return the includePrecepitation
     */
    public boolean isWindSpeedIncluded() {
        return windSpeedIncluded;
    }

    /**
     * @param windSpeedIncluded the includePrecepitation to set
     */
    public void setWindSpeedIncluded(boolean windSpeedIncluded) {
        this.windSpeedIncluded = windSpeedIncluded;
    }

    /**
     * @return the include3DaysForecast
     */
    public boolean isForecastIncluded() {
        return forecastIncluded;
    }

    /**
     * @param forecastIncluded the include3DaysForecast to set
     */
    public void setForecastIncluded(boolean forecastIncluded) {
        this.forecastIncluded = forecastIncluded;
    }

    /**
     * @return the isRectangular
     */
    public boolean isIsRectangular() {
        return isRectangular;
    }

    /**
     * @param isRectangular the isRectangular to set
     */
    public void setIsRectangular(boolean isRectangular) {
        this.isRectangular = isRectangular;
    }

    /**
     * @return the textColor
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * @param textColor the textColor to set
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @return the descriptionIncluded
     */
    public boolean isDescriptionIncluded() {
        return descriptionIncluded;
    }

    /**
     * @param descriptionIncluded the descriptionIncluded to set
     */
    public void setDescriptionIncluded(boolean descriptionIncluded) {
        this.descriptionIncluded = descriptionIncluded;
    }

    /**
     * @return the temperatureIncluded
     */
    public boolean isTemperatureIncluded() {
        return temperatureIncluded;
    }

    /**
     * @param temperatureIncluded the temperatureIncluded to set
     */
    public void setTemperatureIncluded(boolean temperatureIncluded) {
        this.temperatureIncluded = temperatureIncluded;
    }

    /**
     * @return the geocodeResult
     */
    public GeocodeResult getGeocodeResult() {
        return geocodeResult;
    }

    /**
     * @param geocodeResult the geocodeResult to set
     */
    public void setGeocodeResult(GeocodeResult geocodeResult) {
        this.geocodeResult = geocodeResult;
        this.latitude = (float) geocodeResult.geometry.getLocation().getLatitude();
        this.longitude = (float) geocodeResult.geometry.getLocation().getLongitude();
    }
}
