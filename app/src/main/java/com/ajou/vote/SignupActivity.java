package com.ajou.vote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import static com.ajou.vote.MainActivity.IP_ADDR;

public class SignupActivity extends AppCompatActivity {

    EditText txt_id, txt_pw, txt_ph;
    Button btn_chk, btn_join;
    CheckBox man, woman;
    String id, pw, ph;
    char mw;
    static boolean chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        chk = false;

        txt_id = findViewById(R.id.editText_joinName);
        txt_pw = findViewById(R.id.editText_joinPw);
        txt_ph = findViewById(R.id.editText_joinPhone);
        btn_chk = findViewById(R.id.btn_chk);
        btn_join = findViewById(R.id.btn_join);
        man = findViewById(R.id.checkBox_man);
        woman = findViewById(R.id.checkBox_woman);

        // select man
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(man.isChecked()) {

                    mw = 'm';
                    woman.setChecked(false);
                }
            }
        });
        // select woman
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(woman.isChecked()) {

                    mw = 'w';
                    man.setChecked(false);
                }
            }
        });
        // duplication check button
        btn_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ph = txt_ph.getText().toString();
                new getSignup().execute(IP_ADDR + "/dup" + "?phone=" + ph);
            }
        });
        // sign up button
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(chk == true) {
                    //
                    // ### ENC
                    //
                    id = txt_id.getText().toString();
                    pw = txt_pw.getText().toString();

                    // insert data to database
                    new setSignup().execute(IP_ADDR + "/signup"
                            + "?name=" + id
                            + "&password=" + pw
                            + "&phone=" + ph
                            + "&gender=" + mw);
                }else {

                    Toast.makeText(getApplicationContext(), "전화번호 중복확인이 필요합니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // server connection
    private class setSignup extends AsyncTask<String, String, String> {

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

            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show(); // alert
            finish();
        }
    }

    // server connection
    private class getSignup extends AsyncTask<String, String, String> {

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

            if (str.length() > 5) {

                chk = false;
                Toast.makeText(getApplicationContext(), "이미 등록되어 있는 번호입니다.", Toast.LENGTH_LONG).show(); // alert
            } else { // no member data

                chk = true;
                Toast.makeText(getApplicationContext(), "사용 가능한 번호입니다.", Toast.LENGTH_LONG).show(); // alert
            }
        }
    }
}
