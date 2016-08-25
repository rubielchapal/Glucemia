package magnusdroid.com.glucup_2date.Controller;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListAlarm;
import magnusdroid.com.glucup_2date.R;

public class SetAlarmActivity extends AppCompatActivity implements RadialTimePickerDialogFragment.OnTimeSetListener {

    // Get shared preferences
    private PrefManager prefManager;
    private TextView alarmdate;
    private EditText alarmname;
    private String alarm_name, alarm_date;
    private TimePicker timePicker;
    private Button set_alarm_btn;
    private ListAlarm listAlarm;
    private List<ListAlarm> NewAlarm = new ArrayList<>();
    int richard_quote = 0;
    public static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        // Set up shared preferences.
        prefManager = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarmdate = (TextView) findViewById(R.id.alarmdate);
        alarmname = (EditText) findViewById(R.id.alarmname);

        set_alarm_btn = (Button) findViewById(R.id.set_alarm_btn);
        set_alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(SetAlarmActivity.this)
                        .setForced12hFormat();

                rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
            }
        });

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_set_alarm);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm_name = alarmname.getText().toString();
                alarm_date = alarmdate.getText().toString();
                prefManager.setAlarmName(alarm_name);
                prefManager.setAlarmDate(alarm_date);
                prefManager.setFragment(1);
                setAlarm(alarm_date, alarm_name);
                Intent intent = new Intent(SetAlarmActivity.this, PacientActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setAlarm(String s, String s1) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date date = new Date();
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int reqCode = Integer.valueOf(s1);
        Log.d("SetAlarm Texts", "Date : " + calendar + " Note: " + s1);
        Intent myIntent = new Intent(SetAlarmActivity.this, AlarmReceiver.class);
        myIntent.putExtra("extra", "yes");
        myIntent.putExtra("code", reqCode);
        myIntent.putExtra("quote id", String.valueOf(richard_quote));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SetAlarmActivity.this,
                reqCode, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) SetAlarmActivity.this
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String hourConverted = converted(hourOfDay);
        String minutConverted = converted(minute);
        alarmdate.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));
    }

    private String converted(int date){
        String s = ""+date;
        if(date < 10){
            s = "0"+s;
        }
        return s;
    }
}
