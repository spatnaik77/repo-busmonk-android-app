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
import com.busmonk.config.Config;
import com.busmonk.http.HTTPRequestQueue;
import com.busmonk.service.RegistrationStatus;
import com.busmonk.service.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class OtpActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private TextInputLayout otpLayout;
    EditText otpEditText;
    Button continueBtn;

    long mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        otpLayout   = (TextInputLayout) findViewById(R.id.otpLayout);
        otpEditText = (EditText)findViewById(R.id.otpEditText);
        continueBtn = (Button)findViewById(R.id.btn_continue);

        mobile = getIntent().getLongExtra("mobile", -1);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String otp = otpEditText.getText().toString();
                if(otp.length() != 6)
                {
                    otpEditText.setError("Please provide 6 digit OTP");
                    return;
                }
                continueBtn.setEnabled(false);
                invokeVarifyOTP(mobile, otp);

            }
        });

        invokeSendOTP(mobile);

    }

    private void invokeSendOTP(long mobile)
    {
        String url = Config.BASE_URL + "/signup/otp";
        final Type userType = new TypeToken<User>() {
        }.getType();
        final Gson gson = new Gson();
        User u = new User();
        u.setMobile(mobile);
        String payload = gson.toJson(u, userType);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                //don't do anything for now
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
                        otpEditText.setError("Invalid mobile");
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
    private void invokeVarifyOTP(final long mobile, String otp)
    {
        String url = Config.BASE_URL + "/signup/otp/varify";
        final Type userType = new TypeToken<User>() {
        }.getType();
        final Gson gson = new Gson();
        User u = new User();
        u.setMobile(mobile);
        u.setOtp(Integer.parseInt(otp));
        String payload = gson.toJson(u, userType);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                User userResponse = (User) gson.fromJson(response.toString(), userType);
                if (RegistrationStatus.MFA_COMPLETE == userResponse.getRegistrationStatus())
                {
                    //open setEmailPwd activity
                    Intent intent = new Intent(getApplicationContext(), SetemailpasswordActivity.class);
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
                else
                {
                    //???   TODO -
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
                        otpEditText.setError("Invalid OTP");
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
