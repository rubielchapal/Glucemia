package magnusdroid.com.glucup_2date.Controler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListAlarm;
import magnusdroid.com.glucup_2date.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class AlarmFragment extends Fragment implements View.OnClickListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    //UI references
    private Button btn_set_alarm;
    private FloatingActionButton fab_add_alarm;
    private RecyclerView recycler_alarm;
    //Utilities
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ListAlarm> ListAlarm = new ArrayList<>();
    private Intent intent;
    AlarmReceiver alarmReceiver = new AlarmReceiver();
    private int minute, hour;
    private int code = 0;

    public AlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        intent = new Intent(getContext(), AlarmReceiver.class);

        fab_add_alarm = (FloatingActionButton) view.findViewById(R.id.fab_add_alarm);
        fab_add_alarm.setClickable(false);
        fab_add_alarm.setEnabled(false);
        fab_add_alarm.setOnClickListener(this);
        btn_set_alarm = (Button) view.findViewById(R.id.btn_set_alarm);
        btn_set_alarm.setOnClickListener(this);

        recycler_alarm = (RecyclerView) view.findViewById(R.id.recycler_alarm);
        recycler_alarm.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recycler_alarm.setLayoutManager(layoutManager);
        recycler_alarm.setItemAnimator(new DefaultItemAnimator());

        adapter = new SimpleItemRecyclerViewAdapter(ListAlarm, intent, getContext());
        recycler_alarm.setAdapter(adapter);
        recycler_alarm.addItemDecoration(new ItemDecoration(getContext()));

        return view;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i){
            case R.id.btn_set_alarm:
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(this)
                        .setForced12hFormat();
                rtpd.show(getFragmentManager(), AddGlucActivity.FRAG_TAG_TIME_PICKER);
                break;
            case R.id.fab_add_alarm:
                String alarm = btn_set_alarm.getText().toString();
                // Set the alarm's trigger time to select hour.
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.HOUR_OF_DAY, AlarmFragment.this.hour);
                calendar.set(calendar.MINUTE, AlarmFragment.this.minute);
                calendar.set(calendar.SECOND, 0);
                calendar.set(calendar.MILLISECOND, 0);
                // Notify the adapter that an item inserted
                ListAlarm listAlarm = new ListAlarm(alarm, "Alarma", code);
                ListAlarm.add(listAlarm);
                recycler_alarm.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(0);
                recycler_alarm.scrollToPosition(0);
                alarmReceiver.setAlarm(getContext(), calendar, code);
                /*intent.putExtra("extra", "yes");
                intent.putExtra("ID", code);
                alarmIntent = PendingIntent.getBroadcast(getContext(), code, intent, PendingIntent.FLAG_ONE_SHOT);
                // Set the alarm to fire at approximately 8:30 a.m., according to the device's
                // clock, and to repeat once a day.
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);*/
                // Show the added item label
                Toast.makeText(getContext(),"Alarm set to:" + alarm, Toast.LENGTH_SHORT).show();
                code ++;
                break;
        }

    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        AlarmFragment.this.minute = minute;
        AlarmFragment.this.hour = hourOfDay;
        String hourConverted = converted(hourOfDay);
        String minutConverted = converted(minute);
        fab_add_alarm.setClickable(true);
        fab_add_alarm.setEnabled(true);
        btn_set_alarm.setText(getString(R.string.set_actual_time, hourConverted, minutConverted));
    }

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
            holder.txt_alarm.setText(mValues.get(position).getDate());
            //holder.txt_alarm.setText(String.valueOf(id));
            holder.cancel_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the clicked item label
                    String itemLabel = mValues.get(position).getDate();
                    int idintent = mValues.get(position).getId();
                    Log.w("ID"," "+idintent);
                    PendingIntent pi = PendingIntent.getBroadcast(context, idintent, intent, 0);
                    alarmReceiver.cancelAlarm(context, pi);
                    /*PendingIntent pi = PendingIntent.getBroadcast(context, idintent, intent, 0);
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.cancel(pi);*/
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
            public TextView txt_alarm;
            public ImageButton cancel_alarm;
            public final View mView;

            public ViewHolder(View view) {
                super(view);
                txt_alarm = (TextView) view.findViewById(R.id.txt_alarm);
                cancel_alarm = (ImageButton) view.findViewById(R.id.cancel_alarm);
                mView = view;
            }
        }
    }

    private String converted(int date){
        String s = ""+date;
        if(date < 10){
            s = "0"+s;
        }
        return s;
    }

    /**
     * Class to draw a divider item to the list of the Pacient. Inflate the layout and add params
     * using canvas class
     */
    private class ItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public ItemDecoration(Context context) {
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
