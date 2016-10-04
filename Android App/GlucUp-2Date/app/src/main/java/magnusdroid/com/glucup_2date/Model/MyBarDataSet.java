package magnusdroid.com.glucup_2date.Model;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * Custom BarData to fill the bar with colour if the value of record is Hihg-Low-Normal
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
        Double aDouble = Double.valueOf(getEntryForXIndex(index).getVal());
        /*int c1 = Double.compare(getEntryForXIndex(index).getVal(), mLowA);
        int c2 = Double.compare(getEntryForXIndex(index).getVal(), mHighA);
        int c3 = Double.compare(getEntryForXIndex(index).getVal(), mLowB);
        int c4 = Double.compare(getEntryForXIndex(index).getVal(), mHighB);
        int c3 = Double.compare(aDouble, mLowB);
        int c4 = Double.compare(aDouble, mHighB);*/

        if(aDouble < 18 ){
            int c1 = Double.compare(aDouble, mLowA);
            int c2 = Double.compare(aDouble, mHighA);
            if(c1 > 0 && c2 < 0){
                //cCode = "N";
                return mColors.get(0);
            }else if(c1 < 0){
                //cCode = "L";
                return mColors.get(1);
            }else if(c2 > 0){
                //cCode = "H";
                return mColors.get(2);
            }else {
                return mColors.get(3);
            }
        }
        else if(aDouble > 18 ){
            int c3 = Double.compare(aDouble, mLowB);
            int c4 = Double.compare(aDouble, mHighB);
            if(c3 > 0 && c4 < 0){
                //cCode = "N";
                return mColors.get(0);
            }else if(c3 < 0){
                //cCode = "L";
                return mColors.get(1);
            }else if(c4 > 0){
                //cCode = "H";
                return mColors.get(2);
            }else {
                return mColors.get(3);
            }
        }else{
            return mColors.get(3);
        }

        /*if((c3 > 0 && c4 < 0)||(c1 > 0 && c2 < 0)){
            //cCode = "N";
            return mColors.get(0);
        }
        /*if(c1 > 0 && c2 < 0){
            //cCode = "N";
            Log.w("Normal","");
            return mColors.get(0);
        }/
        if(c1 < 0 || c3 < 0){
            //cCode = "L";
            return mColors.get(1);
        }
        if(c3 > 0 || c4 > 0 || c1 > 0 || c2 > 0){
            //cCode = "H";
            return mColors.get(2);
        }else {
            return mColors.get(3);
        }*/
    }
}
