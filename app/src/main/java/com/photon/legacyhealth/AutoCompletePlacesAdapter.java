package com.photon.legacyhealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.photon.legacyhealth.pojo.NeighborhoodListDataResponseData;

import java.util.ArrayList;
import java.util.List;

public class AutoCompletePlacesAdapter extends ArrayAdapter<NeighborhoodListDataResponseData> implements Filterable{
    final List<NeighborhoodListDataResponseData> places;
    List<NeighborhoodListDataResponseData> filteredPlaces = new ArrayList<>();

    public AutoCompletePlacesAdapter(Context context, List<NeighborhoodListDataResponseData> dogs) {
        super(context, 0, dogs);
        this.places = dogs;
    }

    @Override
    public int getCount() {
        return filteredPlaces.size();
    }

    @Override
    public Filter getFilter() {
        return new PlacesFilter(this, places);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        NeighborhoodListDataResponseData place = filteredPlaces.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.places_suggestion_item, parent, false);

        TextView placeName = (TextView) convertView.findViewById(R.id.place);
        TextView stateName = convertView.findViewById(R.id.place_state);
        placeName.setText(place.getNeighborhoodName());
        stateName.setText(new StringBuilder().append(place.getCity()).append(", ").append(place.getState()).toString());

        return convertView;
    }
}
