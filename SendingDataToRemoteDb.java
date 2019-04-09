package com.example.lucifer.androidappforcrimereporting;

/**
 * Created by Lucifer on 2/1/2018.
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;




import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SendingDataToRemoteDb {


    String server_url = "http://192.168.43.254/CrimeReporting/RegisteringNewUserData.php";
    String server_url_incidentreports = "http://192.168.43.254/CrimeReporting/IncidentReportsInsertion.php";
    String server_url_login = "http://192.168.43.254/CrimeReporting/AndroidUserLogin.php";
    String server_url_Fir = "http://192.168.43.254/CrimeReporting/FirReports.php";
    String server_url_CybercRIME="http://192.168.43.254/CrimeReporting/CyberCrimeReportsInsertion.php";
    String server_url_profileupdate="http://192.168.43.254/CrimeReporting/ProfileUpdate.php";
    String server_url_forgot="http://192.168.43.254/CrimeReporting/forgotpassword.php";
    String result;
    public Context context;
    SharedPreferences sharedPreferences;
   String data="";
   String Location="";


    public SendingDataToRemoteDb(Context context) {
        this.context = context;

    }


    public void incidentReports(final String date, final String time, final String category, final String image, final String details, final String filename)
    {
        if(context!=null) {
            Toast.makeText(SendingDataToRemoteDb.this.context,date,Toast.LENGTH_SHORT).show();
            Toast.makeText(SendingDataToRemoteDb.this.context,time,Toast.LENGTH_SHORT).show();
            Toast.makeText(SendingDataToRemoteDb.this.context,category,Toast.LENGTH_SHORT).show();
            Toast.makeText(SendingDataToRemoteDb.this.context,details,Toast.LENGTH_SHORT).show();
            Toast.makeText(SendingDataToRemoteDb.this.context,filename,Toast.LENGTH_SHORT).show();
        }
        sharedPreferences= SendingDataToRemoteDb.this.context.getSharedPreferences("Login",MODE_PRIVATE);
        data= sharedPreferences.getString("username","");
//        final String file=filename.substring(filename.lastIndexOf("/")+1);
    //    Toast.makeText(SendingDataToRemoteDb.this.context, data, Toast.LENGTH_LONG).show();
//        Toast.makeText(SendingDataToRemoteDb.this.context, Location, Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_incidentreports, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    //
                if(response!=null) {
                    Toast.makeText(SendingDataToRemoteDb.this.context, response, Toast.LENGTH_LONG).show();
                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("date", date);
                Params.put("time", time);
                Params.put("category", category);
                Params.put("image", image);
               // Params.put("imagename",file);
                Params.put("details",details);
                Params.put("userdata",data);
                Params.put("location",Location);
                Log.d(data,"value");

                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);



    }



    public void registeringuser(final String name, final String address, final String email, final String phone, final String cnic, final String password) {
     //   Toast.makeText(context,password,Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    //    progress.dismiss();
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("name", name);
                Params.put("address", address);
                Params.put("email", email);
                Params.put("phone", phone);
                Params.put("cnic", cnic);
                Params.put("pass", password);
                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);


    }

    public void Login(final String name, final String Password) {

        sharedPreferences= SendingDataToRemoteDb.this.context.getSharedPreferences("Login",MODE_PRIVATE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(SendingDataToRemoteDb.this.context, response, Toast.LENGTH_LONG).show();
                if (response.length()==13) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("username",response);
                    editor.commit();
                    SendingDataToRemoteDb.this.context.startActivity(new Intent(SendingDataToRemoteDb.this.context, MainHomePage.class));

                } else {
                    Toast.makeText(SendingDataToRemoteDb.this.context,"incorrect login", Toast.LENGTH_LONG).show();

                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("name", name);
                Params.put("address", Password);

                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);


    }

    public void FirDetails(final String date, final String time, final String Details, final String PoliceStationvisit,
                           final String visitdetails, final String timeofvisit, final String dateofvisit,
                           final String LocationDetails) {

        sharedPreferences= SendingDataToRemoteDb.this.context.getSharedPreferences("Login",MODE_PRIVATE);
    final String    adata= sharedPreferences.getString("username","");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_Fir, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    //    progress.dismiss();
                    Toast.makeText(SendingDataToRemoteDb.this.context, response, Toast.LENGTH_LONG).show();
                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("date", date);
                Params.put("time", time);
                Params.put("details", Details);
                Params.put("PoliceStationVisit", PoliceStationvisit);
                Params.put("visitdetails",visitdetails);
                Params.put("dateofvisit", dateofvisit);
                Params.put("timeofvisit",timeofvisit);
                Params.put("location",LocationDetails);
                Params.put("Cnic",adata);
                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);






    }

    public  void CyberCrimeReports(final String date, final String time, final String category, final String image, final String details, final String filename,final String location,final String Longititude,final String latitude)
    {

        sharedPreferences= SendingDataToRemoteDb.this.context.getSharedPreferences("Login",MODE_PRIVATE);
              data= sharedPreferences.getString("username","");
        final String file=filename.substring(filename.lastIndexOf("/")+1);
        Toast.makeText(SendingDataToRemoteDb.this.context, data, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_CybercRIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    //    progress.dismiss();
                    Toast.makeText(SendingDataToRemoteDb.this.context, response, Toast.LENGTH_LONG).show();
                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("date", date);
                Params.put("time", time);
                Params.put("category", category);
                Params.put("image", image);
                Params.put("imagename",file);
                Params.put("details",details);
                Params.put("userdata",data);
                Params.put("location",location);
                Params.put("Longi",Longititude);
                Params.put("Lati",latitude);
                Log.d(data,"value");

                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);





    }




    public void ProfileUpdate(final String Phone, final String Password, final String email,final String address) {
        //   Toast.makeText(context,password,Toast.LENGTH_SHORT).show();

        sharedPreferences= SendingDataToRemoteDb.this.context.getSharedPreferences("Login",MODE_PRIVATE);
        data= sharedPreferences.getString("username","");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_profileupdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    //    progress.dismiss();
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    SendingDataToRemoteDb.this.context.startActivity(new Intent(SendingDataToRemoteDb.this.context, MainHomePage.class));

                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("UID",data);
                Params.put("Phone", Phone);
                Params.put("Password", Password);
                Params.put("email", email);
                Params.put("Address",address);

                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);


    }


    public void ForgotPassword(final String s) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_forgot, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    //    progress.dismiss();
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    SendingDataToRemoteDb.this.context.startActivity(new Intent(SendingDataToRemoteDb.this.context, MainHomePage.class));

                }

            }
        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> Params = new HashMap<>();
                Params.put("email",s);
                return Params;

            }
        };
        RemoteDBConnection.getInstance(context).addTorequestque(stringRequest);



    }
}




