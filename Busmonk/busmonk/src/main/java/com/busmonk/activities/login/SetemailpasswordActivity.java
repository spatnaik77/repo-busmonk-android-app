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
import com.busmonk.service.RegistrationStatus;
import com.busmonk.service.User;
import com.busmonk.session.SessionManager;
import com.busmonk.session.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class SetemailpasswordActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    EditText emailEditText;
    EditText passwordEditText;
    Button continueBtn;

    long mobile;

    SharedPreferenceManager preferenceManager;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setemailpassword);


        preferenceManager = SharedPreferenceManager.getInstance(this);
        sessionManager = SessionManager.getInstance();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        emailLayout   = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordLayout   = (TextInputLayout) findViewById(R.id.passwordLayout);
        emailEditText = (EditText)findViewById(R.id.email);
        passwordEditText = (EditText)findViewById(R.id.password);
        continueBtn = (Button)findViewById(R.id.btn_continue);

        mobile = getIntent().getLongExtra("mobile", -1);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(email.length() <= 0)
                {
                    emailEditText.setError("Please provide your email");
                    return;
                }
                if(password.length() <= 6)
                {
                    passwordEditText.setError("Password should contain atleast 6 characters");
                    return;
                }
                continueBtn.setEnabled(false);
                invokeSetPassword(mobile, email, password);

            }
        });

    }
    private void invokeSetPassword(final long mobile, String email, final String password)
    {
        String url = Config.BASE_URL + "/signup/users";
        final Type userType = new TypeToken<User>() {
        }.getType();
        final Gson gson = new Gson();
        User u = new User();
        u.setMobile(mobile);
        u.setEmail(email);
        u.setPassword(password);
        String payload = gson.toJson(u, userType);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, payload, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                String jsonOutput = response.toString();
                User userResponse = (User) gson.fromJson(jsonOutput, userType);
                if(userResponse != null && userResponse.getRegistrationStatus() == RegistrationStatus.REGISTRATION_COMPLETE)
                {
                    invokeLogin(mobile, password);
                }

            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                continueBtn.setEnabled(true);
                if(error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 404)
                    {
                        passwordEditText.setError("Invalid mobile");
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
                final Type jwtType = new TypeToken<Jwt>() {}.getType();
                Jwt jwt = (Jwt) gson.fromJson(response.toString(), jwtType);
                //put into Shared pref manager
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
                continueBtn.setEnabled(true);
                if(error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 403)
                    {
                        showMessageInSB("Invalid mobile or password");
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
                /*if(error.getCause() instanceof JSONException)
                {
                    //null was returned as response
                    continueBtn.setEnabled(true);
                    showMessageInSB("Invalid mobile or password");
                }
                else
                {
                    showMessageInSB(error.getMessage());
                }*/
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
                User u = (User) gson.fromJson(response.toString(), userType);
                preferenceManager.putAuthToken(token);
                sessionManager.putUser(u);

                //open Main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                continueBtn.setEnabled(true);
                //showMessageInSB(error.getMessage());
                if(error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 404)
                    {
                        showMessageInSB("User not found");
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

    private void showMessageInSB(String msg)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
