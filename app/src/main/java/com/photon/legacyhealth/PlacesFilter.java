package com.photon.legacyhealth;

import android.widget.Filter;

import com.photon.legacyhealth.pojo.NeighborhoodListDataResponseData;

import java.util.ArrayList;
import java.util.List;

class PlacesFilter extends Filter {
    AutoCompletePlacesAdapter adapter;
    List<NeighborhoodListDataResponseData> originalList;
    List<NeighborhoodListDataResponseData> filteredList;
    public PlacesFilter(AutoCompletePlacesAdapter autoCompletePlacesAdapter, List<NeighborhoodListDataResponseData> places) {
        super();
        this.adapter = autoCompletePlacesAdapter;
        this.originalList = places;
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
            for (final NeighborhoodListDataResponseData place : originalList) {
                if (place.getNeighborhoodName().toLowerCase().startsWith(filterPattern)) {
                    filteredList.add(place);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredPlaces.clear();
        adapter.filteredPlaces.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}
