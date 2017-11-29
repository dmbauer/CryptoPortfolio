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

        if(Hawk.get("bitcoin") != null) {
            currentBitcoin = Hawk.get("bitcoin");
            currentBtc = Double.toString(currentBitcoin);
            btcEntered.setText(currentBtc);
        }

        if(Hawk.get("ethereum") != null) {
            currentEthereum = Hawk.get("ethereum");
            currentEth = Double.toString(currentEthereum);
            ethEntered.setText(currentEth);
        }

        if(Hawk.get("bitcoin_cash") != null) {
            currentBitcoinCash = Hawk.get("bitcoin_cash");
            currentBch = Double.toString(currentBitcoinCash);
            bchEntered.setText(currentBch);
        }

        if(Hawk.get("ripple") != null) {
            currentRipple = Hawk.get("ripple");
            currentXrp = Double.toString(currentRipple);
            xrpEntered.setText(currentXrp);
        }

        if(Hawk.get("dash") != null) {
            double currentDash = Hawk.get("dash");
            String currentDsh = Double.toString(currentDash);
            dashEntered.setText(currentDsh);
        }

        if(Hawk.get("litecoin") != null) {
            currentLitecoin = Hawk.get("litecoin");
            currentLtc = Double.toString(currentLitecoin);
            ltcEntered.setText(currentLtc);
        }

        if(Hawk.get("lumen") != null) {
            currentLumen = Hawk.get("lumen");
            currentXlm = Double.toString(currentLumen);
            xlmEntered.setText(currentXlm);
        }

        if(Hawk.get("unikoin_gold") != null) {
            currentUnikoinGold = Hawk.get("unikoin_gold");
            currentUkg = Double.toString(currentUnikoinGold);
            ukgEntered.setText(currentUkg);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CoinAddActivity.this, MainActivity.class);

                String btcEnt = btcEntered.getText().toString();

                if (btcEnt.isEmpty()){
                    Hawk.delete("bitcoin");
                }else {
                    double btc = Double.parseDouble(btcEnt);
                    Hawk.put("bitcoin", btc);
                }

                String ethEnt = ethEntered.getText().toString();

                if (ethEnt.isEmpty()){
                    Hawk.delete("ethereum");
                }else {
                    double eth = Double.parseDouble(ethEnt);
                    Hawk.put("ethereum", eth);
                }

                String bchEnt = bchEntered.getText().toString();

                if (bchEnt.isEmpty()){
                    Hawk.delete("bitcoin_cash");
                }else {
                    double bch = Double.parseDouble(bchEnt);
                    Hawk.put("bitcoin_cash", bch);
                }

                String xrpEnt = xrpEntered.getText().toString();

                if (xrpEnt.isEmpty()){
                    Hawk.delete("ripple");
                }else {
                    double xrp = Double.parseDouble(xrpEnt);
                    Hawk.put("ripple", xrp);
                }

                String dashEnt = dashEntered.getText().toString();

                if (dashEnt.isEmpty()){
                    Hawk.delete("dash");
                }else {
                    double dash = Double.parseDouble(dashEnt);
                    Hawk.put("dash", dash);
                }

                String ltcEnt = ltcEntered.getText().toString();

                if (ltcEnt.isEmpty()) {
                    Hawk.delete("litecoin");
                }else{
                    double ltc = Double.parseDouble(ltcEnt);
                    Hawk.put("litecoin", ltc);
                }

                String xlmEnt = xlmEntered.getText().toString();

                if (xlmEnt.isEmpty()){
                    Hawk.delete("lumen");
                }else {
                    double xlm = Double.parseDouble(xlmEnt);
                    Hawk.put("lumen", xlm);
                }

                String ukgEnt = ukgEntered.getText().toString();

                if (ukgEnt.isEmpty()) {
                    Hawk.delete("unikoin_gold");
                }else {
                    double ukg = Double.parseDouble(ukgEnt);
                    Hawk.put("unikoin_gold", ukg);
                }

                startActivity(intent);
            }

        });

    }
}
