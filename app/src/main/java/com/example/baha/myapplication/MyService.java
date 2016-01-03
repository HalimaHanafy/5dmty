package com.example.baha.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyService extends Service {
    BroadcastReceiver mReceiver;



    public MyService()
    {

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class NetworkChangeReciver extends BroadcastReceiver {
        Context context ;



        public NetworkChangeReciver(){

            super();
        }

        @Override
        public void onReceive(final Context mcontext, final Intent intent) {
               context = mcontext ;

                String status = NetworkUtil.getConnectivityStatusString(mcontext);
                Toast.makeText(mcontext, "" + status, Toast.LENGTH_SHORT).show();

                if(!(status.equals("Not connected to Internet")))
                {
                    SharedPreferences preferences = context.getSharedPreferences("5dmty", context.getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Network", true);
                    editor.commit();

                }
                else
                {

                    SharedPreferences preferences = context.getSharedPreferences("5dmty", context.getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Network", false);
                    editor.commit();

                }



            }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction("action");
        filter.addAction("anotherAction");
        mReceiver = new NetworkChangeReciver();
        registerReceiver(mReceiver, filter);
    }

}
