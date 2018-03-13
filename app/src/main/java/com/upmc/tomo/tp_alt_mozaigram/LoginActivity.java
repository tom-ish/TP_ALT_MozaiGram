package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.toolbox.JsonObjectRequest;
import com.upmc.tomo.tp_alt_mozaigram.model.User;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletCodes;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletTags;
import com.upmc.tomo.tp_alt_mozaigram.task.ConnectUserRequestTask;
import com.upmc.tomo.tp_alt_mozaigram.servlets_communication.VolleyController;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tomo on 20/02/2018.
 */

@EActivity(R.layout.login_activity_layout)
public class LoginActivity extends Activity {
    final static String TAG = LoginActivity.class.getSimpleName();

    @ViewById
    EditText usernameInput;

    @ViewById
    EditText passwordInput;

    Integer doubledValue;

    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Persists.REQUEST_QUEUE = VolleyController.getInstance(getApplicationContext()).getRequestQueue();
    }

    @Click
    public void loginButton() {
        final String username = usernameInput.getText().toString().trim();
        final String password = passwordInput.getText().toString().trim();

        new ConnectUserRequestTask(
                username,
                password,
                new ConnectUserRequestTask.IConnectUserTaskResponse() {
                    @Override
                    public void processFinish(String response) {
                        try {
                            Log.d(TAG, response);
                            JSONObject responseJSONObject = new JSONObject(response);
                            if(responseJSONObject.getInt(ServletTags.CONNECT_USER_SERVLET_TAG) == ServletCodes.SUCCESS_CODE) {
                                Log.d(TAG, responseJSONObject.toString());
                                String sessionKey = responseJSONObject.getString(ServletTags.SESSION_KEY);

                                JSONArray imagesJSONArray = responseJSONObject.getJSONArray(ServletTags.IMAGES);
                                JSONArray friendsRequestsJSONArray = responseJSONObject.getJSONArray(ServletTags.FRIEND_REQUESTS);
                                JSONArray friendsJSONArray = responseJSONObject.getJSONArray(ServletTags.FRIENDS);
                                Persists.currentUser = new User(username, sessionKey, imagesJSONArray, friendsJSONArray, friendsRequestsJSONArray);

                                Intent newIntent = new Intent(getApplicationContext(), MainContainerActivity_.class);
                                startActivity(newIntent);
                                finish();
                            }
                            else {
                                Log.e(TAG, "returned code error : "+responseJSONObject.getInt(ServletTags.CONNECT_USER_SERVLET_TAG));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute();
    }

    @Click
    public void signupButton() {
        Intent newIntent = new Intent(getApplicationContext(), SignupActivity_.class);
        startActivity(newIntent);
        finish();
    }

}
