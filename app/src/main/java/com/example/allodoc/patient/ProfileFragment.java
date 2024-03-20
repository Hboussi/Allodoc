package com.example.allodoc.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.allodoc.R;


public class ProfileFragment extends Fragment {
    private TextView textViewFirstName, textViewLastName, textViewEmail, textViewPhone,
            textViewBirthday, textViewGender, textViewAddress, textViewWeight;

       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);




    }
}