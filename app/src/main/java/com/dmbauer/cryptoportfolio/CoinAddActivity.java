package com.dmbauer.cryptoportfolio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.hawk.Hawk;

public class CoinAddActivity extends AppCompatActivity {

    Button mSaveButton;
    EditText btcEntered, ethEntered, bchEntered, xrpEntered, ltcEntered, ukgEntered, dashEntered, xlmEntered;
    double currentBitcoin, currentEthereum, currentBitcoinCash, currentRipple, currentLitecoin, currentLumen, currentUnikoinGold;
    String currentBtc, currentEth, currentBch, currentXrp, currentLtc, currentUkg, currentXlm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_add);

        mSaveButton = findViewById(R.id.save_button);
        btcEntered = findViewById(R.id.enter_btc);
        ethEntered = findViewById(R.id.enter_eth);
        bchEntered = findViewById(R.id.enter_bch);
        xrpEntered = findViewById(R.id.enter_xrp);
        ltcEntered = findViewById(R.id.enter_ltc);
        ukgEntered = findViewById(R.id.enter_ukg);
        dashEntered = findViewById(R.id.enter_dash);
        xlmEntered = findViewById(R.id.enter_xlm);

        if(Hawk.get("BTC") != null) {
            currentBitcoin = Hawk.get("BTC");
            currentBtc = Double.toString(currentBitcoin);
            btcEntered.setText(currentBtc);
        }

        if(Hawk.get("ETH") != null) {
            currentEthereum = Hawk.get("ETH");
            currentEth = Double.toString(currentEthereum);
            ethEntered.setText(currentEth);
        }

        if(Hawk.get("BCH") != null) {
            currentBitcoinCash = Hawk.get("BCH");
            currentBch = Double.toString(currentBitcoinCash);
            bchEntered.setText(currentBch);
        }

        if(Hawk.get("XRP") != null) {
            currentRipple = Hawk.get("XRP");
            currentXrp = Double.toString(currentRipple);
            xrpEntered.setText(currentXrp);
        }

        if(Hawk.get("DASH") != null) {
            double currentDash = Hawk.get("DASH");
            String currentDsh = Double.toString(currentDash);
            dashEntered.setText(currentDsh);
        }

        if(Hawk.get("LTC") != null) {
            currentLitecoin = Hawk.get("LTC");
            currentLtc = Double.toString(currentLitecoin);
            ltcEntered.setText(currentLtc);
        }

        if(Hawk.get("XLM") != null) {
            currentLumen = Hawk.get("XLM");
            currentXlm = Double.toString(currentLumen);
            xlmEntered.setText(currentXlm);
        }

        if(Hawk.get("UKG") != null) {
            currentUnikoinGold = Hawk.get("UKG");
            currentUkg = Double.toString(currentUnikoinGold);
            ukgEntered.setText(currentUkg);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CoinAddActivity.this, PortfolioActivity.class);

                String btcEnt = btcEntered.getText().toString();

                if (btcEnt.isEmpty()){
                    Hawk.delete("BTC");
                }else {
                    double btc = Double.parseDouble(btcEnt);
                    Hawk.put("BTC", btc);
                }

                String ethEnt = ethEntered.getText().toString();

                if (ethEnt.isEmpty()){
                    Hawk.delete("ETH");
                }else {
                    double eth = Double.parseDouble(ethEnt);
                    Hawk.put("ETH", eth);
                }

                String bchEnt = bchEntered.getText().toString();

                if (bchEnt.isEmpty()){
                    Hawk.delete("BCH");
                }else {
                    double bch = Double.parseDouble(bchEnt);
                    Hawk.put("BCH", bch);
                }

                String xrpEnt = xrpEntered.getText().toString();

                if (xrpEnt.isEmpty()){
                    Hawk.delete("XRP");
                }else {
                    double xrp = Double.parseDouble(xrpEnt);
                    Hawk.put("XRP", xrp);
                }

                String dashEnt = dashEntered.getText().toString();

                if (dashEnt.isEmpty()){
                    Hawk.delete("DASH");
                }else {
                    double dash = Double.parseDouble(dashEnt);
                    Hawk.put("DASH", dash);
                }

                String ltcEnt = ltcEntered.getText().toString();

                if (ltcEnt.isEmpty()) {
                    Hawk.delete("LTC");
                }else{
                    double ltc = Double.parseDouble(ltcEnt);
                    Hawk.put("LTC", ltc);
                }

                String xlmEnt = xlmEntered.getText().toString();

                if (xlmEnt.isEmpty()){
                    Hawk.delete("XLM");
                }else {
                    double xlm = Double.parseDouble(xlmEnt);
                    Hawk.put("XLM", xlm);
                }

                String ukgEnt = ukgEntered.getText().toString();

                if (ukgEnt.isEmpty()) {
                    Hawk.delete("UKG");
                }else {
                    double ukg = Double.parseDouble(ukgEnt);
                    Hawk.put("UKG", ukg);
                }

                startActivity(intent);
            }

        });

    }
}
