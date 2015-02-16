package com.lappdance.grtrealtime.network;

public interface FetchRoutesStrategy {
    /**
     * Fetches a subset of all the GRT routes.
     * @param listener The callback to execute when the routes have been fetched.
     */
    void fetchRoutes(OnRoutesFetchedListener listener);
}
