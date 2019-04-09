package com.example.lucifer.androidappforcrimereporting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashFullscreenActivity extends AppCompatActivity {
    private ImageView img;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_fullscreen);
        img=(ImageView)findViewById(R.id.Image);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.transition);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {


                //if SharedPreferences contains username and password then redirect to Home activity

                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashFullscreenActivity.this, UserLogin.class);
                    SplashFullscreenActivity.this.startActivity(mainIntent);

                    SplashFullscreenActivity.this.finish();
                           }
        }, 5000);






    }


}
