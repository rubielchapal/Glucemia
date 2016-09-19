package magnusdroid.com.glucup_2date.Controler;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

import magnusdroid.com.glucup_2date.Model.MRegisterGlucose;
import magnusdroid.com.glucup_2date.Model.PrefManager;
import magnusdroid.com.glucup_2date.R;

import static android.support.design.widget.Snackbar.*;

/**
 * Activity to add new record of bloodglucose. Use ThemDialog style for show as a Dialog and appear
 * on top from the UI. Connect with Servlet <i>RegisterGlucose</i> through {@link MRegisterGlucose} class
 */
public class AddGlucActivity extends AppCompatActivity implements View.OnClickListener,
        RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener{

    //UI references
    Button btn_date, btn_time;
    TextView txt_date, txt_time;
    EditText edit_value;
    Spinner spin_unid, spin_state;
    private SendGlucTask mAuthTask = null;
    // JsonObject response from server
    private JSONObject jObject;
    //Utilities
    public static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    public static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private String mUser, mPerformer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gluc);
        // Set up shared preferences.
        PrefManager prefManager = new PrefManager(this);
        // Set up toolbar and action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Get data from the previous activity to handle layout
        Bundle extras = getIntent().getExtras();
        int mFlag = extras.getInt("flag");
        //0  = Pacients, 1 = Staff
        if(mFlag == 0){
            mUser = prefManager.getDoc();
            mPerformer = "";
        }else if(mFlag == 1){
            mUser = extras.getString("pacient");
            mPerformer = extras.getString("performer");
        }
        //Set up widget UI
        edit_value = (EditText) findViewById(R.id.value_gluc);
        spin_unid = (Spinner) findViewById(R.id.spinner_unid);
        spin_state = (Spinner) findViewById(R.id.spinner_state);
        txt_date = (TextView) findViewById(R.id.in_date);
        txt_time = (TextView) findViewById(R.id.in_time);
        btn_date = (Button) findViewById(R.id.btn_date);
        assert btn_date != null;
        btn_date.setOnClickListener(this);
        btn_time = (Button) findViewById(R.id.btn_time);
        assert btn_time != null;
        btn_time.setOnClickListener(this);
        // Get Current Datetime
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        String monthConverted = converted(mMonth+1);
        String dayConverted = converted(mDay);
        String hourConverted = converted(mHour);
        String minutConverted = converted(mMinute);
        //Show current datetime in textview
        txt_time.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));
        txt_date.setText(getString(R.string.calendar_date_picker_value, mYear, monthConverted, dayConverted));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send_gluc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.send_value:
                Enviar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param v Handle the action button. Show a dialog to select time and select date
     *          Using open library @see <a href="https://github.com/code-troopers/android-betterpickers">android-betterpickers</a>
     *
     */
    @Override
    public void onClick(View v) {
        if(v == btn_time){
            RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                    .setOnTimeSetListener(AddGlucActivity.this)
                    .setForced12hFormat();

            rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
        }
        if(v == btn_date) {
            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                    .setOnDateSetListener(AddGlucActivity.this);
            cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
        }
    }

    /**
     * Class to start the AsynTask Thread and handle the input values. Show alerts/error if found
     * empties values or wrong values
     */
    private void Enviar(){
        if (mAuthTask != null) {
            return;
        }

        edit_value.setError(null);

        boolean cancel;
        View focusView = null;

        String value = edit_value.getText().toString();
        String unit = spin_unid.getSelectedItem().toString();
        String state = spin_state.getSelectedItem().toString();
        String fecha = txt_date.getText().toString()+"T"+txt_time.getText().toString();
        double data;
        // Check for empty field
        if(TextUtils.isEmpty(value)){
            error(getString(R.string.field_empties));
            focusView = edit_value;
            cancel = true;
        }else {
            data = Double.parseDouble(value);
            //Check for wrong value
            if(unit.equalsIgnoreCase("mg/dl")){
                if(data>=21 && data<=190){
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.CEILING);
                    value = String.valueOf(df.format(data/18));
                    cancel = false;
                }else{
                    dialog();
                    focusView = edit_value;
                    cancel = true;
                }
            }else{
                if(data>=10){
                    cancel = true;
                    focusView = edit_value;
                    dialog();
                }else{ cancel = false;}
            }
        }


        if (cancel) {
            // There was an error; don't attempt send and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Kick off a background task to perform the send attempt.
            mAuthTask = new SendGlucTask(mUser, value, state, fecha, mPerformer);
            mAuthTask.execute();
        }

    }

    /**
     *
     * @param msj String msj to show in the error dialog caused by an issued in the entries of
     *            data
     */
    private  void error(String msj){
        AlertDialog alertDialog = new AlertDialog.Builder(AddGlucActivity.this).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage(msj);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * Class to show dialog when a wrong value is set in texview. The dialog has sound and vibration
     * to perfom the notification
     */
    private void dialog() {
        String alert= getString(R.string.check_value);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.wrong_value))
                    .setIcon(R.mipmap.alert)
                    .setMessage(alert)
                    .setNeutralButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder1.show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
        MediaPlayer player = MediaPlayer.create(this, R.raw.alert_value);
        player.start();
    }

    /**
     *
     * @param date Int date to rounded with 0 when is 1digit
     * @return ei 01, 02,.. 09
     */
    private String converted(int date){
        String s = ""+date;
        if(date < 10){
               s = "0"+s;
        }
        return s;
    }

    /**
     *
     * @param dialog Fragment dialog to show the UI to select Time for the new record
     * @param hourOfDay int Hour to show the current time
     * @param minute int Minute to show the current time
     */
    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String hourConverted = converted(hourOfDay);
        String minutConverted = converted(minute);
        txt_time.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));

    }

    /**
     *
     * @param dialog Fragment dialog to show the UI to select Date for the new record
     * @param year int Year to show the current year
     * @param monthOfYear int Month to show the current month
     * @param dayOfMonth int Day to show the current day
     */
    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String monthConverted = converted(monthOfYear+1);
        String dayConverted = converted(dayOfMonth);
        txt_date.setText(getString(R.string.calendar_date_picker_value, year, monthConverted, dayConverted));
    }

    /**
     * AsyncTask to handle the connection with the server. Recieve the response and send the data
     * using {@link MRegisterGlucose} class.
     * Data send: <i>Pacient DNI, Value of bloodglucose, State at the moment of the record, Datetime,
     * Performe if exist and Context to use {@link PrefManager}</i>
     * Response: JSON with success or failure show it in a SnackBar Widget
     */
    public class SendGlucTask extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;
        private final String mValue;
        private final String mState;
        private final String mFecha;
        private final String mStaff;

        SendGlucTask(String doc, String value, String state, String fecha, String staff) {
            mDoc = doc;
            mValue = value;
            mState = state;
            mFecha = fecha;
            mStaff = staff;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            MRegisterGlucose mGluc = new MRegisterGlucose();
            try {
                jObject = mGluc.sendGluc(mDoc, mValue, mState, mFecha, mStaff);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            View view = findViewById(android.R.id.content);
            mAuthTask = null;
            try {
                boolean status = jObject.getBoolean("glucose");
                if (status) {
                    make(view, R.string.send_success, LENGTH_LONG)
                            .show();
                }else {
                    make(view, R.string.send_fail, LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}



