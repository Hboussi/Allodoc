package com.example.allodoc.Medecin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.allodoc.R;
import com.example.allodoc.User;

public class MHome extends AppCompatActivity {
    private TextView text;
   // User user = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhome);
    }
}