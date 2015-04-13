package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditMonthly extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_monthly);

        Intent thisIntent = getIntent();
        final int hour = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_HOUR, -1);
        final int minute = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(AlarmDetailsActivity.ALARM_NAME);
        final long id = thisIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(AlarmDetailsActivity.REMINDER_ID, -1);
        final int weekNumber = thisIntent.getIntExtra(AlarmDetailsActivity.WEEK_OF_MONTH,-1);
        final String whichdays = thisIntent.getStringExtra(AlarmDetailsActivity.WEEKDAYS);

        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

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
        }

        if(whichdays.substring(1,2).equals("1")){
            position = 1;
        }
        if(whichdays.substring(2,3).equals("1")){
            position = 2;
        }
        if(whichdays.substring(3,4).equals("1")){
            position = 3;
        }
        if(whichdays.substring(4,5).equals("1")){
            position = 4;
        }
        if(whichdays.substring(5,6).equals("1")){
            position = 5;
        }
        if(whichdays.substring(6,7).equals("1")){
            position = 6;
        }

        alarmName.setText(name);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        whichWeek.setSelection(weekNumber-1);
        whichDay.setSelection(position);

    }
}
