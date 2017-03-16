package com.busmonk.session;


import com.busmonk.service.User;

/**
 * Created by sr250345 on 12/11/15.
 */
public class SessionManager
{

    static SessionManager singleton;

    //private String userID;
    private User user;

    public static SessionManager getInstance()
    {
        if(singleton == null)
        {
            singleton = new SessionManager();
        }
        return singleton;
    }

    private SessionManager()
    {

    }

    /*public void login(String token)
    {
        //1. Put into shared pref manager
        //2. Invoke getUser & put the User object in session store

        SharedPreferenceManager.getInstance(null).putAuthToken(token);




    }
     private void retrieveUserDetails(String token)
     {
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

                 }
             }

         }, new Response.ErrorListener()
         {
             @Override
             public void onErrorResponse(VolleyError error)
             {
                 Log.e("LOGIN", error.getMessage());
             }
         });

         request.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

         HTTPRequestQueue.addToRequestQueue(request);
     }*/


    public User getUser() {
        return user;
    }

    public void putUser(User user) {
        this.user = user;
    }
    public void removeUser()
    {
        this.user = null;
    }

    /*public void putUserId(String userID)
    {
        this.userID = userID;
    }
    public String getUserID()
    {
        return this.userID;
    }*/
}
