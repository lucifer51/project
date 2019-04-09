package com.example.lucifer.androidappforcrimereporting;

import android.content.Context;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/**
 * Created by Lucifer on 2/1/2018.
 */

public class RemoteDBConnection {
    private static RemoteDBConnection mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;


    private RemoteDBConnection (Context Context)
    {
        mCtx = Context;
        requestQueue = getRequestQueue();
    }

    public static  synchronized RemoteDBConnection getInstance (Context context)
    {
        if (mInstance==null)
        {
            mInstance =new RemoteDBConnection(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {

        if (requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;

    }

    public<T> void addTorequestque(Request<T> request)
    {
        requestQueue.add(request);
    }
}

