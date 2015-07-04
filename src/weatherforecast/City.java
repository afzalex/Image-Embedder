package weatherforecast;

import org.json.JSONObject;

public class City {

        public static class GeoCoord {

            private static final String JSON_LAT = "lat";
            private static final String JSON_LON = "lon";

            private float latitude;
            private float longitude;

            GeoCoord(JSONObject json) {
                this.latitude = (float) json.optDouble(GeoCoord.JSON_LAT);
                this.longitude = (float) json.optDouble(GeoCoord.JSON_LON);
            }

            public boolean hasLatitude() {
                return this.latitude != Float.NaN;
            }

            public float getLatitude() {
                return latitude;
            }

            public boolean hasLongitude() {
                return this.longitude != Float.NaN;
            }

            public float getLongitude() {
                return longitude;
            }
        }

        static private final String JSON_ID = "id";
        static private final String JSON_COORD = "coord";
        static private final String JSON_COUNTRY = "country";
        static private final String JSON_NAME = "name";
        static private final String JSON_DT_CALC = "dt_calc";
        static private final String JSON_STATIONS_COUNT = "stations_count";

        private final int id;
        private final GeoCoord coord;
        private final String country;
        private final String name;
        private final long dtCalc;
        private final int stationsCount;

        public City(JSONObject json) {
            this.id = json.optInt(City.JSON_ID, Integer.MIN_VALUE);
            JSONObject jsonCoord = json.optJSONObject(City.JSON_COORD);
            if (jsonCoord != null) {
                this.coord = new GeoCoord(jsonCoord);
            } else {
                this.coord = null;
            }
            this.country = json.optString(City.JSON_COUNTRY);
            this.name = json.optString(City.JSON_NAME);
            this.dtCalc = json.optLong(City.JSON_DT_CALC, Long.MIN_VALUE);
            this.stationsCount = json.optInt(City.JSON_STATIONS_COUNT, Integer.MIN_VALUE);
        }

        public boolean hasId() {
            return this.id != Integer.MIN_VALUE;
        }

        public int getId() {
            return this.id;
        }

        public boolean hasCoordinates() {
            return this.coord != null;
        }

        public GeoCoord getCoordinates() {
            return this.coord;
        }

        public boolean hasCountryCode() {
            return this.country != null;
        }

        public String getCountryCode() {
            return this.country;
        }

        public boolean hasName() {
            return this.name != null;
        }

        public String getName() {
            return this.name;
        }

        public boolean hasDateTimeCalc() {
            return this.dtCalc != Long.MIN_VALUE;
        }

        public long getDateTimeCalc() {
            return this.dtCalc;
        }

        public boolean hasStationsCount() {
            return this.stationsCount != Integer.MIN_VALUE;
        }

        public int getStationsCount() {
            return this.stationsCount;
        }
    }
