package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.upmc.tomo.tp_alt_mozaigram.model.User;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletCodes;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletTags;
import com.upmc.tomo.tp_alt_mozaigram.task.ConnectUserRequestTask;
import com.upmc.tomo.tp_alt_mozaigram.task.CreateUserRequestTask;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tomo on 20/02/2018.
 */

@EActivity(R.layout.signup_activity_layout)
public class SignupActivity extends Activity {
    static final String TAG = SignupActivity.class.getSimpleName();

    @ViewById
    EditText usernameInput;

    @ViewById
    EditText emailInput;

    @ViewById
    EditText passwordInput;

    @ViewById
    EditText confirmPasswordInput;

    Integer doubledValue;


    @Click
    public void signupButton() {
        /*
        Map<String, String> params = new HashMap<String, String>();
        params.put(Parameters.PARAMETER_USERNAME, usernameInput.getText().toString());
        params.put(Parameters.PARAMETER_EMAIL, emailInput.getText().toString());
        params.put(Parameters.PARAMETER_PASSWORD, passwordInput.getText().toString());
        params.put(Parameters.PARAMETER_CONFIRM_PASSWORD, confirmPasswordInput.getText().toString());
        new ServletRequestWrapper(ServletURLs.CREATE_USER_SERVLET, params, getApplicationContext(),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ).addToRequestQueue();
        */

        new CreateUserRequestTask(
                usernameInput.getText().toString(),
                emailInput.getText().toString(),
                passwordInput.getText().toString(),
                confirmPasswordInput.getText().toString(),
                new CreateUserRequestTask.ICreateUserTaskResponse() {
                    @Override
                    public void processFinish(String response) {
                        try {
                            JSONObject responseJSONObject = new JSONObject(response);
                            if(responseJSONObject.getInt(ServletTags.CREATE_USER_SERVLET_TAG) == ServletCodes.SUCCESS_CODE) {
                                Log.d(TAG, responseJSONObject.getString(ServletTags.EMAIL));
                                final String username = responseJSONObject.getString(ServletTags.USERNAME);
                                final String password = responseJSONObject.getString(ServletTags.PASSWORD);

                                // On connecte automatiquement le nouvel utilisateur lorsque la creation de son cpmpte s'est deroulee avec succes
                                new ConnectUserRequestTask(
                                        username,
                                        password,
                                        new ConnectUserRequestTask.IConnectUserTaskResponse() {
                                            @Override
                                            public void processFinish(String response) {
                                                try {
                                                    JSONObject responseJSONObject = new JSONObject(response);
                                                    if(responseJSONObject.getInt(ServletTags.CONNECT_USER_SERVLET_TAG) == ServletCodes.SUCCESS_CODE) {
                                                        Log.d(TAG, responseJSONObject.toString());
                                                        String sessionKey = responseJSONObject.getString(ServletTags.SESSION_KEY);
                                                        JSONArray imagesJSONArray = responseJSONObject.getJSONArray(ServletTags.IMAGES);
                                                        JSONArray friendsRequestsJSONArray = responseJSONObject.getJSONArray(ServletTags.FRIEND_REQUESTS);
                                                        JSONArray friendsJSONArray = responseJSONObject.getJSONArray(ServletTags.FRIENDS);
                                                        Persists.currentUser = new User(username, sessionKey, imagesJSONArray, friendsJSONArray, friendsRequestsJSONArray);
                                                    }
                                                    else {
                                                        Log.e(TAG, "returned code error : "+responseJSONObject.getInt(ServletTags.CONNECT_USER_SERVLET_TAG));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                ).execute();
                            }
                            else {
                                Log.e(TAG, "returned code error : "+responseJSONObject.getInt(ServletTags.CREATE_USER_SERVLET_TAG));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, response);
                    }
                }).execute();
    }

    @Click
    public void loginButton() {
        Intent newIntent = new Intent(getApplicationContext(), LoginActivity_.class);
        startActivity(newIntent);
        finish();
    }

}
