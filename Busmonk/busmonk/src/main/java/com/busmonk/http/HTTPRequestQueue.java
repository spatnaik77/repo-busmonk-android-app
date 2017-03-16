package com.busmonk.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by sr250345 on 12/11/15.
 */
public class HTTPRequestQueue {

    private static HTTPRequestQueue singleton;

    private RequestQueue requestQueue;
    private Context context;

    public static HTTPRequestQueue getInstance(Context context)
    {
        if(singleton == null)
        {
            singleton = new HTTPRequestQueue(context);

        }
        return singleton;
    }

    private HTTPRequestQueue(Context context)
    {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
