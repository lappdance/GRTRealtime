package com.lappdance.grtrealtime.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.lappdance.grtrealtime.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Stop {
    private static transient Map<Integer, Bitmap> sIcons = new HashMap<>();

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

    public static Bitmap getIcon(Resources res, int color) {
        Bitmap bmp = sIcons.get(color);
        if(bmp != null) {
            return bmp;
        }

        Bitmap ob = BitmapFactory.decodeResource(res, R.drawable.ic_map_stop);
        Bitmap obm = Bitmap.createBitmap(ob.getWidth(), ob.getHeight(), ob.getConfig());
        Canvas canvas = new Canvas(obm);

        //draw the bitmap once to adjust the RGB values.
        Paint rgbChannel = new Paint();
        rgbChannel.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(ob, 0f, 0f, rgbChannel);

        //draw the bitmap again to reset the alpha values to
        //match the original bitmap's.
        Paint alphaChannel = new Paint();
        alphaChannel.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(ob, 0, 0, alphaChannel);

        sIcons.put(color, obm);
        return obm;
    }
}
