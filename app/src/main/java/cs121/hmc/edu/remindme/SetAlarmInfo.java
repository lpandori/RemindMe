package cs121.hmc.edu.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by rachelleholmgren on 3/12/15.
 */

public class SetAlarmInfo extends ActionBarActivity {
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private AlarmModel alarmModel = new AlarmModel("");
    public static String ALARM_NAME = "name";//used to access the alarm name in SetFrequency screen
    public static String SNOOZE_TIME = "snooze_time";
    public static String ALARM_TONE = "alarm_tone";
    public Uri ringtone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name);
        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetAlarmInfo.this, SetFrequency.class);
                EditText snooze = (EditText) findViewById(R.id.snoozeTime);
                snooze.setInputType(InputType.TYPE_CLASS_NUMBER);
                EditText name = (EditText)findViewById(R.id.setName);
                i.putExtra("prevActivity", "SetAlarmInfo");
                i.putExtra(ALARM_NAME, name.getText().toString());
                i.putExtra(ALARM_TONE, ringtone.toString());
                try {
                    i.putExtra(MainActivity.MIN_BETWEEN_SNOOZE, Integer.valueOf(snooze.getText().toString()));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent, 1);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1: {
                      ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                      TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
                      txtToneSelection.setText(RingtoneManager.getRingtone(this, ringtone).getTitle(this));
                    break;
                }
                default: {
                    break;
                }
            }
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SetAlarmInfo.this);
        // Setting Dialog Title
        alertDialog.setTitle("Confirm:");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to stop creating a new reminder?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(SetAlarmInfo.this, AlarmOverviewActivity.class);
//                        i.putExtra(SetAlarmInfo.ALARM_NAME, alarmName);
//                        i.putExtra(MainActivity.EXISTING_MODEL_ID, existingModelId);
//                        i.putExtra(MainActivity.ALARM_TONE, alarmTone);
                        startActivity(i);
                    }
                });
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
