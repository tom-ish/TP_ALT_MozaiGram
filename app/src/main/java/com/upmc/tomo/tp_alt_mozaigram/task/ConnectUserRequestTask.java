package com.upmc.tomo.tp_alt_mozaigram.task;

import android.os.AsyncTask;
import android.util.Log;

import com.upmc.tomo.tp_alt_mozaigram.persists.ServletURLs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Tomo on 06/03/2018.
 */

public class ConnectUserRequestTask extends AsyncTask<String, Void, String> {
    static final String TAG = ConnectUserRequestTask.class.getSimpleName();

    String username, password;
    public IConnectUserTaskResponse delegate = null;

    public interface IConnectUserTaskResponse {
        void processFinish(String response);
    }

    public ConnectUserRequestTask(String username, String password, IConnectUserTaskResponse delegate) {
        this.username = username;
        this.password = password;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String parameters = "?username=" + username + "&password=" + password;
        try {
            URL connectUserURL = new URL(ServletURLs.CONNECT_USER_SERVLET + parameters);

            HttpURLConnection conn = (HttpURLConnection) connectUserURL.openConnection();
            conn.setReadTimeout(150000 /* milliseconds */);
            conn.setConnectTimeout(150000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Log.e(TAG, "OK");
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            //       writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            Log.e(TAG, "OK : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            } else {
                return new String("false : " + responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "return code :\n" + s);
        delegate.processFinish(s);
    }
}
