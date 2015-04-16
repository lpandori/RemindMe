package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import 	java.io.File;

/**
 * Created by rachelleholmgren on 3/12/15.
 */
public class SetName extends ActionBarActivity {

    public static String ALARM_NAME = "name";//used to access the alarm name in AlarmFrequency screen
    public static String SNOOZE_TIME = "snooze_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name);
        System.out.println("BLAH!");


        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetName.this, AlarmFrequency.class);
                EditText name = (EditText)findViewById(R.id.setName);
                EditText snoozeTime = (EditText) findViewById(R.id.snoozeTime);
                i.putExtra(ALARM_NAME, name.getText().toString());
                i.putExtra(SNOOZE_TIME, snoozeTime.getText().toString());//passes alarm name
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
}
