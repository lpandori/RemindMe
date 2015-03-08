package cs121.hmc.edu.remindme;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by lepandori on 3/7/15.
 */
public class OneTimeReminder implements ReminderTime {

    private long id;
    //pre: date must include m/d/y AND hour and minute
    private Calendar date;


    //constructor
    //pre: date must include m/d/y AND hr and min
    public OneTimeReminder(long id, Calendar date){
        this.date = date;
        this.id = id;
    }

    //return what type of reminder it is
    @Override
    public int getReminderType(){
        return ONE_TIME;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public int getHour() { return date.get(Calendar.HOUR_OF_DAY); }

    @Override
    public int getMin() { return date.get(Calendar.MINUTE); }

    @Override
    public long getNextTime() {
        if(hasNextTime()){
            date.set(Calendar.SECOND, 00);
            return date.getTimeInMillis();
        }else{
            return -1;
        }
    }

    @Override
    public boolean hasNextTime() {
        //check if the current time  is not past the reminder date
        return !date.before(Calendar.getInstance());
    }
}
