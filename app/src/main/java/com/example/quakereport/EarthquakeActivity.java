package com.example.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        ListView earthquakeListView = findViewById(R.id.list);
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EarthquakeActivity.this, EarthquakeDetailActivity.class);

                Earthquake currentEarthquake = (Earthquake) parent.getItemAtPosition(position);
                intent.putExtra("title", currentEarthquake.getmLocation());
                intent.putExtra("time", currentEarthquake.getmDate());
                intent.putExtra("tsunamiAlert", currentEarthquake.getmMagnitude());

                // TODO: How to pass the object to the next screen instead of above value
//                Event earthquakeDetails = new Event(currentEarthquake.getmLocation(), currentEarthquake.getmDate(), currentEarthquake.getmMagnitude());
//                intent.putExtra("earthquakeDetails", earthquakeDetails);

                startActivity(intent);
            }
        });

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
        this.checkInternetConnectivity();
    }

    private void updateUI(ArrayList<Earthquake> earthquakes) {

        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {

        @Override
        protected ArrayList<Earthquake> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            ArrayList<Earthquake> earthquakeList = QueryUtils.getEarthquakeData(strings[0]);
            return earthquakeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            ProgressBar progressBar = findViewById(R.id.progress_circular);
            progressBar.setVisibility(View.GONE);

            if (earthquakes == null || earthquakes.isEmpty()) {
                ListView earthquakeListView = findViewById(R.id.list);
                earthquakeListView.setEmptyView(findViewById(R.id.list_empty_view));
                return;
            }
            updateUI(earthquakes);
        }
    }

    private void checkInternetConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            TextView emptyTextView = findViewById(R.id.list_empty_view);
            emptyTextView.setText("No internet connection");
            emptyTextView.setVisibility(View.VISIBLE);
        }
        Log.d("rkkk", "onCreate: isConnected ::" + isConnected);
    }
}