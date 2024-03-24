package com.example.allodoc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.allodoc.Auth.Login;
import com.example.allodoc.patient.Home;

import com.example.allodoc.patient.Home;

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

    private ImageButton goToFilesButton;
    private Button getUserButton;
    private Button goToLoginButton;

    private View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        goToFilesButton = findViewById(R.id.to_files);
        getUserButton = findViewById(R.id.getUserButton);
        goToLoginButton = findViewById(R.id.go_tologin);
        fragmentContainer = findViewById(R.id.fragment_container);

        getUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(MainActivity.this, Login.class));
             finish();
            }
        });

        goToFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Button clicked");
                showFilesFragment();
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Home.class));
                finish();
            }
        });
    }

    private void showFilesFragment() {
        Fragment filesFragment = new com.example.allodoc.files.FilesFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, filesFragment)
                .commit();

        // Set background color to white to hide buttons
        fragmentContainer.setBackgroundColor(getResources().getColor(android.R.color.white));
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