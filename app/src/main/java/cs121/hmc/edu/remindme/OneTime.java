package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by rachelleholmgren on 3/20/15.
 */
public class OneTime extends Activity {
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_time);
        ImageButton calendarbutton = (ImageButton) findViewById(R.id.calendarbutton);
        calendarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar();

            }
        });
    }


    public void showCalendar(){
        DialogFragment newFragment = CalendarDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "dialog");

    }


//    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
//        Toast.makeText(getApplicationContext(),hourOfDay+":" + minute,Toast.LENGTH_LONG).show();
//
//    }
}
