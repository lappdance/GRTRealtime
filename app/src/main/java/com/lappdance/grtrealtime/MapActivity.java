package com.lappdance.grtrealtime;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.lappdance.grtrealtime.model.Route;
import com.lappdance.grtrealtime.model.Stop;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends FragmentActivity {
    private static final String LOG_TAG = "MapActivity";

    /**
     * The lat/long coordinates for the transit hub at King & Victoria.
     * If the user hasn't enabled location services for this app, we'll use
     * these coordinates as the "current location".
     */
    private static final LatLng VICTORIA_TRANSIT_HUB = new LatLng(43.452846, -80.498223);

    private static final String URL_ALL_ROUTES = "http://realtimemap.grt.ca/Map/GetRoutes/";
    private static final String URL_STOPS_FOR_ROUTE = "http://realtimemap.grt.ca/Stop/GetByRouteId?routeId=%d";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager mLocationManager;
    private RequestQueue mRequestQueue;
    protected Map<Integer, Route> mRoutes = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();

        if (mRequestQueue == null) {
            setRequestQueue(newRequestQueue());
        }

        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        centerMapOnLastLocation();
        mLocationManager.requestSingleUpdate(getLocationProviderCriteria(), new UpdateMapLocationListener(), Looper.getMainLooper());

        fetchAllRoutes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }

    private RequestQueue newRequestQueue() {
        return Volley.newRequestQueue(getApplicationContext());
    }

    void setRequestQueue(RequestQueue queue) {
        mRequestQueue = queue;
    }

    Criteria getLocationProviderCriteria() {
        Criteria providerCriteria = new Criteria();
        providerCriteria.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
        providerCriteria.setCostAllowed(false);
        providerCriteria.setBearingRequired(false);
        providerCriteria.setAltitudeRequired(false);
        providerCriteria.setSpeedRequired(false);

        return providerCriteria;
    }

    /**
     * @return The user's last known location.
     * If we can't get a fix on the user's location, we'll treat the
     * King / Victoria transit hub as their last location.
     */
    private LatLng getLastLocation() {
        Location userLocation = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(getLocationProviderCriteria(), true));
        if(userLocation != null) {
            return new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        }

        return VICTORIA_TRANSIT_HUB;
    }

    /**
     * Centers the map on the user's last known location.
     */
    private void centerMapOnLastLocation() {
        if(mMap != null) {
            LatLng location = getLastLocation();
            centerMap(location);
        }
    }

    private void centerMap(LatLng coords) {
        if(mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(coords.latitude, coords.longitude), 16));
        }
    }

    public void fetchAllRoutes() {
        mRequestQueue.add(newFetchRoutesRequest());
    }

    Request<?> newFetchRoutesRequest() {
        return new StringRequest(Request.Method.GET, URL_ALL_ROUTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mRoutes.clear();
                            Document doc = Jsoup.parse(response);

                            for(Element li : doc.select("li")) {
                                int id = Integer.parseInt(li.attr("data-value"));
                                int color = Integer.parseInt(li.attr("color-scheme"), 16);

                                Elements spans = li.select("span");
                                String name = spans.last().text();

                                Route r = new Route(id, name, color);
                                mRoutes.put(id, r);
                            }

                        } catch(RuntimeException ex) {
                            Log.e(LOG_TAG, "failed to parse XML", ex);
                            throw ex;
                        }

                        onRoutesFetched();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "failed to get routes", error);
                    }
                }
        );
    }

    protected void onRoutesFetched() {
        fetchAllStops();
    }

    public void fetchAllStops() {
        for(Route route : mRoutes.values()) {
            mRequestQueue.add(newFetchAllStopsRequest(route));
        }
    }

    Request<?> newFetchAllStopsRequest(final Route route) {
        return new GsonRequest<>(String.format(URL_STOPS_FOR_ROUTE, route.getId()),
                new TypeToken<List<Stop>>() {}.getType(), null,
                new Response.Listener<List<Stop>>() {
                    @Override
                    public void onResponse(List<Stop> response) {
                        for(Stop stop : response) {
                            if(mMap != null) {
                                mMap.addMarker(new MarkerOptions()
                                                .position(stop.getLatLng())
                                                .anchor(0.5f, 0.5f)
                                                .icon(BitmapDescriptorFactory.fromBitmap(Stop.getIcon(getResources(), route.getColor())))
                                );
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "failed to get stops", error);
                    }
                }
        );
    }

    class UpdateMapLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng coords = new LatLng(location.getLatitude(), location.getLongitude());
            centerMap(coords);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
