package com.example.allodoc.patient;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

public class CompletInfo extends Fragment {
    private EditText PAddress;
    private EditText Pweight;
    private Button submit;
    private User user;
    private RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complet_info, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        user = User.getInstance(requireContext());
        int userId = user.getId();
         submit = view.findViewById(R.id.submit);
        PAddress = view.findViewById(R.id.PAddress);
        Pweight = view.findViewById(R.id.Pweight);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PAddress.getText().toString().isEmpty() && !Pweight.getText().toString().isEmpty()) {
                    user.setAddress(PAddress.getText().toString());
                    user.setWeight(Pweight.getText().toString());

                    JSONObject patientData = new JSONObject();
                    try {
                        patientData.put("mobile", user.getPhone());
                        patientData.put("birthday", user.getBirthday());
                        patientData.put("adress", user.getAddress());
                        patientData.put("weight", user.getWeight());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    sendPatientInformation(user.getIdp(), patientData);
                }
            }
        });

        return view;
    }
    private void sendPatientInformation(int patientId, JSONObject patientData) {
        String url = "https://allodoc.uxuitrends.com/api/patients/" + patientId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, patientData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Navigate back to ProfileFragment
                        Fragment profileFragment = new ProfileFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout, profileFragment);
                        transaction.addToBackStack(null); // Optional: Add fragment to back stack
                        transaction.commit();
                        Log.d("update response", "onResponse: updat sss");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("Patient update", "Error updating patient");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}