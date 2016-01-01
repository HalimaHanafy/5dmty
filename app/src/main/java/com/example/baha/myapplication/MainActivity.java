package com.example.baha.myapplication;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager_sec;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private SolventRecyclerViewAdapter rcAdapter;
    android.support.v7.app.ActionBar abar;


    private String URL_TOP_250 = "http://mar.gt4host.com/market/public/webservice/listallmarkets";
    private SwipeRefreshLayout swipeRefreshLayout;
//    private ListView listView;
    private SwipeListAdapter adapter;
    private List<ItemObjects> movieList;
    private JSONArray market = null;

    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

//    public static List<ItemObjects> listViewItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");

        abar = getSupportActionBar();

        abar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_title_search, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams( android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,  android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,  Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview1);
        textviewTitle.setText("خدمتى");
        textviewTitle.setTypeface(face);
        textviewTitle.setTextSize(20);

        abar.setDisplayUseLogoEnabled(true);


        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);

//        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.colorPrimaryDark);
//        abar.setHomeButtonEnabled(true);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(false);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, gaggeredGridLayoutManager.VERTICAL);
        gaggeredGridLayoutManager_sec = new StaggeredGridLayoutManager(2, gaggeredGridLayoutManager_sec.VERTICAL);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        adapter = new SwipeListAdapter(MainActivity.this, movieList);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        movieList = new ArrayList<>();

        rcAdapter= new SolventRecyclerViewAdapter(MainActivity.this, movieList);
        recyclerView.setAdapter(rcAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        gaggeredGridLayoutManager.invalidateSpanAssignments();
                                        new GetMarkets().execute();
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
        if (id == R.id.action_about_us) {
            Intent i = new Intent(getApplicationContext(), AboutUs.class);
            startActivity(i);
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
            ServiceHandler servicehandler = new ServiceHandler();
            String jsonStr = servicehandler.makeServiceCall(URL_TOP_250+"?offset="+offSet, ServiceHandler.GET);
            if(jsonStr!=null){
                Log.d("Markets >>",jsonStr);
                Bitmap bitmap=null;

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    market = jsonObj.getJSONArray("markets");
                    for (int i = 0; i < market.length(); i++) {
                        JSONObject c = market.getJSONObject(i);
                        int img_id = parseInt(c.getString("market_id"));
                        String img_name = c.getString("market_name");
                        String img_url = c.getString("market_url");
                        try {

                            bitmap = BitmapFactory.decodeStream((InputStream)new URL(img_url).getContent());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ItemObjects m = new ItemObjects(img_name, bitmap,img_id);
                        movieList.add(0, m);
                        bitmap=null;

                        // updating offset value to highest value
                        if (img_id >= offSet)
                            offSet = img_id;


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
            // stopping swipe refresh

            if(s!=null) {
                /////*********************الحـــــــــــــــــــــــــــــــــــــل*************//////
                gaggeredGridLayoutManager.onDetachedFromWindow(recyclerView, null);
                gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                rcAdapter = new SolventRecyclerViewAdapter(MainActivity.this, movieList);
                recyclerView.setAdapter(rcAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
            else {

                //snakebar
                Toast.makeText(getApplicationContext(), "open network", Toast.LENGTH_LONG).show();
                // stopping swipe refresh
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