package cs121.hmc.edu.remindme;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.List;

/**
 * Created by heatherseaman on 3/1/15.
 * Schedules alarms using AlarmManger. Extends BroadcastReceiver
 * to run on boot and reset all the alarms stored in the database.
 */
public class AlarmManagerHelper extends BroadcastReceiver{
    public static final String REMINDER_ID = "id";
    public static final String NAME = "name";
    //public static final String TIME_HOUR = "timeHour";
    //public static final String TIME_MINUTE = "timeMinute";
    //public static final String TONE = "alarmTone";

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);
        AlarmDBHelper dbHelper = new AlarmDBHelper(context);
        List<AlarmModel> alarms = dbHelper.getAlarms();

        if(alarms != null){
            for(AlarmModel alarm: alarms){
                if(alarm.isEnabled()){

                    long timeInMillis = alarm.getNextReminderTime();
                    PendingIntent pIntent = createPendingIntent(context, alarm);//TODO need to redo this
                    if(timeInMillis > 0) {
                        setAlarm(context, timeInMillis, pIntent);
                    }

                }
            }
        }
    }

    @SuppressLint("NewApi")
    //used to be: setAlarm(Context context, Calendar calendar, PendingIntent pIntent)
    private static void setAlarm(Context context, long timeInMillis, PendingIntent pIntent) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pIntent);
            }
    }

    /*
     * Note: to cancel an alarm we need to build an instance of the pending
     * intent exactly as we did when the alarm was sent. We need to make sure
     * we cancel alarms before we make changes to them, only then can we set them
     * again. Otherwise we might leave scheduled alarms that we can no longer
     * reference.
     */
    //TODO we need to do this!!!
    public static void cancelAlarms(Context context) {
        AlarmDBHelper dbHelper = new AlarmDBHelper(context);
        List<AlarmModel> alarms = dbHelper.getAlarms();

        if (alarms != null) {
            for (AlarmModel alarm : alarms) {
                if (alarm.isEnabled()) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);//TODO redo this method
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }

    //creates pending intent in uniform way
    //TODO double check this
    //Need help on this one
    private static PendingIntent createPendingIntent(Context context, AlarmModel model) {
        Intent intent = new Intent(context, AlarmService.class);

        //from the model get currentreminder id :)

        intent.putExtra(NAME, model.name);
        intent.putExtra(REMINDER_ID, model.getReminderId());//TODO catch -1 case here?
        return PendingIntent.getService(context, (int) model.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}
