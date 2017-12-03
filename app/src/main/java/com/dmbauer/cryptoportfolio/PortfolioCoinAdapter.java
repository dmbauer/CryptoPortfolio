package com.dmbauer.cryptoportfolio;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by davidbauer on 11/30/17.
 */

public class PortfolioCoinAdapter extends ArrayAdapter<Coin> {

    public PortfolioCoinAdapter(Context context, List<Coin> coins) {
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
                    R.layout.portfolio_list_item2, parent, false);
        }

        // Find the coin at the given position in the list of coins
        Coin currentCoin = getItem(position);

        String coinName = currentCoin.getCoinName();
        String coinID = currentCoin.getCoinID();
        String coinSymbol = currentCoin.getCoinSymbol();
        double coinPriceUsd = currentCoin.getCoinPriceUsd();
        int coinRank = currentCoin.getCoinRank();
        String sCoinOwn = "0.0";
        String sCoinWorth = "0.0";

        if (Hawk.get(coinSymbol) != null) {
            double coinOwn = Hawk.get(coinSymbol);
            sCoinOwn = Double.toString(coinOwn) + " " + coinSymbol;
            double coinWorth = coinOwn * coinPriceUsd;
            sCoinWorth = "$" + NumberFormat.getNumberInstance(Locale.US).format(coinWorth);
        }

        ImageGetter imageGetter = new ImageGetter(coinSymbol);
        int image = imageGetter.getImage();

        ImageView coinImageView = (ImageView) listItemView.findViewById(R.id.coin_image);
        coinImageView.setImageResource(image);

        TextView coinNameTextView = (TextView) listItemView.findViewById(R.id.coin_name);
        coinNameTextView.setText(coinName);

        // Find the TextView with view ID weather_text_view
        TextView coinPriceTextView = (TextView) listItemView.findViewById(R.id.coin_price);
        String sCoinPrice = "$" + NumberFormat.getNumberInstance(Locale.US).format(coinPriceUsd);
        coinPriceTextView.setText(sCoinPrice);

        TextView coinOwnedTextView = (TextView) listItemView.findViewById(R.id.coin_owned);
        coinOwnedTextView.setText(sCoinOwn);

        TextView coinWorthTextView = (TextView) listItemView.findViewById(R.id.coin_worth_usd);
        coinWorthTextView.setText(sCoinWorth);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
