package com.example.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakeList) {
        super(context, 0, earthquakeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magnitudeView = convertView.findViewById(R.id.list_item_magnitude);
        String formattedMagnitude = formatMagnitude(currentEarthquake.getmMagnitude());
        magnitudeView.setText(formattedMagnitude);

        // Get the circle
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        TextView subLocationView = convertView.findViewById(R.id.list_item_sub_location);
        TextView locationView = convertView.findViewById(R.id.list_item_location);

        setLocation(currentEarthquake.getmLocation(), subLocationView, locationView);

        TextView dateView = convertView.findViewById(R.id.list_item_date);
        dateView.setText(currentEarthquake.getmDate());

        return convertView;
    }

    void setLocation(String location, TextView subLocationView, TextView locationView) {
        if (location.contains(",")) {
            int index = location.indexOf(",");
            String sub1 = location.substring(0, index).toUpperCase();
            String sub2 = location.substring(index + 1);
            subLocationView.setText(sub1);
            locationView.setText(sub2);
        } else {
            locationView.setText(location);
        }
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int colorId;
        int value = (int) Math.floor(magnitude);

        switch (value) {
            case 0:
            case 1:
                colorId = R.color.magnitude1;
                break;
            case 2:
                colorId = R.color.magnitude2;
                break;
            case 3:
                colorId = R.color.magnitude3;
                break;
            case 4:
                colorId = R.color.magnitude4;
                break;
            case 5:
                colorId = R.color.magnitude5;
                break;
            case 6:
                colorId = R.color.magnitude6;
                break;
            case 7:
                colorId = R.color.magnitude7;
                break;
            case 8:
                colorId = R.color.magnitude8;
                break;
            case 9:
                colorId = R.color.magnitude9;
                break;
            default:
                colorId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), colorId);  // ContextCompatibility:: Need to convert the colorId
    }
}
