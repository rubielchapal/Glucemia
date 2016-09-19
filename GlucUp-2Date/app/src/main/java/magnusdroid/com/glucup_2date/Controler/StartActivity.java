package magnusdroid.com.glucup_2date.Controler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.R;

/**
 * An full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction. Show the {@link LoginActivity} if
 * not exist session opened, if exist show {@link PersonalActivity} or {@link PacientActivity} according
 * to the shared preferences
 */
public class StartActivity extends AppCompatActivity implements Animation.AnimationListener {

    // Get shared preferences
    private PrefManager prefManager;
    private static final int SPLASH_TIME_OUT = 4500;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    TextView titulo;
    Animation anim1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        // Set up shared preferences.
        prefManager = new PrefManager(this);
        titulo = (TextView) findViewById(R.id.titulo);
        anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        anim1.setAnimationListener(this);
        titulo.setVisibility(View.VISIBLE);
        titulo.setAnimation(anim1);

        mVisible = true;

        toggle();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
        new Handler().postDelayed(new Runnable() {
            //Showing splash screen with a timer
            @Override
            public void run() {
                Intent i = null;
                // This method will be executed once the timer is over
                if (prefManager.getUser().isEmpty() && prefManager.getPassword().isEmpty() && prefManager.getRol().isEmpty()) {
                    // Start your app main activity
                    i = new Intent(StartActivity.this, LoginActivity.class);
                } else if (prefManager.getRol().equalsIgnoreCase("pacient")) {
                    i = new Intent(StartActivity.this, PacientActivity.class);
                    Toast.makeText(StartActivity.this, getString(R.string.welcome_login), Toast.LENGTH_SHORT).show();
                } else if (prefManager.getRol().equalsIgnoreCase("personal")) {
                    i = new Intent(StartActivity.this, PersonalActivity.class);
                    Toast.makeText(StartActivity.this, getString(R.string.welcome_login), Toast.LENGTH_SHORT).show();
                }
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        mVisible = true;
        // Schedule a runnable to display UI elements after a delay
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {}

    @Override
    public void onAnimationRepeat(Animation animation) {}

}
