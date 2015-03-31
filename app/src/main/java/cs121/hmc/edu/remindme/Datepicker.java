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

    //keys for passing m,d,y date between activtites
    public final static String DATE_YEAR = "date_year";
    public final static String DATE_MONTH = "date_month";
    public final static String DATE_DAY = "date_day";



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker);
        setCurrentDate();

        Intent prevIntent = getIntent(); // gets the previously created intent
        final String alarmName = prevIntent.getStringExtra(SetName.ALARM_NAME);//TODO double check that this is ok to do
        final int reminderType = prevIntent.getIntExtra(AlarmFrequency.REMINDER_TYPE, -1);
        System.out.println("from the date picker the alarmname is: "+alarmName);
        System.out.println("from the date picker the remindertype is: "+reminderType);
        button = (Button) findViewById(R.id.done);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(reminderType == ReminderTime.ONE_TIME){
                        Intent i = new Intent(Datepicker.this, Timepicker.class);
                        i.putExtra(SetName.ALARM_NAME, alarmName);
                        i.putExtra(AlarmFrequency.REMINDER_TYPE, reminderType);
                        i.putExtra(DATE_YEAR, datePicker.getYear());
                        System.out.println("year selected: " +datePicker.getYear());
                        //month starts counting at 0 so must add 1
                        int month = datePicker.getMonth()+1;
                        i.putExtra(DATE_MONTH, month);
                        System.out.println("month selected: " +month);
                        i.putExtra(DATE_DAY, datePicker.getDayOfMonth());
                        System.out.println("day selected: " +datePicker.getDayOfMonth());
                        startActivity(i);
                    }else{
                        System.out.println("One time reminder seems not to be recognized");
                    }
                }
            });
    }
    public void setCurrentDate(){
        datePicker = (DatePicker) findViewById(R.id.date);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        System.out.println("month from set current: " + month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year,month,day);


    }
}
