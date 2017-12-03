package com.dmbauer.cryptoportfolio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import com.orhanobut.hawk.Hawk;

public class ChartActivity extends AppCompatActivity {

    float bitcoinValue, ethereumValue, bitcoinCashValue, rippleValue, litecoinValue, unikoinGoldValue, dashValue, lumenValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        PieChart pieChart = findViewById(R.id.pie_chart);

        ArrayList<PieEntry> entries = new ArrayList<>();

        if(Hawk.get("bitcoin") != null) {
            bitcoinValue = Hawk.get("bitcoin_value");
            entries.add(new PieEntry(bitcoinValue, "Bitcoin"));
        }

        if(Hawk.get("ethereum") != null) {
            ethereumValue = Hawk.get("ethereum_value");
            entries.add(new PieEntry(ethereumValue, "Ethereum"));
        }

        if(Hawk.get("bitcoin_cash") != null) {
            bitcoinCashValue = Hawk.get("bitcoin_cash_value");
            entries.add(new PieEntry(bitcoinCashValue, "Bitcoin Cash"));
        }

        if(Hawk.get("ripple") != null) {
            rippleValue = Hawk.get("ripple_value");
            entries.add(new PieEntry(rippleValue, "Ripple"));
        }

        if(Hawk.get("dash") != null) {
            dashValue = Hawk.get("dash_value");
            entries.add(new PieEntry(dashValue, "dash"));
        }

        if(Hawk.get("litecoin") != null) {
            litecoinValue = Hawk.get("litecoin_value");
            entries.add(new PieEntry(litecoinValue, "Litecoin"));
        }

        if(Hawk.get("lumen") != null) {
            lumenValue = Hawk.get("lumen_value");
            entries.add(new PieEntry(lumenValue, "Lumen"));
        }

        if(Hawk.get("unikoin_gold") != null) {
            unikoinGoldValue = Hawk.get("unikoin_gold_value");
            entries.add(new PieEntry(unikoinGoldValue, "Unikoin Gold"));
        }

        float portfolioValue = Math.round(((bitcoinValue + ethereumValue +
                bitcoinCashValue + rippleValue + litecoinValue + lumenValue + unikoinGoldValue + dashValue) * 100.0) / 100.0);
        String totalValue = Float.toString(portfolioValue);

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setValueTextColor(Color.parseColor("#212121"));
        pieChart.setEntryLabelColor(Color.parseColor("#757575"));
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.setCenterText("$" + totalValue);
        pieChart.setCenterTextSize(42);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
    }

}
