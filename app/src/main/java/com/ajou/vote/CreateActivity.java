package com.ajou.vote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, items);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //plus button event
        Button plusBtn = findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edit = findViewById(R.id.edit);
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
                finish();
            }
        });
    }
}
