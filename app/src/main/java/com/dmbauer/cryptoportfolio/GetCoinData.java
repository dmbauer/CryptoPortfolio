package com.dmbauer.cryptoportfolio;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCoinData {

    private double mCoinPrice;
    private double mPercentChange24;
    private double mPercentChange1;
    private double mPercentChange7d;
    private double mMarketCapUsd;
    private int mRank;

    public static GetCoinData fromJson(JSONArray jsonArray) {

        Log.d("CryptoPortfolio", "GetCoinData() called.");

        try {
            GetCoinData coinData = new GetCoinData();

            double coinPrice = jsonArray.getJSONObject(0).getDouble("price_usd");
            double percentChange24h = jsonArray.getJSONObject(0).getDouble("percent_change_24h");
            double percentChange1h = jsonArray.getJSONObject(0).getDouble("percent_change_1h");
            double percentChange7d = jsonArray.getJSONObject(0).getDouble("percent_change_7d");
            int rank = jsonArray.getJSONObject(0).getInt("rank");
            double marketCapUsd = jsonArray.getJSONObject(0).getDouble("market_cap_usd");

            coinData.mCoinPrice = coinPrice;
            coinData.mPercentChange24 = percentChange24h;
            coinData.mPercentChange1 = percentChange1h;
            coinData.mPercentChange7d = percentChange7d;
            coinData.mMarketCapUsd = marketCapUsd;
            coinData.mRank = rank;

            return coinData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getCoinPrice() {
        return mCoinPrice;
    }

    public double getPercentChange24() {
        return mPercentChange24;
    }

    public double getPercentChange1() {
        return mPercentChange1;
    }

    public double getPercentChange7d() {
        return mPercentChange7d;
    }

    public double getMarketCapUsd() {
        return mMarketCapUsd;
    }

    public int getRank() {
        return mRank;
    }
}
