package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/8/15.
 */
public class MonthlyReminder implements ReminderTime {

    private long id;
    private int hour;
    private int min;
    private int weekNumber;
    private boolean[] weekdays;

    //return what type of reminder it is
    @Override
    public int getReminderType(){
        return MONTHLY;
    }

    public MonthlyReminder(long id, int hour, int min, int weekNumber, boolean[] weekdays){
        this.id = id;
        this.hour = hour;
        this.min = min;
        this.weekNumber = weekNumber;
        this.weekdays = weekdays;
    }
    @Override
    public long getId() {
        return id;
    }

    @Override
    public int getHour() { return hour; }

    @Override
    public int getMin() { return min; }

    @Override
    public long getNextTime() {
        Calendar setTime = Calendar.getInstance();
        setTime.set(Calendar.SECOND, 00);


        return -1;//TODO fix
    }

    @Override
    public boolean hasNextTime() {
        return true;
    }
}
