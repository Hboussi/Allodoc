package com.example.allodoc;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static byte[] getFileDataFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "Error reading file data from URI: " + e.getMessage());
            return null;
        }
    }

    public static File getFile(Context context, Uri uri) {
        File file = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                file = new File(context.getCacheDir(), "temp_file");
                FileInputStream fileInputStream = new FileInputStream(fileDescriptor);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileInputStream.close();
                fileOutputStream.close();
                parcelFileDescriptor.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error creating temporary file: " + e.getMessage());
        }
        return file;
    }
}
