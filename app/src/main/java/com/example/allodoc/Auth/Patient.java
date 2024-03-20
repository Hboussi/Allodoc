package com.example.allodoc.Auth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Patient {
//    private String address;
//    private String weight;
//
//    // Singleton instance
//    private static Patient instance;
//
//    // Private constructor to prevent instantiation outside of the class
//    private Patient() {
//    }
//
//    // Singleton method to get an instance of the Patient class
//    public static synchronized Patient getInstance() {
//        if (instance == null) {
//            instance = new Patient();
//        }
//        return instance;
//    }
//
//    // Getters and setters for address
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    // Getters and setters for weight
//    public String getWeight() {
//        return weight;
//    }
//
//    public void setWeight(String weight) {
//        this.weight = weight;
//    }
Patient() {
}

    public int getPatientId(int userId) {
        int patientId = -1; // Default value if user ID is not found
        try {
            // API URL
            String apiUrl = "https://allodoc.uxuitrends.com/api/patients";
            URL url = new URL(apiUrl);

            // Establish HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
            JSONArray dataArray = jsonResponse.getJSONArray("data");

            // Iterate over data array to find the matching user ID
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject patientObject = dataArray.getJSONObject(i);
                int patientUserId = patientObject.getInt("user_id");
                if (patientUserId == userId) {
                    patientId = patientObject.getInt("id");
                    break;
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patientId;
    }
}
