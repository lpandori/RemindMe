package cs121.hmc.edu.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by rachelleholmgren on 3/5/15.
 */
public class SetFrequency extends ActionBarActivity {

    public static String REMINDER_TYPE = "reminder_type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_frequency);

        Button once = (Button) findViewById(R.id.one_time);
        Button daily = (Button) findViewById(R.id.daily);
        Button weekly = (Button) findViewById(R.id.weekly);
        Button monthly = (Button) findViewById(R.id.monthly);

        Intent thisIntent = getIntent(); // gets the previously created intent
        final String alarmName = thisIntent.getStringExtra(SetAlarmInfo.ALARM_NAME);
        final int snoozeTime = thisIntent.getIntExtra(MainActivity.MIN_BETWEEN_SNOOZE, ReminderTime.DEFAULT_MIN_BETWEEN_SNOOZE);
        final boolean existingModel = thisIntent.getBooleanExtra(MainActivity.EXISTING_MODEL, false);
        final long existingModelId = thisIntent.getLongExtra(MainActivity.EXISTING_MODEL_ID, -1);
        final String alarmTone = thisIntent.getStringExtra(MainActivity.ALARM_TONE);


        //create a ReminderTime based on which was clicked and pass it as
        once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetFrequency.this, SetDate.class);
                i.putExtra(SetAlarmInfo.ALARM_TONE, alarmTone);
                System.out.println(alarmTone);
                i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
                i.putExtra(MainActivity.EXISTING_MODEL, existingModel);
                i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.ONE_TIME);
                i.putExtra(MainActivity.MIN_BETWEEN_SNOOZE, snoozeTime);

                startActivity(i);
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetFrequency.this, SetTime.class);
                i.putExtra(SetAlarmInfo.ALARM_TONE, alarmTone);
                i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
                i.putExtra(MainActivity.EXISTING_MODEL, existingModel);
                i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.DAILY);
                i.putExtra(MainActivity.MIN_BETWEEN_SNOOZE, snoozeTime);
                startActivity(i);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetFrequency.this, SetWeekly.class);
                i.putExtra(SetAlarmInfo.ALARM_TONE, alarmTone);
                i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
                i.putExtra(MainActivity.EXISTING_MODEL, existingModel);
                i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.WEEKLY);
                i.putExtra(MainActivity.MIN_BETWEEN_SNOOZE, snoozeTime);
                startActivity(i);
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetFrequency.this, SetMonthly.class);
                i.putExtra(SetAlarmInfo.ALARM_TONE, alarmTone);
                i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
                i.putExtra(MainActivity.EXISTING_MODEL, existingModel);
                i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
                i.putExtra(REMINDER_TYPE, ReminderTime.MONTHLY);
                i.putExtra(MainActivity.MIN_BETWEEN_SNOOZE, snoozeTime);
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

    @Override
    public void onBackPressed() {

        //Display alert message when back button has been pressed
        backButtonHandler();
    }

    public void backButtonHandler() {
        Intent thisIntent = getIntent(); // gets the previously created intent
        final String alarmName = thisIntent.getStringExtra(SetAlarmInfo.ALARM_NAME);
        final long existingModelId = thisIntent.getLongExtra(MainActivity.EXISTING_MODEL_ID, -1);
        final String alarmTone = thisIntent.getStringExtra(MainActivity.ALARM_TONE);
        final String prevActivity = thisIntent.getStringExtra("prevActivity");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SetFrequency.this);
        // Setting Dialog Title
        alertDialog.setTitle("Confirm:");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to stop creating a new reminder?");
        // Setting Positive "Yes" Button
        if (prevActivity.equals("SetAlarmInfo")) {
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(SetFrequency.this, AlarmOverviewActivity.class);
                            startActivity(i);
                        }
                    });
        }
        else if (prevActivity.equals("AlarmDetails")) {
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(SetFrequency.this, MainActivity.class);
                            i.putExtra(MainActivity.ALARM_NAME, alarmName);
                            i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
                            i.putExtra(MainActivity.ALARM_TONE, alarmTone);
                            startActivity(i);
                            startActivity(i);
                        }
                    });

        }
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

}
