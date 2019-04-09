package com.example.lucifer.androidappforcrimereporting;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;
/**
 * Created by Lucifer on 11/25/2017.
 */


import android.app.IntentService;
        import android.content.Intent;
import android.util.Log;
import android.content.Context;

import com.google.android.gms.location.Geofence;
        import com.google.android.gms.location.GeofencingEvent;

        import java.util.List;

/**
 * Created by andresdavid on 13/09/16.
 */
public class GeofencingService extends IntentService {
    protected static final String LOG_TAG = GeofencingService.class.getName();
    //MainActivity context;
  public static   String requestId;

    public static final String BROADCAST_ACTION = "com.example.lucifer.androidappforcrimereporting";
    UpdateEditTextListener Updatelistener;

    private Handler mHandler;
Context context;
    public GeofencingService() {
        super(LOG_TAG);


    }

    public GeofencingService(Context context) {
        super(LOG_TAG);
        this.context=context;


    }

    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        GeofencingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GeofencingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mHandler = new Handler();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(LOG_TAG, "geofencingEvent hasError");
        } else {
            int transition = geofencingEvent.getGeofenceTransition();
            List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
            final Geofence geofence = geofenceList.get(0);
            requestId = geofence.getRequestId();
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(LOG_TAG, String.format("GEOFENCE_TRANSITION_ENTER on %s", requestId));


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    //   Toast.makeText(getApplicationContext(), requestId, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setAction("Geo");
                        intent.putExtra("location",requestId);
                        sendBroadcast(intent);



                    }
                });


            } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(LOG_TAG, String.format("GEOFENCE_TRANSITION_EXIT  on %s", requestId));
            }
        }

    }
}



