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
 * Gets all the GRT routes that are in service at the time the request is made.
 */
public class FetchInServiceRoutesStrategy implements FetchRoutesStrategy {
    private static final String LOG_TAG = "FetchInServiceRoutesStrategy";
    private static final String URL_ALL_ROUTES = "http://realtimemap.grt.ca/Map/GetRoutes/";

    private RequestQueue mRequestQueue;

    public FetchInServiceRoutesStrategy(RequestQueue queue) {
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
                            Document doc = Jsoup.parse(response);

                            for(Element li : doc.select("li")) {
                                int id = Integer.parseInt(li.attr("data-value"));
                                int color = Integer.parseInt(li.attr("color-scheme"), 16);

                                Elements spans = li.select("span");
                                String name = spans.last().text();

                                Route r = new Route(id, name, color);
                                routes.put(id, r);
                            }

                            listener.onRoutesFetched(routes);

                        } catch(RuntimeException ex) {
                            Log.e(LOG_TAG, "failed to parse XML", ex);
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
