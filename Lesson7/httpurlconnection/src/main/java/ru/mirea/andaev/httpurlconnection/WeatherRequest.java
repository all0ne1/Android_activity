package ru.mirea.andaev.httpurlconnection;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import ru.mirea.andaev.httpurlconnection.databinding.ActivityMainBinding;

class WeatherRequest extends HttpRequest {
    ActivityMainBinding binding;
    public WeatherRequest(String address, ActivityMainBinding binding) {
        super(address);
        this.binding = binding;
    }
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject responseJson = new JSONObject(result);
            JSONObject currentWeather = responseJson.getJSONObject("current_weather");
            binding.temperatureView.setText(String.format(Locale.getDefault(),"Temperature: %.2f°C", currentWeather.getDouble("temperature")));
            binding.windView.setText(String.format(Locale.getDefault(),"Wind Speed: %.2f km/h", currentWeather.getDouble("windspeed")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
