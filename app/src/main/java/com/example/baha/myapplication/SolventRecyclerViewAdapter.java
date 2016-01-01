package com.example.baha.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SolventRecyclerViewAdapter  extends RecyclerView.Adapter<SolventViewHolders> {

    private List<ItemObjects> itemList;
    private Context context;

    public SolventRecyclerViewAdapter(Context context, List<ItemObjects> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.solvent_list, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SolventViewHolders holder, int position) {
        holder.countryName.setText(itemList.get(position).getName());
        if(itemList.get(position).getPhoto()!=null) {
            holder.countryPhoto.setImageBitmap(itemList.get(position).getPhoto());
        }
        else
        {
            Drawable res = context.getResources().getDrawable(R.drawable.add);
            holder.countryPhoto.setImageDrawable(res);


        }

        holder.imageId=itemList.get(position).getId();

        //font
        Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/DroidKufi-Bold.ttf");

        holder.countryName.setTypeface(face);
        holder.country_subName.setTypeface(face);




    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
