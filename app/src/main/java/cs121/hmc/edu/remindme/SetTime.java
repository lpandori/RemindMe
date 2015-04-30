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
 * Class: SetTime.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: Set time allows the user to set the date of the remindertime they are
 * creating.
 */
public class SetTime extends ActionBarActivity {
        private TimePicker timePicker;
        private Button button;
        private AlarmDBHelper dbHelper = new AlarmDBHelper(this);


        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            //get previously set information from earlier activities
            final Intent prevIntent = getIntent();
            final String alarmName = prevIntent.getStringExtra(SetAlarmInfo.ALARM_NAME);
            final String alarmTone = prevIntent.getStringExtra(SetAlarmInfo.ALARM_TONE);
            final int reminderType = prevIntent.getIntExtra(SetFrequency.REMINDER_TYPE, -1);
            final boolean existingModel = prevIntent.getBooleanExtra(ReminderListActivity.EXISTING_MODEL, false);
            final long existingModelId = prevIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
            final int minBetweenSnooze = prevIntent.getIntExtra(ReminderListActivity.MIN_BETWEEN_SNOOZE, ReminderTime.DEFAULT_MIN_BETWEEN_SNOOZE);

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
                    //depending on the reminderType create a new reminderTime
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
                    //if user is creating a new alarm, create a new alarmModel
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
                        //otherwise, just add the new reminder to the existing
                        //alarmModel's array of reminders
                    }else{
                        AlarmModel alarmModel = dbHelper.getAlarm(existingModelId);
                        alarmModel.addReminder(r);
                        dbHelper.deleteAlarm(existingModelId);
                        dbHelper.createAlarm(alarmModel);
                        i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, existingModelId);
                    }
                    //trigger setting alarm
                    AlarmManagerHelper.setAlarms(context);
                    i.putExtra(ReminderListActivity.ALARM_NAME, alarmName);
                    i.putExtra(ReminderListActivity.ALARM_TONE, alarmTone);
                    startActivity(i);
                }
            });
        }


    //inflate action bar to have a cancel icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //on the click of the cancel icon, go back to the list of alarms
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

        //set timepicker to show current time
        public void setCurrentTimeOnView(){
            timePicker = (TimePicker) findViewById(R.id.test);
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);


        }
    }


