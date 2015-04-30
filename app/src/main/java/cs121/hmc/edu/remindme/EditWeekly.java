package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Class: EditWeekly.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: EditWeekly allows a user to change the days of week and time
 * that a weekly remindertime rings.
 *
 */
public class EditWeekly extends ActionBarActivity {
    public static long id;
    Context mContext = this;
    AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_weekly);
        final TextView alarmName = (TextView) findViewById(R.id.editBlank);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final CustomSwitch sunday = (CustomSwitch) findViewById(R.id.alarm_details_label_sunday);
        final CustomSwitch monday = (CustomSwitch) findViewById(R.id.alarm_details_label_monday);
        final CustomSwitch tuesday = (CustomSwitch) findViewById(R.id.alarm_details_label_tuesday);
        final CustomSwitch wednesday = (CustomSwitch) findViewById(R.id.alarm_details_label_wednesday);
        final CustomSwitch thursday = (CustomSwitch) findViewById(R.id.alarm_details_label_thursday);
        final CustomSwitch friday = (CustomSwitch) findViewById(R.id.alarm_details_label_friday);
        final CustomSwitch saturday = (CustomSwitch) findViewById(R.id.alarm_details_label_saturday);

        //get alarm details previously set for weekly remindertime
        Intent thisIntent = getIntent();
        final int hour = thisIntent.getIntExtra(ReminderListActivity.ALARM_HOUR, -1);
        final int minute = thisIntent.getIntExtra(ReminderListActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(ReminderListActivity.ALARM_NAME);
        final String alarm_tone = thisIntent.getStringExtra(ReminderListActivity.ALARM_TONE);
        id = thisIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(ReminderListActivity.REMINDER_ID, -1);
        final String whichdays = thisIntent.getStringExtra(ReminderListActivity.WEEKDAYS);


        //populate this information on the screen for easy and quick editing
        alarmName.setText(name);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        boolean sun = whichdays.substring(0,1).equals("1");
        boolean mon = whichdays.substring(1,2).equals("1");
        boolean tue =  whichdays.substring(2,3).equals("1");
        boolean wed =  whichdays.substring(3,4).equals("1");
        boolean thu =  whichdays.substring(4,5).equals("1");
        boolean fri =  whichdays.substring(5,6).equals("1");
        boolean sat = whichdays.substring(6,7).equals("1");

        sunday.setChecked(sun);
        monday.setChecked(mon);
        tuesday.setChecked(tue);
        wednesday.setChecked(wed);
        thursday.setChecked(thu);
        friday.setChecked(fri);
        saturday.setChecked(sat);

        //when the user clicks done, get the new time, and days of week for the weekly reminder
        //time to ring. then create a new remindertime with this new info and the old reminderId
        // and delete the old remindertime. Finally start the reminderlist activity
        //to show all the remindertimes set under this alarm.
        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                boolean[] weekdays = {sunday.isChecked(),
                        monday.isChecked(),
                        tuesday.isChecked(),
                        wednesday.isChecked(),
                        thursday.isChecked(),
                        friday.isChecked(),
                        saturday.isChecked()};

                ReminderTime weekly = new ReminderWeekly(h,m,weekdays);
                weekly.setId(reminderId);
                AlarmManagerHelper.cancelAlarms(mContext);
                dbHelper.updateReminder(weekly, id);
                AlarmManagerHelper.setAlarms(mContext);

                Intent i = new Intent(EditWeekly.this, ReminderListActivity.class);
                i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
                i.putExtra(ReminderListActivity.ALARM_NAME, name);
                i.putExtra(ReminderListActivity.ALARM_TONE, alarm_tone);
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

