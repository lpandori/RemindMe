package cs121.hmc.edu.remindme;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                false);
        //to switch to a 24 hour view, just switch boolean value to true
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        Intent intent = new Intent(getActivity(),AlarmListActivity.class);
        startActivity(intent);
    }


}
