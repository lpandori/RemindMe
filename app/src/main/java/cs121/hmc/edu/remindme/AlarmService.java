package cs121.hmc.edu.remindme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by heatherseaman on 3/1/15.
 * Launches a new activity after an alarm will go off.
 */
public class AlarmService extends Service {

    //public static String TAG = AlarmService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);

        AlarmManagerHelper.setAlarms(this);
        return super.onStartCommand(intent, flags, startId);
    }


}
