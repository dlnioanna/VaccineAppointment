package unipi.protal.vaccineappointment;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
/*
Depending on the position of the ordered list of hospitals the color of the marker
will change. The closer the hospital, the darker the color. This method only returns
the color based on the hospital position
 */

public class ColorUtils {
    public static BitmapDescriptor getMarkerIcon(int position) {
        int color = Color.WHITE;
        if (position == 0) {
            color = R.color.material550Green;
        } else if (position == 1) {
            color = R.color.material500Green;
        } else if (position == 2) {
            color = R.color.material450Green;
        } else if (position == 3) {
            color = R.color.material400Green;
        } else if (position == 4) {
            color = R.color.material350Green;
        } else if (position == 5) {
            color = R.color.material300Green;
        } else if (position == 6) {
            color = R.color.material250Green;
        } else if (position == 7) {
            color = R.color.material200Green;
        } else if (position == 8) {
            color = R.color.material150Green;
        } else if (position == 9) {
            color = R.color.material100Green;
        }
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        Log.e("hsv", "color "+color+" hsv "+hsv[0]);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
//    public static int getDistanceColor(Context context, int listIndex) {
//        switch (listIndex) {
//            case 0:
//                return ContextCompat.getColor(context, R.color.material550Green);
//            case 1:
//                return ContextCompat.getColor(context, R.color.material500Green);
//            case 2:
//                return ContextCompat.getColor(context, R.color.material450Green);
//            case 3:
//                return ContextCompat.getColor(context, R.color.material400Green);
//            case 4:
//                return ContextCompat.getColor(context, R.color.material350Green);
//            case 5:
//                return ContextCompat.getColor(context, R.color.material300Green);
//            case 6:
//                return ContextCompat.getColor(context, R.color.material250Green);
//            case 7:
//                return ContextCompat.getColor(context, R.color.material200Green);
//            case 8:
//                return ContextCompat.getColor(context, R.color.material150Green);
//            case 9:
//                return ContextCompat.getColor(context, R.color.material100Green);
//            default:
//                return Color.WHITE;
//        }
//    }
}
