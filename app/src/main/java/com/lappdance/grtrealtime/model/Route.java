package com.lappdance.grtrealtime.model;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

public class Route implements Comparable<Route> {
    @SerializedName("RouteId")
    int mId;

    @SerializedName("ShortName")
    String mShortName;

    @SerializedName("Color")
    String mColorValue;

    @SerializedName("RouteNameHtml")
    String mRouteName;

    @SerializedName("OrderBy")
    String mSortKey;

    public int getId() {
        return mId;
    }

    public String getShortName() {
        return mShortName;
    }

    public int getColor() {
        return Color.parseColor("#" + mColorValue);
    }

    public String getDisplayName() {
        return mRouteName;
    }

    @Override
    public int compareTo(Route another) {
        return mSortKey.compareTo(another.mSortKey);
    }
}
