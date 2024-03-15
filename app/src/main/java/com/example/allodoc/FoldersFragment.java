package com.example.allodoc;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.allodoc.Folder;
import com.example.allodoc.FolderAdapter;
import com.example.allodoc.R;

import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment {

    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private Button addFolderButton;
    private LinearLayout folderInfoLayout;
    private  Button ok;

    private  Button cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addFolderButton = view.findViewById(R.id.add_folder);
        folderInfoLayout = view.findViewById(R.id.folderinfo);
        ok = view.findViewById(R.id.okBtn);
        cancel = view.findViewById(R.id.cancelBtn);

        addFolderButton.setOnClickListener(new View.OnClickListener() {
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
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dossier ajouter avec success", Toast.LENGTH_SHORT).show();
                folderInfoLayout.setVisibility(View.GONE);
            }
        });
        // Create a list of folders (for testing purposes)
        List<Folder> folders = new ArrayList<>();
        folders.add(new Folder("Folder 1"));
        folders.add(new Folder("Folder 2"));
        // Add more folders as needed

        adapter = new FolderAdapter(folders);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
