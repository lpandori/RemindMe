package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditWeekly extends Activity {
    Context mContext = this;
    AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_weekly);
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final CustomSwitch sunday = (CustomSwitch) findViewById(R.id.alarm_details_label_sunday);
        final CustomSwitch monday = (CustomSwitch) findViewById(R.id.alarm_details_label_monday);
        final CustomSwitch tuesday = (CustomSwitch) findViewById(R.id.alarm_details_label_tuesday);
        final CustomSwitch wednesday = (CustomSwitch) findViewById(R.id.alarm_details_label_wednesday);
        final CustomSwitch thursday = (CustomSwitch) findViewById(R.id.alarm_details_label_thursday);
        final CustomSwitch friday = (CustomSwitch) findViewById(R.id.alarm_details_label_friday);
        final CustomSwitch saturday = (CustomSwitch) findViewById(R.id.alarm_details_label_saturday);

        Intent thisIntent = getIntent();
        final int hour = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_HOUR, -1);
        final int minute = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(AlarmDetailsActivity.ALARM_NAME);
        final long id = thisIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);
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

                ReminderTime weekly = new WeeklyReminder(h,m,weekdays);
                weekly.setId(reminderId);
                AlarmManagerHelper.cancelAlarms(mContext);
                dbHelper.updateReminder(weekly, id);
                AlarmManagerHelper.setAlarms(mContext);

                Intent i = new Intent(EditWeekly.this, AlarmDetailsActivity.class);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, id);
                startActivity(i);
            }
        });

    }
}
