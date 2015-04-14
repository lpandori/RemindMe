package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditWeekly extends ActionBarActivity {
    public static long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_weekly);
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        CustomSwitch sunday = (CustomSwitch) findViewById(R.id.alarm_details_label_sunday);
        CustomSwitch monday = (CustomSwitch) findViewById(R.id.alarm_details_label_monday);
        CustomSwitch tuesday = (CustomSwitch) findViewById(R.id.alarm_details_label_tuesday);
        CustomSwitch wednesday = (CustomSwitch) findViewById(R.id.alarm_details_label_wednesday);
        CustomSwitch thursday = (CustomSwitch) findViewById(R.id.alarm_details_label_thursday);
        CustomSwitch friday = (CustomSwitch) findViewById(R.id.alarm_details_label_friday);
        CustomSwitch saturday = (CustomSwitch) findViewById(R.id.alarm_details_label_saturday);

        Intent thisIntent = getIntent();
        final int hour = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_HOUR, -1);
        final int minute = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(AlarmDetailsActivity.ALARM_NAME);
        id = thisIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(AlarmDetailsActivity.REMINDER_ID, -1);
        final String whichdays = thisIntent.getStringExtra(AlarmDetailsActivity.WEEKDAYS);



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
                Intent intent = new Intent(this, AlarmDetailsActivity.class);
                intent.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, id);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }
}

