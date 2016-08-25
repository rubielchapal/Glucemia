package magnusdroid.com.glucup_2date.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by Dell on 22/07/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        int Noti_code = intent.getExtras().getInt("ID");
        Log.e("Code is" , " "+Noti_code);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", state);
        serviceIntent.putExtra("code", Noti_code);

        context.startService(serviceIntent);

        /*String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        String richard_id = intent.getExtras().getString("quote id");
        Log.e("Richard quote is" , richard_id);

        int Noti_code = intent.getExtras().getInt("code");
        Log.e("Code is" , " "+Noti_code);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", state);
        serviceIntent.putExtra("quote id", richard_id);
        serviceIntent.putExtra("code", Noti_code);

        context.startService(serviceIntent);*/
    }

}