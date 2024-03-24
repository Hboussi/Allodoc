package com.example.allodoc.files;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allodoc.AddFileActivity;
import com.example.allodoc.R;
import com.example.allodoc.files.FileModel;
import com.example.allodoc.files.FilesResponse;
import com.example.allodoc.patient.FolderAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilesFragment extends Fragment implements FilesAdapter.OnFileClickListener {
    private static final int FOLDER_ID = 6;
    private static final String ARG_FOLDER_ID = "folder_id";
    private int folderId;
    private RecyclerView recyclerView;
    private FilesAdapter filesAdapter;
    private com.example.allodoc.files.ApiService apiService;

    // Méthode newInstance pour créer une nouvelle instance de FilesFragment avec folderId
    public static FilesFragment newInstance(int folderId) {
        FilesFragment fragment = new FilesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FOLDER_ID, folderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_files, container, false);

        int folderId = getArguments() != null ? getArguments().getInt("folder_id") : FOLDER_ID;

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
        apiService = retrofit.create(com.example.allodoc.files.ApiService.class);

        Toast.makeText(getActivity(), "Folder ID: " + folderId, Toast.LENGTH_SHORT).show();

        // Appeler la méthode API pour récupérer les fichiers en fonction du folder_id
        FolderIdRequest folderIdRequest = new FolderIdRequest(folderId);
        Call<FilesResponse> call = apiService.getFilesByFolderId(String.valueOf(folderId));

        call.enqueue(new Callback<FilesResponse>() {
            @Override
            public void onResponse(Call<FilesResponse> call, Response<FilesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Réponse reçue", Toast.LENGTH_SHORT).show();

                    List<FileModel> fileList = response.body().getData();
                    if (!fileList.isEmpty()) {
                        filesAdapter.setFileList(fileList);
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
                // Lorsque le bouton est cliqué, ouvrez AddFileActivity avec le folderId
                Intent intent = new Intent(getActivity(), AddFileActivity.class);
                intent.putExtra("folder_id", folderId); // Ajoutez la valeur folder_id à l'intent
                startActivity(intent);
            }
        });
        filesAdapter.setOnFileClickListener(this);
        return root;
    }

    @Override
    public void onFileClick(int fileId, String path, String description) {
        // Créer une instance de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://allodoc.uxuitrends.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Créer une instance de votre ApiService
        ApiService apiService = retrofit.create(ApiService.class);

        // Effectuer la requête en utilisant votre interface ApiService pour obtenir les détails du fichier
        Call<FileResponse> fileCall = apiService.getFileDetails(fileId);
        fileCall.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FileResponse fileDetails = response.body();

                    String imageUrl = "https://allodoc.uxuitrends.com/" + path;
                    Log.d("FilesFragment", "Path : " + fileDetails.getPath());

                    // Rendre la visibilité de folderinfo visible
                    if (getView() != null) {
                        View folderInfoLayout = getView().findViewById(R.id.folderinfo);
                        folderInfoLayout.setVisibility(View.VISIBLE);

                        // Afficher la description dans le TextView descriptionFile
                        TextView descriptionTextView = folderInfoLayout.findViewById(R.id.descriptionFile);
                        descriptionTextView.setText(description);

                        Button deleteImg = folderInfoLayout.findViewById(R.id.delete);

                        // Supprimer le fichier lorsque l'utilisateur clique sur le bouton de suppression
                        deleteImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Effectuer la requête DELETE pour supprimer le fichier
                                Call<Void> deleteCall = apiService.deleteFile(fileId);
                                deleteCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            // Fichier supprimé avec succès
                                            Log.d("FilesFragment", "Fichier supprimé avec succès.");
                                            // Cacher folderinfo après la suppression
                                            folderInfoLayout.setVisibility(View.GONE);
                                        } else {
                                            Log.e("FilesFragment", "Erreur lors de la suppression du fichier.");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("FilesFragment", "Erreur lors de la suppression du fichier: " + t.getMessage());
                                    }
                                });
                            }
                        });

                        // Afficher l'image dans l'imageView avec l'ID "file"
                        ImageView fileImageView = folderInfoLayout.findViewById(R.id.file);
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_launcher_background) // Image de chargement temporaire
                                .error(R.drawable.add_folder) // Image à afficher en cas d'erreur de chargement
                                .into(fileImageView);
                    }
                } else {
                    Log.e("FilesFragment", "Erreur lors de la récupération des détails du fichier.");
                }
            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Log.e("FilesFragment", "Erreur lors de la récupération des détails du fichier: " + t.getMessage());
            }
        });
    }



}
