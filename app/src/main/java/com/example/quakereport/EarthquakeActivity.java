package com.example.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    private static final String LOG_TAG = "EarthquakeActivity";
//    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private static final int EARTHQUAKE_LOADER_ID = 1;

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

        this.checkInternetConnectivity();

        // With LoaderManager
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    private void updateUI(ArrayList<Earthquake> earthquakes) {

        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    // With LoaderManager: Fetching response from API and updating the list
    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader: ");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {
        Log.d(LOG_TAG, "onLoadFinished: ");
        ProgressBar progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.GONE);

        if (earthquakes == null || earthquakes.isEmpty()) {
            ListView earthquakeListView = findViewById(R.id.list);
            earthquakeListView.setEmptyView(findViewById(R.id.list_empty_view));
            return;
        }
        updateUI(earthquakes);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        Log.d(LOG_TAG, "onLoadFinished: ");
        mAdapter.clear();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}