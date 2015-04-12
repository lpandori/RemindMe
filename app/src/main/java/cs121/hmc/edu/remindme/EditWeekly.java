package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditWeekly extends Activity {
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
        //alarmName.setText();
        //timePicker.setCurrentHour();
        //timePicker.setCurrentMinute();

        //sunday.setChecked();
        //monday.setChecked();
    }
}
