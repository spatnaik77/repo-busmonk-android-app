package com.busmonk.util;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sr250345 on 11/10/16.
 */

public class BusmonkApplication extends Application {

    private Map<String, Object> data = new HashMap<String, Object>();

    public Object get(String key)
    {
        return data.get(key);
    }

    public void put(String key, Object val)
    {
        data.put(key, val);
    }
}
