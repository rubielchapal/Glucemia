package magnusdroid.com.glucup_2date.Model;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import magnusdroid.com.glucup_2date.Controller.PacientActivity;
import magnusdroid.com.glucup_2date.Controller.PrefManager;
import magnusdroid.com.glucup_2date.R;

/**
 * Created by Dell on 29/06/2016.
 */
public class ViewDialog {


    private PrefManager prefManager;

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.error_server);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void LogOutDialog(final Activity activity, String msg, final Context context){

        prefManager = new PrefManager(context);
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout_layout);

        Button okButton = (Button) dialog.findViewById(R.id.btn_yes);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setClear();
                activity.finish();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.btn_no);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        dialog.show();

    }


    /*public void setGluc(Activity activity, String msg) {

        //UI references
        Button btn_date, btn_time;
        TextView txt_date, txt_time;
        EditText edit_value;
        Spinner spin_unid, spin_state;
        CheckBox chk_date;
        final int[] mYear = new int[1];
        final int[] mMonth = new int[1];
        final int[] mDay = new int[1];
        final int mHour;
        final int mMinute;
        final int mDate;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.gluc_value);

        edit_value = (EditText) dialog.findViewById(R.id.value_gluc);
        spin_unid = (Spinner) dialog.findViewById(R.id.spinner_unid);
        spin_state = (Spinner) dialog.findViewById(R.id.spinner_state);
        txt_date = (TextView) dialog.findViewById(R.id.in_date);
        txt_time = (TextView) dialog.findViewById(R.id.in_time);

        btn_date = (Button) dialog.findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_time = (Button) dialog.findViewById(R.id.btn_time);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        chk_date = (CheckBox) dialog.findViewById(R.id.checkBox);
        chk_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
         dialog.show();

    }

    public void setFilter(final Activity activity){

        final int[] mYear = new int[1];
        final int[] mMonth = new int[1];
        final int[] mDay = new int[1];
        int mHour;
        int mMinute;

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filter_values);

        final RelativeLayout Day = (RelativeLayout) dialog.findViewById(R.id.dia);
        final RelativeLayout Week = (RelativeLayout) dialog.findViewById(R.id.semana);
        final Button button_day = (Button) dialog.findViewById(R.id.btn_date);
        txtDate =(EditText) dialog.findViewById(R.id.in_date);

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) dialog.findViewById(checkedId);
                String plan = (String) rb.getText();
                if (plan.equalsIgnoreCase("Dia")) {
                    Day.setVisibility(View.VISIBLE);
                    Week.setVisibility(View.GONE);
                    button_day.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear[0] = c.get(Calendar.YEAR);
                            mMonth[0] = c.get(Calendar.MONTH);
                            mDay[0] = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(activity.getApplicationContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear[0], mMonth[0], mDay[0]);


                           *//* DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear[0], mMonth[0], mDay[0]);*//*
                            datePickerDialog.show();
                        }
                    });
                } else if (plan.equalsIgnoreCase("Semana")) {
                    Day.setVisibility(View.GONE);
                    Week.setVisibility(View.VISIBLE);
                } else if (plan.equalsIgnoreCase("Valor")) {
                    Toast.makeText(activity, plan, Toast.LENGTH_SHORT).show();
                }
            }
        });

        *//*
        // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
         *//*



        dialog.show();
    }*/

}
