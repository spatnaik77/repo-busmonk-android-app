package com.busmonk.service;

/**
 * Created by sr250345 on 11/6/16.
 */

public class BusmonkServer {


    /*public List<Stop> getStopsForRouteAndShowRouteOnMap(String routeId)
    {
        final ArrayList<Stop> stopList = new ArrayList<Stop>();

        String url = Config.BASE_URL_API + "/routes/" + routeId + "/stops";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response)
            {
                if(response.length() == 0)
                {
                    showMessageInSB("No stops found");
                }
                try
                {
                    for (int c = 0; c < response.length(); c++)
                    {

                        JSONObject j = (JSONObject) response.get(c);
                        String id = j.getString("id");
                        String name = j.getString("name");
                        double lattitude = j.getDouble("lattitude");
                        double longitude = j.getDouble("longitude");

                        Stop s = new Stop(id, name, lattitude, longitude);

                        stopList.add(s);

                    }

                    showRouteOnMap(stopList);


                }catch(Exception e)
                {
                    showMessageInSB(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                showMessageInSB(error.getMessage());
                Log.e("", error.getMessage());
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        HTTPRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }*/

}
