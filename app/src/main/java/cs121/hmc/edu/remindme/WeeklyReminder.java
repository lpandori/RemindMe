package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/7/15.
 */

public class WeeklyReminder implements ReminderTime {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    //note: although sunday is in our array at index 0, Calendar.SUNDAY = 1
    //(i.e. DAY_OF_WEEK is 1-7)

    private long id;
    boolean[] weekdays;
    int hour;
    int min;

    //construct a weekly reminder
    //weekdays - boolean array indicating which weekdays to repeat on
    public WeeklyReminder(long id, int hour, int min, boolean[] weekdays){
        this.id = id;
        this.weekdays = weekdays;
        this.hour = hour;
        this.min = min;
    }

    //return what type of reminder it is
    @Override
    public int getReminderType(){
        return WEEKLY;
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
    public String getDateString(){
        return "";
    }

    //return int for the week of each month the alarm goes off (-1 if not applicable)
    @Override
    public int getWeekOfMonth(){
        return -1;
    }

    //pre: will always be the same as it's db id!
    @Override
    public long getId(){
        return id;
    }

    @Override
    public int getHour() { return hour; }

    @Override
    public int getMin() { return min; }

    //returns the soonest upcoming alarm time (in milliseconds)
    public long getNextTime(){
        //go through all possible days
        Calendar now = Calendar.getInstance();
        Calendar setTime = Calendar.getInstance();

        int nowWeekday = now.get(Calendar.DAY_OF_WEEK);//get today's weekday

        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMin = now.get(Calendar.MINUTE);
        setTime.set(Calendar.HOUR_OF_DAY, hour);
        setTime.set(Calendar.MINUTE, min);
        setTime.set(Calendar.SECOND, 00);

        //check if alarm needs to happen today
        //TODO fucked this up :(
        //TODO setting it as today kinda regardless of if the alarm happens today
        //probably cludged this :(
        //need to test this
        if(weekdays[nowWeekday-1] && (hour > nowHour || (nowHour == hour && min > nowMin))){
            //set for today
            setTime.set(Calendar.DAY_OF_WEEK, nowWeekday);
            return setTime.getTimeInMillis();
        }

        //if alarm between tomorrow and saturday
        for(int i = nowWeekday+1; i <= Calendar.SATURDAY; i++){

            //if there is a timer for this weekday
            if(weekdays[i-1]){//timer exists for this weekday (be careful to reach into correct index)
                setTime.set(Calendar.DAY_OF_WEEK, i);
                return setTime.getTimeInMillis();
            }
        }

        //otherwise alarm must be between sunday and nowWeekday
        for(int i = Calendar.SUNDAY; i < nowWeekday; i++){
            if(weekdays[i-1]){//timer exists for this weekday (be careful to reach into correct index)
                setTime.set(Calendar.DAY_OF_WEEK, i);
                return setTime.getTimeInMillis();
            }
        }
        return -1;//error occurred

    }

    //returns if this scheme will produce an upcoming alarm
    public boolean hasNextTime(){
        return true; //weekly alarm will always be upcoming
    }
}
