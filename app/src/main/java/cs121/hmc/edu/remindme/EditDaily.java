package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by rachelleholmgren on 4/12/15.
 */
public class EditDaily extends ActionBarActivity {
    public static long id;
    private Context mContext = this;
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_daily);
        Intent thisIntent = getIntent();
        int hour = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_HOUR, -1);
        int minute = thisIntent.getIntExtra(AlarmDetailsActivity.ALARM_MINUTE, -1);
        final String name = thisIntent.getStringExtra(AlarmDetailsActivity.ALARM_NAME);
        final long id = thisIntent.getLongExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, -1);
        final long reminderId = thisIntent.getLongExtra(AlarmDetailsActivity.REMINDER_ID, 7);


        TextView alarmName = (TextView) findViewById(R.id.editBlank);
        alarmName.setText(name);

        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        Button doneEditing = (Button) findViewById(R.id.btn_done);
        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int h = timePicker.getCurrentHour();
                int m = timePicker.getCurrentMinute();
                ReminderTime daily = new DailyReminder(h,m);
                daily.setId(reminderId);
                AlarmManagerHelper.cancelAlarms(mContext);
                dbHelper.updateReminder(daily, id);
                AlarmManagerHelper.setAlarms(mContext);

                Intent i = new Intent(EditDaily.this, AlarmDetailsActivity.class);
                i.putExtra(AlarmDetailsActivity.ALARM_NAME, name);
                i.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, id);
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
                Intent intent = new Intent(this, AlarmDetailsActivity.class);
                intent.putExtra(AlarmDetailsActivity.EXISTING_MODEL_ID, id);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
