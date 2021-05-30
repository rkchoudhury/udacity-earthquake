package com.example.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryUtils {

    public static ArrayList<Earthquake> getEarthquakeData(String requestUrl) {
        URL url = Utils.createURL(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = Utils.makeHttpRequest(url);
        } catch (IOException e) {

        }

        ArrayList<Earthquake> earthquakes = extractEarthquakes(jsonResponse);
        return earthquakes;
    }

    public static ArrayList<Earthquake> extractEarthquakes(String response) {

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(response);
            JSONArray earthquakeList = rootObject.getJSONArray("features");

            for (int i = 0; i < earthquakeList.length(); i++) {
                JSONObject currentEarthquake = earthquakeList.getJSONObject(i);
                JSONObject currentProperties = currentEarthquake.getJSONObject("properties");

                Double currentMagnitude = currentProperties.getDouble("mag");
                String currentPlace = currentProperties.getString("place");

                long timeInMilliSecond = currentProperties.getLong("time");
                SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
                String currentDate = formatter.format(timeInMilliSecond);

                earthquakes.add(new Earthquake(currentMagnitude, currentPlace, currentDate));
            }

            Log.d("QueryUtils", "rkkkkk" + earthquakes.toString());
        } catch (JSONException e) {
            Log.e("QueryUtils", "rkkkkk Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
