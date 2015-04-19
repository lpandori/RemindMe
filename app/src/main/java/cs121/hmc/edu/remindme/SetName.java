package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import 	java.io.File;

/**
 * Created by rachelleholmgren on 3/12/15.
 */
public class SetName extends ActionBarActivity {
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    // better way to do this??!?!??!?!?!??!
    private AlarmModel alarmModel = new AlarmModel("");
    public static String ALARM_NAME = "name";//used to access the alarm name in AlarmFrequency screen
    public static String SNOOZE_TIME = "snooze_time";
    public static String ALARM_TONE = "alarm_tone";
    //private TextView textToneSelection ;
    //private Ringtone ringtone;
    //private Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name);
        Button next = (Button) findViewById(R.id.btn_next);
        TextView textToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetName.this, AlarmFrequency.class);
                EditText name = (EditText)findViewById(R.id.setName);
                EditText snoozeTime = (EditText) findViewById(R.id.snoozeTime);
                TextView tone = (TextView) findViewById(R.id.alarm_label_tone_selection);
                i.putExtra(ALARM_NAME, name.getText().toString());
                i.putExtra(SNOOZE_TIME, snoozeTime.getText().toString());//passes alarm name
                i.putExtra(ALARM_TONE, tone.getText().toString());
                //System.out.println(tone.getText().toString());
                        startActivity(i);
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
                      alarmModel.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                      TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
                      txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmModel.alarmTone).getTitle(this));
                    break;
                }
                default: {
                    break;
                }
            }
        }

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
}
