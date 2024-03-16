package com.example.allodoc;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class FileUtil {
    public static String getPath(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }

        // Log pour afficher le chemin du fichier
        Log.d("FilePath", "Chemin du fichier : " + filePath);

        return filePath;
    }
}
