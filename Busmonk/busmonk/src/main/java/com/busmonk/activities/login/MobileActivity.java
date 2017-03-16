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

import static com.busmonk.R.id.mobile;

public class MobileActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    private TextInputLayout mobileLayout;
    EditText mobileEditText;
    Button nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mobileLayout = (TextInputLayout) findViewById(R.id.mobileLayout);
        mobileEditText = (EditText)findViewById(mobile);
        nextBtn = (Button)findViewById(R.id.btn_next);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //CharSequence mobileNumber = mobileEditText.getText();
                String mobileNumber = mobileEditText.getText().toString();
                if(mobileNumber.length() != 10)
                {
                    mobileEditText.setError("Please provide your 10 digit mobile number");
                }
                else
                {
                    nextBtn.setEnabled(false);
                    invokeCreateUser(mobileNumber);
                }
            }
        });

    }
    private void invokeCreateUser(final String mobile)
    {
        String url = Config.BASE_URL + "/signup/users";
        final Type userType = new TypeToken<User>() {
        }.getType();
        final Gson gson = new Gson();
        User u = new User();
        u.setMobile(Long.parseLong(mobile));
        String payload = gson.toJson(u, userType);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                if(response != null && response.length() > 0)
                {
                    User userResponse = (User) gson.fromJson(response.toString(), userType);
                    if (RegistrationStatus.MFA_PENDING == userResponse.getRegistrationStatus()) {
                        //open otpActivity
                        Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                        intent.putExtra("mobile", Long.parseLong(mobile));
                        startActivity(intent);
                    } else if (RegistrationStatus.MFA_COMPLETE == userResponse.getRegistrationStatus()) {
                        //open setEmailPasswordActivity
                        Intent intent = new Intent(getApplicationContext(), SetemailpasswordActivity.class);
                        intent.putExtra("mobile", Long.parseLong(mobile));
                        startActivity(intent);
                    } else if (RegistrationStatus.REGISTRATION_COMPLETE == userResponse.getRegistrationStatus()) {
                        //open loginActivity
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("mobile", Long.parseLong(mobile));
                        startActivity(intent);
                    }
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                nextBtn.setEnabled(true);
                showMessageInSB(error.getMessage());

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
