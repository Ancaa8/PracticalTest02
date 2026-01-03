package ro.pub.cs.systems.eim.practicaltest02;

public class WeatherForecastInformation {


        private String temperature;
        private String windSpeed;
        private String description;
        private String pressure;
        private String humidity;

        public WeatherForecastInformation() {}

        public WeatherForecastInformation(String temperature, String windSpeed, String description,
                                          String pressure, String humidity) {
            this.temperature = temperature;
            this.windSpeed = windSpeed;
            this.description= description;
            this.pressure = pressure;
            this.humidity = humidity;
        }

        public String getTemperature() { return temperature; }
        public void setTemperature(String temperature) { this.temperature = temperature; }

        public String getWindSpeed() { return windSpeed; }
        public void setWindSpeed(String windSpeed) { this.windSpeed = windSpeed; }

        public String getDescription () { return description; }
        public void setDescription (String description) { this.description = description; }

        public String getPressure() { return pressure; }
        public void setPressure(String pressure) { this.pressure = pressure; }

        public String getHumidity() { return humidity; }
        public void setHumidity(String humidity) { this.humidity = humidity; }

        @Override
        public String toString() {
            return "temperature: " + temperature + "\n" +
                    "wind_speed: " + windSpeed + "\n" +
                    "description: " + description + "\n" +
                    "pressure: " + pressure + "\n" +
                    "humidity: " + humidity;
        }
}
