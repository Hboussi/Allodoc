package com.example.allodoc.patient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.allodoc.Auth.User;
import com.example.allodoc.R;
import com.example.allodoc.TokenService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.text.Html;




public class ShareFragment extends Fragment {



    private static final int DURATION_15_MINUTES = 15;
    private static final int DURATION_30_MINUTES = 30;
    private static final int DURATION_45_MINUTES = 45;
    private static final int DURATION_1_HOUR = 60;
    private static final int DURATION_2_HOURS = 120;
    private static final int DURATION_UNLIMITED = -1;





    private RecyclerView recyclerViewFolders;
    private FolderAdapter folderAdapter;
    private List<Folder> folderList;
    private FrameLayout folders;
    User user;
    int PatientID;
    TextView ici;
    int selectedFolderId;
    TextView folderNameTextView;
    String folderName;
    String folderDescription;
    private String selectedDuration;
    private TextView expirationTimeTextView;
    private ImageView imgQrCode;
    String expirationTime;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_share, container, false);


        imgQrCode = root.findViewById(R.id.img_qrcode);
        Button generateButton = root.findViewById(R.id.generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appel de la méthode pour générer le code QR avec les informations nécessaires
                generateQRCode();

                // Envoi de la requête POST vers Endpoint Tokens pour créer un token
                sendTokenRequest();
            }
        });


        Spinner spinnerDuration = root.findViewById(R.id.spinner_duration);
        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDuration = getResources().getStringArray(R.array.durations)[position];
                Toast.makeText(getContext(), selectedDuration, Toast.LENGTH_SHORT).show();
                calculateExpirationTime(selectedDuration);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recyclerViewFolders = root.findViewById(R.id.recyclerViewFolders);
        recyclerViewFolders.setLayoutManager(new LinearLayoutManager(requireContext()));
        folderList = new ArrayList<>();
        folderAdapter = new FolderAdapter(folderList);
        recyclerViewFolders.setAdapter(folderAdapter);
        folders = root.findViewById(R.id.folders);
        folderNameTextView = root.findViewById(R.id.flder_name); // Initialisation du TextView pour le nom du dossier
        expirationTimeTextView = root.findViewById(R.id.flder_name);

        user = User.getInstance(getContext());
        PatientID = user.getIdp();

        ici = root.findViewById(R.id.ici);
        ici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folders.getVisibility() == View.VISIBLE) {
                    folders.setVisibility(View.GONE);
                } else {
                    folders.setVisibility(View.VISIBLE);
                }
            }
        });

        folderAdapter.setOnFolderClickListener(new FolderAdapter.OnFolderClickListener() {
            @Override
            public void onFolderClick(int folderId) {
                selectedFolderId = folderId;
                folders.setVisibility(View.INVISIBLE);

                // Recherche du nom du dossier correspondant à l'ID sélectionné
                for (Folder folder : folderList) {
                    if (folder.getId() == folderId) {
                        folderName = folder.getName();
                        folderDescription = folder.getDescription();
                        folderNameTextView.setText(Html.fromHtml("Dossier "+"<font color='#0000FF'><b>"+folderName +"</b></font>"+" selectioné ✅")); // Affichage du nom du dossier dans le TextView
                        break; // Sortir de la boucle une fois que le nom du dossier est trouvé
                    }
                }
            }
        });

        getFoldersFromApi();

        return root;
    }

    private void getFoldersFromApi() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        FolderService folderService = retrofit.create(FolderService.class);

        PatientIdRequest patientIdRequest = new PatientIdRequest(String.valueOf(PatientID));

        Call<List<Folder>> call = folderService.getFoldersByPatientId(patientIdRequest);
        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    folderList.addAll(response.body());
                    folderAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                Toast.makeText(requireContext(), "Failed to fetch folders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateExpirationTime(String selectedDuration) {
        if (selectedDuration != null) {
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();

            int durationInMinutes = getDurationInMinutes(selectedDuration);

            if (durationInMinutes == DURATION_UNLIMITED) {
                expirationTimeTextView.setText("Illimité");
                return;
            }


            calendar.add(Calendar.MINUTE, durationInMinutes);


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            expirationTime = dateFormat.format(calendar.getTime());


            expirationTimeTextView.setText(expirationTime);
        }
    }

    private int getDurationInMinutes(String selectedDuration) {
        switch (selectedDuration) {
            case "15 min":
                return DURATION_15_MINUTES;
            case "30 min":
                return DURATION_30_MINUTES;
            case "45 min":
                return DURATION_45_MINUTES;
            case "1 h":
                return DURATION_1_HOUR;
            case "2 h":
                return DURATION_2_HOURS;
            case "Illimité":
                return DURATION_UNLIMITED;
            default:
                return 0; // Valeur par défaut
        }
    }


    private void generateQRCode() {
        // Concaténation des informations nécessaires pour le code QR
        String qrData = selectedFolderId + "\n" + PatientID + "\n" + folderName + "\n" + user.getFirstName() + "\n" + user.getLastName() + "\n" + folderDescription + "\n" + expirationTime;

        try {

            Bitmap bitmap = encodeAsBitmap(qrData);


            imgQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur lors de la génération du code QR", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 400, 400, null);
        } catch (IllegalArgumentException e) {
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }



    private void sendTokenRequest() {
        // Créer une instance de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://allodoc.uxuitrends.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Créer une instance du service API
        TokenService tokenService = retrofit.create(TokenService.class);

        // Appeler la méthode sendTokenRequest avec les paramètres nécessaires
        Call<Void> call = tokenService.sendTokenRequest(expirationTime.length() == 8 ? null : expirationTime, selectedFolderId);

        // Exécuter la requête de manière asynchrone
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Vérifier si la requête a été réussie
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Token created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to create token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Gérer les erreurs de connexion ici, par exemple afficher un message d'erreur
                Toast.makeText(requireContext(), "Failed to create token", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

