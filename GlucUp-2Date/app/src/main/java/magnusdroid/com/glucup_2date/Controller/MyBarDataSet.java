package magnusdroid.com.glucup_2date.Controller;

import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Created by Dell on 05/08/2016.
 */
public class MyBarDataSet extends BarDataSet {

    //Reference Range
    double mLowA = 3.1;
    double mHighA = 6.2;
    double mLowB = 55.8;
    double mHighB = 111.6;

    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        int c1 = Double.compare(getEntryForXIndex(index).getVal(), mLowA);
        int c2 = Double.compare(getEntryForXIndex(index).getVal(), mHighA);
        int c3 = Double.compare(getEntryForXIndex(index).getVal(), mLowB);
        int c4 = Double.compare(getEntryForXIndex(index).getVal(), mHighB);
        if(c3 > 0 && c4 < 0){
            //cCode = "N";
            Log.w("Normal","");
            return mColors.get(0);
        }
        if(c1 > 0 && c2 < 0){
            //cCode = "N";
            Log.w("Normal","");
            return mColors.get(0);
        }
        if(c1 < 0 || c3 < 0){
            //cCode = "L";
            Log.w("Low","");
            return mColors.get(1);
        }
        if((c2 > 0 && c1 >0) || c4 > 0){
            //cCode = "H";
            Log.w("High","");
            return mColors.get(2);
        }else {
            return mColors.get(3);
        }
        /*else if(c1 < 0 || c3 < 0){
            //cCode = "L";
            //Log.w("Low","");
            return mColors.get(1);
        }else if((c2 > 0 && c1 >0) || c4 > 0){
            //cCode = "H";
            //Log.w("High","");
            return mColors.get(2);
        }else{
            return mColors.get(3);
        }*/
    }
}
