package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upmc.tomo.tp_alt_mozaigram.model.User;
import com.upmc.tomo.tp_alt_mozaigram.persists.Parameters;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletCodes;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletTags;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletURLs;
import com.upmc.tomo.tp_alt_mozaigram.servlets_communication.ServletRequestWrapper;
import com.upmc.tomo.tp_alt_mozaigram.servlets_communication.VolleyController;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> params = new HashMap<String, String>();
        params.put(Parameters.PARAMETER_USERNAME, username);
        params.put(Parameters.PARAMETER_PASSWORD, password);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ServletURLs.CONNECT_USER_SERVLET, new JSONObject(params),
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            if(response.getString(ServletTags.CONNECT_USER_SERVLET_TAG).equals(String.valueOf(ServletCodes.SUCCESS_CODE))) {
                                Persists.currentUser = new User(
                                        response.getString(ServletTags.USERNAME),
                                        response.getString(ServletTags.SESSION_KEY),
                                        response.getJSONArray(ServletTags.IMAGES),
                                        response.getJSONArray(ServletTags.FRIENDS),
                                        response.getJSONArray(ServletTags.FRIEND_REQUESTS));
                                Intent newIntent = new Intent(getApplicationContext(), Test_.class);
                                startActivity(newIntent);
                            }

                            /*
                            					localStorage.setItem("username", json.username);
						localStorage.setItem("sessionKey", json.sessionKey);
						localStorage.setItem("friends", json.friends);
						localStorage.setItem("friendRequests", json.friendRequests);
						localStorage.setItem("requestedpage", json.username);
						localStorage.setItem("images", json.images)
                             */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage().toString());
                    }
                })
/*        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Parameters.PARAMETER_USERNAME, username);
                params.put(Parameters.PARAMETER_PASSWORD, password);

                if(params == null)
                    Log.d(TAG, "params nill");
                else {
                    for (String s : params.keySet())
                        Log.d(TAG, params.get(s));
                }
                return params;
            }
        }*/;

        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, Persists.REQUEST_TAG);
    }

    @Click
    public void signupButton() {
        Intent newIntent = new Intent(getApplicationContext(), SignupActivity_.class);
        startActivity(newIntent);
        finish();
    }

}
