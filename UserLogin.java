package com.example.lucifer.androidappforcrimereporting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity {
    AppCompatButton btn;
        EditText name,password;
        SharedPreferences sp;

    TextView tv,forgotpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp=getSharedPreferences("Login",MODE_PRIVATE);
        setContentView(R.layout.activity_user_login);
       btn=(AppCompatButton)findViewById(R.id.btn_login);
       name=(EditText)findViewById(R.id.input_email);
       password=(EditText)findViewById(R.id.input_password);
       tv=(TextView)findViewById(R.id.link_signup);
       forgotpassword=(TextView)findViewById(R.id.link_forgot);
        if(sp.contains("username")){
            Intent mainIntent = new Intent(UserLogin.this,MainHomePage.class);
            UserLogin.this.startActivity(mainIntent);
            Toast.makeText(UserLogin.this,  sp.toString(),Toast.LENGTH_LONG).show();

            UserLogin.this.finish();  //finish current activity
        }


       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               SendingDataToRemoteDb sendingDataToRemoteDb= new SendingDataToRemoteDb(UserLogin.this);
               sendingDataToRemoteDb.Login(name.getText().toString(),password.getText().toString());



           ///    Intent i= new Intent(UserLogin.this,MainHomePage.class);
              // startActivity(i);
           }
       });
       tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i= new Intent(UserLogin.this,SignUp.class);
               startActivity(i);


           }
       });
       forgotpassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LayoutInflater li = LayoutInflater.from(UserLogin.this);
               View promptsView = li.inflate(R.layout.customdialogbox, null);

               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                       UserLogin.this);

               // set prompts.xml to alertdialog builder
               alertDialogBuilder.setView(promptsView);

               final EditText userInput = (EditText) promptsView
                       .findViewById(R.id.editTextDialogUserInput);

               // set dialog message
               alertDialogBuilder
                       .setCancelable(false)
                       .setPositiveButton("OK",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog,int id) {
                                       // get user input and set it to result
                                       // edit text
                                     ///  result.setText(userInput.getText());
                                       SendingDataToRemoteDb sendingDataToRemoteDb=new SendingDataToRemoteDb(UserLogin.this);
                                       sendingDataToRemoteDb.ForgotPassword(userInput.getText().toString());

                                   }
                               })
                       .setNegativeButton("Cancel",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int id) {
                                       dialog.cancel();
                                   }
                               });

               // create alert dialog
               AlertDialog alertDialog = alertDialogBuilder.create();

               // show it
               alertDialog.show();

           }
       });


    }
}
