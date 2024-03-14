package com.example.allodoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailaddressLogin);
        password = findViewById(R.id.passwordLogin);

        Button loginBtn = findViewById(R.id.loginBtn);
        TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString().trim();
                String enteredPassword = password.getText().toString().trim();

                if (!enteredEmail.isEmpty() && !enteredPassword.isEmpty()) {
                    // Execute AsyncTask to fetch data from API
                    new FetchDataAsyncTask().execute("https://allodoc.uxuitrends.com/api/users", enteredEmail, enteredPassword);
                } else {
                    Toast.makeText(Login.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
    }

    private class FetchDataAsyncTask extends AsyncTask<String, Void, JSONObject> {

        private String enteredEmail;
        private String enteredPassword;

        @Override
        protected JSONObject doInBackground(String... params) {
            String apiUrl = params[0];
            enteredEmail = params[1];
            enteredPassword = params[2];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    in.close();
                    return new JSONObject(stringBuilder.toString());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace(); // Handle the exception, display an error message, or log it
                // Return null or throw an exception to indicate a failure in fetching data
                return null;
            }

        }
        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    JSONArray users = response.getJSONArray("data");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        String userEmail = user.getString("email");
                        String userPassword = user.getString("password");
                        Log.d("Login", userEmail);
                        Log.d("Login", userPassword);

                        if (userEmail.equals(enteredEmail)) {
                            if (userPassword.equals(enteredPassword)) {
                                startActivity(new Intent(Login.this, Home.class));
                                finish();
                                return;
                            } else {
                                Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    Toast.makeText(Login.this, "Email does not exist", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Login.this, "Failed to fetch data from server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
