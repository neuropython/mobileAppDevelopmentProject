package com.example.bioaddmed.ui.aditional;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CiteHttpRequest extends AsyncTask<String, Void, String> {
    private OnTaskCompleted listener;

    public CiteHttpRequest(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String quote;
        try {
            JSONObject jsonObject = new JSONObject(result);
            quote = jsonObject.getString("quote");
            listener.onTaskCompleted(quote); // Pass the quote to the listener
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String quote);
    }

}
