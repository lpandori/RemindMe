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
 * Created by rachelleholmgren on 4/12/15.
 * EditDaily allows a user to change the time of an alarm that goes off every day.
 */
public class EditDaily extends ActionBarActivity {
    public static long id = -1;
    private Context mContext = this;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_daily);
        Intent thisIntent = getIntent();

        TextView alarmName = (TextView) findViewById(R.id.editBlank);

        setContentView(R.layout.edit_daily);
        int hour = thisIntent.getIntExtra(ReminderListActivity.ALARM_HOUR, -1);
        int minute = thisIntent.getIntExtra(ReminderListActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(ReminderListActivity.ALARM_NAME);
        final String alarm_tone = thisIntent.getStringExtra(ReminderListActivity.ALARM_TONE);
        final long id = thisIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(ReminderListActivity.REMINDER_ID, 7);






        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        alarmName.setText(name);

        //when the done button is clicked,
        Button doneEditing = (Button) findViewById(R.id.btn_done);
        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                ReminderTime daily = new ReminderDaily(h,m);
                daily.setId(reminderId);
                AlarmManagerHelper.cancelAlarms(mContext);
                dbHelper.updateReminder(daily, id);
                AlarmManagerHelper.setAlarms(mContext);

                Intent i = new Intent(EditDaily.this, ReminderListActivity.class);
                i.putExtra(ReminderListActivity.ALARM_NAME, name);
                i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
                i.putExtra(ReminderListActivity.ALARM_TONE, alarm_tone);
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
                Intent intent = new Intent(this, ReminderListActivity.class);
                intent.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
                startActivity(intent);
                break;
            }
        }
                return super.onOptionsItemSelected(item);
        }

}
