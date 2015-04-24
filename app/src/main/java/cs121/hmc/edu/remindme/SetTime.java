package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/26/15.
 */
public class SetTime extends ActionBarActivity {
        private TimePicker timePicker;
        private Button button;
        private AlarmDBHelper dbHelper = new AlarmDBHelper(this);


        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            final Intent prevIntent = getIntent(); // gets the previously created intent
            final String alarmName = prevIntent.getStringExtra(SetAlarmInfo.ALARM_NAME);
            final String alarmTone = prevIntent.getStringExtra(SetAlarmInfo.ALARM_TONE);
            final int reminderType = prevIntent.getIntExtra(SetFrequency.REMINDER_TYPE, -1);
            final boolean existingModel = prevIntent.getBooleanExtra(ReminderListActivity.EXISTING_MODEL, false);
            final long existingModelId = prevIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);

            final int minBetweenSnooze = prevIntent.getIntExtra(ReminderListActivity.MIN_BETWEEN_SNOOZE, ReminderTime.DEFAULT_MIN_BETWEEN_SNOOZE);


            System.out.println(alarmTone);


            final Context context = this;
            setContentView(R.layout.time_picker);
            setCurrentTimeOnView();
            button = (Button) findViewById(R.id.done);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    boolean[] weekdays;

                    ReminderTime r = null;
                    //use switch to make reminder time
                    switch(reminderType){
                        case ReminderTime.ONE_TIME:

                            int year = prevIntent.getIntExtra(SetDate.DATE_YEAR, -1);
                            int month = prevIntent.getIntExtra(SetDate.DATE_MONTH, -1);
                            int day = prevIntent.getIntExtra(SetDate.DATE_DAY, -1);
                            r = new ReminderOneTime(year, month, day, hour, minute);
                            break;
                        case ReminderTime.DAILY:
                            r = new ReminderDaily(hour, minute);
                            break;
                        case ReminderTime.WEEKLY:
                            weekdays = prevIntent.getBooleanArrayExtra(SetWeekly.WEEKDAY_ARRAY);
                            r = new ReminderWeekly(hour, minute, weekdays);
                            break;
                        case ReminderTime.MONTHLY:
                            weekdays = prevIntent.getBooleanArrayExtra(SetWeekly.WEEKDAY_ARRAY);
                            int weekNumber = prevIntent.getIntExtra(SetMonthly.WEEK_NUMBER,-1);
                            r = new ReminderMonthly(hour, minute, weekNumber, weekdays);
                            break;
                    }
                    r.setMinBetweenSnooze(minBetweenSnooze);
                    Intent i = new Intent(SetTime.this, ReminderListActivity.class);
                    if(!existingModel){
                        AlarmModel alarmModel = new AlarmModel(alarmName);

                        // we create a unique id using the system time
                        long alarmId = System.currentTimeMillis();
                        alarmModel.setId(alarmId);

                        alarmModel.setAlarmTone(alarmTone);
                        alarmModel.setId(System.currentTimeMillis());

                        alarmModel.addReminder(r);
                        i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, alarmId);


                        dbHelper.createAlarm(alarmModel);//add to db
                    }else{
                        AlarmModel alarmModel = dbHelper.getAlarm(existingModelId);
                        alarmModel.addReminder(r);
                        dbHelper.deleteAlarm(existingModelId);
                        dbHelper.createAlarm(alarmModel);
                        i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, existingModelId);

                        System.out.println("added new reminder time");

                    }
                    AlarmManagerHelper.setAlarms(context);//trigger setting alarm
                    System.out.println("ALARM NAME IS " + alarmName);
                    System.out.println("ALARM TONE IS " + alarmTone);


                    i.putExtra(ReminderListActivity.ALARM_NAME, alarmName);
                    i.putExtra(ReminderListActivity.ALARM_TONE, alarmTone);
                    // pass timePicker.getCurrentHour() &&
                    //timePicker.getCurrentMinute() as extras
                    startActivity(i);
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_button: {
                Intent intent = new Intent(this, AlarmListActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
        public void setCurrentTimeOnView(){
            timePicker = (TimePicker) findViewById(R.id.test);
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);


        }
    }


