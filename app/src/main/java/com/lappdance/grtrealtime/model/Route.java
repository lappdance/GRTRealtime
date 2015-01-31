package com.lappdance.grtrealtime.model;

public class Route implements Comparable<Route> {
    private int mId;
    private String mName;
    private int mColor;

    public Route() {
    }

    public Route(int id, String name, int color) {
        mId = id;
        mName = name;
        mColor = color;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int compareTo(Route other) {
        return mId - other.mId;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", mId, mName);
    }
}
