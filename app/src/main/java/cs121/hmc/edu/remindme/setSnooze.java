package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rachelleholmgren on 4/8/15.
 */
public class setSnooze extends Activity{
    public static String SNOOZE_TIME = "snooze_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_custom_snooze);
        Intent thisIntent = getIntent();
        final String alarmName = thisIntent.getStringExtra(SetName.ALARM_NAME);
        Button next = (Button) findViewById(R.id.btn_next);
            next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText snooze = (EditText) findViewById(R.id.snoozeTime);
                Intent i = new Intent(setSnooze.this, AlarmFrequency.class);
                i.putExtra(SetName.ALARM_NAME, alarmName);
                i.putExtra(SNOOZE_TIME, snooze.getText().toString());
                startActivity(i);
            }
        });
    }
}

