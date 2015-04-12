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


    public static String WEEKDAY_ARRAY= "weekday_array";


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
        final String alarmName = prevIntent.getStringExtra(SetName.ALARM_NAME);//TODO double check that this is ok to do
        final int reminderType = prevIntent.getIntExtra(AlarmFrequency.REMINDER_TYPE, -1);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(reminderType == ReminderTime.WEEKLY) {
                   boolean[] weekdays = new boolean[7];//boolean string i.e "0011010"
                   int x = 0;
                   for(CustomSwitch c: customSwitches){
                       weekdays[x] = c.isChecked();
                       x++;
                   }

                   Intent i = new Intent(AlarmDaysOfWeek.this, Timepicker.class);
                   i.putExtra(SetName.ALARM_NAME, alarmName);
                   i.putExtra(AlarmFrequency.REMINDER_TYPE, reminderType);
                   i.putExtra(WEEKDAY_ARRAY, weekdays);
                   startActivity(i);
               }else{
                   System.out.println("Weekly reminder seems not to be recognized");
               }
            }
        });

    }
}
