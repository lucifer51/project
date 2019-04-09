package com.example.lucifer.androidappforcrimereporting;

import android.*;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.android.gms.location.LocationServices.API;

public class FirComplaints extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {
    boolean mBound = false;
    EditText date,time,visitdetails,TimeofVisit,DateofVisit,PoliceStation,details;
    Button submit,PoliceStationTeller;
    RadioButton yes,no;
    protected static final String LOG_TAG = MainActivity.class.getName();
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static final String GEOFENCE_ID = "MirpurPoliceStation";
    private GoogleApiClient googleApiClient;
    SQLiteDatabase mDatabase;
    public List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    String server_url = "http://192.168.0.102/CrimeReporting/IncidentReportsInsertion.php";
    AlertDialog.Builder builder;

    private double LATITUDE;
    private double LONGITUDE;
    private float RADIO = 1000;
    public EditText longi, lati, police;
    public TextView tv;
    DataBaseHelper myDbHelper;
    Spinner mySpinner;
    String imagedata;
   
    FirComplaints.MyReceiver mReciver;
    GeofencingService mService;
    String loc;
///    private Button starLocationMonitor, stargeofencemonitor, stopgeofencemonitor, capturepic, submit;
    ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;




    String checked;
     @RequiresApi(api = Build.VERSION_CODES.M)
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir_complaints);
         myDbHelper = new DataBaseHelper(this);
         requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);

         // ATTENTION: This was auto-generated to implement the App Indexing API.
         // See https://g.co/AppIndexing/AndroidStudio for more information.
         client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
         buildGoogleApiCliente();
        date=(EditText)findViewById(R.id.Date1);
        time=(EditText)findViewById(R.id.Time1);
        TimeofVisit=(EditText)findViewById(R.id.Timeofvistit);
        DateofVisit=(EditText)findViewById(R.id.dateofvisit);
        visitdetails=(EditText)findViewById(R.id.VisitDetails);
        DateofVisit.addTextChangedListener(mDateEntryWatcher);
        TimeofVisit.addTextChangedListener(mDateEntryWatcher);

        yes=(RadioButton)findViewById(R.id.yes);
        no=(RadioButton)findViewById(R.id.no);
        submit=(Button)findViewById(R.id.Submit);
        PoliceStation=(EditText)findViewById(R.id.Police);
         PoliceStation.addTextChangedListener(mDateEntryWatcher);
        details=(EditText)findViewById(R.id.Details);
        PoliceStationTeller=(Button)findViewById(R.id.FetchingGeoFences) ;
        PoliceStationTeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starGeofenceMonitor();
                starLocationMonitor();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   // submit.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(FirComplaints.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Report is being Submitted");
                    progressDialog.show();
                    String _dateofreport=date.getText().toString();
                    String _timeofreport=time.getText().toString();
                    String _details=details.getText().toString();
                    String _visitdetails=visitdetails.getText().toString();
                    String _timeofviist=TimeofVisit.getText().toString();
                    String _dateofvisit=DateofVisit.getText().toString();



                    SendingDataToRemoteDb sendingDataToRemoteDb=new SendingDataToRemoteDb(FirComplaints.this);

                    if(checked=="yes")
                    {
                        sendingDataToRemoteDb.FirDetails(_dateofreport,_timeofreport,_details,checked
                                ,_visitdetails,_timeofviist,_dateofvisit,loc);
                    }
                    else {
                        sendingDataToRemoteDb.FirDetails(_dateofreport,_timeofreport,_details,checked
                                ,"Not visited","not Available","Not Available",loc);
                    }

                  //  sendingDataToRemoteDb.FirDetails();



                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onSignupSuccess or onSignupFailed
                                    // depending on success
                               //     onSignupSuccess();
                                    // onSignupFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);


            }
        });


        DateofVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FirComplaints.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();

                                DateofVisit.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        TimeofVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(FirComplaints.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                TimeofVisit.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
         Calendar calendar = Calendar.getInstance();
         SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
         String strDate = mdformat.format(calendar.getTime());
         time.setText(strDate);

         calendar = Calendar.getInstance();
         SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy ");
         String Date = simpledateformat.format(calendar.getTime());
         date.setText(Date);

    }
    public void Date()
    {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(FirComplaints.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();

                        date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void Time()
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(FirComplaints.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
public void  onRadioButtonClicked(View view) {





        if (view.getId() == R.id.yes) {
            if (yes.isChecked()) {

                if (!validate1()) {
                    Toast.makeText(FirComplaints.this, "Plz Enter Your Visit Details", Toast.LENGTH_LONG).show();
                } else {
                    checked="yes";
                    Toast.makeText(FirComplaints.this, "Thank You", Toast.LENGTH_LONG).show();

                }
            }
        }else if (view.getId() == R.id.no) {
            if(no.isChecked())
            {
                checked="no";
                Toast.makeText(FirComplaints.this,"No is checked",Toast.LENGTH_LONG).show();
            }


            //submit.setEnabled(true);
        }
    }

    public boolean validate1()
    {
        Boolean result=true;
        String _time,_date,_details,_police;
        _date=date.getText().toString();
        _time=time.getText().toString();
        _details=details.getText().toString();
        _police=PoliceStation.getText().toString();
        if(_date.isEmpty())
        {
            date.setError("Plz Enter the Date ");
           // submit.setEnabled(false);
            result=false;
        }
        else {
            DateofVisit.setError(null);
        }
        if(_time.isEmpty())
        {
            time.setError("Plz Enter the Time of your Visit");
            //submit.setEnabled(false);
            result=false;
        }
        else {
            DateofVisit.setError(null);
        }
        if(_details.isEmpty())
        {
            details.setError("Plz Enter the Details of your Visit");
           // submit.setEnabled(false);
            result=false;
        }
        else {
            DateofVisit.setError(null);
        }
        if(_police.isEmpty()){

            PoliceStation.setError("Plz cLICK on button to set Location");
         //   submit.setEnabled(false);
            result=false;
        }
        else {
            PoliceStation.setError(null);
        }

        return  result;


        //return  result;
    }
    private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Boolean result=true;
            String _visittime,_visitdate,_visitdetails;
            _visitdate=DateofVisit.getText().toString();
            _visittime=TimeofVisit.getText().toString();
            _visitdetails=visitdetails.getText().toString();
          String   _police=PoliceStation.getText().toString();
            if(_visitdate.isEmpty())
            {
            }
            else {
                DateofVisit.setError(null);
            }
            if(_visittime.isEmpty())
            {
                TimeofVisit.setError("Plz Enter the Time of your Visit");
            //    submit.setEnabled(false);
                result=false;
            }
            else {
                TimeofVisit.setError(null);
            }
            if(_visitdetails.isEmpty())
            {
                visitdetails.setError("Plz Enter the Details of your Visit");
          //      submit.setEnabled(false);
                result=false;
            }
            else {
                DateofVisit.setError(null);
            }

        }


        @Override
        public void afterTextChanged(Editable s) {

        }

    };





    protected synchronized void buildGoogleApiCliente() {
        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (response != ConnectionResult.SUCCESS)
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
        else {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(AppIndex.API).build();
        }

    }

    private void starLocationMonitor() {
        Toast.makeText(FirComplaints.this, "function started", Toast.LENGTH_SHORT).show();

        Log.d(LOG_TAG, "starLocationMonitor");
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10000)
                    .setFastestInterval(5000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        } catch (Exception e) {
            Log.d(LOG_TAG, String.format("Error starLocationMonitor %s", e.getMessage()));

        }

    }

    @Override
    protected void onResume() {


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Geo");
        mReciver = new FirComplaints.MyReceiver();
        registerReceiver(mReciver, intentFilter);

        super.onResume();
    }

    @Override
    protected void onStart() {
        if (!googleApiClient.isConnecting() || !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
        Intent intent = new Intent(this, GeofencingService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnecting() || googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        super.onStop();

        client.disconnect();
    }

    protected void onDestroy() {

        if (mBound) {
            unbindService(mConnection);
            unregisterReceiver(mReciver);
            mBound = false;
        }


        super.onDestroy();

    }


    private void starGeofenceMonitor() {


        mDatabase = myDbHelper.getReadableDatabase();




       /* Geofence geofence = new Geofence.Builder()
                .setRequestId("cantt police station")
                .setCircularRegion(34.189784, 73.23169, 3000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
                */
        //  mGeofenceList.add(geofence);
        Cursor getfencesfromdb =
                mDatabase.rawQuery("Select * From GEOFENCES ", null);


        if (!(getfencesfromdb.moveToFirst()) || getfencesfromdb.getCount() == 0) {
            Toast.makeText(this, "No record ", Toast.LENGTH_LONG).show();


            // mDatabase.close();
        } else {

            do {
                double a = getfencesfromdb.getDouble(2);
                String test = String.valueOf(a);


                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(getfencesfromdb.getString(0))
                        .setCircularRegion(getfencesfromdb.getDouble(2), getfencesfromdb.getDouble(1), getfencesfromdb.getInt(3))
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .build());
                //        Toast.makeText(this,getfencesfromdb.getString(0),Toast.LENGTH_LONG).show();
                //    Toast.makeText(this, test,Toast.LENGTH_LONG).show();

            } while (getfencesfromdb.moveToNext());


        }


        Intent intent = new Intent(this, GeofencingService.class);
//        startActivity(intent);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (!googleApiClient.isConnected()) {
            Log.d(LOG_TAG, "GoogleApiClient is not Conectec");
        } else {


            LocationServices.GeofencingApi.addGeofences(googleApiClient, getfence(), pendingIntent)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d(LOG_TAG, "Successfully add geofencingRequest");
                                Toast.makeText(getApplicationContext(), "FenceCreated", Toast.LENGTH_LONG).show();

                                //Intent i = new Intent(MainActivity.this, GeofencingService.class);
                                //  i.putExtra("lat",GEOFENCE_ID);
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction("Geo");
                                mReciver = new FirComplaints.MyReceiver();
                                registerReceiver(mReciver,intentFilter);


                            } else {
                                police.setText("geofence not creaeted");
                            }
                        }
                    });

        }


    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, String.format("onLocationChanged New Latitude: %s ,Longitude %s", location.getLatitude(), location.getLongitude()));
        double lon = location.getLongitude();
//        double lati=location.getLatitude();
        LONGITUDE = location.getLongitude();
        LATITUDE = location.getLatitude();
        //      String longi= String.valueOf(LONGITUDE);
        //    Toast.makeText(FirComplaints.this,longi,Toast.LENGTH_SHORT).show();

      //  longi.setText("" + location.getLongitude());
       // lati.setText("" + location.getLatitude());
        // police.setText(""+GEOFENCE_ID);


    }

    private GeofencingRequest getfence() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            //    starLocationMonitor();
            //      starGeofenceMonitor();

            //  Toast.makeText(MainActivity.this, "connected with service", Toast.LENGTH_LONG).show();
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GeofencingService.LocalBinder binder = (GeofencingService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            Intent send= new Intent();
            loc = arg1.getStringExtra("location");
            send.putExtra("loc",loc);
            PoliceStation.setText(loc);

            Toast.makeText(FirComplaints.this, loc, Toast.LENGTH_LONG).show();

            //  fetchedText.setText(String.valueOf(datapassed));

        }

    }


}
