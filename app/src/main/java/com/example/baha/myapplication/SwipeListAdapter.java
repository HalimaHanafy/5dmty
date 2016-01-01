package com.example.baha.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ravi on 13/05/15.
 */
public class SwipeListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ItemObjects> movieList;


    public SwipeListAdapter(MainActivity activity, List<ItemObjects> movieList) {
        this.activity = activity;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int location) {
        return movieList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
//            convertView = inflater.inflate(R.layout.list_row, null);
        convertView = inflater.inflate(R.layout.solvent_list, null);

//        TextView serial = (TextView) convertView.findViewById(R.id.serial);
//        TextView title = (TextView) convertView.findViewById(R.id.title);

        TextView subcountry_name = (TextView) convertView.findViewById(R.id.subcountry_name);
        TextView country_name = (TextView) convertView.findViewById(R.id.country_name);
        ImageView countryPhoto = (ImageView)  convertView.findViewById(R.id.country_photo);

//        serial.setText(String.valueOf(movieList.get(position).id));
//        title.setText(movieList.get(position).title);

        subcountry_name.setText(String.valueOf(movieList.get(position).getId()));
        country_name.setText(movieList.get(position).getName());
        countryPhoto.setImageBitmap(movieList.get(position).getPhoto());

//

        return convertView;
    }

}