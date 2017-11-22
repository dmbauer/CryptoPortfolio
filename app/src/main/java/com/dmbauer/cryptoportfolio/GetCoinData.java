package com.dmbauer.cryptoportfolio;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class GetCoinData {

    private double mCoinPrice;
    private double mPercentChange24;

    public static GetCoinData fromJson(JSONArray jsonArray) {

        Log.d("CryptoPortfolio", "GetCoinData() called.");

        try {
            GetCoinData coinData = new GetCoinData();

            double coinPrice = jsonArray.getJSONObject(0).getDouble("price_usd");
            double percentChange24h = jsonArray.getJSONObject(0).getDouble("percent_change_24h");

            coinData.mCoinPrice = coinPrice;
            coinData.mPercentChange24 = percentChange24h;

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



}
