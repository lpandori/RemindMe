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
 * Class: EditDaily.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: EditDaily allows a user to change the time of an alarm that goes off every day.
 * It also displays a timePicker populated with the previously set time.
 *
 */

public class EditDaily extends ActionBarActivity {
    public static long id;
    private Context mContext = this;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gets the alarm details
        setContentView(R.layout.edit_daily);
        Intent thisIntent = getIntent();
        int hour = thisIntent.getIntExtra(ReminderListActivity.ALARM_HOUR, -1);
        int minute = thisIntent.getIntExtra(ReminderListActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(ReminderListActivity.ALARM_NAME);
        final String alarm_tone = thisIntent.getStringExtra(ReminderListActivity.ALARM_TONE);
        final long id = thisIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(ReminderListActivity.REMINDER_ID, 7);

        //displays the alarm details as they currently are before edits
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        alarmName.setText(name);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        //when the done button is clicked, get the new hour and minute from
        //the timePicker. create a new remindertime with the same reminderId
        //and the new time, and cancel the remindertime with the old time.
        Button doneEditing = (Button) findViewById(R.id.btn_done);
        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                ReminderTime daily = new ReminderDaily(h,m);
                daily.setId(reminderId);
                AlarmModel alarm = dbHelper.getAlarm(id);
                alarm.replaceReminder(daily);//replace existing reminder
                dbHelper.updateAlarm(alarm);

                //start the reminderListActivity to show a list of all the different time
                //frequencies/remindertimes under the alarm. Pass the alarm name, modelid,
                //and tone in as extras so they can be accessed in future activities.
                Intent i = new Intent(EditDaily.this, ReminderListActivity.class);
                i.putExtra(ReminderListActivity.ALARM_NAME, name);
                i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
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
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
