package com.example.allodoc.Auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

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

    // patient information
    private  int idp;
    private String address;
    private String weight;
    // medecin information
    private int idm;
    private String fax;
    private String siteweb;
    // folder information
    private int idfm;
    private String namefm;
    private String description;
    private int idp_folder;
    private String namep_folder;
    private String exeperation;

    public int getIdfm() {
        return idfm;
    }

    public void setIdfm(int idfm) {
        this.idfm = idfm;
    }

    public String getNamefm() {
        return namefm;
    }

    public void setNamefm(String namefm) {
        this.namefm = namefm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdp_folder() {
        return idp_folder;
    }

    public void setIdp_folder(int idp_folder) {
        this.idp_folder = idp_folder;
    }

    public String getNamep_folder() {
        return namep_folder;
    }

    public void setNamep_folder(String namep_folder) {
        this.namep_folder = namep_folder;
    }

    public String getExeperation() {
        return exeperation;
    }

    public void setExeperation(String exeperation) {
        this.exeperation = exeperation;
    }




    public int getIdm() {
        return idm;
    }

    public void setIdm(int idm) {
        this.idm = idm;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSiteweb() {
        return siteweb;
    }

    public void setSiteweb(String siteweb) {
        this.siteweb = siteweb;
    }

    public String getLoscation() {
        return loscation;
    }

    public void setLoscation(String loscation) {
        this.loscation = loscation;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    private String loscation;
    private  String reviews;






    private RequestQueue requestQueue;

    private User() {}

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

    public  int getIdp(){return idp;}
    public void setIdp(int idp){this.idp=idp;}
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
    public String getAddress() {return address;}
        public void setAddress(String address) {this.address = address;}
    public String getWeight() {return weight;}

    public void setWeight(String weight) {this.weight = weight;}

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
    public void getPatientInformation(int userId, final UserCallback callback) {
        String url = "https://allodoc.uxuitrends.com/public/api/FindPatientByUserId?user_id=" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                int patientId = data.getInt("id");
                                setIdp(patientId);
                                String mobile = data.isNull("mobile") ? null : data.getString("mobile");
                                String birthday = data.isNull("birthday") ? null : data.getString("birthday");
                                String address = data.isNull("adress") ? null : data.getString("adress");
                                String weight = data.isNull("weight") ? null : data.getString("weight");
                                callback.onPatientReceived(patientId, mobile, birthday, address, weight);
                            } else if (response.has("error")) {
                                String errorMessage = response.getString("error");
                                callback.onPatientError(errorMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onPatientError("Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onPatientError("Error fetching patient information");
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    public void getMedecinInformation(int userId, final UserCallback callback) {
        String url = "https://allodoc.uxuitrends.com/api/FindMedecinByUserId?user_id=" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                int medecinId = data.getInt("id");
                                String reviews = data.isNull("reviews") ? null : data.getString("reviews");
                                String fax = data.isNull("fax") ? null : data.getString("fax");
                                String siteweb = data.isNull("siteweb") ? null : data.getString("siteweb");
                                String location = data.isNull("loscation") ? null : data.getString("loscation");
                                callback.onMedecinReceived(medecinId, reviews, fax, siteweb, location);
                            } else if (response.has("error")) {
                                String errorMessage = response.getString("error");
                                callback.onMedecinError(errorMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onMedecinError("Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onMedecinError("Error fetching medecin information");
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public void putShareFolder(String folderName, int folderId, int patientId, int medecinId, String description, String expiration, final UserCallback callback) {
        String url = "https://allodoc.uxuitrends.com/api/sharedfolder";

        JSONObject userInfoJson = new JSONObject();
        try {
            userInfoJson.put("folder_name", folderName);
            userInfoJson.put("folder_id", folderId);
            userInfoJson.put("patient_id", patientId);
            userInfoJson.put("medecin_id", medecinId);
            userInfoJson.put("description", description);
            userInfoJson.put("expiration", expiration);
        } catch (JSONException e) {
            // Handle JSON exception gracefully
            Log.e("JSON Exception", "Error creating JSON object", e);
            callback.onPatientError("Error creating JSON object");
            return; // Exit method if JSON creation fails
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, userInfoJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onUserReceived();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network error gracefully
                        callback.onPatientError("An internal server error occurred. Please try again later.");
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    public interface UserCallback {
        void onUserReceived();
        void onPatientNotFound();
        void onPatientError(String errorMessage);

        void onPatientReceived(int patientId, String mobile, String birthday, String address, String weight);

        void onMedecinReceived(int medecinId,String reviews,String fax,String siteweb,String location);

        void onMedecinError(String errorMessage);
    }

}
