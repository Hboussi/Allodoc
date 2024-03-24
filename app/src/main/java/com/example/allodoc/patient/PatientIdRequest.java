package com.example.allodoc.patient;

import com.google.gson.annotations.SerializedName;

public class PatientIdRequest {
    @SerializedName("patient_id")
    private String patientId;

    public PatientIdRequest(String patientId) {
        this.patientId = patientId;
    }
}
