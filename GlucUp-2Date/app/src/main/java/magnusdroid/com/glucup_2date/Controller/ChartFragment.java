package magnusdroid.com.glucup_2date.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
/*import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;*/
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MDateGlucose;
import magnusdroid.com.glucup_2date.R;


public class ChartFragment extends Fragment
        implements OnChartGestureListener, OnChartValueSelectedListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    private LineChart mLineChart;
    protected BarChart mBarChart;
    private CombinedChart mCombiChart;
    private DownloadDate  downloadDate;
    private Button btn_chartday, btn_chart, mShow;
    private PrefManager prefManager;
    private int med;
    private Boolean mBoolean;
    private String unit;
    private int mYear, mMonth, mDay, mHour, mMinute, mDate;
    private int mInt;
    String[] xaxes;
    XAxis xLineAxis, xBarAxis, xCombiAxis;
    YAxis leftBarAxis, leftLineAxis, leftCombiAxis;
    // Model
    private MDateGlucose mDateGlucose;
    // JsonObject response from server
    private JSONObject jObject;
    private JSONArray jArray;
    private List<ListGluc> glucList = new ArrayList<>();
    private ArrayList<Entry> yLineValues;
    private ArrayList<BarEntry> yBarValues;
    private ArrayList<String> xAxes;
    private MyMarkerView mv;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        prefManager = new PrefManager(getContext());
        xAxes = new ArrayList<>();
        yLineValues = new ArrayList<>();
        yBarValues = new ArrayList<>();

        // Get Current Datetime
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        String monthConverted = converted(mMonth+1);
        String dayConverted = converted(mDay);

        //txt_title = (TextView) view.findViewById(R.id.txt_titlechart);
        btn_chartday = (Button) view.findViewById(R.id.btn_chartday);
        btn_chart = (Button) view.findViewById(R.id.btn_chart);
        mShow = (Button) view.findViewById(R.id.show_linebar);
        mLineChart = (LineChart) view.findViewById(R.id.line_chart);
        mLineChart.setOnChartGestureListener(this);
        mLineChart.setOnChartValueSelectedListener(this);
        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mLineChart.setTouchEnabled(true);
        //mLineChart.setDragDecelerationFrictionCoef(0.9f);
        mLineChart.setDrawGridBackground(false);
        // enable scaling and dragging
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(false);
        //mLineChart.animateX(2500);
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getLegend().setEnabled(false);
        //mLineChart.setHighlightPerDragEnabled(true);
        //AxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mLineChart);

        mBarChart = (BarChart) view.findViewById(R.id.bar_chart);
        // no description text
        mBarChart.setDescription("");
        mBarChart.setNoDataTextDescription("You need to provide data for the chart.");
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setScaleEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDragEnabled(false);
        mBarChart.getLegend().setEnabled(false);

        mCombiChart = (CombinedChart) view.findViewById(R.id.combi_chart);
        mCombiChart.setDescription("");
        mCombiChart.setDrawGridBackground(false);
        mCombiChart.setDrawBarShadow(false);
        mCombiChart.setPinchZoom(false);
        mCombiChart.setDragEnabled(false);
        mCombiChart.getLegend().setEnabled(false);
        //mCombiChart.setHighlightFullBarEnabled(false);
        // draw bars behind lines
        mCombiChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        xLineAxis = mLineChart.getXAxis();
        xLineAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLineAxis.setTextSize(10f);
        xLineAxis.setDrawGridLines(false);
        xLineAxis.setDrawAxisLine(true);
        xLineAxis.setTextColor(R.color.primary_text);
        //xLineAxis.setCenterAxisLabels(true);
        //xLineAxis.setValueFormatter(xAxisFormatter);

        xBarAxis = mBarChart.getXAxis();
        xBarAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xBarAxis.setTextSize(10f);
        xBarAxis.setDrawGridLines(false);
        xBarAxis.setDrawAxisLine(true);
        xBarAxis.setTextColor(R.color.primary_text);

        xCombiAxis = mCombiChart.getXAxis();
        xCombiAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xCombiAxis.setTextSize(10f);
        xCombiAxis.setDrawGridLines(false);
        xCombiAxis.setDrawAxisLine(true);
        xCombiAxis.setTextColor(R.color.primary_text);

        leftBarAxis = mBarChart.getAxisLeft();
        leftBarAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftBarAxis.setDrawAxisLine(true);
        leftBarAxis.setDrawGridLines(false);
        /*/leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(10f);*/
        leftBarAxis.setYOffset(-9f);
        leftBarAxis.setTextColor(R.color.primary_text);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        leftLineAxis = mLineChart.getAxisLeft();
        leftLineAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftLineAxis.setDrawAxisLine(true);
        leftLineAxis.setDrawGridLines(false);
        /*/leftAxis.setGranularityEnabled(true);
        leftLineAxis.setAxisMinValue(0f);
        leftLineAxis.setAxisMaxValue(10f);*/
        leftLineAxis.setYOffset(-9f);
        leftLineAxis.setTextColor(R.color.primary_text);

        YAxis rightLineAxis = mLineChart.getAxisRight();
        rightLineAxis.setDrawGridLines(false);
        rightLineAxis.setEnabled(false);

        leftCombiAxis = mCombiChart.getAxisLeft();
        leftCombiAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftCombiAxis.setDrawAxisLine(true);
        leftCombiAxis.setDrawGridLines(false);
        /*/leftAxis.setGranularityEnabled(true);
        leftCombiAxis.setAxisMinValue(0f);
        leftCombiAxis.setAxisMaxValue(10f);*/
        leftCombiAxis.setYOffset(-9f);
        leftCombiAxis.setTextColor(R.color.primary_text);

        YAxis rightCombiAxis = mCombiChart.getAxisRight();
        rightCombiAxis.setDrawGridLines(false);
        rightCombiAxis.setEnabled(false);

        btn_chartday.setText(getString(R.string.calendar_date_picker_value, mYear, monthConverted, dayConverted));
        btn_chartday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(ChartFragment.this);
                cdp.show(getFragmentManager(), AddGlucActivity.FRAG_TAG_DATE_PICKER);
            }
        });

        med = 0;

        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task(med);
            }
        });

        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBarChart.setVisibility(View.VISIBLE);
                mLineChart.setVisibility(View.VISIBLE);

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_chart:
                //ViewDialog alert = new ViewDialog();
                //alert.setFilter(getActivity());
                Intent i = new Intent(getContext(), FilterActivity.class);
                i.putExtra("flag", 1);
                startActivity(i);
                break;
            case R.id.change_chart:
                if(med ==0){
                    Task(med);
                    med = 1;
                }else{
                    Task(med);
                    med = 0;
                }
                Log.d("FAB", "Change"+med);
                //Task(med);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void Task(int Mmed){
        Log.w("Task",""+Mmed);
        String document = prefManager.getDoc();
        String date = btn_chartday.getText().toString();
        downloadDate = new DownloadDate(document, date, Mmed, getContext());
        downloadDate.execute();
        med = 0;
    }

    //AsynTask values filter by date
    private class DownloadDate extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;
        private final String mDate;
        private final int mMed;
        private final Context mContext;

        DownloadDate(String doc, String date, int med, Context context) {
            mDoc = doc;
            mDate = date;
            mMed = med;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mDateGlucose = new MDateGlucose();
            DecimalFormat df = new DecimalFormat("#.##");
            glucList.clear();
            yLineValues.clear();
            yBarValues.clear();
            xAxes.clear();
            try {
                jObject = mDateGlucose.getDay(mDoc, mDate, mContext);
                mInt = jObject.getInt("status");
                if(mInt == 1){
                    mBoolean = true;
                    jArray = jObject.getJSONArray("obs_glucose");
                    for (int i = 0; i < jArray.length(); i++) {
                        String value;
                        jObject = jArray.getJSONObject(i);
                        ListGluc listGluc = new ListGluc();
                        listGluc.setIssued(jObject.getString("issued"));
                        listGluc.setCode(jObject.getString("code"));
                        listGluc.setUnit("mmol/l");
                        listGluc.setValue(jObject.getString("value"));
                        glucList.add(listGluc);
                        if(mMed == 1){ //mmol/l -> mg/dl
                            Double aDouble = Double.parseDouble(jObject.getString("value"));
                            value = String.valueOf(df.format(aDouble*18));
                            unit = "mg/dl";
                        }else{
                            value = jObject.getString("value");
                            unit  = "mmol/l";
                        }
                        //Entry entry = new Entry()
                        yLineValues.add(new Entry(Float.parseFloat(value), i));
                        yBarValues.add(new BarEntry(Float.parseFloat(value), i));
                        //yLineValues.add(new Entry(Float.parseFloat(jObject.getString("value")), Float.parseFloat(jObject.getString("issued"))));
                        xAxes.add(i, jObject.getString("issued"));
                        //xAxes[i] = jObject.getString("issued");
                    }
                    xaxes = new String[xAxes.size()];
                    for(int i=0; i<xAxes.size(); i++) {
                        xaxes[i] = jObject.getString("issued");
                        //xaxes[i] = xAxes.get(i).toString();
                    }
                }else if(mInt == 0){
                    mBoolean = false;
                    /*Toast.makeText(ListGlucoseActivity.this, "No hay registros para esa fecha", Toast.LENGTH_SHORT).show();
                    dialog();
                    Intent i = new Intent(ListGlucoseActivity.this, PacientActivity.class);
                    startActivity(i);
                    finish();*/
                }

            } catch (JSONException e) {e.printStackTrace();}
            return mBoolean;
        }

        @Override
        protected void onPostExecute(Boolean bb) {
            Log.d("Boolean"," "+bb);
            Log.e("yLineValues"," "+ yLineValues);
            Log.e("yBarValues"," "+ yBarValues);
            Log.i("xValues"," "+xAxes);
            //Log.i("xLineAxis"," "+xaxes);
            if(bb){
                downloadDate = null;
                mLineChart.animateXY(3000, 3000);
                mBarChart.animateY(2500);
                mCombiChart.animateXY(3000, 2500);
                if(unit.equalsIgnoreCase("mmol/l")){
                    leftBarAxis.setAxisMinValue(0f);
                    leftBarAxis.setAxisMaxValue(10f);
                    leftLineAxis.setAxisMinValue(0f);
                    leftLineAxis.setAxisMaxValue(10f);
                    leftCombiAxis.setAxisMinValue(0f);
                    leftCombiAxis.setAxisMaxValue(10f);
                }else if(unit.equalsIgnoreCase("mg/dl")){
                    leftBarAxis.setAxisMinValue(60f);
                    leftBarAxis.setAxisMaxValue(190f);
                    leftLineAxis.setAxisMinValue(60f);
                    leftLineAxis.setAxisMaxValue(190f);
                    leftCombiAxis.setAxisMinValue(60f);
                    leftCombiAxis.setAxisMaxValue(190f);
                }
                //Fetch the data and populate linechart
                LineDataSet set1 = new LineDataSet(yLineValues, "Muestras");
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                //set1.setColor(ColorTemplate.getHoloBlue());
                set1.setValueTextSize(15f);
                set1.setCircleColor(Color.WHITE);
                set1.setLineWidth(2f);
                //xLineAxis.setValueFormatter(new MyValueFormatter());
                //xLineAxis.setValueFormatter(new CustomXAxis(xAxes, xAxes.size()));
                //xLineAxis.setValueFormatter(new DayAxisValueFormatter(mLineChart));
                //Fetch the data and populate barchart
                /*BarDataSet set2 = new BarDataSet(yBarValues, "Muestras");
                set2.setColors(ColorTemplate.VORDIPLOM_COLORS);*/
                MyBarDataSet set2 = new MyBarDataSet(yBarValues, "");
                set2.setValueTextSize(15f);
                set2.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.normal_value),
                        ContextCompat.getColor(getContext(), R.color.low_value),
                        ContextCompat.getColor(getContext(), R.color.high_value),
                        ContextCompat.getColor(getContext(), R.color.primary_text)});

                // use the interface LineDataSet - BarDataSet
                ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
                lineDataSets.add(set1);
                ArrayList<BarDataSet> barDataSets = new ArrayList<>();
                barDataSets.add(set2);

                LineData lineData = new LineData(xAxes, lineDataSets);
                BarData barData = new BarData(xAxes, barDataSets);


                //Fetch the data and populate combinedchart
                LineData d = new LineData(xAxes, set1);
                d.addDataSet(set1);
                BarData db = new BarData(xAxes, set2);
                CombinedData combinedData = new CombinedData(xAxes);
                combinedData.setData(d);
                combinedData.setData(db);
                /*CombinedData data = new CombinedData(xAxes);
                data.setData(generateLineData(yLineValues));
                data.setData(generateBarData(yBarValues));*/
                //combinedData.setValueTypeface();


                // create a custom MarkerView (extend MarkerView) and specify the layout
                // to use for it
                mv = new MyMarkerView(getContext(), R.layout.custom_marker_view, glucList);
                // set the marker to the chart
                mLineChart.setMarkerView(mv);

                // create a data object with the datasets
                mLineChart.setData(lineData);
                mLineChart.invalidate();
                mBarChart.setData(barData);
                mBarChart.invalidate();
                mCombiChart.setData(combinedData);
                mCombiChart.invalidate();

            }else{
                // no description text
                mLineChart.setDescription("");
                mLineChart.setNoDataTextDescription("You need to provide data for the chart.");
            }
        }
    }



    /*@Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mLineChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }*/

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    /*@Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOWHIGH", "mLowA: " + mLineChart.getLowestVisibleX() + ", mHighA: " + mLineChart.getHighestVisibleX());
        Log.i("MIN MAX", "xmin: " + mLineChart.getXChartMin() + ", xmax: " + mLineChart.getXChartMax() + ", ymin: " + mLineChart.getYChartMin() + ", ymax: " + mLineChart.getYChartMax());
    }*/

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String monthConverted = converted(monthOfYear+1) ;//"" + (monthOfYear + 1);
        String dayConverted = converted(dayOfMonth) ;//"" + (dayOfMonth);
        /*if (dayOfMonth < 10) {
            dayConverted = "0" + dayConverted;
        }
        if (monthOfYear < 10) {
            monthConverted = "0" + monthConverted;
        }*/
        btn_chartday.setText(getString(R.string.calendar_date_picker_value, year, monthConverted, dayConverted));
    }

    private String converted(int date){
        String s = ""+date;
        if(date < 10){
            s = "0"+s;
        }
        return s;
    }

    /*private LineData generateLineData(ArrayList<Entry> yLineValues) {

         //ArrayList<Entry> entries = new ArrayList<Entry>();

        /for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));*/

        /*LineDataSet set = new LineDataSet(yLineValues, "Line DataSet");
        set.setLineWidth(2.5f);
        set.setDrawValues(true);
        set.setValueTextSize(15f);
        set.setValueTextColor(Color.rgb(240, 238, 70));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);*

        LineDataSet set1 = new LineDataSet(yLineValues, "Muestras");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ContextCompat.getColor(getContext(), R.color.primary_text));
        set1.setValueTextSize(15f);
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);

        LineData d = new LineData(xAxes, set1);
        //d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(ArrayList<BarEntry> yBarValues) {

        *BarDataSet set1 = new BarDataSet(yBarValues, "Bar 1");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(15f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        MyBarDataSet set2 = new MyBarDataSet(yBarValues, "");
        set2.setValueTextSize(15f);
        set2.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.normal_value),
                ContextCompat.getColor(getContext(), R.color.low_value),
                ContextCompat.getColor(getContext(), R.color.high_value),
                ContextCompat.getColor(getContext(), R.color.primary_text)});

        BarData d = new BarData(xAxes, set2);

        return d;
    }*/

}
