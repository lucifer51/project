package com.example.lucifer.androidappforcrimereporting;

import android.*;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.android.gms.location.LocationServices.API;

public class CyberCrime extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
  //  EditText date, time;
    boolean mBound = false;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String path;
    String category;
    Button image,submit,locationstr;
    ImageView iv;
    String _date,_time,_details;
    DialogBoxes dialogBoxes;
    protected static final String LOG_TAG = CyberCrime.class.getName();
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
    CyberCrime.MyReceiver mReciver;
    GeofencingService mService;
    String loc;
///    private Button starLocationMonitor, stargeofencemonitor, stopgeofencemonitor, capturepic, submit;
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
        setContentView(R.layout.activity_cyber_crime);
        myDbHelper = new DataBaseHelper(this);
        date = (EditText) findViewById(R.id.Date1);
        longi = (EditText) findViewById(R.id.Longitude);
        lati = (EditText) findViewById(R.id.Latitude);
        locationstr=(Button)findViewById(R.id.FetchingGeoFences);
         dialogBoxes= new DialogBoxes(CyberCrime.this);
        _date=date.getText().toString();
        time = (EditText) findViewById(R.id.Time);
        _time=time.getText().toString();
        details = (EditText) findViewById(R.id.Detaillss);
        _details=details.getText().toString();
        iv=(ImageView)findViewById(R.id.image1);
        image=(Button)findViewById(R.id.CapturingPic);
        submit=(Button)findViewById(R.id.Submit);
        mReciver = new CyberCrime.MyReceiver();
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        buildGoogleApiCliente();
       //// starGeofenceMonitor();
    ////    starLocationMonitor();

     final Spinner   mySpinner = (Spinner) findViewById(R.id.spinner1);
        mySpinner.setSelection(1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.CyberCrimes, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category= mySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //category = mySpinner.getSelectedItem().toString();


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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




locationstr.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        starLocationMonitor();
        starGeofenceMonitor();
    }
});


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemOfMySpinner = mySpinner.getSelectedItemPosition();
                String actualPositionOfMySpinner = (String) mySpinner.getItemAtPosition(selectedItemOfMySpinner);

                if( !validate1() && actualPositionOfMySpinner.isEmpty() )
                {
                    setSpinnerError(mySpinner,"field can't be empty");
                    Toast.makeText(CyberCrime.this,"Plz Fill the required Fields",Toast.LENGTH_LONG).show();
                }
                else {
                    submit.setEnabled(false);
                    final ProgressDialog progressDialog = new ProgressDialog(CyberCrime.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Report is being Submitted");
                    progressDialog.show();



                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    SendingDataToRemoteDb sendingDataToRemoteDb=new SendingDataToRemoteDb(CyberCrime.this);
                                    sendingDataToRemoteDb.CyberCrimeReports(date.getText().toString(),time.getText().toString(),category,imagedata,details.getText().toString(),path,loc,String.valueOf(LONGITUDE),String.valueOf(LATITUDE));

                                    // On complete call either onSignupSuccess or onSignupFailed
                                    // depending on success
                                    onSignupSuccess();
                                    // onSignupFailed();
                                    progressDialog.dismiss();
                                }
                            }, 3000);

                }

            }
        });


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

    private void starLocationMonitor() {
        Toast.makeText(CyberCrime.this, "function started", Toast.LENGTH_SHORT).show();

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
        mReciver = new CyberCrime.MyReceiver();
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
                                mReciver = new CyberCrime.MyReceiver();
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
    //    Toast.makeText(CyberCrime.this,longi,Toast.LENGTH_SHORT).show();

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
    private void selectImage() {
        final CharSequence[] items = { "Choose from Library",
                "Cancel"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CyberCrime.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(CyberCrime.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    // cameraIntent();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);

        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
              path=  RealPathUtil.getRealPathFromURI_API19(this, data.getData());

                Toast.makeText(CyberCrime.this,path,Toast.LENGTH_SHORT).show();

               // File destination = new File(Environment.getExternalStorageDirectory(),
                 //       System.currentTimeMillis() + ".jpg");
                Toast.makeText(CyberCrime.this,bm.toString(),Toast.LENGTH_SHORT).show();

                byte[] bytearray= bytes.toByteArray();

                imagedata = Base64.encodeToString(bytearray, Base64.DEFAULT);
                iv.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean validate1()
    {
        Boolean result=true;
        String _time,_date,_details;
        _date=date.getText().toString();
        _time=time.getText().toString();
        _details=details.getText().toString();
        if(_date.isEmpty())
        {
            date.setError("Plz Enter the Date ");
            // submit.setEnabled(false);
            result=false;
        }
        else {
            date.setError(null);
        }
        if(_time.isEmpty())
        {
            time.setError("Plz Enter the Time of your Visit");
            submit.setEnabled(false);
            result=false;
        }
        else {
            date.setError(null);
        }
        if(_details.isEmpty())
        {
            details.setError("Plz Enter the Details of your Visit");
            submit.setEnabled(false);
            result=false;
        }
        else {
            date.setError(null);
        }

        return  result;


        //return  result;
    }
    public void onSignupSuccess() {
        submit.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
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

            Toast.makeText(CyberCrime.this, loc, Toast.LENGTH_LONG).show();

            //  fetchedText.setText(String.valueOf(datapassed));

        }

    }


}