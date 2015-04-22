package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class AlarmDaysOfWeek extends ActionBarActivity {

    public static String WEEKDAY_ARRAY= "weekday_array";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_days);

        final CustomSwitch sunday = (CustomSwitch) findViewById(R.id.Sunday);
        final CustomSwitch monday = (CustomSwitch) findViewById(R.id.Monday);
        final CustomSwitch tuesday = (CustomSwitch) findViewById(R.id.Tuesday);
        final CustomSwitch wednesday = (CustomSwitch) findViewById(R.id.Wednesday);
        final CustomSwitch thursday = (CustomSwitch) findViewById(R.id.Thursday);
        final CustomSwitch friday = (CustomSwitch) findViewById(R.id.Friday);
        final CustomSwitch saturday = (CustomSwitch) findViewById(R.id.Saturday);
        final ArrayList<CustomSwitch> customSwitches = new ArrayList<>();
        customSwitches.add(sunday);
        customSwitches.add(monday);
        customSwitches.add(tuesday);
        customSwitches.add(wednesday);
        customSwitches.add(thursday);
        customSwitches.add(friday);
        customSwitches.add(saturday);


        Button next = (Button) findViewById(R.id.btn_next);

        //get passed in data
        final Intent prevIntent = getIntent(); //gets the previously created intent
        final String alarmName = prevIntent.getStringExtra(SetName.ALARM_NAME);
        final int reminderType = prevIntent.getIntExtra(AlarmFrequency.REMINDER_TYPE, -1);
        final boolean existingModel = prevIntent.getBooleanExtra(AlarmDetailsActivity.EXISTING_MODEL, false);
        final long existingModelId = prevIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);

        final int minBetweenSnooze = prevIntent.getIntExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, ReminderTime.DEFAULT_MIN_BETWEEN_SNOOZE);

        final String alarmTone = prevIntent.getStringExtra(SetName.ALARM_TONE);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   boolean[] weekdays = new boolean[7];//boolean string i.e "0011010"
                   int x = 0;
                   for(CustomSwitch c: customSwitches){
                       weekdays[x] = c.isChecked();
                       x++;
                   }

                   Intent i = new Intent(AlarmDaysOfWeek.this, Timepicker.class);
                   i.putExtra(SetName.ALARM_NAME, alarmName);
                   i.putExtra(SetName.ALARM_TONE, alarmTone);
                   i.putExtra(AlarmDetailsActivity.EXISTING_MODEL, existingModel);
                   i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, existingModelId);
                   i.putExtra(AlarmDetailsActivity.MIN_BETWEEN_SNOOZE, minBetweenSnooze);
                   i.putExtra(AlarmFrequency.REMINDER_TYPE, reminderType);
                   i.putExtra(WEEKDAY_ARRAY, weekdays);
                   startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
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
