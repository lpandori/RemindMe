package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/26/15.
 */
public class Datepicker extends Activity {
    private DatePicker datePicker;
    private Button button;
    private int year;
    private int month;
    private int day;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker);
        setCurrentDate();
        button = (Button) findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Datepicker.this, Timepicker.class);
                startActivity(i);
            }
        });
        //addButtonListener();
    }
    public void setCurrentDate(){
        datePicker = (DatePicker) findViewById(R.id.date);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year,month,day);


    }
}
