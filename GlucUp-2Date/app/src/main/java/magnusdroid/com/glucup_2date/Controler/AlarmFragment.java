package magnusdroid.com.glucup_2date.Controler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListAlarm;
import magnusdroid.com.glucup_2date.R;

public class AlarmFragment extends Fragment implements View.OnClickListener,
        RadialTimePickerDialogFragment.OnTimeSetListener {


    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;
    private ToggleButton[] mWeekByDayButtons = new ToggleButton[7];
    //private static MainActivity inst;
    private TextView alarmTextView;
    private Button setAlarm;
    private Button unsetAlarm;
    private AlarmReceiver alarm;
    private Context context;
    private Intent myIntent;
    private int minute, hour;
    int richard_quote = 0;
    ArrayList<Integer> test = new ArrayList<>();
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private RelativeLayout rLayout;
    private String mTitle, mDate;
    private ArrayList<ListAlarm> ListAlarm = new ArrayList<>();
    private List<ListAlarm> NewAlarm = new ArrayList<>();
    private ListAlarm listAlarm;
    ArrayList<PendingIntent> intentArray;
    private int code = 0;
    private JSONArray jsonArray = new JSONArray();
    boolean[] weeklyByDayOfWeek = new boolean[7];
    public static final String PREFS_NAME = "MyPrefsFile";
    private String alarmset;
    private FloatingActionButton fab_add_alarm;
    private ToggleButton sun, mon, tue, wed, thu, fri, sat;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        settings = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        sun = (ToggleButton) view.findViewById(R.id.sun_tb);
        mon = (ToggleButton) view.findViewById(R.id.mon_tb);
        tue = (ToggleButton) view.findViewById(R.id.tue_tb);
        wed = (ToggleButton) view.findViewById(R.id.wed_tb);
        thu = (ToggleButton) view.findViewById(R.id.thu_tb);
        fri = (ToggleButton) view.findViewById(R.id.fri_tb);
        sat = (ToggleButton) view.findViewById(R.id.sat_tb);

        // In Time.java day of week order e.g. Sun = 0
        Calendar cnow = Calendar.getInstance();
        int idx = cnow.get(Calendar.DAY_OF_WEEK);

        enableDay(idx);

        fab_add_alarm = (FloatingActionButton) view.findViewById(R.id.fab_add_alarm);
        fab_add_alarm.setClickable(false);
        fab_add_alarm.setOnClickListener(this);

        context = getContext();

        recycler = (RecyclerView) view.findViewById(R.id.recycler_alarm);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(lManager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        //alarm = new AlarmReceiver();
        //alarmTextView = (TextView) view.findViewById(R.id.alarmText);
        setAlarm= (Button) view.findViewById(R.id.fab_alarm);

        myIntent = new Intent(getContext(), AlarmReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // set the alarm to the time that you picked
        //calendar = Calendar.getInstance();
        //calendar.add(Calendar.SECOND, 3);
        //alarmTimePicker = (TimePicker) view.findViewById(R.id.alarmTimePicker);

        /*if(!(ListAlarm.isEmpty())){
            ListAlarm.add(listAlarm);
        }*/

        //alarmTextView.setOnClickListener(this);
        setAlarm.setOnClickListener(this);

        //adapter = new AlarmAdapter(ListAlarm, context, myIntent, alarmManager, intentArray);
        adapter = new SimpleItemRecyclerViewAdapter(ListAlarm, myIntent, context);
        recycler.setAdapter(adapter);

        return view;
    }

    private void enableDay(int idx) {
        if (idx == 2){
            mon.setChecked(true);
        } else if (idx == 3) {
            tue.setChecked(true);
        } else if (idx == 4) {
            wed.setChecked(true);
        } else if (idx == 5) {
            thu.setChecked(true);
        } else if (idx == 6) {
            fri.setChecked(true);
        } else if (idx == 7) {
            sat.setChecked(true);
        } else if (idx == 1) {
            sun.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id_view = v.getId();
        switch (id_view){
            case R.id.fab_add_alarm:
                alarmset = setAlarm.getText().toString();
                setAlarm.setEnabled(false);
                Log.w("Datetime"," "+AlarmFragment.this.hour+" "+AlarmFragment.this.minute);
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.HOUR_OF_DAY, AlarmFragment.this.hour);
                calendar.set(calendar.MINUTE, AlarmFragment.this.minute);
                calendar.set(calendar.SECOND, 0);
                calendar.set(calendar.MILLISECOND, 0);
                long sdl = calendar.getTimeInMillis();
                ListAlarm alarm = new ListAlarm(alarmset, "Alarma", code);
                ListAlarm.add(alarm);
                // Notify the adapter that an item inserted
                recycler.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(0);
                recycler.scrollToPosition(0);
                intentArray = new ArrayList<>();
                Log.e("Code"," "+code);
                myIntent.putExtra("extra", "yes");
                myIntent.putExtra("ID", code);
                pending_intent = PendingIntent.getBroadcast(context, code, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sdl, AlarmManager.INTERVAL_DAY, pending_intent);
                intentArray.add(pending_intent);
                // Show the added item label
                Toast.makeText(context,"New: Alarm set to:" + alarmset, Toast.LENGTH_SHORT).show();
                code ++;
                Log.w("Code"," "+code);

                break;
            /*case R.id.alarmText :                     //cuando se da click para nueva alarma
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                /*TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        AlarmFragment.this.minute = selectedMinute;
                        AlarmFragment.this.hour = selectedHour;
                        String hourConverted = converted(AlarmFragment.this.hour);
                        String minutConverted = converted(AlarmFragment.this.minute);
                        alarmset = hourConverted + ":" + minutConverted;
                        alarmTextView.setText(alarmset);
                        Log.w("Alarm Set:"," "+alarmset);
                        setAlarm.setEnabled(true);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();}*
                break;*/
            case R.id.fab_alarm :                      //cuando se da click para agregar alarma
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(this)
                        .setForced12hFormat();
                rtpd.show(getFragmentManager(), AddGlucActivity.FRAG_TAG_TIME_PICKER);
                /*setAlarm.setEnabled(false);
                Log.w("Datetime"," "+AlarmFragment.this.hour+" "+AlarmFragment.this.minute);
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.HOUR_OF_DAY, AlarmFragment.this.hour);
                calendar.set(calendar.MINUTE, AlarmFragment.this.minute);
                calendar.set(calendar.SECOND, 0);
                calendar.set(calendar.MILLISECOND, 0);
                long sdl = calendar.getTimeInMillis();
                //String string = alarmTextView.getText().toString();
                *//*ListAlarm alarm = new ListAlarm(alarmset, "Alarma", code);
                ListAlarm.add(alarm);
                editor.putBoolean("Alarms",true);
                for (int i=0; i < ListAlarm.size(); i++) {
                    jsonArray.put(ListAlarm.get(i).getJSONObject());
                }*//*
                // Notify the adapter that an item inserted
                adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(0);
                recycler.scrollToPosition(0);

                // context variable contains your `Context`
                //AlarmManager mgrAlarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                intentArray = new ArrayList<>();

                //              for(int i = 0; i < 10; ++i)
//                {
                //Intent intent = new Intent(context, OnAlarmReceiver.class);
                // Loop counter `i` is used as a `requestCode`
                Log.e("Code"," "+code);
                myIntent.putExtra("extra", "yes");
                myIntent.putExtra("ID", code);
                pending_intent = PendingIntent.getBroadcast(context, code, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sdl, AlarmManager.INTERVAL_DAY, pending_intent);
                    *//*alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            AlarmManager.INTERVAL_DAY,
                            pending_intent);*//*

                intentArray.add(pending_intent);
                //}

                //pending_intent = PendingIntent.getBroadcast(context, _id, myIntent, PendingIntent.FLAG_ONE_SHOT);

                //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                //AlarmManager.INTERVAL_DAY, pending_intent);

                // Show the added item label
                Toast.makeText(context,"New: Alarm set to:" + alarmset, Toast.LENGTH_SHORT).show();

                code ++;
                Log.w("Code"," "+code);*/
                //Intent intent = new Intent(context, SetAlarmActivity.class);
                //context.startActivity(intent);
                break;
        }

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
        AlarmFragment.this.minute = minute;
        AlarmFragment.this.hour = hourOfDay;
        String hourConverted = converted(hourOfDay);
        String minutConverted = converted(minute);
        fab_add_alarm.setVisibility(View.VISIBLE);
        fab_add_alarm.setClickable(true);
        setAlarm.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));
    }

    /*private void startReminderAlarm(int id, Calendar from_date, long interval) {

        // TODO Auto-generated method stub
        Intent remIntent;
        PendingIntent pendingIntent;
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        remIntent = new Intent(mContext, ReminderReceiver.class);
        remIntent.putExtra("ID", id);
        pendingIntent = PendingIntent.getBroadcast(mContext, id, remIntent, 0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, from_date.getTimeInMillis(), interval, pendingIntent);
        Toast.makeText(mContext, "Alarm Set for id" + id, Toast.LENGTH_SHORT).show();
        Log.d("", "Alarm Set for id: " + id);
    }*/

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //private final List<DummyContent.DummyItem> mValues;
        private List<ListAlarm> mValues;
        private Context context;
        private Intent intent;

        public SimpleItemRecyclerViewAdapter(List<ListAlarm> listAlarm, Intent intent, Context context) {
            this.mValues = listAlarm;
            this.context = context;
            this.intent = intent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.alarmlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            int id = mValues.get(position).getId();
            holder.date.setText(mValues.get(position).getDate());
            holder.idalarm.setText(String.valueOf(id));
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the clicked item label
                    String itemLabel = mValues.get(position).getDate();
                    int idintent = mValues.get(position).getId();
                    Log.w("ID"," "+idintent);
                    PendingIntent pi = PendingIntent.getBroadcast(context, idintent, intent, 0);
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.cancel(pi);
                    // Remove the item on remove/button click
                    mValues.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,mValues.size());
                    // Show the removed item label
                    Toast.makeText(context,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // Campos respectivos de un item
            public TextView date;
            public TextView idalarm;
            public ImageButton remove;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                date = (TextView) view.findViewById(R.id.tv);
                idalarm = (TextView) view.findViewById(R.id.idalarm);
                remove = (ImageButton) view.findViewById(R.id.ib_remove);
                mView = view;
            }
        }
    }



    /*AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker alarmTimePicker;
    private List<ListAlarm> ListAlarm = new ArrayList<>();
    private List<ListAlarm> NewAlarm = new ArrayList<>();
    //private static MainActivity inst;
    private TextView alarmTextView;
    private Button setAlarm;
    private Button unsetAlarm;
    private AlarmReceiver alarm;
    private Context context;
    private Calendar calendar;
    private Intent myIntent;
    int richard_quote = 0;
    ArrayList<Integer> test = new ArrayList<>();
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private RelativeLayout rLayout;
    private ListAlarm listAlarm;
    // Get shared preferences
    private PrefManager prefManager;
    private String mTitle, mDate;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        context = getContext();

        String alarm_time = getArguments().getString("Alarm");
        String name_alarm = getArguments().getString("Date");

        assert alarm_time != null;
        if(alarm_time.equalsIgnoreCase("null")){
            mTitle = "Seleccione";
            mDate = "hora";
        }else {
            mTitle = name_alarm;
            mDate = alarm_time;
            richard_quote = 2;
        }


        Log.w("Prefs"," "+mTitle+" y "+mDate);

        recycler = (RecyclerView) view.findViewById(R.id.recycler_alarm);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(lManager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        //alarm = new AlarmReceiver();
        //alarmTextView = (TextView) view.findViewById(R.id.alarmText);

        myIntent = new Intent(getContext(), AlarmReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // set the alarm to the time that you picked
       //calendar = Calendar.getInstance();
        //calendar.add(Calendar.SECOND, 3);
        //alarmTimePicker = (TimePicker) view.findViewById(R.id.alarmTimePicker);

        setAlarm= (Button) view.findViewById(R.id.fab_alarm);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                test.add(1);
                test.add(2);
                test.add(3);
                test.add(4);
                test.add(5);

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
                i.putExtra(AlarmClock.EXTRA_DAYS, test);
                i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                i.putExtra(AlarmClock.EXTRA_HOUR, 10);
                i.putExtra(AlarmClock.EXTRA_MINUTES, 30);
                startActivity(i);

                //Intent intent = new Intent(context, SetAlarmActivity.class);
                //context.startActivity(intent);


                *//*RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AlarmFragment.this)
                        .setForced12hFormat();

                rtpd.show(getFragmentManager(), AddGlucActivity.FRAG_TAG_TIME_PICKER);

                *//*calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                final int hour = alarmTimePicker.getHour();
                final int minute = alarmTimePicker.getMinute();

                String minute_string = String.valueOf(minute);
                String hour_string = String.valueOf(hour);

                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12) ;
                }*//*
            }

        });

        *//*unsetAlarm= (Button) view.findViewById(R.id.stop_alarm);
        unsetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myIntent.putExtra("extra", "no");
                myIntent.putExtra("quote id", String.valueOf(richard_quote));
                getActivity().sendBroadcast(myIntent);

                alarmManager.cancel(pending_intent);
                setAlarmText("Alarm canceled");

                //setAlarmText("ID is " + richard_quote);
            }
        });*//*

        if(!(ListAlarm.isEmpty())){
            ListAlarm.add(listAlarm);
        }

        adapter = new AlarmAdapter(ListAlarm, context, myIntent, alarmManager);
        recycler.setAdapter(adapter);


            if(richard_quote == 2) {
                listAlarm = new ListAlarm(mDate, mTitle);
                NewAlarm.add(listAlarm);
                if(NewAlarm.size() > ListAlarm.size()){
                    setList();
                }
            }



        return view;
    }

    private void setList() {
        ListAlarm.add(listAlarm);
        // Notify the adapter that an item inserted
        adapter.notifyItemInserted(0);
        recycler.scrollToPosition(0);
    }


    *//*public void setAlarmText(String alarmText) {
        //alarmTextView.setText(alarmText);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        String hourConverted = converted(hourOfDay);
        String minutConverted = converted(minute);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        myIntent.putExtra("extra", "yes");
        myIntent.putExtra("quote id", String.valueOf(richard_quote));
        final int _id = (int) System.currentTimeMillis();

        listAlarm = new ListAlarm(hourConverted + ":" + minutConverted, "Alarma");
        ListAlarm.add(listAlarm);
        // Notify the adapter that an item inserted
        adapter.notifyItemInserted(0);
        recycler.scrollToPosition(0);

        // context variable contains your `Context`
        //AlarmManager mgrAlarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        *//**//*for(int i = 0; i < 10; ++i)
        {
            //Intent intent = new Intent(context, OnAlarmReceiver.class);
            // Loop counter `i` is used as a `requestCode`
            myIntent.putExtra("ID", i);
            pending_intent = PendingIntent.getBroadcast(context, i, myIntent, 0);
            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    AlarmManager.INTERVAL_DAY,
                    pending_intent);

            intentArray.add(pending_intent);
        }*//**//*

        //pending_intent = PendingIntent.getBroadcast(context, _id, myIntent, PendingIntent.FLAG_ONE_SHOT);

        *//**//*//**//*alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pending_intent);*//**//*

        // Show the added item label
        Toast.makeText(context,"New: Alarm set to" + hourConverted + ":" + minutConverted, Toast.LENGTH_SHORT).show();

        //setAlarmText("Alarm set to " + hourConverted + ":" + minutConverted);
        //txt_time.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));
        //txt_time.setText(getString(R.string.radial_time_picker_value, hourOfDay, minute));

    }

    private String converted(int date){
        String s = ""+date;
        if(date < 10){
            s = "0"+s;
        }
        return s;
    }

    private void startReminderAlarm(int id, Calendar from_date, long interval) {

        // TODO Auto-generated method stub
        Intent remIntent;
        PendingIntent pendingIntent;
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        remIntent = new Intent(mContext, ReminderReceiver.class);
        remIntent.putExtra("ID", id);
        pendingIntent = PendingIntent.getBroadcast(mContext, id, remIntent, 0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, from_date.getTimeInMillis(), interval, pendingIntent);
        Toast.makeText(mContext, "Alarm Set for id" + id, Toast.LENGTH_SHORT).show();
        Log.d("", "Alarm Set for id: " + id);
    }*//*
*/



}
