package com.example.allodoc.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allodoc.R;
import com.example.allodoc.User;

public class HomeFragment extends Fragment {

    private TextView userNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_fragement, container, false);

        // Initialize the TextView
        userNameTextView = view.findViewById(R.id.userName);

        // Retrieve user name from User singleton
        User user = User.getInstance(this.getContext());
        String userName = user.getFirstName(); // Assuming you have the user's first name stored
        userNameTextView.setText(" "+userName);

        return view;
    }
}
