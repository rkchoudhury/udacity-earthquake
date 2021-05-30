package com.example.quakereport;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URL;

public class EarthquakeDetailActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2012-12-01&minmagnitude=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    private void updateUI(Event earthquake) {
        Log.d("rkkk Details", earthquake.toString());
        Log.d("rkkk Details", earthquake.title);
        Log.d("rkkk Details", earthquake.time);
        Log.d("rkkk Details", String.valueOf(earthquake.tsunamiAlert));

        TextView titleView = findViewById(R.id.title);
        titleView.setText(earthquake.title);

        TextView timeView = findViewById(R.id.date);
        timeView.setText(earthquake.time);

        TextView alertView = findViewById(R.id.tsunami_alert);
        alertView.setText(String.valueOf(earthquake.tsunamiAlert));
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, Event> {

        @Override
        protected Event doInBackground(String... stringUrls) {
            if (stringUrls.length < 1 || stringUrls[0] == null) {
                return null;
            }

            Event event = Utils.fetchEarthquakeData(stringUrls[0]);
            return event;
        }

        @Override
        protected void onPostExecute(Event result) {
            if (result == null) {
                return;
            }
            updateUI(result);
        }
    }
}