package com.ajou.vote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class CreateActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;

    EditText editTitle, editExplanation;
    EditText editCandidate;
    String title;
    String explanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        editTitle = findViewById(R.id.editTitle);
        editExplanation = findViewById(R.id.editExplanation);
        editCandidate = findViewById(R.id.editCandidate);

        title = editTitle.getText().toString();
        explanation = editExplanation.getText().toString();

        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, items);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //plus button event
        Button plusBtn = findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edit = findViewById(R.id.editCandidate);
                String text = edit.getText().toString();
                if (!text.isEmpty()) {
                    items.add(text);
                    edit.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //minus button event
        Button minusBtn = findViewById(R.id.minusBtn);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = listView.getCheckedItemPosition();
                if (pos != ListView.INVALID_POSITION) {
                    items.remove(pos);
                    listView.clearChoices();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // upload button event
        Button uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new setCreate().execute(IP_ADDR + "/create"
                        + "?title=" + editTitle.getText()
                        + "&description=" + editExplanation.getText()
                        + "&candidate_name=" + items.toString());
            }
        });
    }

    // server connection
    private class setCreate extends AsyncTask<String, String, String> {

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

            Toast.makeText(getApplicationContext(), "새로운 투표가 생성되었습니다.", Toast.LENGTH_LONG).show(); // alert
            finish();
        }
    }
}