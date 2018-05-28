package com.ajou.vote;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.ajou.vote.MainActivity.IP_ADDR;
import static com.ajou.vote.MainActivity.pos;

public class ResultActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    TextView txt_per, txt_name;
    PieChart pieChart;
    BarChart barChart;
    ConstraintLayout layout;
    Spinner spinner_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txt_name = findViewById(R.id.txt_name);
        pieChart = findViewById(R.id.pie);
        barChart = findViewById(R.id.bar);
        layout = findViewById(R.id.layout);
        spinner_name = findViewById(R.id.spinner_name);;

        layout.setVisibility(View.GONE);

        // vote result pie chart
        pieChart.setUsePercentValues(true); // percent value
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setHoleRadius(45f); // center circle size
        pieChart.setHoleColor(Color.GRAY);
        pieChart.setTransparentCircleAlpha(10);
        pieChart.setRotationAngle(270);
        pieChart.setRotationEnabled(false); // rotation
        pieChart.setCenterTextSize(30);
        pieChart.setDescription(null);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad); // animation
        pieChart.setTouchEnabled(true);
        pieChart.getLegend().setEnabled(true); // legend label
        pieChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);

        new getResultPie().execute(IP_ADDR + "/result/pie?vote_title=" + MainActivity.title[pos]);

        // name spinner
        final ArrayList<String> arr = new ArrayList<String>();
        final ArrayAdapter<String> adt_name = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr);

        arr.add("투표 인원 명단");
        spinner_name.setAdapter(adt_name);
        for(int i = 0; i < 10; i++)
            arr.add("name "+i);
        spinner_name.setAdapter(adt_name);
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

        layout.setVisibility(View.VISIBLE);

        PieEntry e = (PieEntry) entry;
        txt_name.setText(e.getLabel()); // set up label name as title

        barChart.getDescription().setEnabled(false);
        barChart.setBackgroundColor(Color.rgb(21,21,21));
        barChart.setBorderColor(Color.GRAY);
        barChart.getXAxis().setEnabled(false); // hide x axis grid
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        barChart.getLegend().setEnabled(false); // hide label

        // set x axis start, end range
        XAxis bottomAxis = barChart.getXAxis();
        bottomAxis.setAxisMinimum(-0.75f);
        bottomAxis.setAxisMaximum(1.75f);
        // set y axis text color
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setAxisMinimum(0f);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setAxisMinimum(0f);
        rightAxis.setTextColor(Color.GRAY);
        // add data - line, bar graph
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();

        for (int index = 0; index < 2; index++)
            entries1.add(new BarEntry(index, index + 2));

        BarDataSet set1 = new BarDataSet(entries1, "error");
        set1.setColors(Color.RED, Color.BLUE);
        set1.setValueTextColor(Color.rgb(61, 99, 176));
        set1.setValueTextSize(0f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData data = new BarData(set1);
        data.setBarWidth(0.55f);
        data.addDataSet(set1);

        barChart.setData(data);
        barChart.invalidate();

    }

    @Override
    public void onNothingSelected() {

    }

    // server connection
    private class getResultPie extends AsyncTask<String, String, String> {

        JSONObject json;

        @Override
        protected String doInBackground(String... urls) {

            json = null;

            try {
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    InputStream stream = null;
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    for (int i = 0; i < urls.length; i++) {

                        URL url = new URL(urls[i]);
                        con = (HttpURLConnection) url.openConnection();
                        con.connect();
                        stream = con.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(stream));
                        while ((line = reader.readLine()) != null)
                            buffer.append(line + "\n");
                    }

                    return buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null)
                        con.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String str) {

            float[] yData = new float[10];
            String[] label = new String[50];
            String[] arr = str.split("\"");
            int j = 0;
            for (int i = 3; i < arr.length; i += 8) {

                label[j] = arr[i];
                yData[j++] = Integer.parseInt(arr[i + 4]);
            }

            // add pie data
            ArrayList<PieEntry> entry = new ArrayList<PieEntry>();
            for (int i = 0; i < j; i++)
                entry.add(new PieEntry(yData[i], label[i]));
            PieDataSet dataSet = new PieDataSet(entry, null);
            dataSet.setColors(ColorTemplate.PASTEL_COLORS);

            // show pie chart
            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            pieChart.setData(data);
            pieChart.invalidate();
        }
    }
}
