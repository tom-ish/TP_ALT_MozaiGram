package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.upmc.tomo.tp_alt_mozaigram.persists.Parameters;
import com.upmc.tomo.tp_alt_mozaigram.persists.ServletURLs;
import com.upmc.tomo.tp_alt_mozaigram.servlets_communication.ServletRequestWrapper;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    }


    @Click
    public void loginButton() {
        Intent newIntent = new Intent(getApplicationContext(), LoginActivity_.class);
        startActivity(newIntent);
        finish();
    }
}
