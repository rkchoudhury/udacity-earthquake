package com.example.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static Event fetchEarthquakeData(String requestUrl) {
        URL url = Utils.createURL(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = Utils.makeHttpRequest(url);
        } catch (IOException e) {

        }

        Event event = Utils.extractInfromation(jsonResponse);
        Log.d("rkkk Details", event.toString());
        return event;
    }

    public static URL createURL(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.d("MakeHttpRequest Error: ", e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return String.valueOf(output);
    }

    public static Event extractInfromation(String data) {

        ArrayList<Event> earthquakes = new ArrayList<>();

        Log.d("QueryUtils", "rkkkkk" + data);

        try {
            JSONObject rootObject = new JSONObject(data);


            JSONArray earthquakeList = rootObject.getJSONArray("features");

            for (int i = 0; i < earthquakeList.length(); i++) {
                JSONObject currentEarthquake = earthquakeList.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                String currentTitle = properties.getString("title");
                long timeInMilliSecond = properties.getLong("time");
                Double currentMagnitude = properties.getDouble("mag");

                Date date = new Date(timeInMilliSecond);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
                String currentDate = dateFormatter.format(date);

                earthquakes.add(new Event(currentTitle, currentDate, currentMagnitude));
            }

            Log.d(LOG_TAG, earthquakes.toString());
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        if (earthquakes.isEmpty()) {
            return new Event("", "", 0.0);
        }
        return earthquakes.get(0);
    }
}
