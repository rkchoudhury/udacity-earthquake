package com.example.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private static final String LOG_TAG = "EarthquakeLoader";
    String mUrl;

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
        Log.d(LOG_TAG, "EarthquakeLoader: Constructor");
    }

    @Nullable
    @Override
    public ArrayList<Earthquake> loadInBackground() {
        Log.d(LOG_TAG, "EarthquakeLoader: loadInBackground");
        if (mUrl == null) {
            return null;
        }
        ArrayList<Earthquake> earthquakeList = QueryUtils.getEarthquakeData(mUrl);
        return earthquakeList;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, "EarthquakeLoader: onStartLoading");
        forceLoad();
    }

}
