package com.example.allodoc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private Button go_tologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        go_tologin = findViewById(R.id.go_tologin);

        Button getUserButton = findViewById(R.id.getUserButton);
        getUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Execute AsyncTask to fetch data
//                new FetchDataAsyncTask().execute("https://allodoc.uxuitrends.com/api/users");
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        go_tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Home.class));
                finish();
            }
        });
    }

    private class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Process JSON data and update UI
            try {
                JSONObject response = new JSONObject(result);
                JSONArray users = response.getJSONArray("data");

                // Assuming you want to display the first user's data
                JSONObject user = users.getJSONObject(1); // Index 1 because index 0 is an empty object in the provided JSON

                String name = user.getString("first_name") + " " + user.getString("last_name");
                String email = user.getString("email");

                nameTextView.setText("Name: " + name);
                emailTextView.setText("Email: " + email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
