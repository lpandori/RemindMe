package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditDaily extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_daily);
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        //alarmName.setText();

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        //timePicker.setCurrentHour();
        //timePicker.setCurrentMinute();

    }
}
