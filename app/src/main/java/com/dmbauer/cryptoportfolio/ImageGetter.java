package com.dmbauer.cryptoportfolio;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by davidbauer on 12/2/17.
 */

public class ImageGetter {

    private int image;

    public ImageGetter(String s){

        Map<String, Integer> imageMap = new HashMap<>();
        imageMap.put("BTC", R.drawable.btc2x);
        imageMap.put("ETH", R.drawable.eth2x);
        imageMap.put("BCH", R.drawable.bch2x);
        imageMap.put("XRP", R.drawable.xrp2x);
        imageMap.put("DASH", R.drawable.dash2x);
        imageMap.put("LTC", R.drawable.ltc2x);
        imageMap.put("XLM", R.drawable.xlm2x);
        imageMap.put("UKG", R.drawable.ic_ukg);
        imageMap.put("ARK", R.drawable.ark2x);
        imageMap.put("IOTA", R.drawable.miota2x);
        imageMap.put("ADA", R.drawable.ada2x);
        imageMap.put("ETC", R.drawable.etc2x);
        imageMap.put("NEO", R.drawable.neo2x);
        imageMap.put("LSK", R.drawable.lsk2x);
        imageMap.put("EOS", R.drawable.eos2x);
        imageMap.put("STRAT", R.drawable.strat2x);
        imageMap.put("XMR", R.drawable.xmr2x);

        if (imageMap.containsKey(s)) {
            image = imageMap.get(s);
        } else {
            image = R.drawable.ic_btc;
        }

    }

    public int getImage() {
        return image;
    }
}
