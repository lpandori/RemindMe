package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.os.Bundle;
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
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        Spinner whichWeek = (Spinner) findViewById(R.id.which_week);
        Spinner whichDay = (Spinner) findViewById(R.id.day_of_week);
    }
}
