package com.example.lucifer.androidappforcrimereporting;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.API;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //private EditText fromDateEtxt;
    private EditText Datetxt;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private SimpleDateFormat dateFormatter;
    String path;
    String category;
    String location;
    GeofencingService mBoundService;

    ImageView ivImage;
    boolean mBound = false;
    String loc;
    protected static final String LOG_TAG = MainActivity.class.getName();
    public static final String GEOFENCE_ID = "MirpurPoliceStation";
    private GoogleApiClient googleApiClient;
    SQLiteDatabase mDatabase;
    public List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    String server_url = "http://192.168.0.102/CrimeReporting/IncidentReportsInsertion.php";
    AlertDialog.Builder builder;

    private double LATITUDE;
    private double LONGITUDE;
    private float RADIO = 1000;
    public EditText longi, lati, police, date, time;
    public TextView tv;
    DataBaseHelper myDbHelper;
    Spinner mySpinner;
    String imagedata;
    EditText details;
    MyReceiver mReciver;
    GeofencingService mService;
    private Button starLocationMonitor, stargeofencemonitor, stopgeofencemonitor, capturepic, submit;
    ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        details = (EditText) findViewById(R.id.details);
        /// mDatabase= new SQLiteDatabase(this);
        myDbHelper = new DataBaseHelper(this);
        starLocationMonitor = (Button) findViewById(R.id.FetchingGeoFences);
        longi = (EditText) findViewById(R.id.Longitude);
        lati = (EditText) findViewById(R.id.Latitude);
        //   submit=(Button)findViewById(R.id.submitbtn);
        police = (EditText) findViewById(R.id.PoliceStation);
        mySpinner = (Spinner) findViewById(R.id.spinner1);
        // mySpinner.setSelection(1);


        category = mySpinner.getSelectedItem().toString();
        final Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.IncidentCrimeReportsList, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = mySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mReciver = new MyReceiver();
        date = (EditText) findViewById(R.id.Date1);
        date.addTextChangedListener(mDateEntryWatcher);
        time = (EditText) findViewById(R.id.Time);
        time.addTextChangedListener(mDateEntryWatcher);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        time.setText(strDate);

        calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy ");
        String Date = simpledateformat.format(calendar.getTime());
        date.setText(Date);


