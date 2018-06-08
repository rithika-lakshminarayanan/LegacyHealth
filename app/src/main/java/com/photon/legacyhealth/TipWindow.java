package com.photon.legacyhealth;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class TipWindow implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public TipWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.tip_window, null);

        TextView symptomName = view.findViewById(R.id.sym_name);
        ImageView img = view.findViewById(R.id.symptom_img);

        TextView tipHeading = view.findViewById(R.id.tip_heading);
        TextView tipBody = view.findViewById(R.id.tip_body);



        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        GlideApp.with(view).load(infoWindowData.getImgUrl()).fitCenter().placeholder(R.drawable.allergies).into(img);
        symptomName.setText(infoWindowData.getSymptomName());
        tipHeading.setText(infoWindowData.getTipHeading());
        tipBody.setText(infoWindowData.getTipBody());

        return view;
    }
}
