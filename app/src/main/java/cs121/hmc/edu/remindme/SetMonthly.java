package cs121.hmc.edu.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by rachelleholmgren on 3/7/15.
 */
public class SetMonthly extends ActionBarActivity {

    public static final String WEEK_NUMBER = "week_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_month);

        final Spinner whichWeek = (Spinner) findViewById(R.id.which_week);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.which_week_options,
                R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whichWeek.setAdapter(adapter);

        final Spinner whichDay = (Spinner) findViewById(R.id.day_of_week);
        ArrayAdapter<CharSequence> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.day_options,
                        R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whichDay.setAdapter(adapter2);

        final Intent prevIntent = getIntent(); // gets the previously created intent
        final String alarmName = prevIntent.getStringExtra(SetAlarmInfo.ALARM_NAME);
        final String alarmTone = prevIntent.getStringExtra(MainActivity.ALARM_TONE);
        final int reminderType = prevIntent.getIntExtra(SetFrequency.REMINDER_TYPE, -1);
        final boolean existingModel = prevIntent.getBooleanExtra(MainActivity.EXISTING_MODEL, false);
        final long existingModelId = prevIntent.getLongExtra(MainActivity.EXISTING_MODEL_ID, -1);

        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //make array of weekdays to show which was selected
                    boolean[] weekdays = {false,false,false,false,false,false,false};
                    weekdays[whichDay.getSelectedItemPosition()] = true;

                    Intent i = new Intent(SetMonthly.this, SetTime.class);
                    i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
                    i.putExtra(MainActivity.ALARM_TONE, alarmTone);
                    i.putExtra(SetFrequency.REMINDER_TYPE, reminderType);
                    i.putExtra(MainActivity.EXISTING_MODEL, existingModel);
                    i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
                    i.putExtra(SetWeekly.WEEKDAY_ARRAY, weekdays);
                    i.putExtra(WEEK_NUMBER, whichWeek.getSelectedItemPosition()+1);
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
                Intent intent = new Intent(this, AlarmOverviewActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