/*
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        */
        ivImage = (ImageView) findViewById(R.id.capturepic1);
        /*
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
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
        });
        */
        capturepic = (Button) findViewById(R.id.CapturingPic);
        capturepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        starLocationMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              starGeofenceMonitor();
              starLocationMonitor();


            }
        });


        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        buildGoogleApiCliente();


    }

    private void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }

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


    @Override
    protected void onResume() {


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Geo");
        mReciver = new MyReceiver();
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


    private void starLocationMonitor() {
        Toast.makeText(MainActivity.this, "function started", Toast.LENGTH_SHORT).show();

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
                                mReciver = new MyReceiver();
                                registerReceiver(mReciver,intentFilter);


                            } else {
                                police.setText("geofence not creaeted");
                            }
                        }
                    });

        }


    }

    private void stopGeofenceMonitor() {

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


        longi.setText("" + location.getLongitude());
        lati.setText("" + location.getLatitude());
        // police.setText(""+GEOFENCE_ID);


    }

    private GeofencingRequest getfence() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    public void databaseopener() {

        //   = new DataBaseHelper(this);


        try {

            myDbHelper.createDataBase();

        } catch (Exception ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        byte[] bytearray = bytes.toByteArray();

        imagedata = Base64.encodeToString(bytearray, Base64.DEFAULT);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                // path=  RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                //  Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte[] bytearray = bytes.toByteArray();


                imagedata = Base64.encodeToString(bytearray, Base64.DEFAULT);

                ivImage.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String _date, _time, _lati, _longi;
            _date = date.getText().toString();
            _time = time.getText().toString();
            _lati = lati.getText().toString();
            _longi = longi.getText().toString();
            if (_date.isEmpty()) {
                date.setError("Plz Enter the Date ");
                // submit.setEnabled(false);

//                result = false;
            } else {
                date.setError(null);
                // result=true;
            }
            if (_time.isEmpty()) {
                time.setError("Plz Enter the Time of your Visit");
                //  submit.setEnabled(false);

            } else {
                time.setError(null);
                // result=true;
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    public Boolean validate() {

        Boolean result = true;
        String _date, _time, _lati, _longi;
        _date = date.getText().toString();
        _time = time.getText().toString();
        _lati = lati.getText().toString();
        _longi = longi.getText().toString();
        if (_date.isEmpty()) {
            date.setError("Plz Enter the Date ");
            // submit.setEnabled(false);

            result = false;
        } else {
            date.setError(null);
            // result=true;
        }
        if (_time.isEmpty()) {
            time.setError("Plz Enter the Time of your Visit");
            //  submit.setEnabled(false);
            result = false;
        } else {
            time.setError(null);
            // result=true;
        }
        if (_lati.isEmpty()) {
            lati.setError("Plz click on gps icon to know your locoation");
        } else {
            lati.setError(null);
        }
        if (_longi.isEmpty()) {
            longi.setError("");
        } else {
            longi.setError(null);
        }


        return result;

    }

    public void onclicks(View view) {

        final Intent intent= new Intent();
        if (view.getId() == R.id.submitbtn) {
            int selectedItemOfMySpinner = mySpinner.getSelectedItemPosition();
            String actualPositionOfMySpinner = (String) mySpinner.getItemAtPosition(selectedItemOfMySpinner);

            if (actualPositionOfMySpinner.isEmpty()) {
                setSpinnerError(mySpinner,"field can't be empty");
            }
else {
                //  Toast.makeText(MainActivity.this, "btn is pressed", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, loc, Toast.LENGTH_SHORT).show();
                final ProgressDialog progress;

                progress = new ProgressDialog(MainActivity.this);
                progress.setMessage("Registering report");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.show();

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // SendingDataToRemoteDb sendingDataToRemoteDb=new SendingDataToRemoteDb(MainActivity.this);
                                // sendingDataToRemoteDb.incidentReports(date.getText().toString(),time.getText().toString(),category,imagedata,details.getText().toString(),path);

                                //    final String file=path.substring(path.lastIndexOf("/")+1);
                                //    Toast.makeText(SendingDataToRemoteDb.this.context, data, Toast.LENGTH_LONG).show();
//        Toast.makeText(SendingDataToRemoteDb.this.context, Location, Toast.LENGTH_LONG).show();
                                String server_url_incidentreports = "http://192.168.43.254/CrimeReporting/IncidentReportsInsertion.php";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_incidentreports, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //

                                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                                        Intent i= new Intent(MainActivity.this,MainHomePage.class);
                                        startActivity(i);


                                    }
                                }

                                        , new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();

                                    }
                                }) {

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {


                                        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("Login", MODE_PRIVATE);
                                        String data = sharedPreferences.getString("username", "");
//
                                        Map<String, String> Params = new HashMap<>();
                                        Params.put("date", date.getText().toString());
                                        Params.put("time", time.getText().toString());
                                        Params.put("category", category);
                                        Params.put("image", imagedata);
                                        //  Params.put("imagename","");
                                        Params.put("details", details.getText().toString());
                                        Params.put("userdata", data);

                                        Params.put("Longi",String.valueOf(LONGITUDE));
                                        Params.put("Lati",String.valueOf(LATITUDE));

                                        Params.put("location", loc);
                                        // Log.d(data,"value");

                                        return Params;

                                    }
                                };
                                RemoteDBConnection.getInstance(MainActivity.this).addTorequestque(stringRequest);


                                // On complete call either onSignupSuccess or onSignupFailed
                                // depending on success

                                // onSignupFailed();
                                progress.dismiss();
                            }
                        }, 3000);

            }
        }


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

            Toast.makeText(MainActivity.this, loc, Toast.LENGTH_LONG).show();

            //  fetchedText.setText(String.valueOf(datapassed));

        }

    }

}


