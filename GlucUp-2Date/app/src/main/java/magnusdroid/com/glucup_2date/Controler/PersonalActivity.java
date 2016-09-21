package magnusdroid.com.glucup_2date.Controler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import magnusdroid.com.glucup_2date.Model.MListPacient;
import magnusdroid.com.glucup_2date.Model.Pacient;
import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.Model.ViewDialog;
import magnusdroid.com.glucup_2date.R;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Pacients. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PacientDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PersonalActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    // Keep track of the task to ensure we can cancel it if requested.
    private DownloadList mAuthTask = null;
    // Utilities
    private ProgressDialog progress;
    private List<Pacient> glucList = new ArrayList<>();
    private View recyclerView;
    private RelativeLayout rLayout;
    private TextView txtlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_list);
        // Set up shared preferences.
        PrefManager prefManager = new PrefManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(prefManager.getUser());

        rLayout = (RelativeLayout) findViewById(R.id.nonpacient);
        txtlayout = (TextView) findViewById(R.id.nonpacient_txt);
        recyclerView = findViewById(R.id.pacient_list);
        assert recyclerView != null;

        if (findViewById(R.id.pacient_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        progress = new ProgressDialog(PersonalActivity.this);
        progress.setMessage(getString(R.string.fetching_data));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        mAuthTask = new DownloadList(prefManager.getDoc());
        mAuthTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_personal_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_personal_help){

        }else if(id == R.id.action_personal_logout){
            ViewDialog alert = new ViewDialog();
            alert.LogOutDialog(PersonalActivity.this, getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param recyclerView RecyclyView item to fetching data
     * @param pacientList List of the all relationed  pacient with the personal logged
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Pacient> pacientList) {
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(pacientList));
    }

    /**
     * Adapter to setup the Recycler and show the List. See {@link RecyclerViewAdapter}
     */
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Pacient> mValues;

        public SimpleItemRecyclerViewAdapter(List<Pacient> pacientList) {
            mValues = pacientList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pacient_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).subject);
            holder.mName = mValues.get(position).subject;

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(PacientDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        arguments.putString(PacientDetailFragment.ARG_ITEM_NAME, holder.mName);
                        PacientDetailFragment fragment = new PacientDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.pacient_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PacientDetailActivity.class);
                        intent.putExtra(PacientDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        intent.putExtra(PacientDetailFragment.ARG_ITEM_NAME, holder.mName);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public final ImageView mImageView;
            public Pacient mItem;
            public String mName;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.txt_name_pacient);
                mImageView = (ImageView) view.findViewById(R.id.icon_pacient);
            }
        }
    }

    /**
     * AsyncTask to handle the connection with the server. Recieve the response and send the data
     * using {@link MListPacient} class.
     * Data send: <i>Stafff DNI</i>
     * Response: JSON with list of pacients
     */
    private class DownloadList extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;

        DownloadList(String doc) {
            mDoc = doc;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            glucList.clear();
            MListPacient mList = new MListPacient();
            int mInteger;
            Boolean mBoolean = true;
            try {
                JSONObject jObject = mList.getList(mDoc);
                mInteger = jObject.getInt("status");
                if(mInteger == 0){
                    mBoolean = true;
                    JSONArray jArray = jObject.getJSONArray("pacient");
                    for (int i = 0; i < jArray.length(); i++) {
                        jObject = jArray.getJSONObject(i);
                        Pacient pacient = new Pacient(jObject.getString("id"), jObject.getString("subject"));
                        glucList.add(pacient);
                    }

                }else if(mInteger == 1){
                    mBoolean = false;
                }else{
                    mBoolean = false;
                }
            } catch (JSONException e) {e.printStackTrace();}
            return mBoolean;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            progress.dismiss();
            if(aVoid) {
                mAuthTask = null;
                setupRecyclerView((RecyclerView) recyclerView, glucList);
                recyclerView.setVisibility(View.VISIBLE);
                rLayout.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.GONE);
                rLayout.setVisibility(View.VISIBLE);
                txtlayout.setText(getString(R.string.login_issued_server));
            }
        }
    }


    /**
     * Class to draw a divider item to the list of the Pacient. Inflate the layout and add params
     * using canvas class
     */
    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context,R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }


}
