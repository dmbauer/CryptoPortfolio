package com.dmbauer.cryptoportfolio;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidbauer on 11/30/17.
 */

public class QueryUtils {

    public static final String LOG_TAG = CoinsActivity.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the coinmarketcap dataset and return a list of {@link Coin} objects.
     */
    public static List<Coin> fetchCoinData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Weather}s
        List<Coin> coins = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Weather}s
        return coins;
    }

    /**
     * Return a list of {@link Coin} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Coin> extractFeatureFromJson(String coinJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(coinJSON)) {
            return null;
        }
        double coin7D;
        double coinPriceUsd;
        double coinPriceBtc;
        double coinMarketCap;
        double coin1Hour;
        double coin24Hour;

        // Create an empty ArrayList that we can start adding coins to
        List<Coin> coins = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(coinJSON);

            // For each coin in the baseJsonResponse, create an {@link Coin} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                // Extract the coin data from the array at each position i
                JSONObject coinObject = baseJsonResponse.getJSONObject(i);
                String coinID = coinObject.getString("id");
                String coinName = coinObject.getString("name");
                String coinSymbol = coinObject.getString("symbol");
                int coinRank = coinObject.getInt("rank");
                if (coinObject.getString("price_usd").equals("null")) {
                    coinPriceUsd = 0.0;
                } else {
                    coinPriceUsd = coinObject.getDouble("price_usd");
                }
                if (coinObject.getString("price_btc").equals("null")) {
                    coinPriceBtc = 0.0;
                } else {
                    coinPriceBtc = coinObject.getDouble("price_btc");
                }
                if (coinObject.getString("market_cap_usd").equals("null")) {
                    coinMarketCap = 0.0;
                } else {
                    coinMarketCap = coinObject.getDouble("market_cap_usd");
                }
                if (coinObject.getString("percent_change_1h").equals("null")) {
                    coin1Hour = 0.0;
                } else {
                    coin1Hour = coinObject.getDouble("percent_change_1h");
                }
                if (coinObject.getString("percent_change_24h").equals("null")) {
                    coin24Hour = 0.0;
                } else {
                    coin24Hour = coinObject.getDouble("percent_change_24h");
                }
                if (coinObject.getString("percent_change_7d").equals("null")) {
                    coin7D = 0.0;
                } else {
                    coin7D = coinObject.getDouble("percent_change_7d");
                }

                Coin coin = new Coin(coinID,
                        coinName,
                        coinSymbol,
                        coinRank,
                        coinPriceUsd,
                        coinPriceBtc,
                        coinMarketCap,
                        coin1Hour,
                        coin24Hour,
                        coin7D);

                // Add the new {@link Coin} to the list of coins.
                coins.add(coin);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the coin JSON results", e);
        }

        // Return the list of weathers
        return coins;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the weather JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
