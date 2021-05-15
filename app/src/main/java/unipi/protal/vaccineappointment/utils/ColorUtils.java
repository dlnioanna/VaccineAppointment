package unipi.protal.vaccineappointment.utils;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import unipi.protal.vaccineappointment.R;

public class ColorUtils {

    // change color on each row
    public static int getViewHolderBackgroundColor(Context context, int position) {
        int color = Color.WHITE;
        if(position%2==0){
            color=ContextCompat.getColor(context, R.color.adapter_color);
        }
      return color;
    }
}
