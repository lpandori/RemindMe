package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/26/15.
 */
public class Timepicker extends Activity {
        private TimePicker timePicker;
        private Button button;
        private int hour;
        private int minute;



        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.time_picker);
            setCurrentTimeOnView();
            button = (Button) findViewById(R.id.done);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Timepicker.this, AlarmListActivity.class);
                    // pass timePicker.getCurrentHour() &&
                    //timePicker.getCurrentMinute() as extras
                    startActivity(i);
                }
            });
            //addButtonListener();
        }
        public void setCurrentTimeOnView(){
            timePicker = (TimePicker) findViewById(R.id.test);
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);


        }
    }


