package com.lappdance.grtrealtime.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lappdance.grtrealtime.model.Route;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets all GRT routes, regardless of if they are in service at this time or not.
 */
public class FetchAllRoutesStrategy implements FetchRoutesStrategy {
    private static final String LOG_TAG = "FetchInServiceRoutesStrategy";
    private static final String URL_ALL_ROUTES = "http://www.regionofwaterloo.ca/opendatadownloads/routes.csv";

    private RequestQueue mRequestQueue;

    public FetchAllRoutesStrategy(RequestQueue queue) {
        mRequestQueue = queue;
    }

    @Override
    public void fetchRoutes(OnRoutesFetchedListener listener) {
        mRequestQueue.add(newFetchRoutesRequest(listener));
    }

    Request<?> newFetchRoutesRequest(final OnRoutesFetchedListener listener) {
        return new StringRequest(Request.Method.GET, URL_ALL_ROUTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Map<Integer, Route> routes = new HashMap<>();
                            String[] lines = response.split("\n");
                            for(String line : lines) {
                                try {
                                    String[] parts = line.split(",");

                                    String name = parts[0];
                                    int id = Integer.parseInt(parts[5]);
                                    Route r = new Route(id, name, 0);
                                    routes.put(id, r);

                                } catch(NumberFormatException ex) {
                                    Log.w(LOG_TAG, "skipping merged route", ex);
                                }
                            }

                            listener.onRoutesFetched(routes);

                        } catch(RuntimeException ex) {
                            Log.e(LOG_TAG, "failed to parse routes master list", ex);
                            listener.onRoutesFetched(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "failed to get routes", error);
                        listener.onRoutesFetched(null);
                    }
                }
        );
    }
}
