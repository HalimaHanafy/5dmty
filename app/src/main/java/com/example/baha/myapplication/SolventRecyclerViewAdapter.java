package com.example.baha.myapplication;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SolventRecyclerViewAdapter  extends RecyclerView.Adapter<SolventViewHolders>{

    private ArrayList<ItemObjects> designs;
    Context context;

    // Adapter's Constructor
    public SolventRecyclerViewAdapter(Context context, ArrayList<ItemObjects> designs) {
        this.designs = designs;
        this.context = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.solvent_list, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SolventViewHolders holder, int position) {
         Typeface face= Typeface.createFromAsset(context.getAssets(), "fonts/DroidKufi-Bold.ttf");
        final TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.country_name);
        final ImageView imageViewImage = (ImageView) holder.itemView.findViewById(R.id.country_photo);
        holder.imageId=designs.get(position).getId();
        holder.imagename=designs.get(position).getName();
        holder.market_url=designs.get(position).getUrl();
        holder.market_details=designs.get(position).getDetails();
        holder.market_other=designs.get(position).getOther();
        holder.market_long= designs.get(position).getMlong();
        holder.market_latt= designs.get(position).getMlatt();
        holder.place_name=designs.get(position).getPlacename();
        textViewTitle.setText(designs.get(position).getName());
        holder.countryName.setTypeface(face);
        holder.country_subName.setTypeface(face);
        Picasso.with(context).load(designs.get(position).getUrl())
                .placeholder(holder.itemView.getResources()
                        .getDrawable(R.drawable.add)).into(imageViewImage);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return designs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
