package magnusdroid.com.glucup_2date.Controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;

import magnusdroid.com.glucup_2date.Model.MRegisterGlucose;
import magnusdroid.com.glucup_2date.R;

public class AddGlucActivity extends AppCompatActivity implements View.OnClickListener,
        RadialTimePickerDialogFragment.OnTimeSetListener,
        CalendarDatePickerDialogFragment.OnDateSetListener{

    //UI references
    Button btn_date, btn_time;
    TextView txt_date, txt_time;
    EditText edit_value;
    Spinner spin_unid, spin_state;
    CheckBox chk_date;
    private int mYear, mMonth, mDay, mHour, mMinute, mDate;
    // Get shared preferences
    private PrefManager prefManager;
    private SendGlucTask mAuthTask = null;
    private MRegisterGlucose mGluc;
    // JsonObject response from server
    private JSONObject jObject;
    public static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    public static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gluc);
        // Set up shared preferences.
        prefManager = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_value = (EditText) findViewById(R.id.value_gluc);
        spin_unid = (Spinner) findViewById(R.id.spinner_unid);
        spin_state = (Spinner) findViewById(R.id.spinner_state);
        txt_date = (TextView) findViewById(R.id.in_date);
        txt_time = (TextView) findViewById(R.id.in_time);

        btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(this);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_time.setOnClickListener(this);
        /*chk_date = (CheckBox) findViewById(R.id.checkBox);
        chk_date.setOnClickListener(this);*/
        // Get Current Datetime
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        String monthConverted = converted(mMonth+1);
        String dayConverted = converted(mDay);
        String hourConverted = converted(mHour);
        String minutConverted = converted(mMinute);
        /*if(mMonth < 10){
            monthConverted = "0"+monthConverted;
        }*/
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

    @Override
    public void onClick(View v) {
        if(v == btn_time){
            RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                    .setOnTimeSetListener(AddGlucActivity.this)
                    .setForced12hFormat();

            rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);

            /*DatePickerDialog datePickerDialog = new DatePickerDialog(AddGlucActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String monthConverted = ""+(monthOfYear+1);
                            String dayConverted = converted(dayOfMonth);
                            if(monthOfYear < 10){
                                monthConverted = "0"+monthConverted;
                            }
                            txt_date.setText(year + "-" + monthConverted + "-" + dayConverted);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();*/
            /*DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txt_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();*/
        }
        if(v == btn_date){
            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                    .setOnDateSetListener(AddGlucActivity.this);
            cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);

            /*// Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txt_time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();*/
        }
        /*if(v == chk_date){
            if(chk_date.isChecked()){ //Fecha actual
                btn_date.setVisibility(View.GONE);
                txt_date.setVisibility(View.GONE);
                btn_time.setVisibility(View.GONE);
                txt_time.setVisibility(View.GONE);
                mDate = 0;
            }else{ //Otra fecha
                btn_date.setVisibility(View.VISIBLE);
                txt_date.setVisibility(View.VISIBLE);
                btn_time.setVisibility(View.VISIBLE);
                txt_time.setVisibility(View.VISIBLE);
                mDate = 1;
            }
        }*/

    }

    private void Enviar(){
        if (mAuthTask != null) {
            return;
        }

        edit_value.setError(null);

        boolean cancel;
        View focusView = null;

        String doc = prefManager.getDoc();
        String value = edit_value.getText().toString();
        String unit = spin_unid.getSelectedItem().toString();
        String state = spin_state.getSelectedItem().toString();
        double data = 0;
        if(TextUtils.isEmpty(value)){
            error(getString(R.string.field_empties));
            focusView = edit_value;
        }else {
            data = Double.parseDouble(value);
        }
        String fecha;
        Context context = this;
        fecha = txt_date.getText().toString()+"T"+txt_time.getText().toString();
        /*if(mDate == 0){
            // Get Current Date
            final Calendar now = Calendar.getInstance();
            String monthConverted = ""+(now.get(Calendar.MONTH)+1);
            String dayConverted = converted(now.get(Calendar.DAY_OF_MONTH));
            String hourConverted = converted(now.get(Calendar.HOUR_OF_DAY));
            if(now.get(Calendar.MONTH) < 10){
                monthConverted = "0"+monthConverted;
            }
            fecha = String.valueOf(now.get(Calendar.YEAR))+"-"+monthConverted+"-"+dayConverted+
                    "T"+hourConverted+":"+String.valueOf(now.get(Calendar.MINUTE));
            *//*DateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
            fecha = df.format(Calendar.getInstance().getTime());
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            Date d = null;
            try {
                d = sdf.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formattedTime = output.format(d);*//*
        }else{
            fecha = txt_date.getText().toString()+"T"+txt_time.getText().toString();
        }*/

        Log.w("Fecha: ",""+fecha);
        Log.w("Unit: ",""+unit);

        if(unit.equalsIgnoreCase("mg/dl")){
            if(data>=60 && data<=190){
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

        Log.d("Dato: ",""+value+" "+unit);

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(value)) {
            error(getString(R.string.field_empties));
            focusView = edit_value;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new SendGlucTask(doc, value, state, fecha, context);
            mAuthTask.execute();
        }

    }

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

    private void dialog() {
        String alert= "Revisa las unidades y el valor";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                    .setTitle("Algo no anda bien....")
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

    private String converted(int date){
        String s = ""+date;
        if(date < 10){
               s = "0"+s;
        }
        return s;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String hourConverted = converted(hourOfDay);
        String minutConverted = converted(minute);
        txt_time.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));

    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        String monthConverted = converted(monthOfYear+1);
        String dayConverted = converted(dayOfMonth);
        txt_date.setText(getString(R.string.calendar_date_picker_value, year, monthConverted, dayConverted));
    }

    public class SendGlucTask extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;
        private final String mValue;
        private final String mState;
        private final String mFecha;
        private final Context mContext;

        SendGlucTask(String doc, String value, String state, String fecha, Context context) {
            mDoc = doc;
            mValue = value;
            mState = state;
            mFecha = fecha;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mGluc = new MRegisterGlucose();
            //String document, String value, String unit, String state, String fecha, Context context
            try {
                jObject = mGluc.sendGluc(mDoc, mValue, mState, mFecha, "", mContext);
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
                    Snackbar.make(view, "Mensaje enviado :}", Snackbar.LENGTH_LONG)
                            .show();
                }else {
                    Snackbar.make(view, "Mensaje fail }:", Snackbar.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}



