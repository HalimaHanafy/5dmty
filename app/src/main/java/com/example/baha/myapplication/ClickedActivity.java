package com.example.baha.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;

import models.market;

public class ClickedActivity extends ActionBarActivity {
    private static String SERVICE_URL = "http://mar.gt4host.com/market/public/webservice/getimage";
    private ProgressDialog pDialog;
    private int imageId;
    private String imageName;
    private ImageView image;
    private TextView image_name,image_namee, image_place, image_details, image_other;
    private TextView one,two,three,four;
    private market marketobject;
    private Bitmap bitmap;

    public static GoogleMap map;
    GPSTracker gps;
    Marker mMarker;
    android.support.v7.app.ActionBar abar;
    private View mLoadingView;
    private View content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.clicked_activity);
        setContentView(R.layout.cardnew);

        mLoadingView = findViewById(R.id.loading_spinner);
        content = findViewById(R.id.content);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");

        abar = getSupportActionBar();
        abar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_title, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams( android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,  android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,  Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.colorPrimaryDark);
        abar.setHomeButtonEnabled(true);


        Intent mIntent = getIntent();
        imageId = mIntent.getExtras().getInt("imageId");
        imageName = mIntent.getExtras().getString("imageName");



        SpannableString s = new SpannableString(imageName);

        textviewTitle.setText(s);
        textviewTitle.setTextSize(20);
        textviewTitle.setTypeface(face);


//        image = (ImageView) findViewById(R.id.imagepic);

        image = (ImageView)findViewById(R.id.imagepic);


        image_name = (TextView) findViewById(R.id.image_name);
//        image_namee = (TextView) findViewById(R.id.image_namee);
        image_place = (TextView) findViewById(R.id.image_place);
        image_details = (TextView) findViewById(R.id.image_details);
        image_other = (TextView) findViewById(R.id.image_other);

        one= (TextView) findViewById(R.id.imag);
        two= (TextView) findViewById(R.id.image_d);
        three= (TextView) findViewById(R.id.image_de);
        four= (TextView) findViewById(R.id.image_pace);

        one.setTypeface(face);
        two.setTypeface(face);
        three.setTypeface(face);
        four.setTypeface(face);

        new GetImage().execute();

        //font
        image_details.setTypeface(face);
        image_place.setTypeface(face);
        image_name.setTypeface(face);
//        image_namee.setTypeface(face);



        //map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //drawMarkerWithCircle(30.043650, 31.236545);

        map.setMyLocationEnabled(true);
        gps = new GPSTracker(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
//            Toast.makeText(MainActivity.this, "back", Toast.LENGTH_LONG).show();

            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            finish();

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_us) {

            Intent i = new Intent(getApplicationContext(), AboutUs.class);
            startActivity(i);
            finish();

        }
        if (id == R.id.search) {

            Intent i = new Intent(getApplicationContext(), search.class);
            startActivity(i);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
    public void PutPlacesMarker(double h,double l,String place)
    {

        LatLng StaticMine = new LatLng(h ,l);
            /*     My location Static One   */
        Marker meHere = map.addMarker(new MarkerOptions().position(StaticMine).title(place).snippet("I am here in Mac ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        // zoom in the camera to My place
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(StaticMine, 17));
        // animate the zoom process
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

    }
    private class GetImage extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            pDialog = new ProgressDialog(ClickedActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();

            content.setVisibility(View.INVISIBLE);
            mLoadingView.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... arg0) {
            ServiceHandler servicehandler = new ServiceHandler();
            String jsonStr = servicehandler.makeServiceCall(SERVICE_URL+"?market_id="+imageId, ServiceHandler.GET);
            if(jsonStr!=null){

                Gson gson= new Gson();
                marketobject=gson.fromJson(jsonStr,market.class);
                try {

                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(marketobject.getMarket_url()).getContent());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "true";

            }
            else
            {
                return "false";
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           if ( result=="true") {
//
//                if(pDialog.isShowing())
//                pDialog.dismiss();
                image.setImageBitmap(bitmap);
                image_name.setText(marketobject.getMarket_name());
                image_place.setText(marketobject.getPlace_name());
                image_details.setText(marketobject.getMarket_details());
               PutPlacesMarker(marketobject.getMarket_latt(), marketobject.getMarket_long(),marketobject.getPlace_name());

               mLoadingView.setVisibility(View.INVISIBLE);
               content.setVisibility(View.VISIBLE);

//               if(marketobject.getMarket_other()!=null)
//                    image_other.setText(marketobject.getMarket_other());

            }
            else{

//                pDialog.dismiss();
//                Toast.makeText(ClickedActivity.this, "Can't reload Network Error", Toast.LENGTH_SHORT).show();



           }


        }
    }
}
