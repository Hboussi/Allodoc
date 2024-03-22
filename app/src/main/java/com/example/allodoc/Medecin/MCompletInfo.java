package com.example.allodoc.Medecin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.allodoc.patient.ProfileFragment;

import org.json.JSONException;
import org.json.JSONObject;


public class MCompletInfo extends Fragment {
    private EditText reviews;
    private EditText location;

    private EditText siteweb;
    private EditText fax;
    private User user;
    private RequestQueue requestQueue;
    private Button submit;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m_complet_info, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        user = User.getInstance(requireContext());
        int userId = user.getId();
        submit = view.findViewById(R.id.Mtsubmit);
        fax = view.findViewById(R.id.Mtfax);
        siteweb = view.findViewById(R.id.Mtsiteweb);
        location = view.findViewById(R.id.Mtlocation);
        reviews = view.findViewById(R.id.Mtreviews);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fax.getText().toString().isEmpty() && !siteweb.getText().toString().isEmpty() && !location.getText().toString().isEmpty() && !reviews.getText().toString().isEmpty()) {
                  try{  user.setFax(fax.getText().toString());
                    user.setSiteweb(siteweb.getText().toString());
                    user.setLoscation(location.getText().toString());
                    user.setReviews(reviews.getText().toString());}
                  catch (Exception e){
                      Log.d("medecin singleton", "data dosent stored");
                  }
                    JSONObject medecinData = new JSONObject();
                    try {
                        medecinData.put("mobile", user.getPhone());
                        medecinData.put("birthday", user.getBirthday());
                        medecinData.put("fax", user.getFax());
                        medecinData.put("siteweb", user.getSiteweb());
                        medecinData.put("loscation", user.getLoscation());
                        medecinData.put("reviews", user.getReviews());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                  sendMedecinInformation(user.getIdm(),medecinData);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }


    private void sendMedecinInformation(int medecinId, JSONObject medecinData) {
        String url = "https://allodoc.uxuitrends.com/api/medecins/" + medecinId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, medecinData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Navigate back to ProfileFragment
                        Fragment MProfileFragment = new MProfileFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.mframeLayout, MProfileFragment);
                        transaction.addToBackStack(null); // Optional: Add fragment to back stack
                        transaction.commit();
                        Log.d("update response", "onResponse: update successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("Medecin update", "Error updating medecin");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}