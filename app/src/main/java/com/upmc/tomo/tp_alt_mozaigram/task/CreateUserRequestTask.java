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
 * Created by Tomo on 20/02/2018.
 */

public class CreateUserRequestTask extends AsyncTask<String, Void, String> {
    static final String TAG = CreateUserRequestTask.class.getSimpleName();

    public interface ICreateUserTaskResponse {
        void processFinish(String response);
    }

    public ICreateUserTaskResponse delegate = null;

    String username, email, password, password2;

    public CreateUserRequestTask(String username, String email, String password, String password2, ICreateUserTaskResponse delegate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

//            username=toto&email=toto@toto.toto&password=passpass&password2=passpass"); // here is your URL path
            String parameters = "?username=" + username + "&email=" + email + "&password=" + password + "&password2=" + password2;
/*
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("username", username);
            postDataParams.put("email", email);
            postDataParams.put("password", password);
            postDataParams.put("password2", password2);
            Log.e("params",postDataParams.toString());
*/
            URL createUserURL = new URL(ServletURLs.CREATE_USER_SERVLET + parameters);

            HttpURLConnection conn = (HttpURLConnection) createUserURL .openConnection();
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

            int responseCode=conn.getResponseCode();

            Log.e(TAG, "OK : " + responseCode);

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
                return new String("false : "+responseCode);
            }
        }
        catch(Exception e){
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
