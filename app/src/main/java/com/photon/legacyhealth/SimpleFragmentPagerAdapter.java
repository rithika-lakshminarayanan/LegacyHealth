package com.photon.legacyhealth;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.photon.legacyhealth.pojo.MetaData;
import com.photon.legacyhealth.pojo.NeighborhoodListDataResponseData;

import java.util.ArrayList;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private double latitude;
    private double longitude;
    private boolean isLocationSupported;
    private boolean dialogBoxOk;
    private ArrayList<Feeling> fl, doingList, Indoor, Outdoor;
    private ArrayList<FeelingFeedback> ffList, dfList;
    private ArrayList<String> tipImgList, tipSymName;
    private ArrayList<MetaData> annotations;
    private ArrayList<NeighborhoodListDataResponseData> allNeighborhoodDetails;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context mContext, double latitude, double longitude, boolean isLocationSupported, boolean dialogBoxOk, ArrayList<Feeling> fl, ArrayList<Feeling> doingList, ArrayList<Feeling> indoor, ArrayList<Feeling> outdoor, ArrayList<FeelingFeedback> ffList, ArrayList<FeelingFeedback> dfList, ArrayList<String> tipImgList, ArrayList<String> tipSymName, ArrayList<MetaData> annotations, ArrayList<NeighborhoodListDataResponseData> allNeighborhoodDetails) {
        super(fm);
        this.mContext = mContext;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLocationSupported = isLocationSupported;
        this.dialogBoxOk = dialogBoxOk;
        this.fl = fl;
        this.doingList = doingList;
        Indoor = indoor;
        Outdoor = outdoor;
        this.ffList = ffList;
        this.dfList = dfList;
        this.tipImgList = tipImgList;
        this.tipSymName = tipSymName;
        this.annotations = annotations;
        this.allNeighborhoodDetails = allNeighborhoodDetails;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FeelingMapFragment.newInstance(latitude,longitude,isLocationSupported,dialogBoxOk, fl, ffList, tipImgList, tipSymName, allNeighborhoodDetails);
        } else
            return DoingMapFragment.newInstance(latitude,longitude, doingList, Indoor, Outdoor, dfList, annotations);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    /* This determines the title for each tab */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab1_value);
            case 1:
                return mContext.getString(R.string.tab2_value);
            default:
                return null;
        }
    }
}


