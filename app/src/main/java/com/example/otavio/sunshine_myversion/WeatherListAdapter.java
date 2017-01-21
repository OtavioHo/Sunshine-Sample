package com.example.otavio.sunshine_myversion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by otavio on 24/08/16.
 */
public class WeatherListAdapter extends ArrayAdapter<WeatherElement>{

    private Context context;
    private ArrayList<WeatherElement> list;

    public WeatherListAdapter(Context context, ArrayList<WeatherElement> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherElement element = this.list.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_forecast_item, null);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
        TextView minTextView = (TextView) convertView.findViewById(R.id.minTextView);
        TextView maxTextView = (TextView) convertView.findViewById(R.id.maxTextView);

        String min = element.getMin() + "ºC";
        String max = element.getMax() + "ºC";

        dateTextView.setText(element.getDate());
        minTextView.setText(min);
        maxTextView.setText(max);

        return convertView;
    }
}
