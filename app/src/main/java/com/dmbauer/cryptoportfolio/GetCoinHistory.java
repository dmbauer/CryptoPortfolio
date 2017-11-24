package com.dmbauer.cryptoportfolio;

/**
 * Created by davidbauer on 11/21/17.
 */
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCoinHistory {

    private static float[] mCoinHistoryList;

    public static GetCoinHistory fromJson(JSONObject jsonObject) {

        Log.d("CryptoPortfolio", "GetCoinHistory() called.");

        try {

            GetCoinHistory coinHistoryData = new GetCoinHistory();

            JSONArray coinArray = jsonObject.getJSONArray("Data");

            float[] coinHistoryList = new float[coinArray.length()];

            for (int i = 0; i < coinArray.length(); i++) {

                coinHistoryList[i] = (float) coinArray.getJSONObject(i).getDouble("close");

            }

            coinHistoryData.mCoinHistoryList = coinHistoryList;

            return coinHistoryData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public float[] getCoinHistory() {
        return mCoinHistoryList;
    }

}
