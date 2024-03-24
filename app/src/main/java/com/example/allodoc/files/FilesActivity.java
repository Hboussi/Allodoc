package com.example.allodoc.files;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allodoc.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilesActivity extends Fragment {
    private RecyclerView recyclerView;
    private FilesAdapter filesAdapter;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_files, container, false);

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
                    Toast.makeText(getActivity(), "Reponse Recut", Toast.LENGTH_SHORT).show();

                    List<FileModel> fileList = response.body().getData();
                    FileModel firstFile = fileList.get(0);
                    Log.d("MainActivity", "File ID: " + firstFile.getId());
                    Log.d("MainActivity", "File Path: " + firstFile.getPath());
                    Log.d("MainActivity", "File Description: " + firstFile.getDescription());
                    filesAdapter.setFileList(fileList); // Mettez à jour la liste des fichiers dans l'adaptateur
                }
            }

            @Override
            public void onFailure(Call<FilesResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Erreur lors de la récupération des fichiers: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
