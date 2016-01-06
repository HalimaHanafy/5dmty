package com.example.baha.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Halima on 12/25/2015.
 */

public class search extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener{


    android.support.v7.app.ActionBar abar;
    public View mLoadingView;
    public View content;

    FirstFragment obj1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
//        getSupportActionBar().setTitle("Search");

        mLoadingView = findViewById(R.id.loading_spinner2);
        content = findViewById(R.id.content2);




        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/DroidKufi-Bold.ttf");

        abar = getSupportActionBar();
        abar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_title, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams( android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,  android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,  Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("بحث");
        textviewTitle.setTypeface(face);
        textviewTitle.setTextSize(20);

        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.colorPrimaryDark);
        abar.setHomeButtonEnabled(true);

        mLoadingView.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onFragmentInteraction(String userContent, String sel_place, String sel_cat, ArrayList<ItemObjects> list) {


        SecondFragment secondFragment = (SecondFragment)getSupportFragmentManager().findFragmentById(R.id.fragment2);
        secondFragment.updateTextField(userContent, sel_place, sel_cat, list);



        Log.d("listttt>>",list+"");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_search, menu);
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
}
