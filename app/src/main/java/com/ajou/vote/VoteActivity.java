package com.ajou.vote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class VoteActivity extends AppCompatActivity{

    Button btn_com;
    TextView txt_title, txt_des;
    Spinner spinner_candidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        btn_com = findViewById(R.id.btn_com);
        txt_title = findViewById(R.id.txt_title);
        txt_des = findViewById(R.id.txt_des);
        spinner_candidate = findViewById(R.id.spinner_candidate);

        final int ptr = MainActivity.pos;
        txt_title.setText(" 제목  :  " + MainActivity.title[ptr]);
        txt_des.setText(" 설명  :  " + MainActivity.description[ptr]);
        // candidate spinner
        final ArrayList<String> arr = new ArrayList<String>();
        final ArrayAdapter<String> adt_name = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        arr.add("선택 가능 후보 명단");
        spinner_candidate.setAdapter(adt_name);
        String tmp = MainActivity.candidate[ptr].substring(1, MainActivity.candidate[ptr].length() - 1);
        String[] str = tmp.split(", ");
        for(int i = 0; i < str.length; i++)
            arr.add(str[i]);
        spinner_candidate.setAdapter(adt_name);

        // vote complete button
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new setVote().execute(IP_ADDR + "/vote"
                        + "?name=" + spinner_candidate.getSelectedItem()
                        + "&vote_title=" + MainActivity.title[ptr]
                        + "&member_name=" + LoginActivity.loginId);
            }
        });
    }

    // server connection
    private class setVote extends AsyncTask<String, String, String> {

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

            Toast.makeText(getApplicationContext(), "투표가 정상적으로 완료되었습니다.", Toast.LENGTH_LONG).show(); // alert
            finish();
        }
    }
}
