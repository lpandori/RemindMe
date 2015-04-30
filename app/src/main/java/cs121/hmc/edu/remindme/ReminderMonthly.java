package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/8/15.
 *
 * Modified by Tyra He
 *
 * Monthly defines specific behavior in the monthly reminder mode. This class implements the
 *  interface ReminderTime class. It sets a monthly repeating reminder, and every time a monthly
 *  reminder rings, its next ringing date will be revised to the next at the same time.
 *  Same as other reminder types, DailyReminder works closely with the RemindMe database.
 *  Functions, such as getReminderType(), getWeekdays(), getDateString(), and getWeekOfMonth() etc,
 *  are all modifying to or reading from the database.
 */
public class ReminderMonthly implements ReminderTime {

    private long id = -1;
    private int snoozeCounter = 0;
    private int snooze;//snooze time in minutes
    private long nextAwakeTime = 0;
    private int hour;
    private int min;
    private int weekNumber;
    private boolean[] weekdays;

    //return what type of reminder it is
    @Override
    public int getReminderType(){
        return MONTHLY;
    }

    //weeknumber is 1-4
    public ReminderMonthly(int hour, int min, int weekNumber, boolean[] weekdays){
        this.hour = hour;
        this.min = min;
        this.weekNumber = weekNumber;
        this.weekdays = weekdays;
    }

    //return boolean rep of weekdays sunday-saturday (will be entirely false boolean array for non-relevants)
    @Override
    public String getWeekdays(){
        String toReturn = "";
        for(boolean b: weekdays){
            if(b){
                toReturn += "1";
            }else{
                toReturn += "0";
            }
        }
        return toReturn;
    }

    //return string rep of one time event (will be empty string for non-relevants)
    @Override
    public String getDateString(){ return ""; }

    //return int for the week of each month the alarm goes off (-1 if not applicable)
    @Override
    public int getWeekOfMonth(){
        return weekNumber;
    }

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

    @Override
    public int getSnoozeTime() { return snooze; }
    @Override
    public void setSnoozeTime(int minBetweenSnooze) { this.snooze = minBetweenSnooze; }

    @Override
    public long getNextAwakeTime() { return nextAwakeTime; }
    @Override
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

        // According to the weekly schedule
        Calendar timeBySchedule = Calendar.getInstance();
        timeBySchedule.set(Calendar.HOUR_OF_DAY, hour);
        timeBySchedule.set(Calendar.MINUTE, min);
        timeBySchedule.set(Calendar.SECOND, 0);
        while (!isScheduledAt(timeBySchedule) || timeBySchedule.before(now)) {
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

    /**
     * check whether this reminder is scheduled for a particular date
     * @param calendar - represent a date
     * @return True iff the date satisfies the monthly schedule defined
     */
    private boolean isScheduledAt(final Calendar calendar) {
        return weekdays[calendar.get(Calendar.DAY_OF_WEEK)-1] &&
                calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == weekNumber;
    }

    @Override
    public boolean hasNextTime() {
        return true;
    }
}
