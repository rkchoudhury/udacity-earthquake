package com.example.quakereport;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        EarthquakeAdapter adapter = new EarthquakeAdapter(this, QueryUtils.extractEarthquakes());

        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("https://stackoverflow.com/questions/43980740/open-url-in-intent/43981160"));

                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }

//                try {
//                    Uri webpage = Uri.parse("https://stackoverflow.com/questions/43980740/open-url-in-intent/43981160");
//                    Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
//                    startActivity(myIntent);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(EarthquakeActivity.this, "No application can handle this request. Please install a web browser or check your URL.",  Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
            }
        });

    }
}