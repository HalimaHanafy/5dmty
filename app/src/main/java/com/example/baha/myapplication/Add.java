package com.example.baha.myapplication;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import database.StorageDatabaseAdapter;
import models.category;
import models.place;
import popuplistener.popupspinner;
import popuplistener.popupview;
import utils.CustomRequest;

import static java.lang.Integer.parseInt;

/**
 * Created by Asmaa on 21/12/2015.
 */
public class Add extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    private Button send,location;
    private EditText name,details,other;
    private ImageView ivImage;
    private TextView textviewlocation;
    private String locationlong,locationlatt;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private String inserturl = "http://sp.cr-prog.com/market/public/webservice/insert";
    private String placescategoriesurl = "http://sp.cr-prog.com/market/public/webservice/getallplacesandcategories";

    private ProgressDialog pDialog;
    private String encodedImage;

    private View mLoadingView;
    private View content;
    private StorageDatabaseAdapter storageHelper ;
    public static Typeface face;


    private static final String PLACE_ID = "place_id";
    private static final String PLACE_NAME = "place_name";
    private JSONArray placearray = null;
    private place[] places;
    List<ItemObjects> listViewPlaces;
    ArrayAdapter<String> dataAdapter;
    ArrayAdapter<String> dataAdapter2;

    private static final String CAT_ID = "category_id";
    private static final String CAT_NAME = "category_name";
    private JSONArray categoryarray = null;
    private category[] categorys;
    List<ItemObjects> listViewCategorys;

    private Spinner Placespinner;
    private Spinner Categoryspinner;

    private popupview nameerror;
    private popupview detailserror;
    private popupview imageerror;
    private popupview locationerror;
    private popupspinner placeerror;
    private popupspinner caterror;

    android.support.v7.app.ActionBar abar;

    category catobject;
    place placeobject;

    public static GoogleMap map;
    GPSTracker gps;




    private static final int PLACE_PICKER_REQUEST = 1000;
    private GoogleApiClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        face= Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");
        storageHelper = new StorageDatabaseAdapter(this);
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


        textviewTitle.setText("إضافة");
        textviewTitle.setTextSize(20);
        textviewTitle.setTypeface(face);




        mLoadingView = findViewById(R.id.loading_spinner8);
        content = findViewById(R.id.content8);

        send=(Button)findViewById(R.id.send);
        send.setTypeface(face);
        location=(Button)findViewById(R.id.location);
        location.setTypeface(face);
        name=(EditText)findViewById(R.id.insert_name);
        name.setTypeface(face);
        nameerror=new popupview(this,name);
        details=(EditText)findViewById(R.id.details);
        details.setTypeface(face);
        detailserror=new popupview(this,details);
        other=(EditText)findViewById(R.id.other);
        other.setTypeface(face);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        imageerror=new popupview(this,ivImage);

        textviewlocation=(TextView)findViewById(R.id.textviewlocation);
        textviewlocation.setTypeface(face);
        locationerror=new popupview(this,textviewlocation);
        listViewPlaces = new ArrayList<ItemObjects>();
        // Spinner element
        Placespinner = (Spinner) findViewById(R.id.spinner_place);
        // Spinner click listener
        Placespinner.setOnItemSelectedListener(this);
        placeerror=new popupspinner(this,Placespinner);
        listViewCategorys = new ArrayList<ItemObjects>();
        // Spinner element
        Categoryspinner = (Spinner)findViewById(R.id.spinner_cat);
        // Spinner click listener
        Categoryspinner.setOnItemSelectedListener(this);
        caterror=new popupspinner(this,Categoryspinner);

        mClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapAdd)).getMap();

        map.setMyLocationEnabled(true);
        gps = new GPSTracker(this);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("هناك خطأ.....");

        alertDialog.setButton("حسنا", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.hide();
            }
        });
        alertDialog.setIcon(R.drawable.status_error);


        if(storageHelper.getallplaces().length>0&&storageHelper.getallcategories().length>0){
            String placesnames[][]=storageHelper.getallplaces();
            places=new place[placesnames.length+1];
            places[0]=new place("0","إختر المكان");

            for(int i=0;i<placesnames.length;i++){
                places[i+1]=new place(placesnames[i][0],placesnames[i][1]);
            }

            String categoriesnames[][]=storageHelper.getallcategories();
            categorys=new category[categoriesnames.length+1];
            categorys[0]=new category("0","إختر الفئة");
            for(int i=0;i<categoriesnames.length;i++){

                categorys[i]=new category(categoriesnames[i][0],categoriesnames[i][1]);


            }
            dataAdapter = new ArrayAdapter(Add.this, android.R.layout.simple_spinner_item, places);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            Placespinner.setAdapter(dataAdapter);

            dataAdapter2 = new ArrayAdapter(Add.this, android.R.layout.simple_spinner_item, categorys);

            // Drop down layout style - list view with radio button
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            Categoryspinner.setAdapter(dataAdapter2);

        }
        else
        {
            if (isNetworkAvailable(this.getBaseContext())) {
                new GetPlacesCategories().execute();
            }
            else{
                Toast.makeText(this, "NO Network connected", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Plz, Turn on network then try again", Toast.LENGTH_LONG).show();

            }

        }



        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Add.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();


            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /// check for internet
                if (isNetworkAvailable(Add.this.getBaseContext())) {
                    boolean data=true;
                    //////data chck

                    if(ivImage.getDrawable() == null){
//                        imageerror.showPopup();
                        data=false;

                    }
                    else {
                        imageerror.dismisspopup();
                    }

                    if(name.getText().toString().equals("")){

//                        nameerror.showPopup();
                        data=false;

                    }
                    else {
                        nameerror.dismisspopup();
                    }
                    if(details.getText().toString().equals("")){
//                        detailserror.showPopup();
                        data=false;

                    }
                    else {
                        detailserror.dismisspopup();
                    }
                    if(catobject!=null)
                    {
                        if(catobject.getId().equals("0")){
//                            caterror.showPopup();
                            data=false;
                        }
                        else {
                            caterror.dismisspopup();
                        }
                    }
                    else{
                        data=false;
                    }

                    if(placeobject!=null){
                        if(placeobject.getId().equals("0")){
//                            placeerror.showPopup();
                            data=false;
                        }
                        else {
                            placeerror.dismisspopup();
                        }

                    }
                    else {
                        data=false;
                    }
                    if(locationlong==null||locationlatt==null){
//                        locationerror.showPopup();
                        data=false;

                    }
                    else {
                        locationerror.dismisspopup();
                    }
                    if(data==false){

                        alertDialog.setMessage("من فضلك أكمل البيانات");
                        alertDialog.show();
                    }

                    else{

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", name.getText().toString());
                        params.put("image", encodedImage);
                        params.put("details",details.getText().toString());
                        params.put("other",other.getText().toString());
                        params.put("cat_id",catobject.getId());
                        params.put("place_id",placeobject.getId());
                        params.put("long",locationlong);
                        params.put("latt",locationlatt);

                        pDialog = new ProgressDialog(Add.this);
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        RequestQueue requestQueue = Volley.newRequestQueue(Add.this);
                        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, inserturl, params,
                                Add.this.createRequestSuccessListener(), Add.this.createRequestErrorListener());


                        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));
                        requestQueue.add(jsObjRequest);

                    }

                }
                else {
                    Toast.makeText(Add.this, "we can't send because of Internet Connection", Toast.LENGTH_LONG).show();
                    Toast.makeText(Add.this, "Plz, Turn on network then try again", Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();


            }
        };
    }
    private Response.Listener<JSONObject> createRequestSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    hideProgressDialog();
