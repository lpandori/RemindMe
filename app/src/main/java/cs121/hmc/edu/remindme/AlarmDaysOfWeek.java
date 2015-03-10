package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class AlarmDaysOfWeek extends Activity {

    AlarmFrequency alarmfreq = new AlarmFrequency();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.set_days);
        super.onCreate(savedInstanceState);
        final CheckBox sunday = (CheckBox) findViewById(R.id.Sunday);
        final CheckBox monday = (CheckBox) findViewById(R.id.Monday);
        final CheckBox tuesday = (CheckBox) findViewById(R.id.Tuesday);
        final CheckBox wednesday = (CheckBox) findViewById(R.id.Wednesday);
        final CheckBox thursday = (CheckBox) findViewById(R.id.Thursday);
        final CheckBox friday = (CheckBox) findViewById(R.id.Friday);
        final CheckBox saturday = (CheckBox) findViewById(R.id.Saturday);
        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showTimePickerDialog(view);

            }
        });
        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    sunday.setBackgroundColor(Color.GREEN);
                }
                else{
                    sunday.setBackgroundColor(Color.WHITE);
                }
            }
        });
        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    monday.setBackgroundColor(Color.GREEN);
                }
                else{
                    monday.setBackgroundColor(Color.WHITE);
                }
            }
        });
        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    tuesday.setBackgroundColor(Color.GREEN);
                }
                else{
                    tuesday.setBackgroundColor(Color.WHITE);
                }
            }
        });
        wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    wednesday.setBackgroundColor(Color.GREEN);
                }
                else{
                    wednesday.setBackgroundColor(Color.WHITE);
                }
            }
        });
        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    thursday.setBackgroundColor(Color.GREEN);
                } else {
                    thursday.setBackgroundColor(Color.WHITE);
                }
            }
        });
        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    friday.setBackgroundColor(Color.GREEN);
                }
                else{
                    friday.setBackgroundColor(Color.WHITE);
                }
            }
        });
        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    saturday.setBackgroundColor(Color.GREEN);
                }
                else{
                    saturday.setBackgroundColor(Color.WHITE);
                }
            }
        });


    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
}