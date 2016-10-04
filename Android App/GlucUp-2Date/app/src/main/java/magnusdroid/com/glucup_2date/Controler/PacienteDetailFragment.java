package magnusdroid.com.glucup_2date.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MDetailPacient;
import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.R;

/**
 * A fragment representing a single Paciente detail screen.
 * This fragment is either contained in a {@link PacienteListActivity}
 * in two-pane mode (on tablets) or a {@link PacienteDetailActivity}
 * on handsets.
 */
public class PacienteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String PACIENT_ID = "pacient_id";
    public static final String PACIENT_NAME = "pacient_name";
    public static final String PERSONAL_ID = "personal_id";
    // Keep track of the detail task to ensure we can cancel it if requested.
    private DownloadDetail mAuthTask = null;
    // Get shared preferences
    private PrefManager prefManager;
    // UI references.
    private TextView txtProfile, txtEmail, txtAge, txtBirth, txtBlood, txtDate;
    // Model
    private MDetailPacient mDetail;
    // JsonObject response from server
    private JSONObject jObject;
    private JSONArray jDetail;
    // Utilities
    private ProgressDialog progress;
    private List<ListGluc> glucList = new ArrayList<>();
    private String mPatientID, mPatientName, mPersonalID, mEmail, mAge, mBirth, mBlood, mDate;

    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PacienteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(PACIENT_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mPatientID = getArguments().getString(PACIENT_ID);
            //mPatientName = getArguments().getString(PACIENT_NAME);
            mPersonalID = getArguments().getString(PERSONAL_ID);

            Activity activity = this.getActivity();
            Toolbar appBarLayout = (Toolbar) activity.findViewById(R.id.detail_toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Perfil paciente");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.paciente_detail, container, false);

        prefManager = new PrefManager(getContext());

        txtProfile = (TextView) rootView.findViewById(R.id.txtProfile);
        txtEmail =  (TextView) rootView.findViewById(R.id.txtEmail);
        txtAge = (TextView) rootView.findViewById(R.id.txtAge);
        txtBirth = (TextView) rootView.findViewById(R.id.txtBirth);
        txtBlood = (TextView) rootView.findViewById(R.id.txtBlood);
        txtDate = (TextView) rootView.findViewById(R.id.txtDate);

        /*mFab = (FloatingActionButton) rootView.findViewById(R.id.fab_fragdetail);
        mFab1 = (FloatingActionButton) rootView.findViewById(R.id.fab_fragdetail1);
        mFab2 = (FloatingActionButton) rootView.findViewById(R.id.fab_fragdetail2);
        mFab3 = (FloatingActionButton) rootView.findViewById(R.id.fab_fragdetail3);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        mFab.setOnClickListener(this);
        mFab1.setOnClickListener(this);
        mFab2.setOnClickListener(this);
        mFab3.setOnClickListener(this);

        recycler = (RecyclerView) rootView.findViewById(R.id.detail_recycler);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        recycler.setItemAnimator(new DefaultItemAnimator());*/

        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.fetching_data));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(true);

        /*/ Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.paciente_detail)).setText(mItem.details);
        }*/

        Task();

        return rootView;
    }

    /**
     * See: {@link ChartFragment#Task(int)}
     */
    private void Task(){
        progress.show();
        mAuthTask = new DownloadDetail(mPatientID);
        mAuthTask.execute();
    }

    /*@Override
    public void onClick(View view) {
        int id = view.getId();
        Intent i;
        switch (id){
            case R.id.fab_fragdetail: //Open FABs
                animateFAB();
                break;
            case R.id.fab_fragdetail1: //Change med
                if(med ==0){
                    Task(med);
                    med = 1;
                }else{
                    Task(med);
                    med = 0;
                }
                isFabOpen = true;
                animateFAB();
                break;
            case R.id.fab_fragdetail2: //Add
                i = new Intent(getActivity(), AddGlucActivity.class);
                i.putExtra("flag", 1);
                i.putExtra("pacient", mPatientID);
                i.putExtra("performer", mPersonalID);
                Log.w("Datos: Paciente ",""+mPatientID+" Performer: "+mPersonalID);
                startActivity(i);
                animateFAB();
                break;
            case R.id.fab_fragdetail3: //Filter
                i = new Intent(getActivity(), FilterActivity.class);
                i.putExtra("flag", 2);
                i.putExtra("pacient", mPatientID);
                startActivity(i);
                animateFAB();
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){
            mFab.startAnimation(rotate_backward);
            mFab1.startAnimation(fab_close);
            mFab2.startAnimation(fab_close);
            mFab3.startAnimation(fab_close);
            mFab1.setClickable(false);
            mFab2.setClickable(false);
            mFab3.setClickable(false);
            isFabOpen = false;
            Log.d("FAB", "close");
        } else {
            mFab.startAnimation(rotate_forward);
            mFab1.startAnimation(fab_open);
            mFab2.startAnimation(fab_open);
            mFab3.startAnimation(fab_open);
            mFab1.setClickable(true);
            mFab2.setClickable(true);
            mFab3.setClickable(true);
            isFabOpen = true;
            Log.d("FAB","open");
        }
    }*/

    private class DownloadDetail extends AsyncTask<Void, Void, Integer> {

        private final String mDoc;

        DownloadDetail(String doc) {
            mDoc = doc;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Calendar c = Calendar.getInstance();
            glucList.clear();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(c.getTime());
            mDetail = new MDetailPacient();
            int mInt;
            try {
                jObject = mDetail.getDetail(mDoc, date);
                mInt = jObject.getInt("status");
                if(mInt == 1 || mInt == 0){
                    //jArray = jObject.getJSONArray("obs_glucose");
                    jDetail = jObject.getJSONArray("detail");
                    mPatientName = jDetail.getJSONObject(0).getString("nombre");
                    mEmail = jDetail.getJSONObject(0).getString("email");
                    mAge = jDetail.getJSONObject(0).getString("age");
                    mBirth = jDetail.getJSONObject(0).getString("birthdate");
                    mBlood = jDetail.getJSONObject(0).getString("blood");
                    mDate = jDetail.getJSONObject(0).getString("last_record");
                    /*for (int i = 0; i < jArray.length(); i++) {
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
                            listGluc.setPerformer(mPatientName);
                        }
                        value = jObject.getString("value");
                        unit  = "mmol/l";
                        listGluc.setUnit(unit);
                        listGluc.setValue(value);
                        glucList.add(listGluc);
                    }*/

                }else{
                    mInt = 2;
                }
            } catch (JSONException e) {e.printStackTrace();mInt = 3;}
            return mInt;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            progress.dismiss();
            mAuthTask = null;
            prefManager.setPatientName(mPatientName);
            txtProfile.setText(mPatientName);
            txtEmail.setText(mEmail);
            txtAge.setText(mAge);
            txtBirth.setText(mBirth);
            txtBlood.setText(mBlood);
            if (aVoid == 0) {
                txtDate.setText(getString(R.string.last_issued, mDate));
            }else if (aVoid == 1){
                //txtProfile.s
                txtDate.setText("Sin historial de registros");
            }
            /*RecyclerView.Adapter adapter = new RecyclerViewAdapter(glucList);
            recycler.setAdapter(adapter);*/
        }
    }

}
