    package com.example.lucifer.androidappforcrimereporting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

    public class MainHomePage extends AppCompatActivity {
        CardView card, fir, cybercrime, Profile, logout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_home_page);
            card = (CardView) findViewById(R.id.IncidentReporting);
            fir = (CardView) findViewById(R.id.Fir);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainHomePage.this, MainActivity.class);
                    startActivity(i);
                }
            });
            fir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainHomePage.this, FirComplaints.class);
                    startActivity(i);
                }
            });
            cybercrime = (CardView) findViewById(R.id.CyberCrime);
            cybercrime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainHomePage.this, CyberCrime.class);
                    startActivity(i);
                }
            });
            Profile = (CardView) findViewById(R.id.profile);
            Profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainHomePage.this, UserProfile.class);
                    startActivity(i);
                }
            });
            logout = (CardView) findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.commit();
                    startActivity(new Intent(MainHomePage.this, UserLogin.class));
                }
            });


        }
    }
