package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class AlarmFrequency extends ActionBarActivity {

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
        final String alarmName = thisIntent.getStringExtra(SetName.ALARM_NAME);
        final int snoozeTime = thisIntent.getIntExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, ReminderTime.DEFAULT_MIN_BETWEEN_SNOOZE);
        final boolean existingModel = thisIntent.getBooleanExtra(AlarmDetailsActivity.EXISTING_MODEL, false);
        final long existingModelId = thisIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);



        //create a ReminderTime based on which was clicked and pass it as
        once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, Datepicker.class);

                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL, existingModel);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.ONE_TIME);
                i.putExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, snoozeTime);

                startActivity(i);
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, Timepicker.class);
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL, existingModel);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.DAILY);
                i.putExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, snoozeTime);
                startActivity(i);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, AlarmDaysOfWeek.class);
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL, existingModel);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.WEEKLY);
                i.putExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, snoozeTime);
                startActivity(i);
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmFrequency.this, AlarmMonthly.class);
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL, existingModel);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.MONTHLY);
                i.putExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, snoozeTime);
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

}
