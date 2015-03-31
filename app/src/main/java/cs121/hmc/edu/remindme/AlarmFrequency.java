package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;


/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class AlarmFrequency extends Activity{

    public static String REMINDER_TYPE = "reminder_type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_frequency);

        Button once = (Button) findViewById(R.id.one_time);
        Button daily = (Button) findViewById(R.id.daily);
        Button weekly = (Button) findViewById(R.id.weekly);
        Button monthly = (Button) findViewById(R.id.monthly);

        Intent thisIntent = getIntent(); // gets the previously created intent
        final String alarmName = thisIntent.getStringExtra(SetName.ALARM_NAME);//TODO double check that this is ok to do
        System.out.println("from the alarm frequency the alarm name is: "+alarmName);

        System.out.println(alarmName);

        //create a ReminderTime based on which was clicked and pass it as
        once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, Datepicker.class);
                System.out.println("chose one time from alarm frequency");
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(REMINDER_TYPE, ReminderTime.ONE_TIME);
                startActivity(i);
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, Timepicker.class);
                System.out.println("chose daily time from alarm frequency");
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(REMINDER_TYPE, ReminderTime.DAILY);
                startActivity(i);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, AlarmDaysOfWeek.class);
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(REMINDER_TYPE, ReminderTime.WEEKLY);
                startActivity(i);
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, AlarmMonthly.class);
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(REMINDER_TYPE, ReminderTime.MONTHLY);
                startActivity(i);
            }
        });

    }

}
