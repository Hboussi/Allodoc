package com.example.allodoc;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private static User instance;

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String birthday;
    private String gender;
    private String accountType;
    private String isActive;

    private RequestQueue requestQueue;

    private User() {
        // Private constructor to prevent instantiation
    }

    public static synchronized User getInstance(Context context) {
        if (instance == null) {
            instance = new User();
            instance.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return instance;
    }

    // Getters and Setters for user information

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }


        public void getUser(String email, final UserCallback callback) {
            String url = "https://allodoc.uxuitrends.com/api/FindByEmail";

            JSONObject requestData = new JSONObject();
            try {
                requestData.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestData,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.has("data")) {
                                    JSONObject userObject = response.getJSONObject("data");
                                    setId(userObject.getInt("id"));
                                    setFirstName(userObject.getString("first_name"));
                                    setLastName(userObject.getString("last_name"));
                                    setEmail(userObject.getString("email"));
                                    setPhone(userObject.getString("phone"));
                                    setBirthday(userObject.getString("birthday"));
                                    setGender(userObject.getString("gender"));
                                    setAccountType(userObject.getString("account_type"));
                                    setIsActive(userObject.getString("is_active"));
                                    // Add more fields as needed
                                    callback.onUserReceived();
                                } else {
                                    // No user found with this email
                                    Log.d("user class", "No user found with this email");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("user class", "Error parsing JSON");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d("user class", "Error getting user data");
                        }
                    });

            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        }
    public interface UserCallback {
        void onUserReceived();
    }
}
