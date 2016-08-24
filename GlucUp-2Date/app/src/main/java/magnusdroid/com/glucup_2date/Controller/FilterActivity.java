package magnusdroid.com.glucup_2date.Controller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.Model.MAllGlucose;
import magnusdroid.com.glucup_2date.Model.MDateGlucose;
import magnusdroid.com.glucup_2date.R;

public class FilterActivity extends AppCompatActivity
        implements CalendarDatePickerDialogFragment.OnDateSetListener {


    // UI references.
    RelativeLayout Day, Week, Value;
    Spinner filter_unit_spinner;
    Button button_day, button_week, btn_show;
    EditText txtDate, txtMin, txtMax, txtFix;
    RadioGroup rg;
    RadioButton rb;
    String[] days;
    String plan, tDate, tWeek, tMin, tMax, tFix;
    TextView txtSWeek, txtWeek;
    private int mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        mFlag = extras.getInt("flag");

        Day = (RelativeLayout) findViewById(R.id.dia_layout);
        Week = (RelativeLayout) findViewById(R.id.semana_layout);
        Value = (RelativeLayout) findViewById(R.id.value_layout);
        button_day = (Button) findViewById(R.id.btn_date);
        button_week = (Button) findViewById(R.id.btn_week);
        btn_show = (Button) findViewById(R.id.btn_show);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtWeek = (TextView) findViewById(R.id.in_week);
        txtMin = (EditText) findViewById(R.id.min_value);
        txtMax = (EditText) findViewById(R.id.max_value);
        txtFix = (EditText) findViewById(R.id.unique_value);
        txtSWeek = (TextView) findViewById(R.id.week_set);
        filter_unit_spinner = (Spinner) findViewById(R.id.filter_unit_spinner);
        //calendarView = (CalendarView) findViewById(R.id.selector_week);
        rb = (RadioButton) findViewById(R.id.show_all);

        if (mFlag == 0){
            assert rb != null;
            rb.setVisibility(View.VISIBLE);
        }else if (mFlag == 1){
            assert rb != null;
            rb.setVisibility(View.GONE);
        }

        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb != null) {
                    plan = (String) rb.getText();
                }
                if (plan.equalsIgnoreCase("Dia")) {
                    btn_show.setVisibility(View.VISIBLE);
                    Value.setVisibility(View.GONE);
                    Week.setVisibility(View.GONE);
                    Day.setVisibility(View.VISIBLE);
                    button_day.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                                    .setOnDateSetListener(FilterActivity.this);
                            cdp.show(getSupportFragmentManager(), AddGlucActivity.FRAG_TAG_DATE_PICKER);
                            /*// Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            String monthConverted = ""+(monthOfYear+1);
                                            String dayConverted = ""+dayOfMonth;
                                            if(monthOfYear < 10){
                                                monthConverted = "0"+monthConverted;
                                            }
                                            if(dayOfMonth < 10){
                                                dayConverted = "0"+dayConverted;
                                            }
                                            txtDate.setText(year + "-" + monthConverted + "-" + dayConverted);
                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();*/
                        }
                    });
                } else if (plan.equalsIgnoreCase("Semana")) {
                    Value.setVisibility(View.GONE);
                    btn_show.setVisibility(View.VISIBLE);
                    Day.setVisibility(View.GONE);
                    Week.setVisibility(View.VISIBLE);
                    button_week.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                                    .setOnDateSetListener(FilterActivity.this);
                            cdp.show(getSupportFragmentManager(), AddGlucActivity.FRAG_TAG_DATE_PICKER);
                        }
                    });
                    /*calendarView.setShowWeekNumber(false);
                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar cal = Calendar.getInstance();
                            try {
                                Date date = format.parse(dayOfMonth + "-" + (month + 1) + "-" + year);
                                cal.setTime(date);
                                int delta = -cal.get(Calendar.DAY_OF_WEEK) + 1; //add 2 if your week start on monday
                                cal.add(Calendar.DAY_OF_MONTH, delta );
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            days = new String[7];
                            for (int i = 0; i < 7; i++)
                            {
                                days[i] = format.format(cal.getTime());

                                cal.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            txtWeek.setText(Arrays.toString(days));

                        }
                    });*/

                    /*button_week.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get Current Date
                            final Calendar now = Calendar.getInstance();
                            mYear = now.get(Calendar.YEAR);
                            mMonth = now.get(Calendar.MONTH);
                            mDay = now.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            Calendar cal = Calendar.getInstance();

                                            try {
                                                Date date = format.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                cal.setTime(date);
                                                int delta = -cal.get(Calendar.DAY_OF_WEEK) + 1; //add 2 if your week start on monday
                                                cal.add(Calendar.DAY_OF_MONTH, delta );
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            days = new String[7];
                                            for (int i = 0; i < 7; i++)
                                            {
                                                days[i] = format.format(cal.getTime());

                                                cal.add(Calendar.DAY_OF_MONTH, 1);
                                            }
                                            txtWeek.setText(Arrays.toString(days));
                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    });*/
                } else if (plan.equalsIgnoreCase("Valor")) {
                    Value.setVisibility(View.VISIBLE);
                    btn_show.setVisibility(View.VISIBLE);
                    Day.setVisibility(View.GONE);
                    Week.setVisibility(View.GONE);
                    txtFix.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            enableEditText();
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else if (plan.equalsIgnoreCase("Todos")) {
                    Value.setVisibility(View.GONE);
                    btn_show.setVisibility(View.VISIBLE);
                    Day.setVisibility(View.GONE);
                    Week.setVisibility(View.GONE);
                    //Toast.makeText(FilterActivity.this, "2", Toast.LENGTH_SHORT).show();

                }
            }
        });


        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FilterActivity.this, ListGlucoseActivity.class);
                tDate = txtDate.getText().toString();
                tWeek = txtWeek.getText().toString();
                tMin = txtMin.getText().toString();
                tMax = txtMax.getText().toString();
                tFix = txtFix.getText().toString();
                //Log.w("Datos",""+tDate);
                if (!(tDate.isEmpty()) && tWeek.isEmpty() && tMin.isEmpty() && tMax.isEmpty() && tFix.isEmpty()){ //Show by Date
                    //i = new Intent(FilterActivity.this, ListGlucoseActivity.class);
                    i.putExtra("date", tDate);
                    i.putExtra("flag", mFlag);
                    startActivity(i);
                    finish();
                    //downloadDate = new DownloadDate(document, tDate, FilterActivity.this);
                    //downloadDate.execute((Void) null);
                } else if (!(tWeek.isEmpty()) && tDate.isEmpty() && tMin.isEmpty() && tMax.isEmpty() && tFix.isEmpty()){ //Show by Week
                    //Show by Week
                    Intent j = new Intent(FilterActivity.this, WeekFilterGlucose.class);
                    //j.putExtra("week", tWeek);
                    j.putExtra("week", days);
                    j.putExtra("flag", mFlag);
                    Log.w("Tweek",""+ tWeek);
                    startActivity(j);
                    finish();
                } else if ((!(tFix.isEmpty()) || !(tMin.isEmpty()) || !(tMax.isEmpty())) && tDate.isEmpty() && tWeek.isEmpty()){ //Show by Value
                    String unit = filter_unit_spinner.getSelectedItem().toString();
                    i.putExtra("date", "valor");
                    i.putExtra("Vfix", tFix);
                    i.putExtra("Vmin", tMin);
                    i.putExtra("Vmax", tMax);
                    i.putExtra("Unit", unit);
                    startActivity(i);
                    finish();
                }else if (tDate.isEmpty() && tWeek.isEmpty() && tMin.isEmpty() && tMax.isEmpty()){ //Show All
                    i.putExtra("date", "todos");
                    startActivity(i);
                    finish();
                    //Toast.makeText(FilterActivity.this, "3", Toast.LENGTH_SHORT).show();
                    //downloadAll = new DownloadAll(document, FilterActivity.this);
                    //downloadAll.execute((Void) null);
                }
            }
        });

    }

    private void enableEditText() {
        boolean isRdy = !(txtFix.getText().toString().length()>0);
        Log.w("Enable ",""+isRdy);
        txtMin.setFocusable(isRdy);
        txtMax.setFocusable(isRdy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        if(Day.getVisibility() == View.VISIBLE) {
            String monthConverted = "" + (monthOfYear + 1);
            String dayConverted = "" + (dayOfMonth);
            if (dayOfMonth < 10) {
                dayConverted = "0" + dayConverted;
            }
            if (monthOfYear < 10) {
                monthConverted = "0" + monthConverted;
            }
            txtDate.setText(getString(R.string.calendar_date_picker_value, year, monthConverted, dayConverted));
        }else{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            try {
                Date date = format.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                cal.setTime(date);
                int delta = -cal.get(Calendar.DAY_OF_WEEK) + 1; //add 2 if your week start on monday
                cal.add(Calendar.DAY_OF_MONTH, delta );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            days = new String[7];
            for (int i = 0; i < 7; i++)
            {
                days[i] = format.format(cal.getTime());

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            txtWeek.setText(Arrays.toString(days));
            txtSWeek.setText(getString(R.string.calendar_week_picker_value, days[0], days[6]));
        }

    }

}
