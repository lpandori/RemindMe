package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/26/15.
 */
public class Timepicker extends Activity {
        private TimePicker timePicker;
        private Button button;
        private AlarmDBHelper dbHelper = new AlarmDBHelper(this);


        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            final Intent prevIntent = getIntent(); // gets the previously created intent
            final String alarmName = prevIntent.getStringExtra(SetName.ALARM_NAME);//TODO double check that this is ok to do
            System.out.println("from the time picker the alarm name is: "+alarmName);
            final int reminderType = prevIntent.getIntExtra(AlarmFrequency.REMINDER_TYPE, -1);
            System.out.println("from the time picker the remindertype is: "+reminderType);

            final Context context = this;
            setContentView(R.layout.time_picker);
            setCurrentTimeOnView();
            button = (Button) findViewById(R.id.done);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlarmModel alarmModel;
                    if(true){//TODO later change this to if alarmModel with this name DOES NOT already exists in the db
                        alarmModel = new AlarmModel(alarmName);
                        alarmModel.setId(System.currentTimeMillis());//TODO check that adding an id in this way is effective
                    }else{
                        //TODO implement this later
                    }
                    //long reminder_id = System.currentTimeMillis();TODO removed id from constructor, added ny db entry
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();

                    //TODO refactor to case statement later
                    if(reminderType == ReminderTime.ONE_TIME) {
                        System.out.println("from the time picker the remindertype is: "+reminderType);
                        int year = prevIntent.getIntExtra(Datepicker.DATE_YEAR, -1);
                        int month = prevIntent.getIntExtra(Datepicker.DATE_MONTH, -1);
                        int day = prevIntent.getIntExtra(Datepicker.DATE_DAY, -1);

                        //TODO check that hour is returned in 0-23
                        System.out.println("time picker's current hour is: " +timePicker.getCurrentHour());
                        System.out.println("time picker's current month is: " +month);

                        ReminderTime oneTime = new OneTimeReminder(year, month, day, hour, minute);

                        alarmModel.addReminder(oneTime);//make new time

                        System.out.println("next reminder time: " +alarmModel.getNextReminderTime());
                        System.out.println("now time: " +Calendar.getInstance().getTimeInMillis());
                        System.out.println("createAlarm");
                        System.out.println("setAlarms");
                        dbHelper.createAlarm(alarmModel);//add to db TODO put back
                        AlarmManagerHelper.setAlarms(context);//trigger setting alarm TODO put back

                    }else if(reminderType == ReminderTime.DAILY){
                        ReminderTime daily = new DailyReminder(hour, minute);

                        alarmModel.addReminder(daily);//make new time

                        dbHelper.createAlarm(alarmModel);//add to db
                        AlarmManagerHelper.setAlarms(context);//trigger setting alarm
                    }else if(reminderType == ReminderTime.WEEKLY){
                        boolean[] weekdays = prevIntent.getBooleanArrayExtra(AlarmDaysOfWeek.WEEKDAY_ARRAY);
                        ReminderTime weekly = new WeeklyReminder(hour, minute, weekdays);

                        alarmModel.addReminder(weekly);//make new time

                        dbHelper.createAlarm(alarmModel);//add to db
                        AlarmManagerHelper.setAlarms(context);//trigger setting alarm
                    }else if(reminderType == ReminderTime.MONTHLY){

                        boolean[] weekdays = prevIntent.getBooleanArrayExtra(AlarmDaysOfWeek.WEEKDAY_ARRAY);
                        int weekNumber = prevIntent.getIntExtra(AlarmMonthly.WEEK_NUMBER,-1);
                        ReminderTime monthly = new MonthlyReminder(hour, minute, weekNumber, weekdays);

                        alarmModel.addReminder(monthly);//make new time

                        dbHelper.createAlarm(alarmModel);//add to db
                        AlarmManagerHelper.setAlarms(context);//trigger setting alarm
                    }else{
                        System.out.println("reminder type seems not to be recognized");
                    }

                    Intent i = new Intent(Timepicker.this, AlarmListActivity.class);
                    startActivity(i);
                }
            });
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


