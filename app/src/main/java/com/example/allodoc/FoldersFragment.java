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

import java.util.ArrayList;
import java.util.List;

public class FoldersFragment extends Fragment {

    private RecyclerView recyclerView;
    private FolderAdapter adapter;
    private Button addFolder;
    private LinearLayout Foldeinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addFolder = view.findViewById(R.id.add_folder); // corrected findViewById
        Foldeinfo = view.findViewById(R.id.folderinfo);
        addFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a new folder item when the button is clicked
                addNewFolder();
                Foldeinfo.setVisibility(View.VISIBLE);
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

    private void addNewFolder() {
        // Get the current list of folders from the adapter
        List<Folder> currentFolders = adapter.getFolders();

        // Create a new folder (you can replace "New Folder" with your desired default folder name)
        Folder newFolder = new Folder("New Folder");

        // Add the new folder to the list
        currentFolders.add(newFolder);

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }
}
