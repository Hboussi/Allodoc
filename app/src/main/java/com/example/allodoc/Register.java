package com.example.allodoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        final TextView loginNow = findViewById(R.id.loginNow);
        final TextView registerBtn = findViewById(R.id.registerBtn);
        final EditText firstNameEditText = findViewById(R.id.first_name);
        final EditText  lastNameEditText = findViewById(R.id.last_name);
        final EditText  phoneEditText = findViewById(R.id.phone);
        final EditText emailEditText = findViewById(R.id.email);
        final EditText   passwordEditText = findViewById(R.id.password);
        final EditText   cPasswordEditText = findViewById(R.id.cpassword);
        final EditText   birthdayEditText = findViewById(R.id.birthday);

        final CheckBox maleCheckBox = findViewById(R.id.checkboxMale);
        final CheckBox femaleCheckBox = findViewById(R.id.checkboxFemale);
        final CheckBox patientCheckBox = findViewById(R.id.checkboxPatient);
        final CheckBox medecinCheckBox = findViewById(R.id.checkboxMedecin);

        String gender = null;
        String acount_type = null;

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String cPassword = cPasswordEditText.getText().toString().trim();
                String birthday = birthdayEditText.getText().toString().trim();

                
//                boolean isMale = maleCheckBox.isChecked();
//                boolean isFemale = femaleCheckBox.isChecked();
//                boolean isPatient = patientCheckBox.isChecked();
//                boolean isMedecin = medecinCheckBox.isChecked();

                // Perform your validation or registration logic here

                // Display a toast message for testing
                String message = "First Name: " + firstName +
                        "\nLast Name: " + lastName +
                        "\nPhone: " + phone +
                        "\nEmail: " + email +
                        "\nPassword: " + password +
                        "\nConfirm Password: " + cPassword +
                        "\nBirthday: " + birthday;

                Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });



    }
}