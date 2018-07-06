package com.photon.legacyhealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteActivitiesAdapter extends ArrayAdapter<String> implements Filterable{
    final List<String> activities;
    List<String> filteredActivities;

    public AutoCompleteActivitiesAdapter(Context context, List<String> activities) {
        super(context, 0, activities);
        this.activities = activities;
        this.filteredActivities = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filteredActivities.size();
    }

    @Override
    public Filter getFilter() {
        return new ActivitiesFilter(this, activities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        String activity = filteredActivities.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.activity_suggestion_item, parent, false);

        TextView activityName = (TextView) convertView.findViewById(R.id.activity_name);
        activityName.setText(activity);

        return convertView;
    }
}
