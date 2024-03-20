package com.example.allodoc.files;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.allodoc.FileUtils;
import com.example.allodoc.R;
import com.example.allodoc.VolleyMultipartRequest;

import java.util.HashMap;
import java.util.Map;

public class AddFileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_SELECT = 1;
    private ImageView imageView;
    private EditText descriptionEditText;
    private Button selectImageButton;
    private Button saveButton;
    private Uri selectedImageUri;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        imageView = findViewById(R.id.imageView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        selectImageButton = findViewById(R.id.selectImageButton);
        saveButton = findViewById(R.id.saveButton);

        // ADD COLOR TO BUTTONS
        selectImageButton.setBackgroundColor(getResources().getColor(R.color.black));
        saveButton.setBackgroundColor(getResources().getColor(R.color.customButtonColor));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    private void saveFile() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "Sélectionnez une image d'abord", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadFile(selectedImageUri);
    }

    private void uploadFile(Uri imageUri) {
        // Convert the image URI to base64
        byte[] imageBytes = FileUtils.getFileDataFromUri(this, imageUri);
        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Base64 Length", "Length: " + base64Image.length());

        String description = descriptionEditText.getText().toString();

        // Initialize MultiPartRequest
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                "https://allodoc.uxuitrends.com/api/files?folder_id=6",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // Handle successful response
                        Toast.makeText(AddFileActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Toast.makeText(AddFileActivity.this, "Error uploading file", Toast.LENGTH_SHORT).show();
                Log.e("Volley Error", error.toString());
            }
        });

        // Set byte data using VolleyMultipartRequest's method
        multipartRequest.setByteData(getByteData(imageBytes));

        // Set description in params
        Map<String, String> params = new HashMap<>();
        params.put("description", description);
        multipartRequest.setParams(params);

        // Add the request to the RequestQueue
        requestQueue.add(multipartRequest);
    }

    private Map<String, VolleyMultipartRequest.DataPart> getByteData(byte[] imageBytes) {
        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
        params.put("path", new VolleyMultipartRequest.DataPart("path", imageBytes, "image/jpeg"));
        return params;
    }

}
