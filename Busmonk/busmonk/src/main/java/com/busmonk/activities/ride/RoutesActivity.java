package com.busmonk.activities.ride;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.busmonk.R;
import com.busmonk.activities.mybus.MyBusActivity;
import com.busmonk.config.Config;
import com.busmonk.http.HTTPRequestQueue;
import com.busmonk.service.UserRouteDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sr250345 on 10/18/16.
 */

public class RoutesActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    private LinearLayout containerLayout;
    private Toolbar mToolbar;
    private Button nextButton;

    private double sourceLat;
    private double sourceLongt;
    private double destLat;
    private double destLongt;
    private String title;
    private String userRouteTag;
    private int action;


    private static int SHOW_ROUTES = 1;
    private static int SHOW_SELECTED_ROUTES_SUMMARY = 2;

    List<UserRouteDetail> data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        containerLayout = (LinearLayout)findViewById(R.id.container);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        nextButton = (Button) findViewById(R.id.btn_next);
        //Make the next button invisible initially. Once the route search data is populated, make it visible
        nextButton.setVisibility(View.INVISIBLE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        //read the parameters passed from caller
        sourceLat = getIntent().getDoubleExtra("sourceLat", -1);
        sourceLongt = getIntent().getDoubleExtra("sourceLongt", -1);
        destLat = getIntent().getDoubleExtra("destLat", -1);
        destLongt = getIntent().getDoubleExtra("destLongt", -1);
        title = getIntent().getStringExtra("title");
        userRouteTag = getIntent().getStringExtra("userRouteTag");
        action = getIntent().getIntExtra("action", -1);

        List<UserRouteDetail> data = getRouteDetailObjects();
        adapter = new RoutesAdapter(this, data);
        recyclerView.setAdapter(adapter);

        this.setTitle(title);

        //on click for next button
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(SHOW_ROUTES == action)//show office-home search route screen
                {
                    Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);

                    intent.putExtra("sourceLat", destLat);
                    intent.putExtra("sourceLongt", destLongt);
                    intent.putExtra("destLat", sourceLat);
                    intent.putExtra("destLongt", sourceLongt);
                    intent.putExtra("title", "Select Office To Home");
                    intent.putExtra("userRouteTag", "office-home");
                    intent.putExtra("action", 2);
                    startActivity(intent);
                }
                else if(SHOW_SELECTED_ROUTES_SUMMARY == action)
                {
                    Intent intent = new Intent(getApplicationContext(), MyBusActivity.class);
                    intent.putExtra("action", 2);
                    startActivity(intent);
                }

            }
        });
    }
    //To make sure a new instance of activity is not created when the back button is pressed
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    private List<UserRouteDetail> getRouteDetailObjects()
    {
        showMessageInSB("Searching...");

        data = new ArrayList<UserRouteDetail>();

        //String url = Config.BASE_URL_API + "/searchbus?srcLat=12.958757&srcLongt=77.705691&destLat=12.927810&destLongt=77.680985";
        String url = Config.BASE_URL_API + "/searchbus?srcLat=" + sourceLat + "&srcLongt=" + sourceLongt + "&destLat=" + destLat + "&destLongt=" + destLongt + "";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {

        @Override
        public void onResponse(JSONArray response)
        {

            if(response.length() == 0)
            {
                showMessageInSB("No routes found");
            }
            else
            {
                Gson gson = new Gson();
                String jsonOutput = response.toString();
                Type listType = new TypeToken<List<UserRouteDetail>>() {}.getType();
                List<UserRouteDetail> dataResponse = (List<UserRouteDetail>) gson.fromJson(jsonOutput, listType);

                data.addAll(dataResponse);

                //set the routeName
                for(UserRouteDetail urd : data)
                {
                    urd.setName(userRouteTag);
                }

                adapter.notifyDataSetChanged();

                //if any data returned, then make the next button visible
                if(data.size() > 0)
                {
                    nextButton.setVisibility(View.VISIBLE);
                }

            }
        }
        }, new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    showMessageInSB(error.getMessage());
                    Log.e("", error.getMessage());
                }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);

        return data;
    }
    private void showMessageInSB(String msg)
    {
        Snackbar snackbar = Snackbar.make(containerLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}
