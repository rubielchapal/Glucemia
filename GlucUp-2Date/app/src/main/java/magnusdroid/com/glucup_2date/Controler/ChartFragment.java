package magnusdroid.com.glucup_2date.Controler;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MDateGlucose;
import magnusdroid.com.glucup_2date.Model.MyBarDataSet;
import magnusdroid.com.glucup_2date.Model.MyMarkerView;
import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.R;


/**
 * Fragment to show the Graphs for the records. Called in {@link PacientActivity} and {@link PacientDetailFragment}
 * Use open library <a href="https://github.com/PhilJay/MPAndroidChart">MPAndroidChart</a> to build the charts
 */
public class ChartFragment extends Fragment
        implements OnChartGestureListener, OnChartValueSelectedListener {

    //UI references
    private LineChart mLineChart;
    protected BarChart mBarChart;
    private CombinedChart mCombiChart;
    private DownloadDate downloadDate;
    private RelativeLayout nonvaluechart;
    private TextView chart_text;
    private ScrollView scroll_chart;
    private MenuItem change_chart;
    // Utilities
    private List<ListGluc> glucList = new ArrayList<>();
    private ArrayList<Entry> yLineValues;
    private ArrayList<BarEntry> yBarValues;
    private ArrayList<String> xAxes;
    private String lDate;
    String[] xaxes;
    XAxis xLineAxis, xBarAxis, xCombiAxis;
    YAxis leftBarAxis, leftLineAxis, leftCombiAxis;
    private PrefManager prefManager;
    private int med;
    private Boolean mBoolean;
    private String unit;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate own menu item
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Allow change of orientation to see the chart in landscape or portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        //Set up shared preferences and utilities
        prefManager = new PrefManager(getContext());
        xAxes = new ArrayList<>();
        yLineValues = new ArrayList<>();
        yBarValues = new ArrayList<>();
        //Set up widget references
        nonvaluechart = (RelativeLayout) view.findViewById(R.id.nonvaluechart);
        scroll_chart = (ScrollView) view.findViewById(R.id.scroll_chart);
        chart_text = (TextView) view.findViewById(R.id.chart_text);
        Button mShow = (Button) view.findViewById(R.id.show_linebar);
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
        // disable scaling and dragging
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getLegend().setEnabled(false);
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
        leftBarAxis.setYOffset(-9f);
        leftBarAxis.setTextColor(R.color.primary_text);
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);
        leftLineAxis = mLineChart.getAxisLeft();
        leftLineAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftLineAxis.setDrawAxisLine(true);
        leftLineAxis.setDrawGridLines(false);
        leftLineAxis.setYOffset(-9f);
        leftLineAxis.setTextColor(R.color.primary_text);
        YAxis rightLineAxis = mLineChart.getAxisRight();
        rightLineAxis.setDrawGridLines(false);
        rightLineAxis.setEnabled(false);
        leftCombiAxis = mCombiChart.getAxisLeft();
        leftCombiAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftCombiAxis.setDrawAxisLine(true);
        leftCombiAxis.setDrawGridLines(false);
        leftCombiAxis.setYOffset(-9f);
        leftCombiAxis.setTextColor(R.color.primary_text);
        YAxis rightCombiAxis = mCombiChart.getAxisRight();
        rightCombiAxis.setDrawGridLines(false);
        rightCombiAxis.setEnabled(false);

        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBarChart.setVisibility(View.VISIBLE);
                mLineChart.setVisibility(View.VISIBLE);
            }
        });

        med = 0;
        Task(med);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chart_menu, menu);
        change_chart = menu.getItem(1);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_chart:
                Intent i = new Intent(getContext(), FilterActivity.class);
                i.putExtra("flag", 1);
                startActivity(i);
                break;
            case R.id.change_chart:
                change_chart.setEnabled(false);
                if (med == 0) {
                    Task(med);
                    med = 1;
                } else {
                    Task(med);
                    med = 0;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *
     * @param Mmed int Unit to star the AsyncTask thread and change the unite of the records
     */
    private void Task(int Mmed) {
        String document = prefManager.getDoc();
        downloadDate = new DownloadDate(document, Mmed);
        downloadDate.execute();
        med = 0;
    }

    /**
     * AsyncTask to handle the connection with the server. Recieve the response and send the data
     * using {@link MDateGlucose} class.
     * Data send: <i>Pacient DNI, Datetime and Context to use {@link PrefManager}</i>
     * Response: JSON with data and parse to build the chart using {@link ListGluc} class
     */
    private class DownloadDate extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;
        private final int mMed;

        DownloadDate(String doc, int med) {
            mDoc = doc;
            mMed = med;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            MDateGlucose mDateGlucose = new MDateGlucose();
            DecimalFormat df = new DecimalFormat("#.##");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String mDate = sdf.format(c.getTime());
            glucList.clear();
            yLineValues.clear();
            yBarValues.clear();
            xAxes.clear();
            try {
                //Handle response
                JSONObject jObject = mDateGlucose.getDay(mDoc, mDate, "0");
                int mInt = jObject.getInt("status");
                if (mInt == 1) {
                    mBoolean = true;
                    JSONArray jArray = jObject.getJSONArray("obs_glucose");
                    lDate = jObject.getString("date");
                    for (int i = 0; i < jArray.length(); i++) {
                        String value;
                        jObject = jArray.getJSONObject(i);
                        //Parse JSON
                        ListGluc listGluc = new ListGluc();
                        listGluc.setIssued(jObject.getString("issued"));
                        listGluc.setCode(jObject.getString("code"));
                        listGluc.setUnit("mmol/l");
                        listGluc.setValue(jObject.getString("value"));
                        glucList.add(listGluc);
                        if (mMed == 1) { //mmol/l -> mg/dl
                            Double aDouble = Double.parseDouble(jObject.getString("value"));
                            value = String.valueOf(df.format(aDouble * 18));
                            unit = "mg/dl";
                        } else {
                            value = jObject.getString("value");
                            unit = "mmol/l";
                        }
                        //Set up data for the chart
                        yLineValues.add(new Entry(Float.parseFloat(value), i));
                        yBarValues.add(new BarEntry(Float.parseFloat(value), i));
                        xAxes.add(i, jObject.getString("issued"));
                    }
                    xaxes = new String[xAxes.size()];
                    for (int i = 0; i < xAxes.size(); i++) {
                        xaxes[i] = jObject.getString("issued");
                    }
                } else {mBoolean = false;}

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mBoolean;
        }

        @Override
        protected void onPostExecute(Boolean bb) {
            if (bb) {
                nonvaluechart.setVisibility(View.GONE);
                scroll_chart.setVisibility(View.VISIBLE);
                chart_text.setText(getString(R.string.last_issued, lDate));
                downloadDate = null;
                change_chart.setEnabled(true);
                mLineChart.animateXY(3000, 3000);
                mBarChart.animateY(2500);
                mCombiChart.animateXY(3000, 2500);
                if (unit.equalsIgnoreCase("mmol/l")) {
                    leftBarAxis.setAxisMinValue(0f);
                    leftBarAxis.setAxisMaxValue(10f);
                    leftLineAxis.setAxisMinValue(0f);
                    leftLineAxis.setAxisMaxValue(10f);
                    leftCombiAxis.setAxisMinValue(0f);
                    leftCombiAxis.setAxisMaxValue(10f);
                } else if (unit.equalsIgnoreCase("mg/dl")) {
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
                set1.setValueTextSize(15f);
                set1.setCircleColor(Color.WHITE);
                set1.setLineWidth(2f);
                //Fetch the data and populate barchart using color to show the range
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
                // create a custom MarkerView (extend MarkerView) and specify the layout
                // to use for it
                MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
                // set the marker to the chart
                mLineChart.setMarkerView(mv);
                // create a data object with the datasets
                mLineChart.setData(lineData);
                mLineChart.invalidate();
                mBarChart.setData(barData);
                mBarChart.invalidate();
                mCombiChart.setData(combinedData);
                mCombiChart.invalidate();

            } else {
                // no description text
                nonvaluechart.setVisibility(View.VISIBLE);
                scroll_chart.setVisibility(View.GONE);
                /*mLineChart.setDescription("");
                mLineChart.setNoDataTextDescription("You need to provide data for the chart.");*/
            }
        }
    }

    //Method using by MPAndroidChart
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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    /**
     *
     * @param date See: {@link AddGlucActivity#converted(int)}
     * @return
     */
    private String converted(int date) {
        String s = "" + date;
        if (date < 10) {
            s = "0" + s;
        }
        return s;
    }
}


