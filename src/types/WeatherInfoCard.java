package types;

import googlegeoloc.GeoLocation;
import googlegeoloc.GeocodeResult;
import googlegeoloc.GeocodeResults;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherInfoCard extends CardItem {

    /**
     * Creates new form WeatherInfoCard
     */
    public WeatherInfoCard() {
        preInitComponents();
        initComponents();
        postInitComponents();
    }

    private void preInitComponents() {

    }

    private void postInitComponents() {
        textClr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textClrClicked();
            }
        });
        backgroundClr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                backgroundClrClicked();
            }
        });
        tester = new Tester();
        consumer = gr -> {
            if (gr != null) {
                addressField.setText(gr.formattedAddress);
                GeoLocation loc = gr.geometry.getLocation();
                info_geocodeResult = gr;
            }
        };
    }

    private void textClrClicked() {
        Color col = javax.swing.JColorChooser.showDialog(this, "Text Color", textClr.getBackground());
        if (col != null) {
            textClr.setBackground(col);
        }
    }

    private void backgroundClrClicked() {
        Color col = javax.swing.JColorChooser.showDialog(this, "Background Color", backgroundClr.getBackground());
        if (col != null) {
            backgroundClr.setBackground(col);
        }
    }

    private void test() {
        tester.test(locField.getText(), consumer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        temperatureUnit = new javax.swing.ButtonGroup();
        locationLbl = new javax.swing.JLabel();
        locField = new javax.swing.JTextField();
        testBtn = new javax.swing.JButton();
        addressField = new javax.swing.JTextField();
        locationChk = new javax.swing.JCheckBox();
        weatherIconChk = new javax.swing.JCheckBox();
        temperatureChk = new javax.swing.JCheckBox();
        weatherDescriptionChk = new javax.swing.JCheckBox();
        textLbl = new javax.swing.JLabel();
        backgroundLbl = new javax.swing.JLabel();
        backgroundClr = new javax.swing.JLabel();
        windSpeedChk = new javax.swing.JCheckBox();
        forecastChk = new javax.swing.JCheckBox();
        humidityChk = new javax.swing.JCheckBox();
        centrigradeRadio = new javax.swing.JRadioButton();
        fahrenheitRadio = new javax.swing.JRadioButton();
        textClr = new javax.swing.JLabel();
        rectangularChk = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(523, 300));

        locationLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        locationLbl.setText(" Location :  ");

        testBtn.setText("Test");
        testBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testBtnActionPerformed(evt);
            }
        });

        addressField.setEnabled(false);

        locationChk.setText("Location");

        weatherIconChk.setText("Weather icon");

        temperatureChk.setText("Temperature");

        weatherDescriptionChk.setText("Weather description");

        textLbl.setText(" Text Color");

        backgroundLbl.setText(" Background Color");

        backgroundClr.setBackground(new java.awt.Color(204, 204, 204));
        backgroundClr.setText(" ");
        backgroundClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        backgroundClr.setOpaque(true);

        windSpeedChk.setText("Wind Speed");

        forecastChk.setText("Future weather forecast");

        humidityChk.setText("Humidity");

        temperatureUnit.add(centrigradeRadio);
        centrigradeRadio.setText("Centigrade");

        temperatureUnit.add(fahrenheitRadio);
        fahrenheitRadio.setSelected(true);
        fahrenheitRadio.setText("Fahrenheit");

        textClr.setBackground(new java.awt.Color(0, 0, 0));
        textClr.setText(" ");
        textClr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        textClr.setOpaque(true);

        rectangularChk.setText("Rectangular");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(locationLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(locField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(testBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rectangularChk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textClr, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(backgroundClr, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(backgroundLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(temperatureChk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(centrigradeRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(humidityChk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(windSpeedChk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(locationChk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(2, 2, 2)
                                            .addComponent(weatherIconChk, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, 0)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(weatherDescriptionChk, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(forecastChk, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .addComponent(fahrenheitRadio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 23, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(locationLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(locField)
                        .addComponent(testBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weatherIconChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weatherDescriptionChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(humidityChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(windSpeedChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(forecastChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(temperatureChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(centrigradeRadio)
                    .addComponent(fahrenheitRadio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backgroundLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backgroundClr)
                    .addComponent(textClr)
                    .addComponent(rectangularChk, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void testBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testBtnActionPerformed
        test();
    }//GEN-LAST:event_testBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressField;
    private javax.swing.JLabel backgroundClr;
    private javax.swing.JLabel backgroundLbl;
    private javax.swing.JRadioButton centrigradeRadio;
    private javax.swing.JRadioButton fahrenheitRadio;
    private javax.swing.JCheckBox forecastChk;
    private javax.swing.JCheckBox humidityChk;
    private javax.swing.JTextField locField;
    private javax.swing.JCheckBox locationChk;
    private javax.swing.JLabel locationLbl;
    private javax.swing.JCheckBox rectangularChk;
    private javax.swing.JCheckBox temperatureChk;
    private javax.swing.ButtonGroup temperatureUnit;
    private javax.swing.JButton testBtn;
    private javax.swing.JLabel textClr;
    private javax.swing.JLabel textLbl;
    private javax.swing.JCheckBox weatherDescriptionChk;
    private javax.swing.JCheckBox weatherIconChk;
    private javax.swing.JCheckBox windSpeedChk;
    // End of variables declaration//GEN-END:variables

    private GeocodeResult info_geocodeResult;
    private Tester tester;
    private java.util.function.Consumer<GeocodeResult> consumer;

    @Override
    void reset() {
        locField.setText("");
        addressField.setText("");
        locationChk.setSelected(false);
        weatherIconChk.setSelected(false);
        weatherDescriptionChk.setSelected(false);
        humidityChk.setSelected(false);
        windSpeedChk.setSelected(false);
        forecastChk.setSelected(false);
        temperatureChk.setSelected(false);
        fahrenheitRadio.setSelected(true);
        rectangularChk.setSelected(false);
        textClr.setBackground(Color.BLACK);
        backgroundClr.setBackground(Color.GRAY);
    }

    @Override
    void resetFromEmbedType(EmbedType embedType) {
        if (embedType.getClass() == EmbedTypeWeatherInfo.class) {
            EmbedTypeWeatherInfo etwi = (EmbedTypeWeatherInfo) embedType;
            locField.setText(etwi.getLocation());
            addressField.setText(etwi.getGeocodeResult().formattedAddress);
            info_geocodeResult = etwi.getGeocodeResult();
            locationChk.setSelected(etwi.isLocationIncluded());
            weatherIconChk.setSelected(etwi.isIconIncluded());
            weatherDescriptionChk.setSelected(etwi.isDescriptionIncluded());
            humidityChk.setSelected(etwi.isHumidityIncluded());
            windSpeedChk.setSelected(etwi.isWindSpeedIncluded());
            forecastChk.setSelected(etwi.isForecastIncluded());
            temperatureChk.setSelected(etwi.isTemperatureIncluded());
            fahrenheitRadio.setSelected(etwi.isIsFahrennheit());
            rectangularChk.setSelected(etwi.isIsRectangular());
            textClr.setBackground(etwi.getTextColor());
            backgroundClr.setBackground(etwi.getBackgroundColor());
        }
    }

    @Override
    void loadIntoEmbedType(EmbedType embedType) {
        if (embedType.getClass() == EmbedTypeWeatherInfo.class) {
            EmbedTypeWeatherInfo etwi = (EmbedTypeWeatherInfo) embedType;
            etwi.setLocation(locField.getText());
            etwi.setLocationIncluded(locationChk.isSelected());
            etwi.setIconIncluded(weatherIconChk.isSelected());
            etwi.setDescriptionIncluded(weatherDescriptionChk.isSelected());
            etwi.setHumidityIncluded(humidityChk.isSelected());
            etwi.setWindSpeedIncluded(windSpeedChk.isSelected());
            etwi.setForecastIncluded(forecastChk.isSelected());
            etwi.setTemperatureIncluded(temperatureChk.isSelected());
            etwi.setIsRectangular(rectangularChk.isSelected());
            etwi.setIsFahrennheit(fahrenheitRadio.isSelected());
            etwi.setTextColor(textClr.getBackground());
            etwi.setBackgroundColor(backgroundClr.getBackground());
            etwi.setGeocodeResult(info_geocodeResult);
        }
    }

    @Override
    EmbedType.Name getEmbedTypeName() {
        return EmbedType.Name.WeatherInfo;
    }

    private class Tester implements Runnable {

        Tester() {
            new Thread(this).start();
        }

        private transient boolean test = false;
        private transient boolean stop = false;

        private String query = "";
        private GeocodeResult geocodeResult;
        private java.util.function.Consumer<GeocodeResult> consumer;

        public void test(String query, java.util.function.Consumer<GeocodeResult> consumer) {
            this.query = query;
            this.consumer = consumer;
            test = true;
            synchronized (this) {
                notify();
            }
        }

        @Override
        public void run() {
            while (!stop) {
                synchronized (this) {
                    try {
                        while (!test) {
                            wait();
                        }
                        GeocodeResult[] results = GeocodeResults.getGeocodeResults(query).results;
                        if (results != null && results.length > 0) {
                            geocodeResult = results[0];
                        } else {
                            geocodeResult = null;
                        }
                        consumer.accept(geocodeResult);
                        test = false;
                    } catch (Exception ex) {
                        Logger.getLogger(WeatherInfoCard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
