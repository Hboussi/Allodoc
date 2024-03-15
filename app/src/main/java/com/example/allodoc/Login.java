package com.example.allodoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Recupere les donne du formulaire

        final EditText email = findViewById(R.id.emailaddressLogin);
        final EditText password = findViewById(R.id.passwordLogin);



        final Button loginbtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()){email.setError("Email ne peut pas être vide");}
                if (password.getText().toString().isEmpty()){password.setError("Mot de passe ne peut pas être vide");}
              if(!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty() ) {
                  startActivity(new Intent(Login.this, Home.class));
                  finish();
              }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });


    }
}