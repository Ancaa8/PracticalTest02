package ro.pub.cs.systems.eim.practicaltest02;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

public class OpenWeatherService {

    public static WeatherForecastInformation getWeather(String city) throws Exception {

        String url = Uri.parse(Constants.OPENWEATHER_URL).buildUpon()
                .appendQueryParameter("q", city)
                .appendQueryParameter("appid", Constants.API_KEY)
                .build().toString();

        String jsonStr = Utilities.httpGet(url);

        JSONObject root = new JSONObject(jsonStr);

        //-----------------------------------------------------------

        JSONObject main = root.optJSONObject("main");
        String temperature = null;
        String pressure = null;
        String humidity = null;

        if(main != null) {
            temperature = String.valueOf(main.optDouble("temp"));
            pressure = String.valueOf(main.optDouble("pressure"));
            humidity = String.valueOf(main.optDouble("humidity"));
        }

        //-----------------------------------------------------------

        JSONObject wind = root.optJSONObject("wind");
        String windSpeed = null;

        if (wind != null) {
            windSpeed = String.valueOf(wind.optDouble("speed"));
        }

        //-----------------------------------------------------------

        JSONArray weatherArray = root.optJSONArray("weather");
        JSONObject weather = null;
        String description = null;

        if (weatherArray != null && weatherArray.length() > 0) {
            weather = weatherArray.optJSONObject(0);
        }
        if (weather != null) {
            description = weather.optString("description");
        }

        //-----------------------------------------------------------

        return new WeatherForecastInformation(temperature, windSpeed, description, pressure, humidity);
    }
}
