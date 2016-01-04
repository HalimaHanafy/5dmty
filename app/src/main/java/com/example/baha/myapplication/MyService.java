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

import database.StorageDatabaseAdapter;


public class MyService extends Service {
    BroadcastReceiver mReceiver;
    private  static StorageDatabaseAdapter storageHelper;
    private static String url = "http://mar.gt4host.com/market/public/webservice/checkupdates";
    private static String lastdate;
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
               storageHelper = new StorageDatabaseAdapter(mcontext);

                String status = NetworkUtil.getConnectivityStatusString(mcontext);

            if(!(status.equals("Not connected to Internet")))
                {
//                    SharedPreferences preferences = context.getSharedPreferences("5dmty", context.getApplicationContext().MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("Network", "true");
//                    editor.commit();
//                    if(storageHelper.getMarketVersion()!=null){
//                        lastdate=storageHelper.getMarketVersion();
//                        new Getupdateddata().execute();
//
//                    }

                }
                else
                {
//                    SharedPreferences preferences = context.getSharedPreferences("5dmty", context.getApplicationContext().MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("Network", "false");
//                    editor.commit();

                }



            }
        private class Getupdateddata extends AsyncTask<Void, Void, Boolean> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected Boolean  doInBackground(Void... arg0) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("updates",lastdate));
                ServiceHandler servicehandler = new ServiceHandler();
                String jsonStr = servicehandler.makeServiceCall(url, ServiceHandler.GET,nameValuePairs);
                if(jsonStr!=null){
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        if(jsonObj.getString("status").equals("True"))
                            return true;
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                // 2. set layoutManger

                if(result==true){
                    Toast.makeText(context,"on post true",Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = context.getSharedPreferences("5dmty", context.getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("updated", "true");
                    editor.commit();

                }
                else{
                    Toast.makeText(context,"on post false",Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = context.getSharedPreferences("5dmty", context.getApplicationContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("updated", "false");
                    editor.commit();

                }

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
