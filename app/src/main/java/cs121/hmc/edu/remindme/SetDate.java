package cs121.hmc.edu.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/26/15.
 */
public class SetDate extends ActionBarActivity {
    private DatePicker datePicker;
    private Button button;

    //keys for passing m,d,y date between activtites
    public final static String DATE_YEAR = "date_year";
    public final static String DATE_MONTH = "date_month";
    public final static String DATE_DAY = "date_day";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker);
        setCurrentDate();

        Intent prevIntent = getIntent(); // gets the previously created intent
        final String alarmTone = prevIntent.getStringExtra(SetAlarmInfo.ALARM_TONE);
        final String alarmName = prevIntent.getStringExtra(SetAlarmInfo.ALARM_NAME);
        final int reminderType = prevIntent.getIntExtra(SetFrequency.REMINDER_TYPE, -1);
        final boolean existingModel = prevIntent.getBooleanExtra(ReminderListActivity.EXISTING_MODEL, false);
        final long existingModelId = prevIntent.getLongExtra(ReminderListActivity.EXISTING_MODEL_ID, -1);
        final String alarm_tone = prevIntent.getStringExtra(ReminderListActivity.ALARM_TONE);
        final int minBetweenSnooze = prevIntent.getIntExtra(ReminderListActivity.MIN_BETWEEN_SNOOZE, ReminderTime.DEFAULT_MIN_BETWEEN_SNOOZE);

        button = (Button) findViewById(R.id.done);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SetDate.this, SetTime.class);
                    i.putExtra(SetAlarmInfo.ALARM_TONE, alarmTone);
                    System.out.println(alarmTone);
                    i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
                    i.putExtra(SetFrequency.REMINDER_TYPE, reminderType);
                    i.putExtra(ReminderListActivity.EXISTING_MODEL, existingModel);
                    i.putExtra(ReminderListActivity.EXISTING_MODEL_ID, existingModelId);
                    i.putExtra(ReminderListActivity.MIN_BETWEEN_SNOOZE, minBetweenSnooze);
                    i.putExtra(ReminderListActivity.ALARM_TONE, alarm_tone);
                    i.putExtra(DATE_YEAR, datePicker.getYear());
                    //month starts counting at 0 so must add 1
                    int month = datePicker.getMonth()+1;
                    i.putExtra(DATE_MONTH, month);
                    i.putExtra(DATE_DAY, datePicker.getDayOfMonth());
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
                Intent intent = new Intent(this, AlarmListActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void setCurrentDate(){
        datePicker = (DatePicker) findViewById(R.id.date);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year,month,day);


    }
}
