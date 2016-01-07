package com.example.baha.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private TextView updateText;

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private RecyclerView recyclerView;
    private SolventRecyclerViewAdapter rcAdapter;


    public SecondFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
//        recyclerView.setHasFixedSize(true);

        /////*********************الحـــــــــــــــــــــــــــــــــــــل*************//////
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
//        gaggeredGridLayoutManager.onDetachedFromWindow(recyclerView, null);

        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        updateText = (TextView)view.findViewById(R.id.text_update);
        return view;
    }

    public void updateTextField(String newText,String newPlace,String newCat,ArrayList<ItemObjects> newList){

        Log.d("txt>>", newText);



        gaggeredGridLayoutManager.onDetachedFromWindow(recyclerView, null);
        gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rcAdapter = new SolventRecyclerViewAdapter(getActivity(), newList);
        recyclerView.setAdapter(rcAdapter);

        if(newText.equals("")&&newPlace.equals("")&&newCat.equals("")&&newList.size()==0)
        {
            updateText.setText("من فضلك أدخل على الأقل الإسم أو الفئة أو المكان !");
        }
        else if((!newText.equals("")||!newPlace.equals("")||!newCat.equals(""))&&newList.size()==0)
        {
            updateText.setText("لا توجد بيانات فى هذا البحث !");
        }
        else
        {
            updateText.setText("");

        }

    }
}
