package magnusdroid.com.glucup_2date.Controler;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import magnusdroid.com.glucup_2date.R;

/**
 * Created by Dell on 22/07/2016.
 */
public class AlarmService extends IntentService {

    public static final String TAG = "Scheduling Demo";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    // The string the app searches for in the Google home page content. If the app finds
    // the string, it indicates the presence of a doodle.
    public static final String SEARCH_STRING = "doodle";
    // The Google home page URL from which the app fetches content.
    // You can find a list of other Google domains with possible doodles here:
    // http://en.wikipedia.org/wiki/List_of_Google_domains
    public static final String URL = "http://www.google.com";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // BEGIN_INCLUDE(service_onhandle)
        int Noti_Code = 0;
        if(intent != null) {
            Noti_Code = intent.getExtras().getInt("code");
        }
        Log.i("Codigo"," "+Noti_Code);
        sendNotification();
        // Release the wake lock provided by the BroadcastReceiver.
        AlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }

    // Post a notification indicating whether a doodle was found.
    private void sendNotification() {
        String msg = "Es hora de registrar tu valor de glucosa! (:";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] aLong = new long[] { 100L, 100L, 200L, 500L };
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, PacientActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setVibrate(aLong)
                        .setLights(Color.CYAN, 300, 1000)
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
                        /*.setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.doodle_alert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);*/

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }



    /*private boolean isRunning;
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

    @Override
    protected void onHandleIntent(Intent intent) {

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
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Es hora de registrar tu valor de glucosa! (:")
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
    }*/


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