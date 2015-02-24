package cs121.hmc.edu.remindme;

import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by heatherseaman on 2/14/15.
 * Activity will show alarm details
 */


public class AlarmDetailsActivity extends ActionBarActivity {

    private AlarmModel alarmDetails = new AlarmModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Create New Alarm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_details);
        alarmDetails = new AlarmModel();

        final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
        ringToneContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent , 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm_details, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    alarmDetails.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
                    txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.action_save_alarm_details: {
                updateModelFromLayout();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateModelFromLayout() {

        TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
        alarmDetails.timeMinute = timePicker.getCurrentMinute();
        alarmDetails.timeHour = timePicker.getCurrentHour();

        EditText edtName = (EditText) findViewById(R.id.alarm_details_name);
        alarmDetails.name = edtName.getText().toString();

        CustomSwitch chkWeekly = (CustomSwitch) findViewById(R.id.alarm_details_label_repeat_weekly);
        alarmDetails.repeatWeekly = chkWeekly.isChecked();

        CustomSwitch chkSunday = (CustomSwitch) findViewById(R.id.alarm_details_label_sunday);
        alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());

        CustomSwitch chkMonday = (CustomSwitch) findViewById(R.id.alarm_details_label_monday);
        alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());

        CustomSwitch chkTuesday = (CustomSwitch) findViewById(R.id.alarm_details_label_tuesday);
        alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());

        CustomSwitch chkWednesday = (CustomSwitch) findViewById(R.id.alarm_details_label_wednesday);
        alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());

        CustomSwitch chkThursday = (CustomSwitch) findViewById(R.id.alarm_details_label_thursday);
        alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());

        CustomSwitch chkFriday = (CustomSwitch) findViewById(R.id.alarm_details_label_friday);
        alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, chkFriday.isChecked());

        CustomSwitch chkSaturday = (CustomSwitch) findViewById(R.id.alarm_details_label_saturday);
        alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());

        alarmDetails.isEnabled = true;
    }
}

