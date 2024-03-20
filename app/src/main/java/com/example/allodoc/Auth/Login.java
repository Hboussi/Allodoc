package com.example.allodoc.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allodoc.Medecin.MHome;
import com.example.allodoc.R;
import com.example.allodoc.patient.Home;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private User user;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerNowTextView;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = User.getInstance(this);

        // Initialize views
        emailEditText = findViewById(R.id.emailaddressLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginBtn);
        registerNowTextView = findViewById(R.id.registerNowBtn);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Perform login
                performLogin(email, password);
            }
        });

        // Set click listener for register now text view
        registerNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to registration activity
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
    }
    private void performLogin(String email, String password) {
        // Define your API URL
        String url = "https://allodoc.uxuitrends.com/api/login";

        // Create a JsonObjectRequest
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("email", email);
            requestData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        try {
                            if (response.has("token")) {
                                // Login successful
                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                user.setEmail(email);

                                user.getUser(email, new User.UserCallback() {
                                    @Override
                                    public void onUserReceived() {
                                        String account_type = user.getAccountType();
                                        if ("patient".equals(account_type)) {
                                            startActivity(new Intent(Login.this, Home.class));
                                        } else {
                                            startActivity(new Intent(Login.this, MHome.class));
                                        }
                                        finish();
                                    }

                                    @Override
                                    public void onPatientNotFound() {
                                    }
                                    @Override
                                    public void onPatientError(String errorMessage) {
                                    }

                                    @Override
                                    public void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight) {

                                    }
                                });
                            } else if (response.has("error") && "Unauthorized".equals(response.getString("error"))) {
                                // Login failed
                                Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Toast.makeText(Login.this, "An error occurred", Toast.LENGTH_SHORT).show();
                Log.e("Volley Error", error.toString());
            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}