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

        image = imageMap.get(s);

    }

    public int getImage() {
        return image;
    }
}
