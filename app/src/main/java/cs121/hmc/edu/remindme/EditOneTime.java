package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        Intent thisIntent = getIntent();
        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        final int hour = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_HOUR, -1);
        final int minute = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(AlarmDetailsActivity.ALARM_NAME);
        final long id = thisIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);
        final String dString = thisIntent.getStringExtra(AlarmDetailsActivity.ALARM_DATE);

        int year = Integer.parseInt(dString.substring(0,4));
        int month = Integer.parseInt(dString.substring(5,7));
        int day = Integer.parseInt(dString.substring(8,10));

        alarmName.setText(name);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        datePicker.updateDate(year, month, day);

        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditOneTime.this, AlarmDetailsActivity.class);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, id);
                startActivity(i);
            }
        });
    }
}
