package com.ajou.vote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

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

        // candidate spinner
        final ArrayList<String> arr = new ArrayList<String>();
        final ArrayAdapter<String> adt_name = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr);

        arr.add("선택 가능 후보 명단");
        spinner_candidate.setAdapter(adt_name);
        for(int i = 0; i < 10; i++)
            arr.add("name "+i);
        spinner_candidate.setAdapter(adt_name);

        // vote complete button
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // exit intent
            }
        });
    }
}
