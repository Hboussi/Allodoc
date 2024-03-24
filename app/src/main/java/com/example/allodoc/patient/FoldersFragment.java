package com.example.allodoc.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allodoc.R;
import com.example.allodoc.files.FilesFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoldersFragment extends Fragment implements FolderAdapter.OnFolderClickListener {

    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private List<Folder> folders;
    private Button addFolder ;
    private LinearLayout folderInfoLayout;
    private TextView folderName;
    private TextView folderDescription;
    private  Button ok;

    private  Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addFolder = view.findViewById(R.id.add_folder);
        folderInfoLayout = view.findViewById(R.id.folderinfo);

        folderName = view.findViewById(R.id.folder_name);
        folderDescription = view.findViewById(R.id.folder_description);


        ok = view.findViewById(R.id.okBtn);
        cancel = view.findViewById(R.id.cancelBtn);
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of folderinfo
                if (folderInfoLayout.getVisibility() == View.VISIBLE) {
                    folderInfoLayout.setVisibility(View.GONE);
                } else {
                    folderInfoLayout.setVisibility(View.VISIBLE);
                    // Adjust layout parameters to take up the entire page
                    ViewGroup.LayoutParams params = folderInfoLayout.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    folderInfoLayout.setLayoutParams(params);
                }
            }
        });

        //Cance addin a folder
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderInfoLayout.setVisibility(View.GONE);
            }
        });

        //Ok send Folder data to the data base
        //Ok send Folder data to the data base
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (folderName.getText().toString().isEmpty() || folderDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Récupérer les valeurs des champs
                String name = folderName.getText().toString();
                String description = folderDescription.getText().toString();

                // Initialiser le service Retrofit pour l'API
                FolderService folderService = RetrofitClient.getRetrofitInstance().create(FolderService.class);

                // Créer un objet Folder à envoyer
                Folder folder = new Folder(name, description, 3, 7);

                // Appeler l'API pour ajouter le dossier
                Call<Folder> call = folderService.addFolder(folder);
                call.enqueue(new Callback<Folder>() {
                    @Override
                    public void onResponse(Call<Folder> call, Response<Folder> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Dossier ajouté avec succès", Toast.LENGTH_SHORT).show();
                            folderInfoLayout.setVisibility(View.GONE);
                            //refreshFoldersList();
                        } else {
                            Toast.makeText(getContext(), "Erreur API: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Folder> call, Throwable t) {
                        Toast.makeText(getContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        // Initialize Retrofit
        FolderService folderService = RetrofitClient.getRetrofitInstance().create(FolderService.class);

        // Call the API to get folders
        Call<ApiFolderResponse> call = folderService.getFolders();
        call.enqueue(new Callback<ApiFolderResponse>() {
            @Override
            public void onResponse(Call<ApiFolderResponse> call, Response<ApiFolderResponse> response) {
                if (response.isSuccessful()) {
                    ApiFolderResponse<List<Folder>> apiResponse = response.body();
                    folders = apiResponse != null ? apiResponse.getData() : null;
                    if (folders != null) {
                        adapter = new FolderAdapter(folders);
                        adapter.setOnFolderClickListener(FoldersFragment.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Empty response or null data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "API error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiFolderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onFolderClick(int folderId) {
        // Créer une instance de FilesFragment avec l'ID du dossier sélectionné
        FilesFragment filesFragment = FilesFragment.newInstance(folderId);

        // Passer l'ID du dossier sélectionné à FilesFragment
        Bundle args = new Bundle();
        args.putInt("folder_id", folderId);
        filesFragment.setArguments(args);

        // Remplacer le fragment actuel par FilesFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, filesFragment)
                .addToBackStack(null)
                .commit();
    }


    private void refreshFoldersList() {
        // Réinitialiser la liste des dossiers pour la recharger à partir de l'API
        FolderService folderService = RetrofitClient.getRetrofitInstance().create(FolderService.class);

        // Appeler l'API pour obtenir les dossiers mis à jour
        Call<ApiFolderResponse> call = folderService.getFolders();
        call.enqueue(new Callback<ApiFolderResponse>() {
            @Override
            public void onResponse(Call<ApiFolderResponse> call, Response<ApiFolderResponse> response) {
                if (response.isSuccessful()) {
                    ApiFolderResponse<List<Folder>> apiResponse = response.body();
                    folders = apiResponse != null ? apiResponse.getData() : null;
                    if (folders != null) {
                        adapter = new FolderAdapter(folders);
                        adapter.setOnFolderClickListener(FoldersFragment.this);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Empty response or null data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "API error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiFolderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
