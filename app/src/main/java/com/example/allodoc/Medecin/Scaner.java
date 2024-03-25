package com.example.allodoc.Medecin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allodoc.Auth.User;
import com.example.allodoc.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Scaner extends Fragment {


    private User user;
    private RequestQueue requestQueue;
    private TextView textScanned; // Use descriptive variable names

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scaner, container, false);

        Button scanButton = view.findViewById(R.id.scanButton);
        textScanned = view.findViewById(R.id.textscaned);
        user = User.getInstance(getContext());

        scanButton.setOnClickListener(v -> startScan());  // Use lambda expressions for concise click listeners

        return view;
    }

    private void startScan() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR Code");
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String[] qrData = result.getContents().split("\n");
                if (qrData.length >= 3) {
                    user.setIdfm(Integer.parseInt( qrData[0]));
                    user.setIdp_folder(Integer.parseInt(qrData[1]));
                    user.setNamefm(qrData[2]);
                    user.setNamep_folder(qrData[3]+qrData[4]);
                    user.setDescription(qrData[5]);
                    user.setExeperation(qrData[6]);
                    user.putShareFolder(user.getNamefm(), user.getIdfm(), user.getIdp_folder(), user.getIdm(), user.getDescription(), user.getExeperation(), new User.UserCallback() {
                        @Override
                        public void onUserReceived() {
                            Toast.makeText(getContext(), "dossier ajouter", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPatientNotFound() {

                        }

                        @Override
                        public void onPatientError(String errorMessage) {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight) {

                        }

                        @Override
                        public void onMedecinReceived(int medecinId, String reviews, String fax, String siteweb, String location) {

                        }

                        @Override
                        public void onMedecinError(String errorMessage) {

                        }
                    });
                    Log.d("Scanned code", result.getContents());
                } else {
                    Toast.makeText(requireActivity(), "Scan cancelled", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle scan errors (code omitted here)
                super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }
}