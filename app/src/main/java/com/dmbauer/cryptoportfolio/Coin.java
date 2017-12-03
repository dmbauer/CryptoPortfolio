package com.dmbauer.cryptoportfolio;

/**
 * Created by davidbauer on 11/30/17.
 */

public class Coin {

    private String mCoinID;
    private String mCoinName;
    private String mCoinSymbol;
    private int mCoinRank;
    private double mCoinPriceUsd;
    private double mCoinPriceBtc;
    private double mCoinMarketCap;
    private double mCoin1Hour;
    private double mCoin24Hour;
    private double mCoin7D;

    public Coin(String coinID,
                String coinName,
                String coinSymbol,
                int coinRank,
                double coinPriceUsd,
                double coinPriceBtc,
                double coinMarketCap,
                double coin1Hour,
                double coin24Hour,
                double coin7D){

        mCoinID = coinID;
        mCoinName = coinName;
        mCoinSymbol = coinSymbol;
        mCoinRank = coinRank;
        mCoinPriceUsd = coinPriceUsd;
        mCoinPriceBtc = coinPriceBtc;
        mCoinMarketCap = coinMarketCap;
        mCoin1Hour = coin1Hour;
        mCoin24Hour =coin24Hour;
        mCoin7D = coin7D;

    }

    public String getCoinID() {
        return mCoinID;
    }

    public String getCoinName() {
        return mCoinName;
    }

    public String getCoinSymbol(){
        return mCoinSymbol;
    }

    public int getCoinRank() {
        return mCoinRank;
    }

    public double getCoinPriceUsd() {
        return mCoinPriceUsd;
    }

    public double getCoinPriceBtc() {
        return mCoinPriceBtc;
    }

    public double getCoinMarketCap() {
        return mCoinMarketCap;
    }

    public double getCoin1Hour() {
        return mCoin1Hour;
    }

    public double getCoin24Hour() {
        return mCoin24Hour;
    }

    public double getCoin7D() {
        return mCoin7D;
    }
}
