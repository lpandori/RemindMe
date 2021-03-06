package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/7/15.
 *
 * Modified by Tyra He
 *
 * OneTimeReminder defines specific behavior in the oneTime reminder mode. This class implements the
 *  interface ReminderTime class. It sets a oneTime reminder, and every time a oneTime reminder
 *  rings, it will disable this alarm by setting a flag in the database. Same as other reminder
 *  types, OneTimeReminder works closely with the RemindMe database. Functions, such as
 *  getReminderType(), getWeekdays(), getDateString(), and getWeekOfMonth() etc, are all
 *  modifying to or reading from the database. For oneTimeReminder, it requires the user to set date
 *  in a specific format: Year has to be an integer, day (from 0-31), month (from 1-12), hour (0-23)
 *  and min (0-59).
 */
public class ReminderOneTime implements ReminderTime {

    private long id = -1;
    private int snoozeCounter = 0;
    private int snooze;//snooze time in minutes
    private long nextAwakeTime = 0; // in seconds
    //pre: date must include m/d/y AND hour and minute
    private int year;
    private int month;
    private int day;
    private Calendar date;

    //improved constructor
    public ReminderOneTime(int year, int month, int day, int hour, int min) {
        this.year = year;
        this.month = month;
        this.day = day;
        date = Calendar.getInstance();

        int setMonth;

        switch(month){
            case 1:
                setMonth = Calendar.JANUARY;
                break;
            case 2:
                setMonth = Calendar.FEBRUARY;
                break;
            case 3:
                setMonth = Calendar.MARCH;
                break;
            case 4:
                setMonth = Calendar.APRIL;
                break;
            case 5:
                setMonth = Calendar.MAY;
                break;
            case 6:
                setMonth = Calendar.JUNE;
                break;
            case 7:
                setMonth = Calendar.JULY;
                break;
            case 8:
                setMonth = Calendar.AUGUST;
                break;
            case 9:
                setMonth = Calendar.SEPTEMBER;
                break;
            case 10:
                setMonth = Calendar.OCTOBER;
                break;
            case 11:
                setMonth = Calendar.NOVEMBER;
                break;
            case 12:
                setMonth = Calendar.DECEMBER;
                break;
            default:
                setMonth = -1;//error
                break;
        }
        date.set(Calendar.MONTH, setMonth);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, min);
        date.set(Calendar.SECOND, 00);

    }

    //return boolean rep of weekdays sunday-saturday (will be entirely false boolean array for non-relevants)
    @Override
    public String getWeekdays(){
        return "0000000";
    }

    //return string rep of one time event (will be empty string for non-relevants)
    @Override
    public String getDateString(){
        //formatted as: yyyy-mm-dd
        String  dayStr = ((""+day).length() == 2) ? ""+day : "0"+day;
        String  monthStr = ((""+month).length() == 2) ? ""+month : "0"+month;
        String date = year + "-" + monthStr + "-" +  dayStr;

        return date;
    }

    //return int for the week of each month the alarm goes off (-1 if not applicable)
    @Override
    public int getWeekOfMonth(){
        return -1;
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
    public void setId(long id) { this.id = id;}

    @Override
    public int getSnoozeCounter(){return snoozeCounter;}
    @Override
    public void setSnoozeCounter(int snoozeCounter){this.snoozeCounter = snoozeCounter;}

    public int getSnoozeTime() { return snooze; }
    public void setSnoozeTime(int minBetweenSnooze) { this.snooze = minBetweenSnooze; }
    public long getNextAwakeTime() { return nextAwakeTime; }
    public void setNextAwakeTime(long nextAwakeTime) { this.nextAwakeTime = nextAwakeTime; }


    @Override
    public int getHour() { return date.get(Calendar.HOUR_OF_DAY); }

    @Override
    public int getMin() { return date.get(Calendar.MINUTE); }

    @Override
    public long getNextTime() {
        if(hasNextTime()){
            return getNextAwakeTime() == 0 ? date.getTimeInMillis() : getNextAwakeTime();
        } else {
            return -1;
        }
    }

    @Override
    public boolean hasNextTime() {
        return getNextAwakeTime() == 0 || getNextAwakeTime() > Calendar.getInstance().getTimeInMillis();
    }
}
