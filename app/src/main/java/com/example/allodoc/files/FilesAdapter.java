package com.example.allodoc.files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allodoc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FileViewHolder> {
    private List<FileModel> fileList;
    private OnFileClickListener listener;

    public FilesAdapter(List<FileModel> fileList) {
        this.fileList = fileList;
    }

    public void setOnFileClickListener(OnFileClickListener listener) {
        this.listener = listener;
    }

    public void setFileList(List<FileModel> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged(); // Notifiez l'adaptateur que le jeu de données a changé
    }
    public interface OnFileClickListener {
        void onFileClick(int fileId, String path, String description);
    }


    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileModel file = fileList.get(position);
        holder.textFileDescription.setText(file.getDescription());

        // Chargement de l'image à partir de l'URL avec le chemin d'accès
        Picasso.get()
                .load("https://allodoc.uxuitrends.com/" + file.getPath()) // Utilisation de l'URL avec le chemin d'accès
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.add_folder)
                .into(holder.imageFile);

        // Ajoutez un OnClickListener pour l'élément de fichier
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Déclenchez l'événement onFileClick de l'interface OnFileClickListener
                if (listener != null) {
                    listener.onFileClick(file.getId(), file.getPath(), file.getDescription()); // Passer l'ID du fichier cliqué
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return fileList == null ? 0 : fileList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView textFileDescription;
        ImageView imageFile;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            textFileDescription = itemView.findViewById(R.id.textFileDescription);
            imageFile = itemView.findViewById(R.id.imageFile);
        }
    }
}