package ru.mirea.andaev.httpurlconnection;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import ru.mirea.andaev.httpurlconnection.databinding.ActivityMainBinding;

class IpInfoRequest extends HttpRequest{
    public static String[] location;
    ActivityMainBinding binding;

    public IpInfoRequest(String address, ActivityMainBinding binding) {
        super(address);
        this.binding = binding;
    }
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        try {
            JSONObject responseJson = new JSONObject(result);
            binding.ipView.setText(responseJson.getString("ip"));
            binding.countryView.setText(responseJson.getString("country"));
            binding.cityView.setText(responseJson.getString("city"));
            location = responseJson.getString("loc").split(",");
            Log.d("Loc", Arrays.toString(location));
            new WeatherRequest(String.format(Locale.getDefault(),
                    "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                    location[0],location[1]),this.binding).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