//                    Toast.makeText(getApplicationContext(),
//                            "THE RESPNOSE"+response,
//                            Toast.LENGTH_LONG).show();
                    Log.e("THE RESPNOSE", response.getString("response"));
                    if (response.getString("response").equals("success")) {
                        Toast.makeText(getApplicationContext(),
                                "Upload successful.", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Add.this,MainActivity.class);
                        startActivity(i);

                    }
                    else if(response.getString("response").equals("fail")){
                        Toast.makeText(getApplicationContext(),
                                "Upload fail please try again.", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong, please try again",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "catch",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
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

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_uss) {

            Dialog dialog = new Dialog(Add.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.aboutus);
            TextView textView = (TextView) dialog.findViewById(R.id.textViewAboutDialogTitle);
            textView.setTypeface(face);

            dialog.show();
            return true;        }
        if (id == R.id.search) {

            Intent i = new Intent(getApplicationContext(), search.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }
    private class GetPlacesCategories extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            content.setVisibility(View.INVISIBLE);
            mLoadingView.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... arg0) {
            ServiceHandler servicehandler = new ServiceHandler();
            String jsonStr = servicehandler.makeServiceCall(placescategoriesurl, ServiceHandler.GET);
            if(jsonStr!=null){
                Log.d("listOfPlaces >>", jsonStr);

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    placearray = jsonObj.getJSONArray("places");

                    places=new place[placearray.length()+1];

                    String place_name, place_id;
                    JSONObject c;
                    for (int i = 0; i < placearray.length()+1; i++) {
                        if(i==0) {
                            place_id = "0";
                            place_name = "إختر المكان";
                        }
                        else
                        {
                            c = placearray.getJSONObject(i-1);
                            place_id = c.getString(PLACE_ID);
                            place_name = c.getString(PLACE_NAME);
                        }


                        places[i]=new place(place_id,place_name);
                        try {
                            int id1 = storageHelper.deleteplace(parseInt(place_id));
                            long id = storageHelper.insertPlace(parseInt(place_id), place_name);
                        }
                        catch(Exception e){

                        }

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("listOfCats >>", jsonStr);

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    categoryarray = jsonObj.getJSONArray("categories");

                    String cat_id,cat_name;
                    categorys=new category[categoryarray.length()+1];
                    JSONObject c;
                    for (int i = 0; i < categoryarray.length()+1; i++) {


                        if(i==0) {
                            cat_id = "0";
                            cat_name = "إختر الفئة";
                        }
                        else
                        {
                            c = categoryarray.getJSONObject(i - 1);
                            cat_id = c.getString(CAT_ID);
                            cat_name = c.getString(CAT_NAME);

                        }


                        categorys[i]=new category(cat_id,cat_name);
                        try {
                            int id2 = storageHelper.deletecategory(parseInt(cat_id));
                            long id = storageHelper.insertCategory(parseInt(cat_id), cat_name);
                        }
                        catch(Exception e){

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("ResultPlaces: >>", result + "");

            if(result !=null) {
                dataAdapter = new ArrayAdapter(Add.this, android.R.layout.simple_spinner_item, places);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                Placespinner.setAdapter(dataAdapter);

                dataAdapter2 = new ArrayAdapter(Add.this, android.R.layout.simple_spinner_item, categorys);

                // Drop down layout style - list view with radio button
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                Categoryspinner.setAdapter(dataAdapter2);

                content.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.INVISIBLE);

            }
            else
            {
                content.setVisibility(View.INVISIBLE);
                mLoadingView.setVisibility(View.VISIBLE);

            }

        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpeg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivImage.setImageBitmap(thumbnail);
                ivImage.setBackground(null);
                byte[] byteArray = bytes .toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                if(bm!=null){

                    ivImage.setImageBitmap(bm);
                    ivImage.setBackground(null);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }
                else
                {
                    Toast.makeText(Add.this,"Sorry you choose video",Toast.LENGTH_LONG).show();

                }


            }
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                locationlatt = String.valueOf(place.getLatLng().latitude);
                locationlong = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
//                stBuilder.append("\n");
//                stBuilder.append("Latitude: ");
//                stBuilder.append(latitude);
//                stBuilder.append("\n");
//                stBuilder.append("Logitude: ");
//                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                textviewlocation.setText(stBuilder.toString());
                PutPlacesMarker(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        catobject= (category)Categoryspinner.getSelectedItem();
        placeobject=(place)Placespinner.getSelectedItem();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //check internet
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }
    @Override
    protected void onStop() {
        mClient.disconnect();
        super.onStop();
    }
    public void PutPlacesMarker(double h,double l)
    {

        LatLng StaticMine = new LatLng(h ,l);
            /*     My location Static One   */
        Marker meHere = map.addMarker(new MarkerOptions().position(StaticMine).title("ME").snippet("I am here in Mac "));
        // zoom in the camera to My place
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(StaticMine, 15));
        // animate the zoom
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }




}



