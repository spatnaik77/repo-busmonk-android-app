package com.busmonk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sr250345 on 11/11/16.
 */

public class Util {

    public static long getRideDuration(String pickupTime, String alightTime)
    {
        long result = 0;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mma");
            Date st = format.parse(pickupTime);
            Date et = format.parse(alightTime);
            long diff = et.getTime() - st.getTime();
            result = diff / (60 * 1000) % 60;

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

}
