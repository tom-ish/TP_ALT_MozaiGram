package com.upmc.tomo.tp_alt_mozaigram.servlets_communication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Tomo on 22/02/2018.
 */

public class ServletRequestWrapper {
    final static String TAG = ServletRequestWrapper.class.getSimpleName();

    JsonObjectRequest jsonObjectRequest;
    Context context;

    public ServletRequestWrapper(String servletURL, final Map<String, String> params, final Context context,
                                 Response.Listener<JSONObject> response, Response.ErrorListener errorListener) {

        //final String connectServletURL = "http://mozaigramgenerator.herokuapp.com/ConnectUserServlet/username="+username+"&password=" + password;
        this.context = context;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, servletURL, new JSONObject(params), response, errorListener);
    }

    public void addToRequestQueue() {
        // Adding Request to Request Queue
        VolleyController.getInstance(context).addToRequestQueue(jsonObjectRequest, Persists.REQUEST_TAG);
    }

}
