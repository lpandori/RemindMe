package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import 	java.io.File;

/**
 * Created by rachelleholmgren on 3/12/15.
 */
public class SetName extends Activity {

    public static String ALARM_NAME = "name";//used to access the alarm name in AlarmFrequency screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name);

        //SQLiteDatabase.deleteDatabase( new File("/data/data/cs121.hmc.edu.remindme/databases/alarmclock.db"));
        //TODO used to clear out db errors

        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetName.this, AlarmFrequency.class);
                EditText name = (EditText)findViewById(R.id.setName);
                i.putExtra(ALARM_NAME, name.getText().toString());//passes alarm name
                startActivity(i);
            }
        });
    }
}
