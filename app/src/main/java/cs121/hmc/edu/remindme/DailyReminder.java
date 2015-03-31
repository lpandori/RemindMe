package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/7/15.
 */
public class DailyReminder implements ReminderTime{

    private long id;
    private int hour;
    private int min;

    //constructor for a daily repeating reminder
    //id is the same id in db
    //hour is 0-24 :)
    public DailyReminder(int hour, int min){
        //this.id = id; TODO removed reminder
        this.hour = hour;
        this.min = min;
    }

    //return what type of reminder it is
    @Override
    public int getReminderType(){
        return DAILY;
    }

    //return boolean rep of weekdays sunday-saturday (will be entirely false boolean array for non-relevants)
    @Override
    public String getWeekdays(){ return "0000000"; }

    //return string rep of one time event (will be empty string for non-relevants)
    @Override
    public String getDateString(){ return ""; }

    //return int for the week of each month the alarm goes off (-1 if not applicable)
    @Override
    public int getWeekOfMonth(){ return -1; }

    @Override
    public long getId() {
        return id;
    }
    @Override
    public void setId(long id) { this.id = id;}


    @Override
    public int getHour() { return hour; }

    @Override
    public int getMin() { return min; }

    @Override
    public long getNextTime() {
        Calendar now = Calendar.getInstance();
        Calendar setTime = Calendar.getInstance();//time when setting next alarm
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMin = now.get(Calendar.MINUTE);

        setTime.set(Calendar.SECOND, 00);
        //if haven't had alarm today
        if(hour > nowHour || (nowHour == hour && min > nowMin)){
            //set for today
            setTime.set(Calendar.HOUR_OF_DAY, hour);
            setTime.set(Calendar.MINUTE, min);

        }else{
            //set for tomorrow
            setTime.set(Calendar.HOUR_OF_DAY, hour);
            setTime.set(Calendar.MINUTE, min);
            setTime.add(Calendar.DATE, 1); //set reminder for tomorrow
        }
        return setTime.getTimeInMillis();
    }

    @Override
    public boolean hasNextTime() {
        return true;//daily will always have an upcoming time
    }
}
