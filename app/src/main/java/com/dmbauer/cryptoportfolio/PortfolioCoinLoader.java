package com.dmbauer.cryptoportfolio;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by davidbauer on 11/30/17.
 */

public class PortfolioCoinLoader extends AsyncTaskLoader<List<Coin>> {

    /** Query URL */
    private String mUrl;


    public PortfolioCoinLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Coin> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of coins.
        List<Coin> coins = PortfolioQueryUtils.fetchCoinData(mUrl);
        return coins;
    }
}

