package com.example.otavio.sunshine_myversion;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        TextView dataTV = (TextView) rootView.findViewById(R.id.dataTV);
        TextView minTV = (TextView) rootView.findViewById(R.id.minTV);
        TextView maxTV = (TextView) rootView.findViewById(R.id.maxTV);

        Intent intent = getActivity().getIntent();
        String date = intent.getStringExtra("date");
        String min = intent.getStringExtra("min");
        String max = intent.getStringExtra("max");

        dataTV.setText(date);
        minTV.setText(min);
        maxTV.setText(max);

        return rootView;
    }
}
