package com.lappdance.grtrealtime.network;

import com.lappdance.grtrealtime.model.Route;

import java.util.Map;

public interface OnRoutesFetchedListener {
    /**
     * Called when the {@link FetchRoutesStrategy} has fetched, or failed to fetch,
     * its list of routes.
     * @param routes A dictionary of route IDs and {@link Route}s,
     * or {@code null} if there was an error.
     */
    void onRoutesFetched(Map<Integer, Route> routes);
}
