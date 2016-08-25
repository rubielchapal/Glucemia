package magnusdroid.com.glucup_2date.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MDateGlucose;
import magnusdroid.com.glucup_2date.Model.MDetailPacient;
import magnusdroid.com.glucup_2date.R;

/**
 * A fragment representing a single Pacient detail screen.
 * This fragment is either contained in a {@link PersonalActivity}
 * in two-pane mode (on tablets) or a {@link PacientDetailActivity}
 * on handsets.
 */
public class PacientDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "pacient_id";
    public static final String ARG_ITEM_NAME = "pacient_name";
    // Keep track of the login task to ensure we can cancel it if requested.
    private DownloadDetail mAuthTask = null;
    // UI references.
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView txtv;
    // Model
    private MDetailPacient mDetail;
    // JsonObject response from server
    private JSONObject jObject;
    private JSONArray jArray, jDetail;
    // Utilities
    private List<ListGluc> glucList = new ArrayList<>();
    private int med;

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;
    private String mItem, mName, mSujeto, mAge, mBirth;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PacientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = getArguments().getString(ARG_ITEM_ID);
            mName = getArguments().getString(ARG_ITEM_NAME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle("Paciente: "+mName);
                appBarLayout.setTitle(getString(R.string.title_pacient_detail));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pacient_detail, container, false);

        txtv = (TextView) rootView.findViewById(R.id.section_detailed);

        recycler = (RecyclerView) rootView.findViewById(R.id.detail_recycler);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);
        recycler.setItemAnimator(new DefaultItemAnimator());



        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.pacient_detail)).setText(mItem);
            Task(med);
        }

        return rootView;
    }

    private void Task(int Mmed){
        Log.w("Task",""+Mmed);
        //String document = prefManager.getDoc();
        mAuthTask = new DownloadDetail(mItem, getContext());
        mAuthTask.execute((Void) null);
        med = 0;
    }

    private class DownloadDetail extends AsyncTask<Void, Void, Void> {

        private final String mDoc;
        private final Context mContext;

        DownloadDetail(String doc, Context context) {
            mDoc = doc;
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            glucList.clear();
            mDetail = new MDetailPacient();
            Boolean mBoolean;
            try {
                jObject = mDetail.getDetail(mDoc, mContext);
                mBoolean = jObject.getBoolean("status");
                if(mBoolean){
                    jArray = jObject.getJSONArray("obs_glucose");
                    jDetail = jObject.getJSONArray("detail");
                    mSujeto = jDetail.getJSONObject(0).getString("nombre");
                    mAge = jDetail.getJSONObject(0).getString("age");
                    mBirth = jDetail.getJSONObject(0).getString("birthdate");
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
                            listGluc.setPerformer(mName);
                        }
                        value = jObject.getString("value");
                        unit  = "mmol/l";
                        listGluc.setUnit(unit);
                        listGluc.setValue(value);
                        glucList.add(listGluc);
                        Log.i("Map",""+glucList);
                    }

                }else{
                    Intent i = new Intent(getContext(), FilterActivity.class);
                    startActivity(i);
                }
            } catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAuthTask = null;
            txtv.setText(getString(R.string.detailed, mSujeto, mBirth, mAge));
            //adapter = new RecyclerAdapter(getContext(), lArray);
            adapter = new RecyclerViewAdapter(glucList);
            recycler.setAdapter(adapter);
        }
    }
}
