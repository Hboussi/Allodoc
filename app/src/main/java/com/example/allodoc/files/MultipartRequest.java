package com.example.allodoc.files;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private ByteArrayOutputStream bos;

    private final Response.Listener<String> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mStringParams;
    private final Map<String, File> mFileParams;

    public MultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        bos = new ByteArrayOutputStream();
        mListener = listener;
        mErrorListener = errorListener;
        mStringParams = new HashMap<>();
        mFileParams = new HashMap<>();
    }

    public void addStringParam(String key, String value) {
        mStringParams.put(key, value);
    }

    public void addFile(String key, File file) {
        mFileParams.put(key, file);
    }

    @Override
    protected Map<String, String> getParams() {
        return mStringParams;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success(new String(response.data), getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Ajoute les paramètres texte
            for (Map.Entry<String, String> entry : mStringParams.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }

            // Ajoute les fichiers
            for (Map.Entry<String, File> entry : mFileParams.entrySet()) {
                buildFilePart(dos, entry.getKey(), entry.getValue());
            }

            // Terminer la requête
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF);

            return bos.toByteArray();
        } catch (IOException e) {
            Log.e("MultipartRequest", "Error writing request body", e);
            return null;
        } finally {
            try {
                bos.close();
                dos.close();
            } catch (IOException e) {
                Log.e("MultipartRequest", "Error closing streams", e);
            }
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String key, String value) throws IOException {
        dataOutputStream.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + CRLF);
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(value + CRLF);
    }

    private void buildFilePart(DataOutputStream dataOutputStream, String key, File file) throws IOException {
        dataOutputStream.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\"" + CRLF);
        dataOutputStream.writeBytes(CRLF);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }

        dataOutputStream.writeBytes(CRLF);
        fileInputStream.close();
    }

    private static final String BOUNDARY = "*****";
    private static final String TWO_HYPHENS = "--";
    private static final String CRLF = "\r\n";

    public void addFile(String key, byte[] fileBytes, String fileName, String contentType) throws IOException {
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"" + CRLF);
        dos.writeBytes("Content-Type: " + contentType + CRLF);
        dos.writeBytes(CRLF);

        dos.write(fileBytes);

        dos.writeBytes(CRLF);
        dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF);

        dos.flush();
        dos.close();
    }
}
