package com.example.baha.myapplication;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView countryName,country_subName;
    public ImageView countryPhoto;
    public int imageId;

    public SolventViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        country_subName = (TextView) itemView.findViewById(R.id.subcountry_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);


    }

    @Override
    public void onClick(View view) {

        Intent ii = new Intent(view.getContext(), ClickedActivity.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        ii.putExtra(Settings.EXTRA_AUTHORITIES, new String[]{"com.example.baha.myapplication"});
        ii.putExtra("imageId", imageId);
        ii.putExtra("imageName", countryName.getText().toString());
        view.getContext().startActivity(ii);



   //     Toast.makeText(view.getContext(), "Clicked Position = " + imageId, Toast.LENGTH_SHORT).show();
    }

}
