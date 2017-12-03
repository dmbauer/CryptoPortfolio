package com.dmbauer.cryptoportfolio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LauncherActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class CoinsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Coin>> {

    public CoinsActivity() {}

    private static final String COIN_MARKET_CAP_URL = "https://api.coinmarketcap.com/v1/ticker/?limit=200";

    /**
     * Constant value for the weather loader ID. Can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int COIN_LOADER_ID = 2;

    public static final String LOG_TAG = CoinsActivity.class.getName();

    /** Adapter for the list of weather */
    private CoinAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    TextView coinsOwndedTV;
    TextView coinSymbolTV;
    EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);

        // Find a reference to the {@link ListView} in the layout
        ListView coinListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        coinListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of weather as input
        mAdapter = new CoinAdapter(getApplicationContext(), new ArrayList<Coin>());

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
            android.app.LoaderManager loaderManager = getLoaderManager();

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(COIN_LOADER_ID, null, this);

        } else {

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No internet service");
        }

        coinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                coinSymbolTV = (TextView) v.findViewById(R.id.list_coin_symbol);
                String coinSymbol = coinSymbolTV.getText().toString();

                coinsOwndedTV = (TextView) v.findViewById(R.id.list_coin_owned);

                if(!coinsOwndedTV.getText().toString().equals("")){
                    String coinOwn = coinsOwndedTV.getText().toString();
                }

                double coinOwned = Hawk.get(coinSymbol);
                String sCoinOwned = Double.toString(coinOwned);
                coinsOwndedTV.setText(sCoinOwned);

                mInput = new EditText(CoinsActivity.this);

                mInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                mInput.setText(coinOwn);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CoinsActivity.this);

                TextView coinNameTV = (TextView) v.findViewById(R.id.list_coin_name);
                String coinName = coinNameTV.getText().toString();

                alertDialogBuilder
                        .setMessage(coinName)
                        .setView(mInput)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id)
                            {
                                String entered = mInput.getText().toString();
                                if(entered.equals("")) {
                                    Hawk.delete(coinSymbol);
                                }else {
                                    double own = Double.parseDouble(entered);
                                    Hawk.put(coinSymbol, own);
                                    coinsOwndedTV.setText(entered);
                                }
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

            }
        });

    }

    @Override
    public android.content.Loader<List<Coin>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new CoinLoader(getApplicationContext(), COIN_MARKET_CAP_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Coin>> loader, List<Coin> coins) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        // Set empty state text to display "No coins found."
        mEmptyStateTextView.setText("No coins found");

        // Clear the adapter of previous coin data
        mAdapter.clear();

        // If there is a valid list of {@link Coin}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (coins != null && !coins.isEmpty()) {
            mAdapter.addAll(coins);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Coin>> loader) {

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
