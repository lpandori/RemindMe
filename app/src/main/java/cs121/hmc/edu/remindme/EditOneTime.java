package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Class: EditOneTime.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: EditOneTime allows a user to change the time and date
 * that a one-time reminderTime within an alarm goes off.
 *
 */
public class EditOneTime extends ActionBarActivity {
    public static long id = -1;
    Context mContext = this;
    AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_one_time);

        //get the alarm details previously set for this one time remindertime
        Intent thisIntent = getIntent();

        TextView alarmName = (TextView) findViewById(R.id.editBlank);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        int hour = thisIntent.getIntExtra(ReminderListActivity.ALARM_HOUR, -1);
        int minute = thisIntent.getIntExtra(ReminderListActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(ReminderListActivity.ALARM_NAME);

        id = thisIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(ReminderListActivity.REMINDER_ID, -1);
        final String dString = thisIntent.getStringExtra(ReminderListActivity.ALARM_DATE);

        int year = Integer.parseInt(dString.substring(0,4));
        int month = Integer.parseInt(dString.substring(5,7));
        int day = Integer.parseInt(dString.substring(8,10));

        //populate screen with the old alarm details for easy and fast editing
        alarmName.setText(name);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        datePicker.updateDate(year, month-1, day);


        //on done, get the information from the datepicker and timepicker.
        //create a new reminder time with this information and the old reminderid
        //and delete the old reminder time. Then start the reminderlist activity
        //to show all the remindertimes set under this alarm.
        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                int yr = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();

                ReminderTime oneTime = new ReminderOneTime(yr,month,day,h,m);
                oneTime.setId(reminderId);
                AlarmManagerHelper.cancelAlarms(mContext);
                dbHelper.updateReminder(oneTime, id);
                AlarmManagerHelper.setAlarms(mContext);

                Intent i = new Intent(EditOneTime.this, ReminderListActivity.class);
                i.putExtra(ReminderListActivity.ALARM_NAME, name);
                i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
                startActivity(i);
            }
        });
    }

    //inflate the action bar to contain a cancel icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when the cancel icon is clicked, go back to the list of remindertimes for
    //that alarm
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_button: {
                Intent intent = new Intent(this, ReminderListActivity.class);
                intent.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }
}

