package com.lappdance.grtrealtime.network;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Factory class for creating {@link FetchRoutesStrategy} instances.
 * The user may choose to show all routes, or only routes that are in service,
 * and this class will determine which class to use.
 */
public class FetchRoutesStrategyFactory {
    private RequestQueue mRequestQueue;

    @SuppressWarnings("unused")
    public FetchRoutesStrategyFactory(Context context, RequestQueue queue) {
        mRequestQueue = queue;
    }

    public FetchRoutesStrategy newStrategy() {
        return new FetchInServiceRoutesStrategy(mRequestQueue);
    }
}
