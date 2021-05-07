package unipi.protal.vaccineappointment;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

public class ColorUtils {
    public static int getViewHolderBackgroundColor(Context context, int position) {
        int color = Color.WHITE;
        if(position%2==0){
            color=ContextCompat.getColor(context, R.color.adapter_color);
        }
      return color;
    }
}
