package com.example.lucifer.androidappforcrimereporting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DatabaseMetaData;


public class SignUp extends AppCompatActivity {
   private TextView tv;
   EditText name,address,email,password,cnic,mobilenumber,ReenterPassword;
    String _name,_address,_email,_password,_cnic,_mobilenumber,_ReenterPassword;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=(EditText)findViewById(R.id.input_name);
        address=(EditText)findViewById(R.id.input_address);
        email=(EditText)findViewById(R.id.input_email);
        cnic=(EditText)findViewById(R.id.input_cnic);
        password=(EditText)findViewById(R.id.input_password);
        mobilenumber=(EditText)findViewById(R.id.input_mobile);
        ReenterPassword=(EditText)findViewById(R.id.input_reEnterPassword);
        signup=(Button) findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        tv=(TextView)findViewById(R.id.link_login);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignUp.this,UserLogin.class);
                startActivity(i);
            }
        });

    }


    public  void  signup()
    {
        if(!validate())
        {
            Toast.makeText(SignUp.this,"Signup Failed",Toast.LENGTH_LONG).show();
        }
        else {
            signup.setEnabled(false);
            final ProgressDialog progressDialog = new ProgressDialog(SignUp.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            String Name = name.getText().toString();
            String Address = address.getText().toString();
            String ____cnic= cnic.getText().toString();
            String Email = email.getText().toString();
            String Mobile = mobilenumber.getText().toString();
            String Password = password.getText().toString();
            String reEnterPassword = ReenterPassword.getText().toString();
            //insertdata();

        SendingDataToRemoteDb sendingDataToRemoteDb=    new SendingDataToRemoteDb(this);
//        Toast.makeText(SignUp.this,password.getText().toString(),Toast.LENGTH_SHORT).show();
        sendingDataToRemoteDb.registeringuser(Name,Address,Email,Mobile,cnic.getText().toString(),password.getText().toString());

            // TODO: Implement your own signup logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            onSignupSuccess();

                            // onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }
    }
    public void onSignupSuccess() {
        signup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }


    public  boolean validate()
    {
        boolean valid=true;
        _name=name.getText().toString();
        _address=address.getText().toString();
        _email=email.getText().toString();
        _cnic=cnic.getText().toString();
        _password=password.getText().toString();
        _mobilenumber=mobilenumber.getText().toString();
        _ReenterPassword=ReenterPassword.getText().toString();

        if (_name.isEmpty() || _name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (_address.isEmpty()) {
            address.setError("Enter Valid Address");
            valid = false;
        } else {
            address.setError(null);
        }


        if (_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if(_cnic.length()<13 || _cnic.length()>13)
        {
            cnic.setError("Please Enter A valid Cnic");
            valid=false;
        }
        else
        {
            cnic.setError(null);
        }

        if (_mobilenumber.isEmpty() || _mobilenumber.length()!=11) {
            mobilenumber.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobilenumber.setError(null);
        }

        if (_password.isEmpty() || password.length() < 4 || _password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (_ReenterPassword.isEmpty() || _ReenterPassword.length() < 4 || _ReenterPassword.length() > 10 || !(_ReenterPassword.equals(_password)))
        {ReenterPassword.setError("Password Do not match");
            ReenterPassword.setError("Password Do not match");
        valid = false;

        } else {
            ReenterPassword.setError(null);
        }

        return valid;




    }




}
