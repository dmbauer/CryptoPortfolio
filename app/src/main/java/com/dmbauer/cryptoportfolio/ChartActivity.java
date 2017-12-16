package com.dmbauer.cryptoportfolio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.orhanobut.hawk.Hawk;

public class ChartActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        TextView totalValueTextView = (TextView) findViewById(R.id.chart_total_value);

        PieChart pieChart = findViewById(R.id.pie_chart);
        double total = 0.0;
        if (Hawk.contains("coins")){
            List<Coin> coins = Hawk.get("coins");

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < coins.size(); i++) {
                double coinUsd = coins.get(i).getCoinPriceUsd();
                String coinSymbol = coins.get(i).getCoinSymbol();
                double coinOwn = Hawk.get(coinSymbol);
                double value = coinOwn * coinUsd;
                float own = (float) value;
                String descrip = "$" + NumberFormat.getNumberInstance(Locale.US).format(own) + " " + "(" + coinSymbol + ")";
                entries.add(new PieEntry(own, descrip));
                total = total + (value);
            }


            totalValueTextView.setText("$" + NumberFormat.getNumberInstance(Locale.US).format(total));

            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setSliceSpace(3);
            pieDataSet.setValueTextSize(0);
            pieDataSet.setValueTextColor(Color.parseColor("#757575"));
            pieChart.setEntryLabelColor(Color.parseColor("#757575"));
            pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            pieChart.setDrawHoleEnabled(false);

            Description description = new Description();
            description.setText("");
            pieChart.setDescription(description);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);

        } else {
            totalValueTextView.setText("Nothing in portfolio");
        }
    }

    }



