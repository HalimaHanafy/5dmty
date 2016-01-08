package com.example.baha.myapplication;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import database.StorageDatabaseAdapter;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{


    public static Typeface face;
    private ArrayList<ItemObjects> designs;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    SolventRecyclerViewAdapter rcAdapter;
    android.support.v7.app.ActionBar abar;
    private String marketscache[][];

    private String URL_TOP_250 = "http://sp.cr-prog.com/market/public/webservice/listallmarkets";
    private SwipeRefreshLayout swipeRefreshLayout;

    private SwipeListAdapter adapter;
    private ArrayList<ItemObjects> cachedmovieList;
    private JSONArray market = null;
    StorageDatabaseAdapter storageHelper ;
    SharedPreferences sharedPref;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

//    public static List<ItemObjects> listViewItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageHelper = new StorageDatabaseAdapter(this);
        face= Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");

        abar = getSupportActionBar();

        abar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_title_search, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams( android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,  android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,  Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview1);
        textviewTitle.setText("خدمتى");
        textviewTitle.setTypeface(face);
        textviewTitle.setTextSize(20);
        sharedPref = this.getSharedPreferences("5dmty", this.getApplicationContext().MODE_PRIVATE);
        abar.setDisplayUseLogoEnabled(true);

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setIcon(R.color.colorPrimaryDark);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        cachedmovieList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(false);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, gaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        adapter = new SwipeListAdapter(MainActivity.this, cachedmovieList);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        rcAdapter= new SolventRecyclerViewAdapter(MainActivity.this, cachedmovieList);
        recyclerView.setAdapter(rcAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        marketscache=storageHelper.getallmarket();
                                        if(marketscache.length>0){
                                            cachedmovieList.clear();
                                            for(int i=0;i<marketscache.length;i++){
                                                ItemObjects m = new ItemObjects(parseInt(marketscache[i][0]),marketscache[i][1],marketscache[i][2],marketscache[i][5],marketscache[i][3],marketscache[i][4],marketscache[i][6],marketscache[i][7]);
                                                cachedmovieList.add(0, m);

                                            }
                                            gaggeredGridLayoutManager.onDetachedFromWindow(recyclerView, null);
                                            gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                                            rcAdapter = new SolventRecyclerViewAdapter(MainActivity.this, cachedmovieList);
                                            recyclerView.setAdapter(rcAdapter);
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                        else{
                                            swipeRefreshLayout.setRefreshing(true);
                                            gaggeredGridLayoutManager.invalidateSpanAssignments();
                                            new GetMarkets().execute();
                                        }

                                    }
                                }
        );


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Add.class);
                startActivity(i);
            }
        });
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
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_uss) {


            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.aboutus);
            TextView textView = (TextView) dialog.findViewById(R.id.textViewAboutDialogTitle);
            textView.setTypeface(face);
            dialog.show();
            return true;



        }
        if (id == R.id.search) {
            Intent i = new Intent(getApplicationContext(), search.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        new GetMarkets().execute();
    }




    private class GetMarkets extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            offSet=sharedPref.getInt("offset", 0);
            ServiceHandler servicehandler = new ServiceHandler();
            String jsonStr = servicehandler.makeServiceCall(URL_TOP_250+"?offset="+offSet, ServiceHandler.GET);
            if(jsonStr!=null){
                Log.d("Markets >>",jsonStr);
                Boolean listid=false;

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    market = jsonObj.getJSONArray("markets");
                    for (int i = 0; i < market.length(); i++) {
                        listid = false;
                        JSONObject c = market.getJSONObject(i);
                        int img_id = parseInt(c.getString("market_id"));
                        String img_name = c.getString("market_name");
                        String img_url = c.getString("market_url");
                        String img_details = c.getString("market_details");
                        String img_other = c.getString("market_other");
                        String img_place = c.getString("place_name");
                        String img_long = c.getString("market_long");
                        String img_latt = c.getString("market_latt");
                        String img_updates = c.getString("market_updates");

                        if (img_other=="null")
                        {
                            img_other="لا توجد معلومات أخرى.";
                        }

                        try {
                            int deleteid=storageHelper.deletemarket(img_id);;
                            long insertedid = storageHelper.insertMarket(img_id, img_name, img_url, img_details, img_other,img_place, img_long, img_latt, img_updates);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ItemObjects m = new ItemObjects(img_id,img_name,img_url,img_place,img_details,img_other,img_long,img_latt);


                        for(int j=0;j<cachedmovieList.size();j++){
                            if(cachedmovieList.get(j).getId()==img_id){
                                listid=true;
                            }

                        }
                        if(listid==false){
                            cachedmovieList.add(0, m);
                        }

                    }
                    if(market.length()==10)
                        {
                        offSet += 10;
                        sharedPref = getSharedPreferences("5dmty", getApplicationContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("offset", offSet);
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


                if(s!=null)
                {

                    /////*********************الحـــــــــــــــــــــــــــــــــــــل*************//////
                    gaggeredGridLayoutManager.onDetachedFromWindow(recyclerView, null);
                    gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                    rcAdapter = new SolventRecyclerViewAdapter(MainActivity.this, cachedmovieList);
                    recyclerView.setAdapter(rcAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                }
            else{
                    Toast.makeText(getApplicationContext(),"open network",Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }



        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);

        }
    }







}