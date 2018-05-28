package com.ajou.vote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_signup;
    EditText txt_id, txt_pw;
    String id, pw, ph;
    static String loginId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_id = findViewById(R.id.editText_name);
        txt_pw = findViewById(R.id.editText_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = txt_id.getText().toString();
                pw = txt_pw.getText().toString();

                //
                // ### ENC
                //
                new getLogin().execute(IP_ADDR + "/login?name=" + id + "&password=" + pw);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });
    }

    // server connection
    private class getLogin extends AsyncTask<String, String, String> {

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

                ph = str.substring(str.indexOf(":") + 2, str.indexOf("}") - 1);
                loginId = id;
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); // enter main activity
            } else { // no member data

                ph = null;
                Toast.makeText(getApplicationContext(), "존재하지 않는 계정입니다.", Toast.LENGTH_LONG).show(); // alert
        }
        }
    }
}
