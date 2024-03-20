package com.example.allodoc.Auth;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;

public class Register extends AppCompatActivity {

    // Declare your UI components and other variables here
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
    private User user;

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

        // Initialize user
        user = User.getInstance(this);

        // go to the login page
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        // Initialize DatePickerDialog
        datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
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

                // Empty validation
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty() || birthday.isEmpty()) {
                    Toast.makeText(Register.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Password and confirm password matching
                    if (!password.equals(confirmPassword)) {
                        Toast.makeText(Register.this, "Les mots de passe ne correspondent pas.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Create JSON object with registration data
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("first_name", firstName);
                            jsonObject.put("last_name", lastName);
                            jsonObject.put("email", email);
                            jsonObject.put("phone", phone);
                            jsonObject.put("birthday", birthday);
                            jsonObject.put("password", password);
                            jsonObject.put("gender", gender);
                            jsonObject.put("account_type", accountType);

                        } catch (JSONException e) {
                            Log.d("json", "error in the json object");
                            e.printStackTrace();
                        }

                        // Send data to the API
                        sendRegistrationData(jsonObject,accountType);
                    }
                }
            }
        });
    }

    private void sendRegistrationData(JSONObject data,String accountType) {
        // Define your API URL
        String url = "https://allodoc.uxuitrends.com/api/users";

        // Create a JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        // Retrieve user instance and set user email
                         user.setEmail(emailEditText.getText().toString());
                        String email= user.getEmail();
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        // Redirect user based on account type
                        if (accountType.equals("patient")) {
                            user.getUser(email, new User.UserCallback() {
                                @Override
                                public void onUserReceived() {
                                    // Extract user ID from the response
                                    int userId = user.getId();
                                    // Create patient with the obtained user ID
                                    createPatient(userId);
                                    // Start Home activity for patients

                                }
                                @Override
                                public void onPatientNotFound() {}
                                @Override
                                public void onPatientError(String errorMessage) {}

                                @Override
                                public void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight) {

                                }
                            });
                        } else {
                            user.getUser(email, new User.UserCallback() {
                                @Override
                                public void onUserReceived() {
                                    int userId= user.getId();
                                    createDoctor(userId);
                                    // Start MHome activity for other account types
                                    startActivity(new Intent(Register.this,MHome.class));
                                }
                                @Override
                                public void onPatientNotFound() {}
                                @Override
                                public void onPatientError(String errorMessage) {}

                                @Override
                                public void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight) {

                                }
                            });
                        }
                        finish(); // Finish current activity
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
        Volley.newRequestQueue(this).add(request);
    }
    // Creatoin of the[----- patien -----]
    private void createPatient(int userId) {
        // Define your API URL for creating a patient
        String url = "https://allodoc.uxuitrends.com/api/patients";
        // Create JSON object with patient data
        JSONObject patientData = new JSONObject();
        try {
            patientData.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest for creating a patient
        JsonObjectRequest patientRequest = new JsonObjectRequest(Request.Method.POST, url, patientData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        startActivity(new Intent(Register.this, Home.class));
                        Toast.makeText(Register.this, "Patient cree avec  sucsse", Toast.LENGTH_SHORT).show();
                        Log.d("Register", "Patient created successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.e("Register", "Error creating patient: " + error.toString());
            }
        });

        // Add the patient creation request to the RequestQueue
        requestQueue.add(patientRequest);
    }


    // Create a [----- doctor -----]

    private void createDoctor(int userId) {
        // Define your API URL for creating a doctor
        Log.d("User ID", String.valueOf(userId));
        String url = "https://allodoc.uxuitrends.com/api/medecins";

        // Create JSON object with doctor data
        JSONObject doctorData = new JSONObject();
        try {
            doctorData.put("loscation","Null");
            doctorData.put("user_id", userId);// Assuming speciality is a string parameter for the doctor
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest for creating a doctor
        JsonObjectRequest doctorRequest = new JsonObjectRequest(Request.Method.POST, url, doctorData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        startActivity(new Intent(Register.this, MHome.class));
                        Toast.makeText(Register.this, "Doctor created successfully", Toast.LENGTH_SHORT).show();
                        Log.d("Register", "Doctor created successfully");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.e("Register", "Error creating doctor: " + error.toString());
            }
        });

        // Add the doctor creation request to the RequestQueue
        requestQueue.add(doctorRequest);
    }





















}