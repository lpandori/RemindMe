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
                    boolean[] weekdays;

                    System.out.println(reminderType);

                    switch(reminderType){
                        case ReminderTime.ONE_TIME:

                            int year = prevIntent.getIntExtra(Datepicker.DATE_YEAR, -1);
                            int month = prevIntent.getIntExtra(Datepicker.DATE_MONTH, -1);
                            int day = prevIntent.getIntExtra(Datepicker.DATE_DAY, -1);

                            ReminderTime oneTime = new OneTimeReminder(year, month, day, hour, minute);
                            alarmModel.addReminder(oneTime);//make new time
                            break;
                        case ReminderTime.DAILY:
                            ReminderTime daily = new DailyReminder(hour, minute);
                            alarmModel.addReminder(daily);//make new time

                            break;
                        case ReminderTime.WEEKLY:
                            weekdays = prevIntent.getBooleanArrayExtra(AlarmDaysOfWeek.WEEKDAY_ARRAY);
                            ReminderTime weekly = new WeeklyReminder(hour, minute, weekdays);

                            alarmModel.addReminder(weekly);//make new time
                            break;
                        case ReminderTime.MONTHLY:
                            weekdays = prevIntent.getBooleanArrayExtra(AlarmDaysOfWeek.WEEKDAY_ARRAY);
                            int weekNumber = prevIntent.getIntExtra(AlarmMonthly.WEEK_NUMBER,-1);
                            ReminderTime monthly = new MonthlyReminder(hour, minute, weekNumber, weekdays);

                            alarmModel.addReminder(monthly);//make new time
                            break;
                    }
                    dbHelper.createAlarm(alarmModel);//add to db
                    AlarmManagerHelper.setAlarms(context);//trigger setting alarm

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


