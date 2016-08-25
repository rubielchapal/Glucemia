package magnusdroid.com.glucup_2date.Controller;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MAllGlucose;
import magnusdroid.com.glucup_2date.Model.MDateGlucose;
import magnusdroid.com.glucup_2date.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PacientListFragment extends Fragment implements View.OnClickListener {

    // Keep track of the login task to ensure we can cancel it if requested.
    private DownloadList mAuthTask = null;
    // UI references.
    private FloatingActionButton mFab, mFab1, mFab2;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private RelativeLayout rLayout, lLayout;
    private TextView txtlayout, ldate_text;
    // Get shared preferences
    private PrefManager prefManager;
    // Model
    private MDateGlucose mList;
    private MAllGlucose mAll;
    // JsonObject response from server
    private JSONObject jObject;
    private JSONArray jArray;
    // Utilities
    private ProgressDialog progress;
    private String lDate;
    private ArrayList<HashMap<String, String>> lArray;
    private List<ListGluc> glucList = new ArrayList<>();
    private Boolean isFabOpen = false;
    private int MInteger = 0;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private int med;


    public PacientListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pacient_list, container, false);

        prefManager = new PrefManager(getContext());

        mFab = (FloatingActionButton) view.findViewById(R.id.fab_pacient);
        mFab1 = (FloatingActionButton) view.findViewById(R.id.fab_pacient1);
        mFab2 = (FloatingActionButton) view.findViewById(R.id.fab_pacient2);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        rLayout = (RelativeLayout) view.findViewById(R.id.nonvalue);
        lLayout = (RelativeLayout) view.findViewById(R.id.relvalue);
        txtlayout = (TextView) view.findViewById(R.id.nonvalue_text);
        ldate_text = (TextView) view.findViewById(R.id.ldate_text);
        mFab.setOnClickListener(this);
        mFab1.setOnClickListener(this);
        mFab2.setOnClickListener(this);

        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        progress = new ProgressDialog(getContext());
        progress.setMessage("Conectando ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(true);

        med = 0;
        Log.i("Med",""+med);
        Task(med);

        return view;
    }

    private void Task(int Mmed){
        Log.w("Task",""+Mmed);
        String document = prefManager.getDoc();
        progress.show();
        mAuthTask = new DownloadList(document, Mmed, getContext());
        mAuthTask.execute((Void) null);
        med = 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_pacient:
                animateFAB();
                break;
            case R.id.fab_pacient1:
                Log.d("FAB", "New");
                animateFAB();
                newGluc();
                break;
            case R.id.fab_pacient2:
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
        Intent i = new Intent(getContext(), AddGlucActivity.class);
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pacient_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.parse_value:
                //ViewDialog alert = new ViewDialog();
                //alert.setFilter(getActivity());
                Intent i = new Intent(getContext(), FilterActivity.class);
                i.putExtra("flag", 0);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadList extends AsyncTask<Void, Void, Boolean>{

        private final String mDoc;
        private final Context mContext;
        private final int mMed;

        DownloadList(String doc, int med, Context context) {
            mDoc = doc;
            mMed = med;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean aBoolean = true;
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            Calendar c = Calendar.getInstance();
            Log.w("Current time => ",""+c.getTime());
            glucList.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(c.getTime());
            mList = new MDateGlucose();
            mAll = new MAllGlucose();
            int mInteger;
            try {
                jObject = mList.getDay(mDoc, date, mContext);
                mInteger = jObject.getInt("status");
                Log.w("Integer"," "+mInteger);
                if(mInteger == 0){
                    aBoolean = true;
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
                            value = String.valueOf(df.format(aDouble*18));
                            unit = "mg/dl";
                        }else{
                            value = jObject.getString("value");
                            unit  = "mmol/l";
                        }
                        listGluc.setUnit(unit);
                        listGluc.setValue(value);
                        glucList.add(listGluc);
                    }
                }else if (mInteger == 1){
                    /*date = c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)-1)+"-"+(c.get(Calendar.DAY_OF_MONTH)+2);
                    Log.w("Fecha"," "+date);*/
                    //jObject = mAll.getAll(mDoc, mContext);
                    //jObject = mList.getDay(mDoc, date, mContext);
                        aBoolean = true;
                        jArray = jObject.getJSONArray("obs_glucose");
                    lDate = jObject.getString("date");
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
                                value = String.valueOf(df.format(aDouble*18));
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
                    aBoolean = false;
                }
            } catch (JSONException e) {e.printStackTrace();}
            return aBoolean;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            Log.w("Estado"," "+aVoid);
            progress.dismiss();
            //adapter = new RecyclerAdapter(getContext(), lArray);
            if(aVoid) {
                mAuthTask = null;
                adapter = new RecyclerViewAdapter(glucList);
                recycler.setAdapter(adapter);
                recycler.setVisibility(View.VISIBLE);
                lLayout.setVisibility(View.VISIBLE);
                ldate_text.setText(getString(R.string.last_issued, lDate));
                rLayout.setVisibility(View.GONE);
            }else{
                recycler.setVisibility(View.GONE);
                rLayout.setVisibility(View.VISIBLE);
                txtlayout.setText("No hay conexión con el servidor :(");
                mFab.setClickable(false);
            }
        }
    }

}
