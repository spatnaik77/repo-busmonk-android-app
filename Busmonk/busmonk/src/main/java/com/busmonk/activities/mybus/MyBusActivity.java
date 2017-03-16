package com.busmonk.activities.mybus;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.busmonk.R;
import com.busmonk.config.Config;
import com.busmonk.http.HTTPRequestQueue;
import com.busmonk.service.User;
import com.busmonk.service.UserRoute;
import com.busmonk.session.SessionManager;
import com.busmonk.util.BusmonkApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MyBusActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    private LinearLayout containerLayout;
    private Toolbar mToolbar;
    private Button confirmPayButton;

    List<UserRoute> userRouteList;


    //Inputs
    int action;

    private int ACTION_VIEW = 1;//view the user routes
    private int ACTION_CONFIRM = 2;//after search, show routes for the user to confirm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybuss);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        containerLayout = (LinearLayout)findViewById(R.id.container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        confirmPayButton = (Button) findViewById(R.id.btn_confirm_pay);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Read inputs passed by caller
        action = getIntent().getIntExtra("action", -1);

        userRouteList = new ArrayList<UserRoute>();

        if(ACTION_CONFIRM == action)
        {
            //Read from Application global variable
            UserRoute homeOffice = (UserRoute)((BusmonkApplication) this.getApplication()).get("home-office");
            UserRoute officeHome = (UserRoute)((BusmonkApplication) this.getApplication()).get("office-home");

            if(homeOffice != null)
            {
                userRouteList.add(homeOffice);
            }
            if(officeHome != null)
            {
                userRouteList.add(officeHome);
            }
            adapter = new MyBusAdapter(this, userRouteList);
            recyclerView.setAdapter(adapter);
            this.setTitle("Confirmation");
        }
        else if(ACTION_VIEW == action)
        {
            //get the user Id. make a rest call and get the UserRouteDetail objects
            confirmPayButton.setVisibility(View.INVISIBLE);
            User u = SessionManager.getInstance().getUser();
            if(u != null)
            {
                adapter = new MyBusAdapter(this, userRouteList);
                recyclerView.setAdapter(adapter);
                this.setTitle("My bus");

                showUserRoutes(u.getId());
            }

        }

        confirmPayButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                List<UserRoute> data = ((MyBusAdapter)adapter).getData();

                /*UserRoute userRouteHO;
                UserRoute userRouteOH;

                UserRoute userRouteHO = data.get(0);
                UserRoute urho = new UserRoute();
                urho.setName(userRouteHO.getName());
                urho.setBoardingPoint(userRouteHO.getBoardingPoint());
                urho.setDropPoint(userRouteHO.getDropPoint());
                urho.setBusId(userRouteHO.getBusId());
                urho.setUserId(userRouteHO.getUserId());

                UserRouteDetail userRouteDetailOH = data.get(1);
                UserRoute uroh = new UserRoute();
                uroh.setName(userRouteDetailOH.getName());
                uroh.setBoardingPoint(userRouteDetailOH.getBoardingPoint().getId());
                uroh.setDropPoint(userRouteDetailOH.getDropPoint().getId());
                uroh.setBusId(userRouteDetailOH.getBus().getId());
                uroh.setUserId(userRouteDetailOH.getUserId());*/
                confirmPayButton.setEnabled(false);
                sendCreateUserRouteRequest(data.get(0), data.get(1));

            }
        });



    }
    private void sendCreateUserRouteRequest(UserRoute urho, final UserRoute uroh) {
        String url = Config.BASE_URL_API + "/users/" + urho.getUserId() + "/userroutes";
        final Type userRouteType = new TypeToken<UserRoute>() {
        }.getType();
        final Gson gson = new Gson();
        String payload = gson.toJson(urho, userRouteType);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                sendCreateUserRouteRequestForOfficeHome(uroh);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                confirmPayButton.setEnabled(true);
                if (error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 500)
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
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
    private void sendCreateUserRouteRequestForOfficeHome(UserRoute uroh) {
        String url = Config.BASE_URL_API + "/users/" + uroh.getUserId() + "/userroutes";
        final Type userRouteType = new TypeToken<UserRoute>() {
        }.getType();
        final Gson gson = new Gson();
        String payload = gson.toJson(uroh, userRouteType);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                //success. what to do next ?  // TODO: 12/14/16
                showMessageInSB("Successfully saved");
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                confirmPayButton.setEnabled(true);
                if (error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 500)
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
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showUserRoutes(String userId)
    {
        String url = Config.BASE_URL_API + "/users/" + userId + "/userroutes";
        final Type userRouteType = new TypeToken<List<UserRoute>>() {}.getType();
        final Gson gson = new Gson();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() == 0) {
                    showMessageInSB("No data found");
                } else {
                    String jsonOutput = response.toString();
                    List<UserRoute> resultList = (List<UserRoute>) gson.fromJson(jsonOutput, userRouteType);
                    if(resultList != null)
                    {
                        userRouteList.addAll(resultList);
                        adapter.notifyDataSetChanged();

                    }
                    //adapter = new MyBusAdapter(getApplicationContext(), userRouteList);
                    //recyclerView.setAdapter(adapter);

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (error.networkResponse != null)
                {
                    if(error.networkResponse.statusCode == 500)
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

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    private void showMessageInSB(String msg)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
