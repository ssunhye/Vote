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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        items = new ArrayList<RecyclerItem>();
        for (int i = 0; i < 5; i++)
            items.add(new RecyclerItem(R.drawable.ls_normal,
                    "6.15 지방선거",
                    "경기도 수원시장",
                    "6월 15일",
                    "16:15"));
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

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

            final int pos = position;
            RecyclerItem item = items.get(position);
            holder.image.setImageResource(item.getImage());
            holder.title.setText(item.getTitle());
            holder.detail.setText(item.getDetail());
            holder.date.setText(item.getDate());
            holder.time.setText(item.getTime());

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getApplicationContext(), ResultActivity.class));
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
            private TextView title, detail, date, time;

            public ItemViewHolder(View view) {

                super(view);

                image = view.findViewById(R.id.imageView);
                title = view.findViewById(R.id.title);
                detail = view.findViewById(R.id.detail);
                date = view.findViewById(R.id.date);
                time = view.findViewById(R.id.time);
            }
        }
    }
}
