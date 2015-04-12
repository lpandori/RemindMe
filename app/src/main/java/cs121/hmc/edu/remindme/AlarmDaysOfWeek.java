package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class AlarmDaysOfWeek extends Activity {

    //maybe we need to create a list of booleans for what days
    //of the week are chosen
    AlarmFrequency alarmfreq = new AlarmFrequency();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.set_days);
        super.onCreate(savedInstanceState);

        final CustomSwitch sunday = (CustomSwitch) findViewById(R.id.Sunday);
        final CustomSwitch monday = (CustomSwitch) findViewById(R.id.Monday);
        final CustomSwitch tuesday = (CustomSwitch) findViewById(R.id.Tuesday);
        final CustomSwitch wednesday = (CustomSwitch) findViewById(R.id.Wednesday);
        final CustomSwitch thursday = (CustomSwitch) findViewById(R.id.Thursday);
        final CustomSwitch friday = (CustomSwitch) findViewById(R.id.Friday);
        final CustomSwitch saturday = (CustomSwitch) findViewById(R.id.Saturday);
        ArrayList<CustomSwitch> customSwitches = new ArrayList<>();
        customSwitches.add(sunday);
        customSwitches.add(monday);
        customSwitches.add(tuesday);
        customSwitches.add(wednesday);
        customSwitches.add(thursday);
        customSwitches.add(friday);
        customSwitches.add(saturday);
        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmDaysOfWeek.this, Timepicker.class);
                //for each checkbox that .isChecked() pass it in an extra as
                //true. else pass it in an extra as false
                startActivity(i);

            }
        });

    }
}
