package com.example.baha.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import database.StorageDatabaseAdapter;
import models.category;
import models.place;

import static java.lang.Integer.parseInt;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FirstFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private StorageDatabaseAdapter storageHelper ;
    private OnFragmentInteractionListener mListener;
    private EditText userInput;
    private String selected_name,selected_place,selected_cat;

    search s_obj;
    public static Spinner Placespinner;
    public static Spinner Categoryspinner;

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
    private View mLoadingView;
    private View content;


    private String placescategoriesurl = "http://sp.cr-prog.com/market/public/webservice/getallplacesandcategories";


    private static String SERVICE_URL_search = "http://sp.cr-prog.com/market/public/webservice/search";
    private static final String MARKET_ID = "market_id";
    private static final String MARKET_NAME = "market_name";
    private static final String MARKET_URL = "market_url";
    private static final String MARKET_DETAILS = "market_details";
    private static final String MARKET_OTHERS = "market_other";
    private static final String MARKET_LONG = "market_long";
    private static final String MARKET_LATT = "market_latt";
    private static final String PLACENAME = "place_name";


    private JSONArray market = null;
    public static ArrayList<ItemObjects> listViewItems;
    public static List<ItemObjects> pub_listViewItems;
    public static Button searchBtn;

    private ProgressDialog pDialog;


    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        userInput = (EditText)view.findViewById(R.id.name_input);
        storageHelper = new StorageDatabaseAdapter(getActivity());
        listViewPlaces = new ArrayList<ItemObjects>();
        // Spinner element
        Placespinner = (Spinner) view.findViewById(R.id.spinner_place);
        // Spinner click listener
        Placespinner.setOnItemSelectedListener(this);

        listViewCategorys = new ArrayList<ItemObjects>();
        // Spinner element
        Categoryspinner = (Spinner) view.findViewById(R.id.spinner_cat);
        // Spinner click listener
        Categoryspinner.setOnItemSelectedListener(this);



//        mLoadingView = view.findViewById(R.id.loading_spinner_oooo);
//        content = view.findViewById(R.id.contentooo);


        listViewItems = new ArrayList<>();


        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/DroidKufi-Bold.ttf");

        searchBtn = (Button)view.findViewById(R.id.search_btn);
        searchBtn.setTypeface(face);
        searchBtn.setClickable(false);

        if(storageHelper.getallplaces().length>0&&storageHelper.getallcategories().length>0){
            searchBtn.setClickable(true);

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
            dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, places);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            Placespinner.setAdapter(dataAdapter);

            dataAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categorys);

            // Drop down layout style - list view with radio button
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            Categoryspinner.setAdapter(dataAdapter2);

        }
        else
        {
            if (isNetworkAvailable(getActivity().getBaseContext())) {
                new GetPlacesCategories().execute();
            }
            else{
                Toast.makeText(getActivity(), "please open network", Toast.LENGTH_LONG).show();

            }

        }


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getActivity().getBaseContext())) {
                    // code here
                    Toast.makeText(getActivity(), "Network connected", Toast.LENGTH_LONG).show();

                    selected_name = userInput.getText().toString();
                    selected_place = Placespinner.getSelectedItem().toString();
                    selected_cat = Categoryspinner.getSelectedItem().toString();

                    if(selected_cat.equals("متعلقات شخصية"))
                    {
                        selected_cat="متعلقات%20شخصية";
                    }

                    //only name
                    if(selected_name!=""&&selected_place.equals( "إختر المكان")&&selected_cat.equals("إختر الفئة")) {
                        selected_place="";
                        selected_cat="";
                        new GetMarkets().execute();
                    }

                    //only place
                    else if(selected_name==""&&selected_place!=""&&selected_cat.equals("إختر الفئة")) {
                        selected_name="";
                        selected_cat="";
                        new GetMarkets().execute();
                    }

                    //only cat
                    else if(selected_name==""&&selected_place.equals( "إختر المكان")&&selected_cat!="") {
                        selected_name="";
                        selected_place="";
                        new GetMarkets().execute();
                    }

                    //name and place
                    else if(selected_name!=""&&selected_place!=""&&selected_cat.equals("إختر الفئة")) {
                        selected_cat="";
                        new GetMarkets().execute();
                    }

                    //name and cat
                    else if(selected_name!=""&&selected_place.equals( "إختر المكان")&&selected_cat!="") {
                        selected_place="";
                        new GetMarkets().execute();
                    }

                    //place and cat
                    else if(selected_name==""&&selected_place!=""&&selected_cat!="") {
                        selected_name="";
                        new GetMarkets().execute();
                    }

                    //name and place and cat
                    else
                    {
                        new GetMarkets().execute();
                    }



                } else {
                    // code
                    Toast.makeText(getActivity(), "NO Network connected", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "Plz, Turn on network then try again", Toast.LENGTH_LONG).show();

                }

            }
        });

        return view;
    }


    private class GetMarkets extends AsyncTask<Void, Void, ArrayList<ItemObjects>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchBtn.setClickable(false);

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ArrayList<ItemObjects> doInBackground(Void... arg0) {
            ServiceHandler servicehandler = new ServiceHandler();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name",""+selected_name));
            nameValuePairs.add(new BasicNameValuePair("place", "" + selected_place));
            nameValuePairs.add(new BasicNameValuePair("category", "" + selected_cat));
            String jsonStr = servicehandler.makeServiceCall(SERVICE_URL_search, ServiceHandler.GET,nameValuePairs);
            if(jsonStr!=null){
                Log.d("Markets >>",jsonStr);
                listViewItems.clear();
                try {
                    market = new JSONArray(jsonStr);
                    for (int i = 0; i < market.length(); i++) {
                        JSONObject c = market.getJSONObject(i);

                        String market_id = c.getString(MARKET_ID);
                        String market_name = c.getString(MARKET_NAME);
                        String market_url = c.getString(MARKET_URL);
                        String market_details = c.getString(MARKET_DETAILS);
                        String market_others = c.getString(MARKET_OTHERS);
                        String market_long = c.getString(MARKET_LONG);
                        String market_latt = c.getString(MARKET_LATT);
                        String place_name = c.getString(PLACE_NAME);
                        listViewItems.add(0,new ItemObjects(parseInt(market_id),market_name,market_url,place_name,market_details,market_others,market_long,market_latt));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return listViewItems;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemObjects> result) {
            super.onPostExecute(result);


                if(pDialog.isShowing())
                    pDialog.dismiss();
                PutSearchResults(selected_name, selected_place, selected_cat, result);
                searchBtn.setClickable(true);


        }
    }


    public void PutSearchResults(String userContent, String sel_place, String sel_cat, ArrayList<ItemObjects> list) {

        Log.d("listbefore>> ",list.toString());
        if (mListener != null) {
            mListener.onFragmentInteraction(userContent,sel_place,sel_cat,list);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String userContent, String sel_place, String sel_cat, ArrayList<ItemObjects> list);
    }


    private class GetPlacesCategories extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            content.setVisibility(View.INVISIBLE);
//            mLoadingView.setVisibility(View.VISIBLE);

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

            if (result!=null) {
                dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, places);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                Placespinner.setAdapter(dataAdapter);

                dataAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categorys);

                // Drop down layout style - list view with radio button
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                Categoryspinner.setAdapter(dataAdapter2);

                searchBtn.setClickable(true);

            }

//            content.setVisibility(View.VISIBLE);
//            mLoadingView.setVisibility(View.INVISIBLE);



        }
    }

    //check internet
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }




}
