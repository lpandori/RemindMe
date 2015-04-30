package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Class: EditMonthly.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: EditMonthly allows a user to change the time, the week of the month,
 * and the day of the week that a monthly reminderTime within an alarm goes off.
 */


public class EditMonthly extends ActionBarActivity {
    public static long id;
    Context mContext = this;
    AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_monthly);

        //get the alarm details previously set for this monthly remindertime, and populate
        //the timepicker, and dropdown menus(Spinners) with this information.
        Intent thisIntent = getIntent();
        int hour = thisIntent.getIntExtra(ReminderListActivity.ALARM_HOUR, -1);
        int minute = thisIntent.getIntExtra(ReminderListActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(ReminderListActivity.ALARM_NAME);
        id = thisIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(ReminderListActivity.REMINDER_ID, -1);
        final int weekNumber = thisIntent.getIntExtra(ReminderListActivity.WEEK_OF_MONTH,-1);
        final String whichdays = thisIntent.getStringExtra(ReminderListActivity.WEEKDAYS);

        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        final Spinner whichWeek = (Spinner) findViewById(R.id.which_week);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.which_week_options,
                        R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whichWeek.setAdapter(adapter);

        final Spinner whichDay = (Spinner) findViewById(R.id.day_of_week);
        ArrayAdapter<CharSequence> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.day_options,
                        R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whichDay.setAdapter(adapter2);

        int position = -1;

        if(whichdays.substring(0,1).equals("1")){
            position = 0;
        }else if(whichdays.substring(1,2).equals("1")){
            position = 1;
        }else if(whichdays.substring(2,3).equals("1")){
            position = 2;
        }else if(whichdays.substring(3,4).equals("1")){
            position = 3;
        }else if(whichdays.substring(4,5).equals("1")){
            position = 4;
        }else if(whichdays.substring(5,6).equals("1")){
            position = 5;
        }else if(whichdays.substring(6,7).equals("1")){
            position = 6;
        }

        alarmName.setText(name);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        whichWeek.setSelection(weekNumber-1);
        whichDay.setSelection(position);

        //when the user clicks done, get the alarm details in the timepicker,
        //and two dropdown menus (Scrollers). Create a new monthly alarm
        //with the new edited information and the same reminderId, and cancel
        //the old reminder. Then go back to the list of remindertimes
        //for that alarm
        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                boolean[] weekdays = {false,false,false,false,false,false,false};
                weekdays[whichDay.getSelectedItemPosition()] = true;
                int weekNumber = whichWeek.getSelectedItemPosition()+1;

                ReminderTime monthly = new ReminderMonthly(h, m, weekNumber, weekdays);
                monthly.setId(reminderId);
                AlarmModel alarm = dbHelper.getAlarm(id);
                alarm.replaceReminder(monthly);
                dbHelper.updateAlarm(alarm);

                //start the reminderlist activity and pass in the alarm name and
                //model id for use in future activities
                Intent i = new Intent(EditMonthly.this, ReminderListActivity.class);
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

