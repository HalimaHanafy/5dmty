package com.example.baha.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by HalimaHanafy on 5/12/2015.
 */
public class splash extends Activity {

    // splash screen timer
    private static int SPLASH_TIME_OUT =2000;
    private ViewGroup sceneRoot;
    public static boolean signedIn = false;
    SharedPreferences sharedPref;
    int offset;

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app log / company
             */

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {

                sharedPref = getSharedPreferences("5dmty", getApplicationContext().MODE_PRIVATE);
                offset = sharedPref.getInt("offset", -1);
                if(offset==-1){
                    SharedPreferences preferences = getSharedPreferences("5dmty", getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("offset", 0);
                    editor.commit();
                }


                Intent i = new Intent(splash.this, MainActivity.class);
                startActivity(i);


                finish();





            }
        }, SPLASH_TIME_OUT);


    }

}

