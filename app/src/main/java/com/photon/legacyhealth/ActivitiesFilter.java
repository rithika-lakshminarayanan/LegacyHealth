package com.photon.legacyhealth;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

class ActivitiesFilter extends Filter {
    AutoCompleteActivitiesAdapter adapter;
    List<String> originalList;
    List<String> filteredList;
    public ActivitiesFilter(AutoCompleteActivitiesAdapter autoCompleteActivitiesAdapter, List<String> activities) {
        super();
        this.adapter =autoCompleteActivitiesAdapter;
        this.originalList = activities;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final String activity : originalList) {
                if (activity.toLowerCase().contains(filterPattern)) {
                    filteredList.add(activity);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredActivities.clear();
        adapter.filteredActivities.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}
