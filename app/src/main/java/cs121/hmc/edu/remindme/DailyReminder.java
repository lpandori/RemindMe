package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/7/15.
 */
public class DailyReminder implements ReminderTime{

    private long id = -1;
    private int snoozeCounter;
    private int minBetweenSnooze = DEFAULT_MIN_BETWEEN_SNOOZE;
    private long nextAwakeTime = 0;
    private int hour;
    private int min;
    private Calendar date;

    //constructor for a daily repeating reminder
    //id is the same id in db
    //hour is 0-24 :)
    public DailyReminder(int hour, int min){
        //this.id = id; TODO removed reminder
        this.hour = hour;
        this.min = min;

        date = Calendar.getInstance();


        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, min);
        date.set(Calendar.SECOND, 00);
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
    public String getDateString(){
        String  minStr = ((""+min).length() == 2) ? ""+min : "0"+min;
        String  hourStr = ((""+hour).length() == 2) ? ""+hour : "0"+hour;
        String date = hourStr + "-" +  minStr;

        return date;
    }

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
    public int getSnoozeCounter(){return snoozeCounter;}
    @Override
    public void setSnoozeCounter(int snoozeCounter){this.snoozeCounter = snoozeCounter;}

    public int getMinBetweenSnooze() { return minBetweenSnooze; }
    public void setMinBetweenSnooze(int minBetweenSnooze) { this.minBetweenSnooze = minBetweenSnooze; }
    public long getNextAwakeTime() { return nextAwakeTime; }
    public void setNextAwakeTime(long nextAwakeTime) { this.nextAwakeTime = nextAwakeTime; }

    @Override
    public int getHour() { return hour; }

    @Override
    public int getMin() { return min; }

    @Override
    public long getNextTime() {
        Calendar now = Calendar.getInstance();

        Calendar setTime = Calendar.getInstance();
        setTime.set(Calendar.HOUR_OF_DAY, hour);
        setTime.set(Calendar.MINUTE, min);

        // According to the daily schedule
        Calendar timeBySchedule = Calendar.getInstance();
        timeBySchedule.set(Calendar.HOUR_OF_DAY, hour);
        timeBySchedule.set(Calendar.MINUTE, min);
        timeBySchedule.set(Calendar.SECOND, 0);
        if (timeBySchedule.before(now)) {
            // The next reminder will be tomorrow
            timeBySchedule.add(Calendar.DATE, 1);
        }

        // See if the snooze should take priority
        if (getNextAwakeTime() > now.getTimeInMillis()) {
            return Math.min(timeBySchedule.getTimeInMillis(), getNextAwakeTime());
        } else {
            return timeBySchedule.getTimeInMillis();
        }
    }

    @Override
    public boolean hasNextTime() {
        return true;
    }
}
