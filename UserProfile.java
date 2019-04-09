package com.example.lucifer.androidappforcrimereporting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class UserProfile extends AppCompatActivity {
    StringRequest stringRequest;
    TextView name,address,email,cnic,profile,checkreportofuser;
    EditText Phone ,email1,address1,Password,Cnic;
    Button update,back;
    String data;
    String phonet,emailt,passt,addresst;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Phone=(EditText)findViewById(R.id.Phone1);
        email1=(EditText)findViewById(R.id.Email1);
        address1=(EditText)findViewById(R.id.Address1);
        Password=(EditText)findViewById(R.id.Password);
        profile=(TextView)findViewById(R.id.p);
        checkreportofuser=(TextView)findViewById(R.id.checkreports);
       // Cnic=(EditText)findViewById(R.id.Cnic);
SharedPreferences sharedPreferences =UserProfile.this.getSharedPreferences("Login",MODE_PRIVATE);
        data= sharedPreferences.getString("username","");
        update=(Button)findViewById(R.id.UPDATE);
        back=(Button)findViewById(R.id.back);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validate()) {
                    Toast.makeText(UserProfile.this,"singupfailed",Toast.LENGTH_LONG).show();

                }
                else {
                    SendingDataToRemoteDb sendingDataToRemoteDb = new SendingDataToRemoteDb(UserProfile.this);
                    sendingDataToRemoteDb.ProfileUpdate(Phone.getText().toString(), Password.getText().toString(), email1.getText().toString(), address1.getText().toString());
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this,MainHomePage.class);
                startActivity(i);
            }
        });

        imageView=(ImageView)findViewById(R.id.Reports);
        checkreportofuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent i= new Intent(UserProfile.this,UserReports.class);
             startActivity(i);
            }
        });


        getSqlDetails();

    }
    private void getSqlDetails() {

        String url= "http://192.168.43.254/CrimeReporting/userprofile.php?cnic="+data;
      //  pd.show();
         stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
Toast.makeText(UserProfile.this,jsonobject.get("Name").toString(),Toast.LENGTH_SHORT).show();

                                String name1 = jsonobject.getString("Name");
                  //              Toast.makeText(UserProfile.this,name1,Toast.LENGTH_SHORT).show();
                                String emaill1 = jsonobject.getString("Email");
                //                Toast.makeText(UserProfile.this,emaill1,Toast.LENGTH_SHORT).show();
                                String cnic11 = jsonobject.getString("Cnic");
                                String address11 = jsonobject.get("Address").toString();
              //                  Toast.makeText(UserProfile.this,address11,Toast.LENGTH_SHORT).show();
                                String Phone1=jsonobject.getString("MobileNumber");
            //                    Toast.makeText(UserProfile.this,Phone1,Toast.LENGTH_SHORT).show();
                                String pass= jsonobject.getString("Password");
          //                      Toast.makeText(UserProfile.this,pass,Toast.LENGTH_SHORT).show();
                                passt=pass;
                                phonet=Phone1;
                                emailt=emaill1;
                                addresst=address11;
                                Phone.setText(Phone1);
                                email1.setText(emaill1);
                              //  Cnic.setText(cnic11);
                                address1.setText(address11);

                                Password.setText(pass);

//Toast.makeText(UserProfile.this, name1+""+phonet+""+passt+""+emailt+addresst,Toast.LENGTH_LONG).show();
                //                result.setText(" ID -"+id+"\n Price -"+price+"\n Name -"+name+"\n Phone -"+phone);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(UserProfile.this,"some error occured" , Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        RemoteDBConnection.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    public  boolean validate()
    {
        boolean valid=true;
  //  String    _name=name.getText().toString();
    String    _address=address1.getText().toString();
   String     _email=email1.getText().toString();

     String   _password=Password.getText().toString();
      String  _mobilenumber=Phone.getText().toString();
    //    _ReenterPassword=ReenterPassword.getText().toString();



        if (_address.isEmpty()) {
            address1.setError("Enter Valid Address");
            valid = false;
        } else {
            address1.setError(null);
        }


        if (_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email1.setError("enter a valid email address");
            valid = false;
        } else {
            email1.setError(null);
        }



        if (_mobilenumber.isEmpty() || _mobilenumber.length()!=11) {
            Phone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            Phone.setError(null);
        }

        if (_password.isEmpty() || _password.length() < 4 || _password.length() > 10) {
            Password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {Password.setError(null);
        }


        return valid;




    }



}
