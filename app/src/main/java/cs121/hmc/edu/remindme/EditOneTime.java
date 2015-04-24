package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditOneTime extends ActionBarActivity {
    public static long id = -1;
    Context mContext = this;
    AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_one_time);
        Intent thisIntent = getIntent();

        TextView alarmName = (TextView) findViewById(R.id.editBlank);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        int hour = thisIntent.getIntExtra(MainActivity.ALARM_HOUR, -1);
        int minute = thisIntent.getIntExtra(MainActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(MainActivity.ALARM_NAME);

        id = thisIntent.getLongExtra(MainActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(MainActivity.REMINDER_ID, -1);
        final String dString = thisIntent.getStringExtra(MainActivity.ALARM_DATE);

        int year = Integer.parseInt(dString.substring(0,4));
        int month = Integer.parseInt(dString.substring(5,7));
        int day = Integer.parseInt(dString.substring(8,10));

        alarmName.setText(name);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        datePicker.updateDate(year, month-1, day);

        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                int yr = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();

                ReminderTime oneTime = new ReminderOneTime(yr,month,day,h,m);
                oneTime.setId(reminderId);
                AlarmManagerHelper.cancelAlarms(mContext);
                dbHelper.updateReminder(oneTime, id);
                AlarmManagerHelper.setAlarms(mContext);

                Intent i = new Intent(EditOneTime.this, MainActivity.class);

                i.putExtra(MainActivity.ALARM_NAME, name);
                i.putExtra(MainActivity.EXISTING_MODEL_ID, id);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_button: {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.EXISTING_MODEL_ID, id);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);

    }
}

