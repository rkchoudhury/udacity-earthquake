package com.example.quakereport;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2019-01-01&endtime=2019-12-01&minmagnitude=6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    private void updateUI(ArrayList<Earthquake> earthquakes) {
        ListView earthquakeListView = findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EarthquakeActivity.this, EarthquakeDetailActivity.class);

                Earthquake currentEarthquake = earthquakes.get(position);
                intent.putExtra("title", currentEarthquake.getmLocation());
                intent.putExtra("time", currentEarthquake.getmDate());
                intent.putExtra("tsunamiAlert", currentEarthquake.getmMagnitude());

                // TODO: How to pass the object to the next screen instead of above value
//                Event earthquakeDetails = new Event(currentEarthquake.getmLocation(), currentEarthquake.getmDate(), currentEarthquake.getmMagnitude());
//                intent.putExtra("earthquakeDetails", earthquakeDetails);

                startActivity(intent);
            }
        });
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>>{

        @Override
        protected ArrayList<Earthquake> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            ArrayList<Earthquake>  earthquakeList = QueryUtils.getEarthquakeData(strings[0]);
            return earthquakeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            if (earthquakes == null) {
                return;
            }
            updateUI(earthquakes);
        }
    }
}