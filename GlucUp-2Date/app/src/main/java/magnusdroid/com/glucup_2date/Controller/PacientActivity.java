package magnusdroid.com.glucup_2date.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import magnusdroid.com.glucup_2date.R;

public class PacientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // UI references.
    private TextView mNameView;
    // Get shared preferences
    private PrefManager prefManager;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient);
        // Set up shared preferences.
        prefManager = new PrefManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(checkNetwork()) {
            Log.w("con red",":)");
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            Log.w("sin red",":(");
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        mNameView = (TextView) header.findViewById(R.id.NameHeader);
        mNameView.setText(prefManager.getUser());

        //Mostrar Fragment Bienvenida
        /*if (savedInstanceState == null) {
            Fragment fragment = null;
            Class clase;
            if(prefManager.getFragment() == 1){
                clase = AlarmFragment.class;
            }else if(prefManager.getFragment() == 2){
                clase = PacientListFragment.class;
            }else{
                clase = PacientListFragment.class;
            }
            try {
                fragment = (Fragment) clase.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Insert the fragment by replacing any existing fragment
            Bundle arguments = new Bundle();
            arguments.putString("Alarm", prefManager.getAlarmName());
            arguments.putString("Date", prefManager.getAlarmDate());
            fragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }*/
        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class clase = PacientListFragment.class;
            try {
                fragment = (Fragment) clase.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pacient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        /*if(checkNetwork()){
            *item.setEnabled(false);
            item.setCheckable(false);
        }else {*/
            if (id == R.id.nav_list) {
                fragmentClass = PacientListFragment.class;
            } else if (id == R.id.nav_graphic) {
                fragmentClass = ChartFragment.class;
            } else if (id == R.id.nav_reminder) {
                fragmentClass = AlarmFragment.class;
            } else if (id == R.id.nav_help) {

            } else if (id == R.id.nav_info) {

            } else if (id == R.id.nav_off) {
                fragmentClass = LogOutFragment.class;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }/*Bundle arguments = new Bundle();
            arguments.putString("Alarm", "null");
            fragment.setArguments(arguments);*/
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
            // Highlight the selected item, update the title, and close the drawer
            item.setChecked(true);
            setTitle(item.getTitle());
        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Check network connection
    private boolean checkNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
        /*ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // HTTP Login
            attemptLogin();
        } else {
            // Show toast
            dialog();
        }*/
    }
}
