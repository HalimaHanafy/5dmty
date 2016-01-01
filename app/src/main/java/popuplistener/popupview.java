package popuplistener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.baha.myapplication.R;

/**
 * Created by Asmaa on 31/12/2015.
 */
public class popupview {
    private Point p;
    private View layout;
    private PopupWindow popup;
    private LinearLayout viewGroup;
    private LayoutInflater layoutInflater;
    Activity context;
    private View label;


    public  popupview(final Activity context ,View label){
        this.context=context;
        this.label=label;
        popup = new PopupWindow(context);

    }
    public void showPopup() {
        int[] location = new int[2];


        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        label.getLocationInWindow(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1]-30;

        int popupWidth = 30;
        int popupHeight = 30;

        // Inflate the popup_layout.xml
        viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(false);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


    }

    public void dismisspopup(){

        if(popup!=null)
        popup.dismiss();
    }
}
