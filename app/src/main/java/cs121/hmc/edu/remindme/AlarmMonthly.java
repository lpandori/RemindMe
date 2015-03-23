package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by rachelleholmgren on 3/7/15.
 */
public class AlarmMonthly extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_month);


        Spinner whichWeek = (Spinner) findViewById(R.id.which_week);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.which_week_options,
                R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whichWeek.setAdapter(adapter);

        Spinner whichDay = (Spinner) findViewById(R.id.day_of_week);
        ArrayAdapter<CharSequence> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.day_options,
                        R.layout.spinner_layout);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        whichDay.setAdapter(adapter2);

        Button done = (Button) findViewById(R.id.btn_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmMonthly.this, AlarmListActivity.class);
                startActivity(i);
            }
        });





    }
}
