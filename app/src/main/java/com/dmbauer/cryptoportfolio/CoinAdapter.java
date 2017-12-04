package com.dmbauer.cryptoportfolio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.util.List;

/**
 * Created by davidbauer on 11/30/17.
 */

public class CoinAdapter extends ArrayAdapter<Coin> {

    public CoinAdapter(Context context, List<Coin> coins) {
        super(context, 0, coins);
    }

    /**
     * Returns a list item view that displays coin information.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.coin_list_item, parent, false);
        }

        // Find the weather at the given position in the list of weathers
        Coin currentCoin = getItem(position);

        String coinName = currentCoin.getCoinName();
        String coinID = currentCoin.getCoinID();
        String coinSymbol = currentCoin.getCoinSymbol();
        double coinPriceUsd = currentCoin.getCoinPriceUsd();
        int coinRank = currentCoin.getCoinRank();
        String sCoinOwn = "";

        if (Hawk.get(coinSymbol) != null) {
            double coinOwn = Hawk.get(coinSymbol);
            sCoinOwn = Double.toString(coinOwn);
        }


        // Find the TextView with view ID weather_text_view
        TextView coinNameTextView = (TextView) listItemView.findViewById(R.id.list_coin_name);
        // Display the location of the current earthquake in that TextView
        coinNameTextView.setText(coinName);

        TextView coinRankTextView = (TextView) listItemView.findViewById(R.id.list_coin_rank);
        String sCoinRank = "#" + Integer.toString(coinRank);
        coinRankTextView.setText(sCoinRank);

        TextView coinSymbolTextView = (TextView) listItemView.findViewById(R.id.list_coin_symbol);
        String sCoinSymbol = coinSymbol;
        coinSymbolTextView.setText(sCoinSymbol);

        TextView coinPriceUsdTextView = (TextView) listItemView.findViewById(R.id.list_coin_price_usd);
        String sCoinPriceUsd = "$" + Double.toString(coinPriceUsd);
        coinPriceUsdTextView.setText(sCoinPriceUsd);

        TextView coinOwnedTextView = (TextView) listItemView.findViewById(R.id.list_coin_owned);
        if (sCoinOwn != null) {
            coinOwnedTextView.setText(sCoinOwn);
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
