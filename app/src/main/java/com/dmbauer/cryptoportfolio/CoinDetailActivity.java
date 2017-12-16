package com.dmbauer.cryptoportfolio;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class CoinDetailActivity extends AppCompatActivity {

    double coinPrice, coinChange24, coinChange1, coinChange7d, marketCapUsd;
    int coinRank;
    float[] coinHistoryArray;
    String coinSymbol, coindata, coinName, coinDetailURL, url1Y, url1M, url24, url1W, url1H, historyURL;
    Intent intent;
    Button yearButton, monthButton, weekButton, hour24Button, oneHourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail);

        new CoinDetailLoader().execute();

        yearButton = (Button) findViewById(R.id.year_button);
        oneHourButton = (Button) findViewById(R.id.one_hour_button);
        hour24Button = (Button) findViewById(R.id.hour_24_button);
        weekButton = (Button) findViewById(R.id.week_button);
        monthButton = (Button) findViewById(R.id.month_button);

        intent = CoinDetailActivity.this.getIntent();
        coinSymbol = intent.getExtras().getString("coinSymbol");
        coindata = intent.getExtras().getString("coinID");
        coinName = intent.getExtras().getString("coinName");

        coinDetailURL = "https://api.coinmarketcap.com/v1/ticker/" + coindata;
        url1Y = "https://min-api.cryptocompare.com/data/histoday?fsym=" + coinSymbol + "&tsym=USD&limit=120&aggregate=3&e=CCCAGG";
        url24 = "https://min-api.cryptocompare.com/data/histominute?fsym=" + coinSymbol + "&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
        url1W = "https://min-api.cryptocompare.com/data/histohour?fsym=" + coinSymbol + "&tsym=USD&limit=84&aggregate=2&e=CCCAGG";
        url1M = "https://min-api.cryptocompare.com/data/histohour?fsym=" + coinSymbol + "&tsym=USD&limit=120&aggregate=6&e=CCCAGG";
        url1H = "https://min-api.cryptocompare.com/data/histominute?fsym=" + coinSymbol + "&tsym=USD&limit=60&aggregate=1&e=CCCAGG";

        hour24Button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        yearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yearButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                oneHourButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                weekButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                monthButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                hour24Button.setBackgroundColor(Color.parseColor("#00ffffff"));

                historyURL = url1Y;

                new HistoryLoader().execute();

            }
        });

        oneHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyURL = url1H;

                oneHourButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                yearButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                weekButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                monthButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                hour24Button.setBackgroundColor(Color.parseColor("#00ffffff"));

                new HistoryLoader().execute();

            }
        });

        hour24Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyURL = url24;

                hour24Button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                oneHourButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                weekButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                monthButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                yearButton.setBackgroundColor(Color.parseColor("#00ffffff"));


                new HistoryLoader().execute();

            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyURL = url1W;

                weekButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                oneHourButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                yearButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                monthButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                hour24Button.setBackgroundColor(Color.parseColor("#00ffffff"));

                new HistoryLoader().execute();

            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                historyURL = url1M;

                monthButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                oneHourButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                weekButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                yearButton.setBackgroundColor(Color.parseColor("#00ffffff"));
                hour24Button.setBackgroundColor(Color.parseColor("#00ffffff"));

                new HistoryLoader().execute();

            }
        });

    }

    public class HistoryLoader extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            SyncHttpClient client = new SyncHttpClient();

            client.get(historyURL, new JsonHttpResponseHandler() {

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

            LineChart coinLineChart = findViewById(R.id.coin_detail_chart);

            if (coinHistoryArray != null) {
                ArrayList<Entry> entries = new ArrayList<>();
                for (int i = 0; i < coinHistoryArray.length; i++) {
                    entries.add(new Entry(i, coinHistoryArray[i]));
                }

                LineDataSet coinDataSet = new LineDataSet(entries, "");
                LineData coinData = new LineData(coinDataSet);
                coinLineChart.setData(coinData);

                setLineProperties(coinLineChart, coinDataSet);

                View progressBar = findViewById(R.id.progress_bar_coin_detail);
                progressBar.setVisibility(View.GONE);

            }
        }
    }

    public class CoinDetailLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            SyncHttpClient client = new SyncHttpClient();

            client.get(coinDetailURL, new JsonHttpResponseHandler() {

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
            if (intent != null) {

                String coin24, coin1, coin7, rank, price, marketCap;
                double coinOwn;

                LineChart coinLineChart = findViewById(R.id.coin_detail_chart);

                historyURL = url24;

                new HistoryLoader().execute();

                if (coinHistoryArray != null) {
                    ArrayList<Entry> entries = new ArrayList<>();
                    for (int i = 0; i < coinHistoryArray.length; i++) {
                        entries.add(new Entry(i, coinHistoryArray[i]));
                    }

                    LineDataSet coinDataSet = new LineDataSet(entries, "");
                    LineData coinData = new LineData(coinDataSet);
                    coinLineChart.setData(coinData);

                    setLineProperties(coinLineChart, coinDataSet);
                }

                ImageGetter imageGetter = new ImageGetter(coinSymbol);
                int image = imageGetter.getImage();

                coinImage.setImageResource(image);

                coinOwn = Hawk.get(coinSymbol);

                String coinNameValue = coinName + " ($" +
                        NumberFormat.getNumberInstance(Locale.US).format(coinOwn * coinPrice) + ")";

                coinTextView.setText(coinNameValue);

                marketCap = "$" + NumberFormat.getNumberInstance(Locale.US).format(marketCapUsd);
                coinMarketCapTextView.setText(marketCap);

                rank = "#" + Integer.toString(coinRank);
                coinRankTextView.setText(rank);

                price = "$" + NumberFormat.getNumberInstance(Locale.US).format(coinPrice);
                coinPriceTextView.setText(price);

                coinOwned.setText(Double.toString(coinOwn) + " " + coinSymbol);
                coin24 = Double.toString(coinChange24) + "%";
                coin1 = Double.toString(coinChange1) + "%";
                coin7 = Double.toString(coinChange7d) + "%";

                if (coinChange24 > 0) {
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

        public void setLineProperties(LineChart lineChart, LineDataSet dataSet) {
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
            lineChart.setTouchEnabled(false);

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


