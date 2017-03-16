package com.busmonk.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.busmonk.R;
import com.busmonk.activities.ride.MainActivity;
import com.busmonk.config.Config;
import com.busmonk.http.HTTPRequestQueue;
import com.busmonk.service.Jwt;
import com.busmonk.service.User;
import com.busmonk.session.SessionManager;
import com.busmonk.session.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class LoginActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private TextInputLayout passwordLayout;
    EditText passwordEditText;
    Button loginBtn;

    long mobile;

    SharedPreferenceManager preferenceManager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = SharedPreferenceManager.getInstance(this);
        sessionManager = SessionManager.getInstance();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        passwordLayout   = (TextInputLayout) findViewById(R.id.passwordLayout);
        passwordEditText = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.btn_login);

        mobile = getIntent().getLongExtra("mobile", -1);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String password = passwordEditText.getText().toString();
                if(password.length() <= 0)
                {
                    passwordEditText.setError("Provide password");
                    return;
                }
                loginBtn.setEnabled(false);
                invokeLogin(mobile, password);

            }
        });
    }

    private void invokeLogin(long mobile, String password)
    {
        String url = Config.BASE_URL + "/auth/login";
        final Type userType = new TypeToken<User>() {
        }.getType();
        final Gson gson = new Gson();
        User u = new User();
        u.setMobile(mobile);
        u.setPassword(password);
        String payload = gson.toJson(u, userType);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Type jwtType = new TypeToken<Jwt>() {}.getType();
                Jwt jwt = (Jwt) gson.fromJson(response.toString(), jwtType);
                /*if(jwt.getToken() == null)
                {
                    //login failed
                    passwordEditText.setError("Wrong password");
                }*/
                //else
                //{
                    if(jwt.getToken() != null)
                    {
                        addUserDetailsToSessionStore(jwt.getToken());
                    }
                //}
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loginBtn.setEnabled(true);
                if(error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 403)//forbidden
                    {
                        passwordEditText.setError("Wrong password");
                    }
                    else if(error.networkResponse.statusCode == 500)
                    {
                        showMessageInSB("Internal server error");
                    }
                    else
                    {
                        showMessageInSB(error.getMessage());
                    }
                }
                else
                {
                    showMessageInSB(error.getMessage());
                }
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
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
                if(response != null && response.length() > 0)
                {
                    User u = (User) gson.fromJson(response.toString(), userType);
                    preferenceManager.putAuthToken(token);
                    sessionManager.putUser(u);

                    //open Main activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loginBtn.setEnabled(true);
                showMessageInSB(error.getMessage());
            }
        });
        //request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showMessageInSB(String msg)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
