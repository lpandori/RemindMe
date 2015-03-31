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
    private int year;
    private int month;
    private int day;
    private Calendar date;

    //improved constructor
    //day: 0 - 31
    //year: postive integer
    //month: 1=jan, 12=dec
    //hour 0-23
    //min 0-59
    public OneTimeReminder(int year, int month, int day, int hour, int min) {
        //this.id = id; TODO removed id
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
        //TODO refactor so not so repetitive
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
    public int getHour() { return date.get(Calendar.HOUR_OF_DAY); }

    @Override
    public int getMin() { return date.get(Calendar.MINUTE); }

    @Override
    public long getNextTime() {
        if(hasNextTime()){
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
