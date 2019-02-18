package com.example.ambit.mytrafficcontrolbee;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterLocation extends ArrayAdapter<ModelClassLocation> {


    private Activity context;
    private ArrayList<ModelClassLocation> modelClassLocationArrayList;

    public CustomAdapterLocation(Activity context,ArrayList<ModelClassLocation> modelClassLocationArrayList) {
        super(context, R.layout.sample_location, modelClassLocationArrayList);
        this.context = context;
        this.modelClassLocationArrayList = modelClassLocationArrayList;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.sample_location, null, false);

        ModelClassLocation modelLocation = modelClassLocationArrayList.get(position);

        TextView location = view.findViewById(R.id.LocationSampleTextView);

        location.setText(modelLocation.getLocation());


        return view;
    }
}
