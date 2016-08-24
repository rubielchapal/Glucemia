package magnusdroid.com.glucup_2date.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MAllGlucose;
import magnusdroid.com.glucup_2date.Model.MDateGlucose;
import magnusdroid.com.glucup_2date.Model.MValueGlucose;
import magnusdroid.com.glucup_2date.R;

public class ListGlucoseActivity extends AppCompatActivity implements View.OnClickListener {


    // Keep track of the filter task to ensure we can cancel it if requested.
    private DownloadDate downloadDate = null;
    private DownloadAll downloadAll = null;
    private DownloadValue downloadValue = null;
    // UI references.
    private FloatingActionButton mFab, mFab1, mFab2;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private RelativeLayout rlayout;
    private ScrollView scrollView;
    private TextView txtv;
    private LineChart mLineChart;
    protected BarChart mBarChart;
    private CombinedChart mCombiChart;
    private Button mShow;
    // Get shared preferences
    private PrefManager prefManager;
    // Utilities
    private Boolean isFabOpen = false;
    private Boolean mBoolean;
    private int mInt, med, mFlag;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private String date, minvalue, maxvalue, fixvalue, unit, value;
    private XAxis xLineAxis, xBarAxis, xCombiAxis;
    private YAxis leftBarAxis, leftLineAxis, leftCombiAxis;
    private ArrayList<Entry> yLineValues;
    private ArrayList<BarEntry> yBarValues;
    private ArrayList<String> xAxes;
    private MyMarkerView mv;
    // Model
    private MDateGlucose mDateGlucose;
    private MAllGlucose mAllGlucose;
    private MValueGlucose mValueGlucose;
    // JsonObject response from server
    private JSONObject jObject;
    private JSONArray jArray;
    private List<ListGluc> glucList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_filter_glucose);
        // Set up shared preferences.
        prefManager = new PrefManager(this);
        // Set up bundle data.
        Bundle extras = getIntent().getExtras();
        date = extras.getString("date");
        minvalue = extras.getString("Vmin");
        maxvalue = extras.getString("Vmax");
        fixvalue = extras.getString("Vfix");
        mFlag = extras.getInt("flag");
        unit = extras.getString("Unit");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(date.equalsIgnoreCase("todos")){
            getSupportActionBar().setTitle("Todas las muestras");
        } else if(date.equalsIgnoreCase("valor")){
            getSupportActionBar().setTitle("Resultado");
        } else{
            getSupportActionBar().setTitle("Muestras del día: "+date);
        }


        xAxes = new ArrayList<>();
        yLineValues = new ArrayList<>();
        yBarValues = new ArrayList<>();


        txtv = (TextView) findViewById(R.id.section_label);
        rlayout = (RelativeLayout) findViewById(R.id.novlauefilter);
        mFab = (FloatingActionButton) findViewById(R.id.fab_date_gluc);
        mFab1 = (FloatingActionButton) findViewById(R.id.fab_date_gluc1);
        mFab2 = (FloatingActionButton) findViewById(R.id.fab_date_gluc2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        scrollView = (ScrollView) findViewById(R.id.scroll_filter);
        mShow = (Button) findViewById(R.id.show_linebar_filter);
        mLineChart = (LineChart) findViewById(R.id.line_chart_filter);
        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mLineChart.setTouchEnabled(true);
        mLineChart.setDrawGridBackground(false);
        // enable scaling and dragging
        mLineChart.setDragEnabled(false);
        mLineChart.setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.getLegend().setEnabled(false);
        mBarChart = (BarChart) findViewById(R.id.bar_chart_filter);
        // no description text
        mBarChart.setDescription("");
        mBarChart.setNoDataTextDescription("You need to provide data for the chart.");
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setScaleEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDragEnabled(false);
        mBarChart.getLegend().setEnabled(false);
        mCombiChart = (CombinedChart) findViewById(R.id.combi_chart_filter);
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
        mFab.setOnClickListener(this);
        mFab1.setOnClickListener(this);
        mFab2.setOnClickListener(this);

        recycler = (RecyclerView) findViewById(R.id.date_recycler);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(lManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        med = 0;
        Task(med);
    }

    private void Task(int Mmed){
        Log.w("Task",""+Mmed);
        String document = prefManager.getDoc();
        if(mFlag == 0){
            recycler.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else if(mFlag == 1){
            recycler.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
        if(date.equalsIgnoreCase("todos")){
            downloadAll = new DownloadAll(document, Mmed, getApplicationContext());
            downloadAll.execute();
        } else if(date.equalsIgnoreCase("valor")){
            downloadValue = new DownloadValue(document, fixvalue, minvalue, maxvalue, unit, Mmed, getApplicationContext());
            downloadValue.execute();
        } else{
            downloadDate = new DownloadDate(document, date, Mmed, getApplicationContext());
            downloadDate.execute();
        }
        /*downloadDate = new DownloadDate(document, date, Mmed, getApplicationContext());
        downloadDate.execute((Void) null);*/
        med = 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_date_gluc:
                animateFAB();
                break;
            case R.id.fab_date_gluc1:
                Log.d("FAB", "New");
                animateFAB();
                newGluc();
                break;
            case R.id.fab_date_gluc2:
                if(med ==0){
                    Task(med);
                    med = 1;
                }else{
                    Task(med);
                    med = 0;
                }
                isFabOpen = true;
                Log.d("FAB", "Change"+med);
                animateFAB();
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){
            mFab.startAnimation(rotate_backward);
            mFab1.startAnimation(fab_close);
            mFab2.startAnimation(fab_close);
            mFab1.setClickable(false);
            mFab2.setClickable(false);
            isFabOpen = false;
            Log.d("FAB", "close");
        } else {
            mFab.startAnimation(rotate_forward);
            mFab1.startAnimation(fab_open);
            mFab2.startAnimation(fab_open);
            mFab1.setClickable(true);
            mFab2.setClickable(true);
            isFabOpen = true;
            Log.d("FAB","open");
        }
    }

    public void newGluc(){
        //ViewDialog alert = new ViewDialog();
        //alert.setGluc(getActivity(), "Error de conexión al servidor");
        Intent i = new Intent(getApplicationContext(), AddGlucActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //AsyncTask for All values
    private class DownloadAll extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;
        private final int mMed;
        private final Context mContext;

        DownloadAll(String doc, int med, Context context) {
            mDoc = doc;
            mMed = med;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean mBoolean = null;
            mAllGlucose = new MAllGlucose();
            try {
                jObject = mAllGlucose.getAll(mDoc, mContext);
                mInt = jObject.getInt("status");
                glucList.clear();
                if(mInt == 0){
                    mBoolean = true;
                    jArray = jObject.getJSONArray("obs_glucose");
                    for (int i = 0; i < jArray.length(); i++) {
                        String value;
                        String unit;
                        jObject = jArray.getJSONObject(i);
                        ListGluc listGluc = new ListGluc();
                        listGluc.setIssued(jObject.getString("issued"));
                        listGluc.setCode(jObject.getString("code"));
                        listGluc.setState(jObject.getString("state"));
                        if(jObject.has("performer")){
                            //performer = jObject.getString("performer");
                            listGluc.setPerformer(jObject.getString("performer"));
                        }else {
//                            performer = prefManager.getUser();
                            listGluc.setPerformer(prefManager.getUser());
                        }
                        if(mMed == 1){ //mmol/l -> mg/dl
                            Double aDouble = Double.parseDouble(jObject.getString("value"));
                            value = String.valueOf((aDouble*18));
                            unit = "mg/dl";
                        }else{
                            value = jObject.getString("value");
                            unit  = "mmol/l";
                        }
                        listGluc.setUnit(unit);
                        listGluc.setValue(value);
                        glucList.add(listGluc);
                    }
                }
                else if (mInt == 1){
                    mBoolean = false;
                    //dialog();
                    /*Intent i = new Intent(ListGlucoseActivity.this, PacientActivity.class);
                    startActivity(i);
                    finish();*/
                }

            } catch (JSONException e) {e.printStackTrace();}
            return mBoolean;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            if(aVoid){
                downloadAll = null;
                recycler.setVisibility(View.VISIBLE);
                rlayout.setVisibility(View.GONE);
                //adapter = new RecyclerAdapter(getApplicationContext(), glucList);
                adapter = new RecyclerViewAdapter(glucList);
                recycler.setAdapter(adapter);
            }else{
                //dialog();
                recycler.setVisibility(View.GONE);
                rlayout.setVisibility(View.VISIBLE);
                txtv.setText("Sin registros");

            }
            /*downloadAll = null;
            //adapter = new RecyclerAdapter(getApplicationContext(), glucList);
            adapter = new RecyclerViewAdapter(glucList);
            recycler.setAdapter(adapter);*/
        }
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
                if(mInt == 0){
                    mBoolean = true;
                    jArray = jObject.getJSONArray("obs_glucose");
                    for (int i = 0; i < jArray.length(); i++) {
                        jObject = jArray.getJSONObject(i);
                        ListGluc listGluc = new ListGluc();
                        listGluc.setIssued(jObject.getString("issued"));
                        listGluc.setCode(jObject.getString("code"));
                        listGluc.setState(jObject.getString("state"));
                        if(jObject.has("performer")){
                            //performer = jObject.getString("performer");
                            listGluc.setPerformer(jObject.getString("performer"));
                        }else {
//                            performer = prefManager.getUser();
                            listGluc.setPerformer(prefManager.getUser());
                        }
                        if(mMed == 1){ //mmol/l -> mg/dl
                            //DecimalFormat df = new DecimalFormat("#.##");
                            df.setRoundingMode(RoundingMode.CEILING);
                            Double aDouble = Double.parseDouble(jObject.getString("value"));
                            value = String.valueOf(df.format(aDouble*18));
                            unit = "mg/dl";
                        }else{
                            value = jObject.getString("value");
                            unit  = "mmol/l";
                        }
                        listGluc.setUnit(unit);
                        listGluc.setValue(value);
                        glucList.add(listGluc);
                        yLineValues.add(new Entry(Float.parseFloat(value), i));
                        yBarValues.add(new BarEntry(Float.parseFloat(value), i));
                        xAxes.add(i, jObject.getString("issued"));
                    }
                }else if(mInt == 1){
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
            if(bb){
                downloadDate = null;
                //recycler.setVisibility(View.VISIBLE);
                rlayout.setVisibility(View.GONE);
                //adapter = new RecyclerAdapter(getApplicationContext(), glucList);
                adapter = new RecyclerViewAdapter(glucList);
                recycler.setAdapter(adapter);
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
                set1.setValueTextSize(15f);
                set1.setCircleColor(Color.WHITE);
                set1.setLineWidth(2f);
                MyBarDataSet set2 = new MyBarDataSet(yBarValues, "");
                set2.setValueTextSize(15f);
                set2.setColors(new int[]{ContextCompat.getColor(getApplicationContext(), R.color.normal_value),
                        ContextCompat.getColor(getApplicationContext(), R.color.low_value),
                        ContextCompat.getColor(getApplicationContext(), R.color.high_value),
                        ContextCompat.getColor(getApplicationContext(), R.color.primary_text)});
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
                mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view, glucList);
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
                recycler.setVisibility(View.GONE);
                rlayout.setVisibility(View.VISIBLE);
                txtv.setText("No hay registro para este día");
                dialog();

            }
        }
    }

    //AsynTask values filter by value
    private class DownloadValue extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc, mFix, mMin, mMax, mUnit;
        private final int mMed;
        private final Context mContext;


        DownloadValue(String doc, String fix, String min, String max, String unit, int med, Context context) {
            mDoc = doc;
            mFix = fix;
            mMin = min;
            mMax = max;
            mUnit = unit;
            mMed = med;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            mValueGlucose = new MValueGlucose();
            glucList.clear();
            try {
                jObject = mValueGlucose.getValue(mDoc, mFix, mMin, mMax, mUnit, mContext);
                mBoolean = jObject.getBoolean("status");
                if(mBoolean){
                    jArray = jObject.getJSONArray("obs_glucose");
                    for (int i = 0; i < jArray.length(); i++) {
                        String value;
                        String unit;
                        jObject = jArray.getJSONObject(i);
                        ListGluc listGluc = new ListGluc();
                        listGluc.setIssued(jObject.getString("issued"));
                        listGluc.setCode(jObject.getString("code"));
                        listGluc.setState(jObject.getString("state"));
                        if(jObject.has("performer")){
                            //performer = jObject.getString("performer");
                            listGluc.setPerformer(jObject.getString("performer"));
                        }else {
//                            performer = prefManager.getUser();
                            listGluc.setPerformer(prefManager.getUser());
                        }
                        if(mMed == 1){ //mmol/l -> mg/dl
                            Double aDouble = Double.parseDouble(jObject.getString("value"));
                            value = String.valueOf((aDouble*18));
                            unit = "mg/dl";
                        }else{
                            value = jObject.getString("value");
                            unit  = "mmol/l";
                        }
                        listGluc.setUnit(unit);
                        listGluc.setValue(value);
                        glucList.add(listGluc);
                    }
                }else{
                    mBoolean = false;
                    /*//Toast.makeText(ListGlucoseActivity.this, "No found :(", Toast.LENGTH_SHORT).show();
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
            if(bb){
                downloadValue = null;
                recycler.setVisibility(View.VISIBLE);
                rlayout.setVisibility(View.GONE);
                //adapter = new RecyclerAdapter(getApplicationContext(), glucList);
                adapter = new RecyclerViewAdapter(glucList);
                recycler.setAdapter(adapter);
            }else{
                recycler.setVisibility(View.GONE);
                rlayout.setVisibility(View.VISIBLE);
                txtv.setText("No hay registro con el valor ingresado");
                dialog();
                /*Intent i = new Intent(ListGlucoseActivity.this, PacientActivity.class);
                startActivity(i);
                finish();*/
            }
        }
    }




    private void dialog() {
        String alert= "No hay registros para el filtro ingresado";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                .setTitle("Sin registro....")
                .setIcon(R.mipmap.alert)
                .setMessage(alert)
                .setNeutralButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(ListGlucoseActivity.this, PacientActivity.class);
                                startActivity(i);
                                finish();
                                dialog.cancel();
                            }
                        });
        builder1.show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
    }

}
