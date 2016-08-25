package magnusdroid.com.glucup_2date.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import magnusdroid.com.glucup_2date.R;

/**
 * An activity representing a single Pacient detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PersonalActivity}.
 */
public class PacientDetailActivity extends AppCompatActivity implements View.OnClickListener {


    // UI references.
    private FloatingActionButton mFab, mFab1, mFab2, mFab3;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private RelativeLayout rlayout;
    private TextView txtv;
    // Get shared preferences
    private PrefManager prefManager;
    // Utilities
    private Boolean isFabOpen = false;
    private Boolean mBoolean;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private int med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        mFab = (FloatingActionButton) findViewById(R.id.fab_detail);
        mFab1 = (FloatingActionButton) findViewById(R.id.fab1_detail);
        mFab2 = (FloatingActionButton) findViewById(R.id.fab2_detail);
        mFab3 = (FloatingActionButton) findViewById(R.id.fab3_detail);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        mFab.setOnClickListener(this);
        mFab1.setOnClickListener(this);
        mFab2.setOnClickListener(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PacientDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PacientDetailFragment.ARG_ITEM_ID));
            arguments.putString(PacientDetailFragment.ARG_ITEM_NAME,
                    getIntent().getStringExtra(PacientDetailFragment.ARG_ITEM_NAME));
            PacientDetailFragment fragment = new PacientDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pacient_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, PersonalActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_detail: //Open FABs
                animateFAB();
                break;
            case R.id.fab1_detail: //Change med
                Log.d("FAB", "New");
                if(med ==0){
                    //Task(med);
                    med = 1;
                }else{
                    //Task(med);
                    med = 0;
                }
                isFabOpen = true;
                Log.d("FAB", "Change"+med);
                animateFAB();
                break;
            case R.id.fab2_detail: //Filter
                Intent i = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(i);
                animateFAB();
                break;
            case R.id.fab3_detail:
                Log.d("FAB", "New");
                animateFAB();
                newGluc();
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
    }

    public void newGluc(){
        //ViewDialog alert = new ViewDialog();
        //alert.setGluc(getActivity(), "Error de conexión al servidor");
        Intent i = new Intent(getApplicationContext(), AddGlucActivity.class);
        startActivity(i);
    }

}
