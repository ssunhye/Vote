package com.ajou.vote;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RecyclerItem> items;

    final static String IP_ADDR = "http://182.230.139.141:3000";
    static int pos;
    static String[] title, description, candidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new getMain().execute(IP_ADDR + "/main");

        // floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CreateActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {

        super.onRestart();

        setContentView(R.layout.activity_main);

        new getMain().execute(IP_ADDR + "/main");

        // floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CreateActivity.class));
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

        @Override
        // create new view
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(v);

            return holder;

        }

        @Override
        // get current view
        public void onBindViewHolder(ItemViewHolder holder, final int position) {

            RecyclerItem item = items.get(position);
            holder.image.setImageResource(item.getImage());
            holder.title.setText(item.getTitle());
            holder.detail.setText(item.getDetail());
            holder.time.setText(item.getTime());

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pos = position;
                    new setMain().execute(IP_ADDR + "/select"
                            + "?vote_title=" + title[pos]
                            + "&member_name=" + LoginActivity.loginId);
                }
            });
        }

        @Override
        // get item count value
        public int getItemCount() {

            return items.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView image;
            private TextView title, detail, time;

            public ItemViewHolder(View view) {

                super(view);

                image = view.findViewById(R.id.imageView);
                title = view.findViewById(R.id.title);
                detail = view.findViewById(R.id.detail);
                time = view.findViewById(R.id.time);
            }
        }
    }

    // server connection
    public class getMain extends AsyncTask<String, String, String> {

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

            String[] arr = str.split("\"");
            title = new String[50];
            description = new String[50];
            candidate = new String[50];

            // recycler view
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            items = new ArrayList<RecyclerItem>();
            int j = 0;
            for (int i = 3; i < arr.length; i += 12) {

                title[j] = arr[i];
                description[j] = arr[i + 4];
                candidate[j] = arr[i + 8];
                items.add(new RecyclerItem(R.drawable.ls_normal, title[j], description[j++], String.valueOf(i / 12 + 1)));
            }

            layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MyAdapter();
            recyclerView.setAdapter(adapter);
        }
    }

    // server connection
    public class setMain extends AsyncTask<String, String, String> {

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

            if (str.contains("1")) // complete
                startActivity(new Intent(getApplicationContext(), ResultActivity.class));
            else // ready
                startActivity(new Intent(getApplicationContext(), VoteActivity.class));
        }
    }
}
