package com.example.allodoc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allodoc.files.AddFileActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilesFragment extends Fragment {
    private static final int FOLDER_ID = 6;
    private RecyclerView recyclerView;
    private FilesAdapter filesAdapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_files, container, false);

        Button addFileButton = root.findViewById(R.id.add_file);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filesAdapter = new FilesAdapter(new ArrayList<>()); // Créez une instance de FilesAdapter
        recyclerView.setAdapter(filesAdapter);

        // Initialiser Retrofit dans onCreateView
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://allodoc.uxuitrends.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Appeler la méthode API pour récupérer les fichiers
        Call<FilesResponse> call = apiService.getFiles();

        call.enqueue(new Callback<FilesResponse>() {
            @Override
            public void onResponse(Call<FilesResponse> call, Response<FilesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Réponse reçue", Toast.LENGTH_SHORT).show();

                    List<FileModel> fileList = response.body().getData();
                    if (!fileList.isEmpty()) {
                        FileModel firstFile = fileList.get(0);
                        Log.d("MainActivity", "File ID: " + firstFile.getId());
                        Log.d("MainActivity", "File Path: " + firstFile.getPath());
                        Log.d("MainActivity", "File Description: " + firstFile.getDescription());
                        filesAdapter.setFileList(fileList); // Mettez à jour la liste des fichiers dans l'adaptateur
                    } else {
                        // Gérez le cas où la liste de fichiers est vide
                        Log.d("MainActivity", "La liste des fichiers est vide.");
                    }
                }
            }

            @Override
            public void onFailure(Call<FilesResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur lors de la récupération des fichiers: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lorsque le bouton est cliqué, ouvrez AddFileActivity
                Intent intent = new Intent(getActivity(), AddFileActivity.class);
                intent.putExtra("folder_id", FOLDER_ID); // Ajoutez la valeur folder_id à l'intent
                startActivity(intent);
            }
        });
        return root;
    }
}
