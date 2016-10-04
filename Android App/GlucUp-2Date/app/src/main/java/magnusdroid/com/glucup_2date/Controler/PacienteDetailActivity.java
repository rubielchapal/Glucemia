package magnusdroid.com.glucup_2date.Controler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.R;

/**
 * An activity representing a single Paciente detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PacienteListActivity}.
 */
public class PacienteDetailActivity extends AppCompatActivity implements View.OnClickListener {


    // UI references.
    private FloatingActionButton fab_pacient_detail, fab_pacient_detail1, fab_pacient_detail2;
    PrefManager prefManager;
    // Utilities
    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    String mPersonalID, mPatientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        // Set up shared preferences.
         prefManager = new PrefManager(this);

        mPatientID = getIntent().getStringExtra(PacienteDetailFragment.PACIENT_ID);
        mPersonalID = prefManager.getDoc();

        fab_pacient_detail = (FloatingActionButton) findViewById(R.id.fab_pacient_detail);
        fab_pacient_detail1 = (FloatingActionButton) findViewById(R.id.fab_pacient_detail1);
        fab_pacient_detail2 = (FloatingActionButton) findViewById(R.id.fab_pacient_detail2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab_pacient_detail.setOnClickListener(this);
        fab_pacient_detail1.setOnClickListener(this);
        fab_pacient_detail2.setOnClickListener(this);
        /*fab_pacient_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add
                Intent i = new Intent(PacienteDetailActivity.this, AddGlucActivity.class);
                i.putExtra("flag", 1);
                i.putExtra("pacient", mPatientID);
                i.putExtra("performer", mPersonalID);
                Log.w("Datos: Paciente ",""+mPatientID+" Performer: "+mPersonalID);
                startActivity(i);
                /*Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*
            }
        });*/

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
            arguments.putString(PacienteDetailFragment.PACIENT_ID,mPatientID);
            /*arguments.putString(PacienteDetailFragment.PACIENT_NAME,
                    getIntent().getStringExtra(PacienteDetailFragment.PACIENT_NAME));*/
            arguments.putString(PacienteDetailFragment.PERSONAL_ID, mPersonalID);
            PacienteDetailFragment fragment = new PacienteDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.paciente_detail_container, fragment)
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
            NavUtils.navigateUpTo(this, new Intent(this, PacienteListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent i;
        switch (id){
            case R.id.fab_pacient_detail:
                animateFAB();
                break;
            case R.id.fab_pacient_detail1:
                animateFAB();
                //Add
                i = new Intent(PacienteDetailActivity.this, AddGlucActivity.class);
                i.putExtra("flag", 1);
                i.putExtra("pacient", mPatientID);
                i.putExtra("performer", mPersonalID);
                Log.w("Datos: Paciente ",""+mPatientID+" Performer: "+mPersonalID);
                startActivity(i);
                break;
            case R.id.fab_pacient_detail2:
                i = new Intent(PacienteDetailActivity.this, FilterActivity.class);
                i.putExtra("pacient", mPatientID);
                i.putExtra("flag", 2);
                startActivity(i);
                isFabOpen = true;
                animateFAB();
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){
            fab_pacient_detail.startAnimation(rotate_backward);
            fab_pacient_detail1.startAnimation(fab_close);
            fab_pacient_detail2.startAnimation(fab_close);
            fab_pacient_detail1.setClickable(false);
            fab_pacient_detail2.setClickable(false);
            isFabOpen = false;
        } else {
            fab_pacient_detail.startAnimation(rotate_forward);
            fab_pacient_detail1.startAnimation(fab_open);
            fab_pacient_detail2.startAnimation(fab_open);
            fab_pacient_detail1.setClickable(true);
            fab_pacient_detail2.setClickable(true);
            isFabOpen = true;
        }
    }

    /**
     * Start {@link AddGlucActivity}
     */
    public void newGluc(){
    }
}
