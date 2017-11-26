package com.dmbauer.cryptoportfolio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String CRYPTO_URL = "https://api.coinmarketcap.com/v1/ticker/";
    final String BITCOIN_URL = CRYPTO_URL + "bitcoin";
    final String ETHEREUM_URL = CRYPTO_URL + "ethereum";
    final String BITCOIN_CASH_URL = CRYPTO_URL + "bitcoin-cash";
    final String RIPPLE_URL = CRYPTO_URL + "ripple";
    final String LITECOIN_URL = CRYPTO_URL + "litecoin";
    final String UNIKOIN_GOLD_URL = CRYPTO_URL + "unikoin-gold";
    final String DASH_URL = CRYPTO_URL + "dash";

    final String BITCOIN_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=BTC&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
    final String ETHEREUM_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=ETH&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
    final String LITECOIN_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=LTC&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
    final String UNIKOIN_GOLD_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=UKG&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
    final String BITCOIN_CASH_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=BCH&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
    final String RIPPLE_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=XRP&tsym=USD&limit=96&aggregate=15&e=CCCAGG";
    final String DASH_HISTORY_URL = "https://min-api.cryptocompare.com/data/histominute?fsym=DASH&tsym=USD&limit=96&aggregate=15&e=CCCAGG";

    TextView mBtcPrice, mBtcOwned, mUsdBtcWorth;
    TextView mEthPrice, mEthOwned, mUsdEthWorth;
    TextView mBchPrice, mBchOwned, mUsdBchWorth;
    TextView mXrpPrice, mXrpOwned, mUsdXrpWorth;
    TextView mLtcPrice, mLtcOwned, mUsdLtcWorth;
    TextView mUkgPrice, mUkgOwned, mUsdUkgWorth;
    TextView mDashPrice, mDashOwned, mUsdDashWorth;

    View mBitcoinView, mEthereumView, mBitcoinCashView, mRippleView, mLitecoinView, mUnikoinGoldView, mDashView;

    SwipeRefreshLayout refreshLayout;

    double btcPrice, ethPrice, bchPrice, xrpPrice, ltcPrice, ukgPrice, dashPrice;
    double btcChange24, ethChange24, bchChange24, xrpChange24, ltcChange24, ukgChange24, dashChange24;

    double btcWealth, bchWealth, ethWealth, xrpWealth, ltcWealth, ukgWealth, dashWealth;

    float[] mBtcHistory, mEthHistory, mLtcHistory, mUkgHistory, mBchHistory, mXrpHistory, mDashHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitcoinView = findViewById(R.id.bitcoin_view);
        mBtcPrice = findViewById(R.id.btc_price);
        mBtcOwned = findViewById(R.id.btc_owned);
        mUsdBtcWorth = findViewById(R.id.usd_worth_btc);

        mEthereumView = findViewById(R.id.ethereum_view);
        mEthPrice = findViewById(R.id.eth_price);
        mEthOwned = findViewById(R.id.eth_owned);
        mUsdEthWorth = findViewById(R.id.usd_worth_eth);

        mBitcoinCashView = findViewById(R.id.bitcoin_cash_view);
        mBchPrice = findViewById(R.id.bch_price);
        mBchOwned = findViewById(R.id.bch_owned);
        mUsdBchWorth = findViewById(R.id.usd_worth_bch);

        mRippleView = findViewById(R.id.ripple_view);
        mXrpPrice = findViewById(R.id.xrp_price);
        mXrpOwned = findViewById(R.id.xrp_owned);
        mUsdXrpWorth = findViewById(R.id.usd_worth_xrp);

        mDashView = findViewById(R.id.dash_view);
        mDashPrice = findViewById(R.id.dash_price);
        mDashOwned = findViewById(R.id.dash_owned);
        mUsdDashWorth = findViewById(R.id.usd_worth_dash);

        mLitecoinView = findViewById(R.id.litecoin_view);
        mLtcPrice = findViewById(R.id.ltc_price);
        mLtcOwned = findViewById(R.id.ltc_owned);
        mUsdLtcWorth = findViewById(R.id.usd_worth_ltc);

        mUnikoinGoldView = findViewById(R.id.unikoin_gold_view);
        mUkgPrice = findViewById(R.id.ukg_price);
        mUkgOwned = findViewById(R.id.ukg_owned);
        mUsdUkgWorth = findViewById(R.id.usd_worth_ukg);

        Hawk.init(this).build();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               new CoinLoader().execute();
            }
        });

        refreshLayout.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CoinAddActivity.class);
                startActivity(intent);
            }
        });

        mBitcoinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "bitcoin");
                intent.putExtra("coinURL", BITCOIN_URL);
                intent.putExtra("coinHistoryURL", BITCOIN_HISTORY_URL);
                startActivity(intent);
            }
        });

        mEthereumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "ethereum");
                intent.putExtra("coinURL", ETHEREUM_URL);
                intent.putExtra("coinHistoryURL", ETHEREUM_HISTORY_URL);
                startActivity(intent);
            }
        });

        mBitcoinCashView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "bitcoin_cash");
                intent.putExtra("coinURL", BITCOIN_CASH_URL);
                intent.putExtra("coinHistoryURL", BITCOIN_CASH_HISTORY_URL);
                startActivity(intent);
            }
        });

        mRippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "ripple");
                intent.putExtra("coinURL", RIPPLE_URL);
                intent.putExtra("coinHistoryURL", RIPPLE_HISTORY_URL);
                startActivity(intent);
            }
        });

        mDashView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "dash");
                intent.putExtra("coinURL", DASH_URL);
                intent.putExtra("coinHistoryURL", DASH_HISTORY_URL);
                startActivity(intent);
            }
        });

        mLitecoinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "litecoin");
                intent.putExtra("coinURL", LITECOIN_URL);
                intent.putExtra("coinHistoryURL", LITECOIN_HISTORY_URL);
                startActivity(intent);
            }
        });

        mUnikoinGoldView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoinDetailActivity.class);
                intent.putExtra("viewID", "unikoin_gold");
                intent.putExtra("coinURL", UNIKOIN_GOLD_URL);
                intent.putExtra("coinHistoryURL", UNIKOIN_GOLD_HISTORY_URL);
                startActivity(intent);
            }
        });

        new CoinLoader().execute();

    }

    public class CoinLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

                SyncHttpClient client = new SyncHttpClient();

                if (Hawk.get("bitcoin") != null) {

                    client.get(BITCOIN_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinData coinData = GetCoinData.fromJson(response);

                            btcPrice = coinData.getCoinPrice();
                            btcChange24 = coinData.getPercentChange24();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });


                    client.get(BITCOIN_HISTORY_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                            mBtcHistory = coinHistory.getCoinHistory();

                            Log.d("Crypto", Double.toString(mBtcHistory[1]));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });

                }

                if(Hawk.get("ethereum") != null) {

                    client.get(ETHEREUM_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinData coinData = GetCoinData.fromJson(response);

                            ethPrice = coinData.getCoinPrice();
                            ethChange24 = coinData.getPercentChange24();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });

                    client.get(ETHEREUM_HISTORY_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                            mEthHistory = coinHistory.getCoinHistory();

                            Log.d("Crypto", Double.toString(mEthHistory[1]));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });
                }

                if(Hawk.get("bitcoin_cash") != null) {

                    client.get(BITCOIN_CASH_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinData coinData = GetCoinData.fromJson(response);

                            bchPrice = coinData.getCoinPrice();
                            bchChange24 = coinData.getPercentChange24();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });

                    client.get(BITCOIN_CASH_HISTORY_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                            mBchHistory = coinHistory.getCoinHistory();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });
                }

                if(Hawk.get("ripple") != null) {

                    client.get(RIPPLE_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinData coinData = GetCoinData.fromJson(response);

                            xrpPrice = coinData.getCoinPrice();
                            xrpChange24 = coinData.getPercentChange24();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });

                    client.get(RIPPLE_HISTORY_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                            mXrpHistory = coinHistory.getCoinHistory();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });
                }

            if (Hawk.get("dash") != null) {

                client.get(DASH_URL, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("CryptoPortfolio", "JSON: " + response.toString());

                        GetCoinData coinData = GetCoinData.fromJson(response);

                        dashPrice = coinData.getCoinPrice();
                        dashChange24 = coinData.getPercentChange24();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.e("CryptoPortfolio", "Fail " + e.toString());
                        Log.d("CryptoPortfolio", "Status code " + statusCode);
                    }

                });

                client.get(DASH_HISTORY_URL, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("CryptoPortfolio", "JSON: " + response.toString());

                        GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                        mDashHistory = coinHistory.getCoinHistory();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        Log.e("CryptoPortfolio", "Fail " + e.toString());
                        Log.d("CryptoPortfolio", "Status code " + statusCode);
                    }

                });

            }

                if(Hawk.get("litecoin") != null) {

                    client.get(LITECOIN_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinData coinData = GetCoinData.fromJson(response);

                            ltcPrice = coinData.getCoinPrice();
                            ltcChange24 = coinData.getPercentChange24();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });

                    client.get(LITECOIN_HISTORY_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                            mLtcHistory = coinHistory.getCoinHistory();

                            Log.d("Crypto", Double.toString(mLtcHistory[1]));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });
                }

                if(Hawk.get("unikoin_gold") != null) {

                    client.get(UNIKOIN_GOLD_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinData coinData = GetCoinData.fromJson(response);

                            ukgPrice = coinData.getCoinPrice();
                            ukgChange24 = coinData.getPercentChange24();

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });

                    client.get(UNIKOIN_GOLD_HISTORY_URL, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // called when response HTTP status is "200 OK"
                            Log.d("CryptoPortfolio", "JSON: " + response.toString());

                            GetCoinHistory coinHistory = GetCoinHistory.fromJson(response);

                            mUkgHistory = coinHistory.getCoinHistory();

                            Log.d("Crypto", Double.toString(mUkgHistory[1]));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.e("CryptoPortfolio", "Fail " + e.toString());
                            Log.d("CryptoPortfolio", "Status code " + statusCode);
                        }

                    });
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            LineChart btcLineChart = findViewById(R.id.btc_line_chart);
            LineChart ethLineChart = findViewById(R.id.eth_line_chart);
            LineChart ltcLineChart = findViewById(R.id.ltc_line_chart);
            LineChart bchLineChart = findViewById(R.id.bch_line_chart);
            LineChart xrpLineChart = findViewById(R.id.xrp_line_chart);
            LineChart ukgLineChart = findViewById(R.id.ukg_line_chart);
            LineChart dashLineChart = findViewById(R.id.dash_line_chart);

            if(mBtcHistory != null) {
                ArrayList<Entry> btcEntries = new ArrayList<>();
                for (int i = 0; i < mBtcHistory.length; i++) {
                    btcEntries.add(new Entry(i, mBtcHistory[i]));
                }

                LineDataSet btcDataSet = new LineDataSet(btcEntries, "");
                LineData btcData = new LineData(btcDataSet);
                btcLineChart.setData(btcData);
                btcDataSet.setColor(getResources().getColor(R.color.colorOrange));

                setLineProperties(btcLineChart, btcDataSet);
            }

            if(mEthHistory != null) {
                ArrayList<Entry> ethEntries = new ArrayList<>();
                for (int i = 0; i < mEthHistory.length; i++) {
                    ethEntries.add(new Entry(i, mEthHistory[i]));
                }

                LineDataSet ethDataSet = new LineDataSet(ethEntries, "");
                LineData ethData = new LineData(ethDataSet);
                ethLineChart.setData(ethData);
                ethDataSet.setColor(getResources().getColor(R.color.colorBlue));

                setLineProperties(ethLineChart, ethDataSet);
            }

            if(mBchHistory != null) {
                ArrayList<Entry> bchEntries = new ArrayList<>();
                for (int i = 0; i < mBchHistory.length; i++) {
                    bchEntries.add(new Entry(i, mBchHistory[i]));
                }

                LineDataSet bchDataSet = new LineDataSet(bchEntries, "");
                LineData bchData = new LineData(bchDataSet);
                bchLineChart.setData(bchData);
                bchDataSet.setColor(getResources().getColor(R.color.colorRed));

                setLineProperties(bchLineChart, bchDataSet);
            }

            if(mXrpHistory != null) {
                ArrayList<Entry> xrpEntries = new ArrayList<>();
                for (int i = 0; i < mXrpHistory.length; i++) {
                    xrpEntries.add(new Entry(i, mXrpHistory[i]));
                }

                LineDataSet xrpDataSet = new LineDataSet(xrpEntries, "");
                LineData xrpData = new LineData(xrpDataSet);
                xrpLineChart.setData(xrpData);
                xrpDataSet.setColor(getResources().getColor(R.color.colorPurple));

                setLineProperties(xrpLineChart, xrpDataSet);
            }

            if(mDashHistory != null) {
                ArrayList<Entry> dashEntries = new ArrayList<>();
                for (int i = 0; i < mDashHistory.length; i++) {
                    dashEntries.add(new Entry(i, mDashHistory[i]));
                }

                LineDataSet dashDataSet = new LineDataSet(dashEntries, "");
                LineData dashData = new LineData(dashDataSet);
                dashLineChart.setData(dashData);
                dashDataSet.setColor(getResources().getColor(R.color.colorGreen));

                setLineProperties(dashLineChart, dashDataSet);
            }

            if(mLtcHistory != null) {
                ArrayList<Entry> ltcEntries = new ArrayList<>();
                for (int i = 0; i < mLtcHistory.length; i++) {
                    ltcEntries.add(new Entry(i, mLtcHistory[i]));
                }

                LineDataSet ltcDataSet = new LineDataSet(ltcEntries, "");
                LineData ltcData = new LineData(ltcDataSet);
                ltcLineChart.setData(ltcData);
                ltcDataSet.setColor(getResources().getColor(R.color.colorSecondaryText));

                setLineProperties(ltcLineChart, ltcDataSet);
            }

            if(mUkgHistory != null) {
                ArrayList<Entry> ukgEntries = new ArrayList<>();
                for (int i = 0; i < mUkgHistory.length; i++) {
                    ukgEntries.add(new Entry(i, mUkgHistory[i]));
                }

                LineDataSet ukgDataSet = new LineDataSet(ukgEntries, "");
                LineData ukgData = new LineData(ukgDataSet);
                ukgLineChart.setData(ukgData);
                ukgDataSet.setColor(getResources().getColor(R.color.colorYellow));

                setLineProperties(ukgLineChart, ukgDataSet);
            }

            if(Hawk.get("bitcoin") != null) {

                double btcOwn = Hawk.get("bitcoin");
                mBtcOwned.setText(Double.toString(btcOwn) + " BTC");

                double bitcoin = Math.round(btcPrice * 100.0) / 100.0;
                String btc = "$" + Double.toString(bitcoin);
                mBtcPrice.setText(btc);

                float bitcoinValue = (float) (Math.round((btcOwn * btcPrice) * 100.0) / 100.0);
                String usdBtcPrice = "$" + Float.toString(bitcoinValue);
                mUsdBtcWorth.setText(usdBtcPrice);

                btcWealth = btcOwn * btcPrice;

                Hawk.put("bitcoin_value", bitcoinValue);

            }else {

                mBitcoinView.setVisibility(View.GONE);

            }

            if(Hawk.get("ethereum") != null){

                double ethOwn = Hawk.get("ethereum");
                mEthOwned.setText(Double.toString(ethOwn) + " ETH");

                double ethereum = Math.round(ethPrice * 100.0) / 100.0;
                String eth = "$" + Double.toString(ethereum);
                mEthPrice.setText(eth);

                float ethereumValue = (float) (Math.round((ethOwn * ethPrice) * 100.0) / 100.0);
                String usdEthPrice = "$" + Float.toString(ethereumValue);
                mUsdEthWorth.setText(usdEthPrice);

                ethWealth = ethOwn * ethPrice;

                Hawk.put("ethereum_value", ethereumValue);

            }else {

                mEthereumView.setVisibility(View.GONE);

            }

            if(Hawk.get("bitcoin_cash") != null){

                double bchOwn = Hawk.get("bitcoin_cash");
                mBchOwned.setText(Double.toString(bchOwn) + " BCH");

                double bitcoinCash = Math.round(bchPrice * 100.0) / 100.0;
                String bch = "$" + Double.toString(bitcoinCash);
                mBchPrice.setText(bch);

                float bitcoinCashValue = (float) (Math.round((bchOwn * bchPrice) * 100.0) / 100.0);
                String usdBchPrice = "$" + Float.toString(bitcoinCashValue);
                mUsdBchWorth.setText(usdBchPrice);

                bchWealth = bchOwn * bchPrice;

                Hawk.put("bitcoin_cash_value", bitcoinCashValue);

            }else {

                mBitcoinCashView.setVisibility(View.GONE);

            }

            if(Hawk.get("ripple") != null){

                double xrpOwn = Hawk.get("ripple");
                mXrpOwned.setText(Double.toString(xrpOwn) + " XRP");

                double ripple = Math.round(xrpPrice * 100.0) / 100.0;
                String xrp = "$" + Double.toString(ripple);
                mXrpPrice.setText(xrp);

                float rippleValue = (float) (Math.round((xrpOwn * xrpPrice) * 100.0) / 100.0);
                String usdXrpPrice = "$" + Float.toString(rippleValue);
                mUsdXrpWorth.setText(usdXrpPrice);

                xrpWealth = xrpOwn * xrpPrice;

                Hawk.put("ripple_value", rippleValue);

            }else {

                mRippleView.setVisibility(View.GONE);

            }

            if(Hawk.get("dash") != null){

                double dashOwn = Hawk.get("dash");
                mDashOwned.setText(Double.toString(dashOwn) + " DASH");

                double dash = Math.round(dashPrice * 100.0) / 100.0;
                String dsh = "$" + Double.toString(dash);
                mDashPrice.setText(dsh);

                float dashValue = (float) (Math.round((dashOwn * dashPrice) * 100.0) / 100.0);
                String usdDashPrice = "$" + Float.toString(dashValue);
                mUsdDashWorth.setText(usdDashPrice);

                dashWealth = dashOwn * dashPrice;

                Hawk.put("dash_value", dashValue);

            }else {

                mDashView.setVisibility(View.GONE);

            }

            if(Hawk.get("litecoin") != null) {

                double ltcOwn = Hawk.get("litecoin");
                mLtcOwned.setText(Double.toString(ltcOwn) + " LTC");

                double litecoin = Math.round(ltcPrice * 100.0) / 100.0;
                String ltc = "$" + Double.toString(litecoin);
                mLtcPrice.setText(ltc);

                float litecoinValue = (float) (Math.round((ltcOwn * ltcPrice) * 100.0) / 100.0);
                String usdLtcPrice = "$" + Float.toString(litecoinValue);
                mUsdLtcWorth.setText(usdLtcPrice);

                ltcWealth = ltcOwn * ltcPrice;

                Hawk.put("litecoin_value", litecoinValue);

            }else{

                mLitecoinView.setVisibility(View.GONE);

            }

            if(Hawk.get("unikoin_gold") != null) {

                double ukgOwn = Hawk.get("unikoin_gold");
                mUkgOwned.setText(Double.toString(ukgOwn) + " UKG");

                double unikoin = Math.round(ukgPrice * 100.0) / 100.0;
                String ukg = "$" + Double.toString(unikoin);
                mUkgPrice.setText(ukg);

                float unikoinValue = (float) (Math.round((ukgOwn * ukgPrice) * 100.0) / 100.0);
                String usdUkgPrice = "$" + Float.toString(unikoinValue);
                mUsdUkgWorth.setText(usdUkgPrice);

                ukgWealth = ukgOwn * ukgPrice;

                Hawk.put("unikoin_gold_value", unikoinValue);

            }else{

                mUnikoinGoldView.setVisibility(View.GONE);

            }

            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }

            refreshLayout.setVisibility(View.VISIBLE);

            ConstraintLayout loadingLayout = findViewById(R.id.loading_layout);
            loadingLayout.setVisibility(View.GONE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(MainActivity.this);

            int totalWealth = (int) (btcWealth + bchWealth + ethWealth + xrpWealth + ltcWealth + ukgWealth + dashWealth);

            getSupportActionBar().setTitle("Total: " + "$" + Double.toString(totalWealth));
        }

    }

    public void setLineProperties(LineChart lineChart, LineDataSet dataSet){
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(false);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);

        // style chart
        lineChart.setBackgroundColor(-1);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_portfolio) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_charts) {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_coins) {
            Intent intent = new Intent(MainActivity.this, CoinAddActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_donate) {
            Intent intent = new Intent(MainActivity.this, DonateActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
