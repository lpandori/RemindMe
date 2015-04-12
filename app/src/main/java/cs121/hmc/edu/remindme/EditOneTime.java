package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditOneTime extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_one_time);
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        //alarmName.setText();
        //timePicker.setCurrentHour();
        //timePicker.setCurrentMinute();
        //datePicker.
    }
}
