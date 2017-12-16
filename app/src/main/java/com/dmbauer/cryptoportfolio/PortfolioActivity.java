package com.dmbauer.cryptoportfolio;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PortfolioActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<Coin>>  {

    public PortfolioActivity() {}

    private static final String COIN_MARKET_CAP_URL = "https://api.coinmarketcap.com/v1/ticker/?limit=1000";

    /**
     * Constant value for the weather loader ID. Can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int COIN_LOADER_ID = 1;

    public static final String LOG_TAG = PortfolioActivity.class.getName();

    /** Adapter for the list of weather */
    private PortfolioCoinAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private SwipeRefreshLayout refreshLayout;
    EditText mInput;
    TextView coinOwnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hawk.init(this).build();

        // Find a reference to the {@link ListView} in the layout
        final ListView coinListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        coinListView.setEmptyView(mEmptyStateTextView);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Create a new adapter that takes an empty list of weather as input
        mAdapter = new PortfolioCoinAdapter(getApplicationContext(), new ArrayList<Coin>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        coinListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(COIN_LOADER_ID, null, this);
        } else {

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No internet service");
        }

        coinListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                TextView coinSymbolTextView = (TextView) v.findViewById(R.id.coin_symbol);
                TextView coinIDTextView = (TextView) v.findViewById(R.id.coin_id);
                TextView coinNameTextView = (TextView) v.findViewById(R.id.coin_name);

                String coinSymbol = coinSymbolTextView.getText().toString();
                String coinID = coinIDTextView.getText().toString();
                String coinName = coinNameTextView.getText().toString();

                Log.d("Crypto", "what is View v? " + coinSymbol);
                Intent intent = new Intent(PortfolioActivity.this, CoinDetailActivity.class);
                intent.putExtra("coinSymbol", coinSymbol);
                intent.putExtra("coinID", coinID);
                intent.putExtra("coinName", coinName);
                startActivity(intent);
            }
        });

        coinListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position,
                                           long arg3)
            {

                mInput = new EditText(PortfolioActivity.this);

                mInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                coinOwnTextView = (TextView) v.findViewById(R.id.coin_owned);
                TextView coinSymbolTextView = (TextView) v.findViewById(R.id.coin_symbol);

                final String coinSym = coinSymbolTextView.getText().toString();

                if(Hawk.get(coinSym) != null) {

                    double coinOwn = Hawk.get(coinSym);
                    String sCoinOwn = Double.toString(coinOwn);
                    coinOwnTextView.setText(sCoinOwn);
                    mInput.setText(sCoinOwn);
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PortfolioActivity.this);

                TextView coinNameTV = (TextView) v.findViewById(R.id.coin_name);
                String coinName = coinNameTV.getText().toString();

                alertDialogBuilder
                        .setMessage(coinName)
                        .setView(mInput)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id)
                        {
                            String entered = mInput.getText().toString();
                            if(entered.equals("")) {
                                dialog.cancel();
                            }else {
                                double own = Double.parseDouble(entered);
                                Hawk.put(coinSym, own);
                                coinOwnTextView.setText(entered);
                                finish();
                                startActivity(getIntent());
                            }
                        }
                        })
                        .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Hawk.delete(coinSym);
                            finish();
                            startActivity(getIntent());
                        }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            }
                        });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    Log.d("Crypto", "Item Clicked: " + position);

                    return true;

                }
            });


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(COIN_LOADER_ID, null, PortfolioActivity.this);
            }
        });

        refreshLayout.setVisibility(View.INVISIBLE);

    }

    public String getTotal(List<Coin> coins){

        double total=0.0;
        for(int i=0;i<coins.size();i++){
            double coinUsd = coins.get(i).getCoinPriceUsd();
            String coinSymbol = coins.get(i).getCoinSymbol();
            double coinOwn = Hawk.get(coinSymbol);
            total = total + (coinUsd * coinOwn);

        }
        String tot = "Total: " + "$" + NumberFormat.getNumberInstance(Locale.US).format(total);
        return tot;
    }

    @Override
    public android.content.Loader<List<Coin>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new PortfolioCoinLoader(getApplicationContext(), COIN_MARKET_CAP_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Coin>> loader, List<Coin> coins) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        // Set empty state text to display "No weathers found."
        mEmptyStateTextView.setText("No coins found, click + to add.");

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Coin}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (coins != null && !coins.isEmpty()) {
            mAdapter.addAll(coins);
            Hawk.put("coins", coins);
        }

        String totalPortfolioValue = getTotal(coins);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(totalPortfolioValue);
        Log.d("Crypto", "total value = " + totalPortfolioValue);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortfolioActivity.this, CoinsActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                PortfolioActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(PortfolioActivity.this);

        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setRefreshing(false);

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Coin>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
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
            Intent intent = new Intent(PortfolioActivity.this, SettingsActivity.class);
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
            Intent intent = new Intent(PortfolioActivity.this, PortfolioActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_charts) {
            Intent intent = new Intent(PortfolioActivity.this, ChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_all_coins) {
            Intent intent = new Intent(PortfolioActivity.this, CoinsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_donate) {
            Intent intent = new Intent(PortfolioActivity.this, DonateActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
