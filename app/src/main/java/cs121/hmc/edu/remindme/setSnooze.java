package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by rachelleholmgren on 4/8/15.
 */
public class setSnooze extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_custom_snooze);
        Button next = (Button) findViewById(R.id.btn_next);
            next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(setSnooze.this, AlarmFrequency.class);
                startActivity(i);
            }
        });
    }
}

