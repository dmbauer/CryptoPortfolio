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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String CRYPTO_URL = "https://api.coinmarketcap.com/v1/ticker/";
    final String bitcoinURL = CRYPTO_URL + "bitcoin";
    final String ethereumURL = CRYPTO_URL + "ethereum";
    final String bitcoinCashURL = CRYPTO_URL + "bitcoin-cash";
    final String rippleURL = CRYPTO_URL + "ripple";
    final String liteCoinURL = CRYPTO_URL + "litecoin";
    final String unikoinGoldURL = CRYPTO_URL + "unikoin-gold";

    TextView mBtcPrice, mBtcOwned, mUsdBtcWorth;
    TextView mEthPrice, mEthOwned, mUsdEthWorth;
    TextView mBchPrice, mBchOwned, mUsdBchWorth;
    TextView mXrpPrice, mXrpOwned, mUsdXrpWorth;
    TextView mLtcPrice, mLtcOwned, mUsdLtcWorth;
    TextView mUkgPrice, mUkgOwned, mUsdUkgWorth;

    String test;

    View mBitcoinView, mEthereumView, mBitcoinCashView, mRippleView, mLitecoinView, mUnikoinGoldView;

    SwipeRefreshLayout refreshLayout;

    double btcPrice, ethPrice, bchPrice, xrpPrice, ltcPrice, ukgPrice;
    double btcChange24, ethChange24, bchChange24, xrpChange24, ltcChange24, ukgChange24;

    double btcOwn, ethOwn, bchOwn, xrpOwn, ltcOwn, ukgOwn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        new CoinLoader().execute();

    }

    public class CoinLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            SyncHttpClient client = new SyncHttpClient();

            client.get(bitcoinURL, new JsonHttpResponseHandler() {

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

            client.get(ethereumURL, new JsonHttpResponseHandler() {

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

            client.get(bitcoinCashURL, new JsonHttpResponseHandler() {

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

            client.get(rippleURL, new JsonHttpResponseHandler() {

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

            client.get(liteCoinURL, new JsonHttpResponseHandler() {

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

            client.get(unikoinGoldURL, new JsonHttpResponseHandler() {

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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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

            mLitecoinView = findViewById(R.id.litecoin_view);
            mLtcPrice = findViewById(R.id.ltc_price);
            mLtcOwned = findViewById(R.id.ltc_owned);
            mUsdLtcWorth = findViewById(R.id.usd_worth_ltc);

            mUnikoinGoldView = findViewById(R.id.unikoin_gold_view);
            mUkgPrice = findViewById(R.id.ukg_price);
            mUkgOwned = findViewById(R.id.ukg_owned);
            mUsdUkgWorth = findViewById(R.id.usd_worth_ukg);

            if(Hawk.get("bitcoin") != null) {

                btcOwn = Hawk.get("bitcoin");
                mBtcOwned.setText(Double.toString(btcOwn) + " BTC");

                double bitcoin = Math.round(btcPrice * 100.0) / 100.0;
                String btc = "$" + Double.toString(bitcoin);
                mBtcPrice.setText(btc);

                float bitcoinValue = (float) (Math.round((btcOwn * btcPrice) * 100.0) / 100.0);
                String usdBtcPrice = "$" + Float.toString(bitcoinValue);
                mUsdBtcWorth.setText(usdBtcPrice);

                Hawk.put("bitcoin_value", bitcoinValue);

            }else {

                mBitcoinView.setVisibility(View.GONE);

            }

            if(Hawk.get("ethereum") != null){

                ethOwn = Hawk.get("ethereum");
                mEthOwned.setText(Double.toString(ethOwn) + " ETH");

                double ethereum = Math.round(ethPrice * 100.0) / 100.0;
                String eth = "$" + Double.toString(ethereum);
                mEthPrice.setText(eth);

                float ethereumValue = (float) (Math.round((ethOwn * ethPrice) * 100.0) / 100.0);
                String usdEthPrice = "$" + Float.toString(ethereumValue);
                mUsdEthWorth.setText(usdEthPrice);

                Hawk.put("ethereum_value", ethereumValue);

            }else {

                mEthereumView.setVisibility(View.GONE);

            }

            if(Hawk.get("bitcoin_cash") != null){

                bchOwn = Hawk.get("bitcoin_cash");
                mBchOwned.setText(Double.toString(bchOwn) + " BCH");

                double bitcoinCash = Math.round(bchPrice * 100.0) / 100.0;
                String bch = "$" + Double.toString(bitcoinCash);
                mBchPrice.setText(bch);

                float bitcoinCashValue = (float) (Math.round((bchOwn * bchPrice) * 100.0) / 100.0);
                String usdBchPrice = "$" + Float.toString(bitcoinCashValue);
                mUsdBchWorth.setText(usdBchPrice);

                Hawk.put("bitcoin_cash_value", bitcoinCashValue);

            }else {

                mBitcoinCashView.setVisibility(View.GONE);

            }

            if(Hawk.get("ripple") != null){

                xrpOwn = Hawk.get("ripple");
                mXrpOwned.setText(Double.toString(xrpOwn) + " XRP");

                double ripple = Math.round(xrpPrice * 100.0) / 100.0;
                String xrp = "$" + Double.toString(ripple);
                mXrpPrice.setText(xrp);

                float rippleValue = (float) (Math.round((xrpOwn * xrpPrice) * 100.0) / 100.0);
                String usdXrpPrice = "$" + Float.toString(rippleValue);
                mUsdXrpWorth.setText(usdXrpPrice);

                Hawk.put("ripple_value", rippleValue);

            }else {

                mRippleView.setVisibility(View.GONE);

            }

            if(Hawk.get("litecoin") != null) {

                ltcOwn = Hawk.get("litecoin");
                mLtcOwned.setText(Double.toString(ltcOwn) + " LTC");

                double litecoin = Math.round(ltcPrice * 100.0) / 100.0;
                String ltc = "$" + Double.toString(litecoin);
                mLtcPrice.setText(ltc);

                float litecoinValue = (float) (Math.round((ltcOwn * ltcPrice) * 100.0) / 100.0);
                String usdLtcPrice = "$" + Float.toString(litecoinValue);
                mUsdLtcWorth.setText(usdLtcPrice);

                Hawk.put("litecoin_value", litecoinValue);

            }else{

                mLitecoinView.setVisibility(View.GONE);

            }

            if(Hawk.get("unikoin_gold") != null) {

                ukgOwn = Hawk.get("unikoin_gold");
                mUkgOwned.setText(Double.toString(ukgOwn) + " UKG");

                double unikoin = Math.round(ukgPrice * 100.0) / 100.0;
                String ukg = "$" + Double.toString(unikoin);
                mUkgPrice.setText(ukg);

                float unikoinValue = (float) (Math.round((ukgOwn * ukgPrice) * 100.0) / 100.0);
                String usdUkgPrice = "$" + Float.toString(unikoinValue);
                mUsdUkgWorth.setText(usdUkgPrice);

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

        }

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
            return true;
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
