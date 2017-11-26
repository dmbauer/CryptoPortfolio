package com.dmbauer.cryptoportfolio;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CoinDetailActivity extends AppCompatActivity {

    double coinPrice, coinChange24,coinChange1, coinChange7d, marketCapUsd;
    int coinRank;
    float[] coinHistoryArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        new CoinDetailLoader().execute();

    }

    public class CoinDetailLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Intent intent = CoinDetailActivity.this.getIntent();
            String coinURL = intent.getExtras().getString("coinURL");
            String coinHistoryURL = intent.getExtras().getString("coinHistoryURL");

            SyncHttpClient client = new SyncHttpClient();

                client.get(coinURL, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("CryptoPortfolio", "JSON: " + response.toString());

                        GetCoinData coinData = GetCoinData.fromJson(response);

                        coinPrice = coinData.getCoinPrice();
                        coinChange24 = coinData.getPercentChange24();
                        coinChange1 = coinData.getPercentChange1();
                        coinChange7d = coinData.getPercentChange7d();
                        coinRank = coinData.getRank();
                        marketCapUsd = coinData.getMarketCapUsd();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.e("CryptoPortfolio", "Fail " + e.toString());
                        Log.d("CryptoPortfolio", "Status code " + statusCode);
                    }

                });


                client.get(coinHistoryURL, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("CryptoPortfolio", "JSON: " + response.toString());

                        GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                        coinHistoryArray = coinHistory.getCoinHistory();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.e("CryptoPortfolio", "Fail " + e.toString());
                        Log.d("CryptoPortfolio", "Status code " + statusCode);
                    }

                });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ImageView coinImage = (ImageView) findViewById(R.id.coin_image);
            TextView coinTextView = (TextView) findViewById(R.id.coin_name);
            TextView coinPriceTextView = (TextView) findViewById(R.id.coin_price_textview);
            TextView coinMarketCapTextView = (TextView) findViewById(R.id.market_cap_textview);
            TextView coinRankTextView = (TextView) findViewById(R.id.rank_textview);
            TextView coinOwned = (TextView) findViewById(R.id.coin_owned_textview);
            TextView coin24hr = (TextView) findViewById(R.id.change_24hr_textview);
            TextView coin1hr = (TextView) findViewById(R.id.change_1hr_textview);
            TextView coin7d = (TextView) findViewById(R.id.change_7d_textview);

            Intent intent = CoinDetailActivity.this.getIntent();
            if(intent !=null) {

                String coin24, coin1, coin7, rank, price, marketCap, ownedValue;
                double coinOwn, owned;

                String strdata = intent.getExtras().getString("viewID");

                LineChart coinLineChart = findViewById(R.id.coin_detail_chart);

                if(coinHistoryArray != null) {
                    ArrayList<Entry> entries = new ArrayList<>();
                    for (int i = 0; i < coinHistoryArray.length; i++) {
                        entries.add(new Entry(i, coinHistoryArray[i]));
                    }

                    LineDataSet coinDataSet = new LineDataSet(entries, "");
                    LineData coinData = new LineData(coinDataSet);
                    coinLineChart.setData(coinData);

                    setLineProperties(coinLineChart, coinDataSet);
                }

                if(strdata.equals("bitcoin")) {

                    coinImage.setImageResource(R.drawable.btc2x);

                    coinOwn = Hawk.get("bitcoin");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Bitcoin ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " BTC");
                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                } if(strdata.equals("ethereum")) {

                    coinImage.setImageResource(R.drawable.eth2x);

                    coinOwn = Hawk.get("ethereum");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Ethereum ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " ETH");

                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                } if(strdata.equals("bitcoin_cash")) {

                    coinImage.setImageResource(R.drawable.bch2x);

                    coinOwn = Hawk.get("bitcoin_cash");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Bitcoin Cash ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " BCH");

                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                } if(strdata.equals("ripple")) {

                    coinImage.setImageResource(R.drawable.xrp2x);

                    coinOwn = Hawk.get("ripple");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Ripple ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " XRP");

                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                } if(strdata.equals("dash")) {

                    coinImage.setImageResource(R.drawable.dash2x);

                    coinOwn = Hawk.get("dash");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Dash ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " DASH");

                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                } if(strdata.equals("litecoin")) {

                    coinImage.setImageResource(R.drawable.ltc2x);

                    coinOwn = Hawk.get("litecoin");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Litecoin ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " LTC");

                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                } if(strdata.equals("unikoin_gold")) {

                    coinImage.setImageResource(R.drawable.ic_ukg);

                    coinOwn = Hawk.get("unikoin_gold");

                    owned = Math.round((coinOwn * coinPrice) * 100.0) / 100.0;
                    ownedValue = "Unikoin Gold ($" + Double.toString(owned) + ")";

                    coinTextView.setText(ownedValue);

                    marketCap = "$" + Double.toString(marketCapUsd);
                    coinMarketCapTextView.setText(marketCap);

                    rank = "#" + Integer.toString(coinRank);
                    coinRankTextView.setText(rank);

                    price = "$" + Double.toString(coinPrice);
                    coinPriceTextView.setText(price);

                    coinOwned.setText(Double.toString(coinOwn) + " UKG");

                    coin24 = Double.toString(coinChange24) + "%";
                    coin1 = Double.toString(coinChange1) + "%";
                    coin7 = Double.toString(coinChange7d) + "%";

                    if (coinChange24 > 0){
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin24hr.setText(coin24);
                        coin24hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange1 > 0) {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin1hr.setText(coin1);
                        coin1hr.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                    if (coinChange7d > 0) {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorGreen));
                    } else {
                        coin7d.setText(coin7);
                        coin7d.setTextColor(getResources().getColor(R.color.colorRed));
                    }

                }
            }
        }
    }

    public void setLineProperties(LineChart lineChart, LineDataSet dataSet){
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setFillColor(getResources().getColor(R.color.colorAccent));

        // style chart
        lineChart.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        lineChart.setDescription(description);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setTouchEnabled(true);

        lineChart.setAutoScaleMinMaxEnabled(true);

        // remove axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setEnabled(false);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);

        // hide legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        lineChart.invalidate();
    }
}
