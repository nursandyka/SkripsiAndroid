package com.example.projekskripsi;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerTask extends AsyncTask<String,Integer,String > {
    protected String doInBackground(String... urls) {

        OutputStreamWriter outputPost = null;
        InputStream stream = null;
        String result = null;

        try {
            JSONObject data = new JSONObject();
            data.put("img",urls[1]);
            URL url = new URL(urls[0]);
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type","application/json");
            client.setDoOutput(true);
            client.setDoInput(true);
            client.connect();

            outputPost = new OutputStreamWriter(client.getOutputStream());
            outputPost.write(data.toString());
            outputPost.flush();

            stream = client.getInputStream();
            result = Helper.convertStreamToString(stream);

        } catch (MalformedURLException e) {
            Helper.Error(e.toString());
        } catch (IOException e) {
            Helper.Error(e.toString());
        } catch (Exception e) {
            Helper.Error(e.toString());
        }
        return result;
    }

    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String result) {
        //
    }

}
