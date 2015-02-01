package com.lappdance.grtrealtime.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Stop {
    @SerializedName("StopId")
    private int mId;

    @SerializedName("Name")
    private String mName;

    @SerializedName("Latitude")
    private double mLatitude;

    @SerializedName("Longitude")
    private double mLongitude;

    public Stop() {
    }

    public LatLng getLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%d %s", mId, mName);
    }
}
