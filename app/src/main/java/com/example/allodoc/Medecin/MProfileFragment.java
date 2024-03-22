package com.example.allodoc.Medecin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allodoc.Auth.User;
import com.example.allodoc.R;

public class MProfileFragment extends Fragment {
    TextView MFirstName ;
    TextView MLastName ;
    TextView MEmail ;
    TextView MPhone ;
    TextView MBirthday;
    TextView Mfax;
    TextView Msiteweb ;
    TextView loscation ;
    TextView Mreviews ;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_m_profile, container, false);
        user = User.getInstance(getContext());

        TextView MFirstName = view.findViewById(R.id.MFirstName);
        TextView MLastName = view.findViewById(R.id.MLastName);
        TextView MEmail = view.findViewById(R.id.MEmail);
        TextView MPhone = view.findViewById(R.id.MPhone);
        TextView MBirthday = view.findViewById(R.id.MBirthday);
        TextView Mfax = view.findViewById(R.id.Mfax);
        TextView Msiteweb = view.findViewById(R.id.Msiteweb);
        TextView loscation = view.findViewById(R.id.loscation);
        TextView Mreviews = view.findViewById(R.id.Mreviews);



        MFirstName.setText(user.getFirstName());
        MLastName.setText(user.getLastName());
        MEmail.setText(user.getEmail());
        MPhone.setText(user.getPhone());
        MBirthday.setText(user.getBirthday());
        Mfax.setText(user.getFax());
        Msiteweb.setText(user.getSiteweb());
        loscation.setText(user.getLoscation());
        Mreviews.setText(user.getReviews());



        // Inflate the layout for this fragment
        return view;
    }
}