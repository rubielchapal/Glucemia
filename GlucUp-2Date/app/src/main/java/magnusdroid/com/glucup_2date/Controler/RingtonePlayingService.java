package magnusdroid.com.glucup_2date.Controler;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import magnusdroid.com.glucup_2date.R;

/**
 * Created by Dell on 22/07/2016.
 */
public class RingtonePlayingService extends Service {

    private boolean isRunning;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;
    private long[] aLong;
    private NotificationManager mNM;

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("MyActivity", "In the Service");
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings({ "static-access", "deprecation" })
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        String state = "nn";
        int Noti_Code = 0;

        if(intent != null){
            state = intent.getExtras().getString("extra");
            Noti_Code = intent.getExtras().getInt("code");
        }


        mNM = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);


        Intent intent1 = new Intent(this.getApplicationContext(), PacientActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Remind to add new gluc" + "!")
                .setContentText("Code:"+Noti_Code)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.CYAN, 300, 1000)
                .setContentIntent(pIntent)
                .setVibrate(aLong)
                .setAutoCancel(true)
                .build();
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mNotify.flags |= Notification.FLAG_AUTO_CANCEL;

        aLong = new long[] { 100L, 100L, 200L, 500L };


        Log.e("what is going on here  ", "");

        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1) {
            Log.e("if there was not sound ", " and you want start");
            mMediaPlayer = MediaPlayer.create(this, R.raw.alert_value);
            mMediaPlayer.start();
            mNM.notify(Noti_Code, mNotify);
            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){
            Log.e("if there was not sound ", " and you want end");
            this.isRunning = false;
            this.startId = 0;

        }
        else if (this.isRunning && startId == 1){
            Log.e("if there is sound ", " and you want start");
            this.isRunning = true;
            this.startId = 0;
        }
        else {
            Log.e("if there is sound ", " and you want end");
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            this.isRunning = false;
            this.startId = 0;
        }

        Log.e("MyActivity", "In the service");
    }

    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

        this.isRunning = false;
    }


}


    /*private boolean isRunning;
    private Context context;
    MediaPlayer mMediaPlayer;
    private int startId;
    private long[] aLong;
    // Get shared preferences
    private PrefManager prefManager;

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("MyActivity", "In the Richard service");
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings({ "static-access", "deprecation" })
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if(intent != null){
        }

        // Set up shared preferences.
        prefManager = new PrefManager(this);

        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        String state = intent.getExtras().getString("extra");
        int Noti_Code = intent.getExtras().getInt("code");
        prefManager.setFragment(2);

        Intent intent1 = new Intent(this.getApplicationContext(), PacientActivity.class);
        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        PendingIntent pIntent = PendingIntent.getActivity(this, Noti_Code, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        aLong = new long[] { 100L, 100L, 200L, 500L };

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Remind to add new gluc" + "!")
                .setContentText("Click me!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.CYAN, 300, 1000)
                .setContentIntent(pIntent)
                .setVibrate(aLong)
                .setAutoCancel(true)
                .build();
        Log.e("what is going on here  ", state);

        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }

        // get richard's thing
        String richard_id = intent.getExtras().getString("quote id");
        Log.e("Service: richard id is " , richard_id);

        if(!this.isRunning && startId == 1) {
            prefManager.setFragment(1);
            Log.e("if there was not sound ", " and you want start");

            mMediaPlayer = MediaPlayer.create(this, R.raw.alert_value);

            mMediaPlayer.start();

            mNM.notify(Noti_Code, mNotify);

            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){
            prefManager.setFragment(2);
            Log.e("if there was not sound ", " and you want end");

            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){
            prefManager.setFragment(1);
            Log.e("if there is sound ", " and you want start");

            this.isRunning = true;
            this.startId = 0;

        }
        else {
            prefManager.setFragment(2);
            Log.e("if there is sound ", " and you want end");

            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }

        Log.e("MyActivity", "In the service");
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {


        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), PacientActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        aLong = new long[] { 100L, 100L, 200L, 500L };

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Remind to add new gluc" + "!")
                .setContentText("Click me!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.CYAN, 300, 1000)
                .setContentIntent(pIntent)
                .setVibrate(aLong)
                .setAutoCancel(true)
                .build();

        String state = intent.getExtras().getString("extra");

        Log.e("what is going on here  ", state);

        assert state != null;
        switch (state) {
            case "no":
                startId = 0;
                break;
            case "yes":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }

        // get richard's thing
        String richard_id = intent.getExtras().getString("quote id");
        Log.e("Service: richard id is " , richard_id);

        if(!this.isRunning && startId == 1) {
            Log.e("if there was not sound ", " and you want start");

            mMediaPlayer = MediaPlayer.create(this, R.raw.alert_value);

            mMediaPlayer.start();

            mNM.notify(0, mNotify);

            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){
            Log.e("if there was not sound ", " and you want end");

            this.isRunning = false;
            this.startId = 0;

        }

        else if (this.isRunning && startId == 1){
            Log.e("if there is sound ", " and you want start");

            this.isRunning = true;
            this.startId = 0;

        }
        else {
            Log.e("if there is sound ", " and you want end");

            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }


        Log.e("MyActivity", "In the service");

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        Log.e("JSLog", "on destroy called");
        super.onDestroy();

        this.isRunning = false;
    }
}*/