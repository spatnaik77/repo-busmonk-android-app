package com.busmonk.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.busmonk.R;
import com.busmonk.activities.ride.MainActivity;
import com.busmonk.config.Config;
import com.busmonk.http.HTTPRequestQueue;
import com.busmonk.service.User;
import com.busmonk.session.SessionManager;
import com.busmonk.session.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    SharedPreferenceManager sharedPreferenceManager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());

        sessionManager = SessionManager.getInstance();


        Thread timerThread = new Thread()
        {
            public void run()
            {
        try
        {
            sleep(3000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //Check if the user already logged in by querying in shared preference
            String authToken = sharedPreferenceManager.getAuthToken();
            if(authToken == null)
            {
                //User has not logged in
                openMobileActivity();

            }
            else
            {
                //Already logged in so directly take him to home screen
                addUserDetailsToSessionStore(authToken);

            }
        }
            }
        };
        timerThread.start();

    }
    private void addUserDetailsToSessionStore(String jwToken)
    {
        final String token = jwToken;
        String url = Config.BASE_URL_API + "/users/" + token + "?type=token";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Type userType = new TypeToken<User>() {}.getType();
                final Gson gson = new Gson();
                User u = (User) gson.fromJson(response.toString(), userType);
                sessionManager.putUser(u);

                openHomeActivity();
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                openMobileActivity();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void openMobileActivity()
    {
        Intent intentForHomeActivity = new Intent(getApplicationContext(), MobileActivity.class);
        startActivity(intentForHomeActivity);
    }

    private void openHomeActivity()
    {
        Intent intentForHomeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentForHomeActivity);
    }



}
