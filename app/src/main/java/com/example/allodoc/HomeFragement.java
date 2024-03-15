package com.example.allodoc;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragement extends Fragment {

    private TextView User_name;
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragement, container, false);

        // Initialize the TextView
        User_name = view.findViewById(R.id.userName);

        // Show the user name
        User user = User.getInstance();
        name = user.getFirstName();
        User_name.setText(name);

        return view;
    }
}
