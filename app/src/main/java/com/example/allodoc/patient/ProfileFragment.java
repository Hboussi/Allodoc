package com.example.allodoc.patient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allodoc.Auth.User;
import com.example.allodoc.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private TextView textViewFirstName, textViewLastName, textViewEmail, textViewPhone,
            textViewBirthday, textViewGender, textViewAddress, textViewWeight;
    private LinearLayout patientinfo, profileinfo;
    private Button buttonModifyProfile, submit;
    private User user;
    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = User.getInstance(requireContext());
        int userId = user.getId();

        buttonModifyProfile = view.findViewById(R.id.buttonModifyProfile);

        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName= view.findViewById(R.id.textViewLastName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewBirthday = view.findViewById(R.id.textViewBirthday);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewWeight = view.findViewById(R.id.textViewWeight);
// set the information in the prfile
        textViewFirstName.setText(user.getFirstName());
        textViewLastName.setText(user.getLastName());
        textViewEmail.setText(user.getEmail());
        textViewPhone.setText(user.getPhone());
        textViewBirthday.setText(user.getBirthday());
        textViewAddress.setText(user.getAddress());
        textViewWeight.setText(user.getWeight());

        return view;
    }
}
