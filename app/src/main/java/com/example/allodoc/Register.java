package com.example.allodoc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allodoc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    private Button birthday_button;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private RadioGroup genderRadioGroup;
    private RadioGroup accountTypeRadioGroup;
    private Button registerButton;

    private RequestQueue requestQueue;
    private TextView loginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.cpassword);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        accountTypeRadioGroup = findViewById(R.id.account_type_radio_group);
        registerButton = findViewById(R.id.registerBtn);
        birthday_button = findViewById(R.id.birthday_button);
        loginNow = findViewById(R.id.loginNow);
        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // go to the login page
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

        // Initialize DatePickerDialog
        datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        birthday_button.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set click listener for birthday button
        birthday_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // Set click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String birthday = birthday_button.getText().toString();

                int selectedGenderRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedGenderRadioButton = findViewById(selectedGenderRadioButtonId);
                String gender = selectedGenderRadioButton.getText().toString();

                int selectedAccountTypeRadioButtonId = accountTypeRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedAccountTypeRadioButton = findViewById(selectedAccountTypeRadioButtonId);
                String accountType = selectedAccountTypeRadioButton.getText().toString();

                // Create JSON object with registration data
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("firstName", firstName);
                    jsonObject.put("lastName", lastName);
                    jsonObject.put("email", email);
                    jsonObject.put("phone", phone);
                    jsonObject.put("password", password);
                    jsonObject.put("birthday", birthday);
                    jsonObject.put("gender", gender);
                    jsonObject.put("accountType", accountType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send data to the API
                sendRegistrationData(jsonObject);
            }
        });
    }

    private void sendRegistrationData(JSONObject data) {
        // Define your API URL
        String url = "https://allodoc.uxuitrends.com/api/users";

        // Create a JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.e("Volley Error", error.toString());

            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}
