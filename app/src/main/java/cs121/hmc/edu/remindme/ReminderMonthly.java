package cs121.hmc.edu.remindme;

import java.util.Calendar;

/**
 * Created by lepandori on 3/8/15.
 */
public class ReminderMonthly implements ReminderTime {

    private long id;
    private int snoozeCounter = 0;
    private int minBetweenSnooze = DEFAULT_MIN_BETWEEN_SNOOZE;
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

    public int getMinBetweenSnooze() { return minBetweenSnooze; }
    public void setMinBetweenSnooze(int minBetweenSnooze) { this.minBetweenSnooze = minBetweenSnooze; }
    public long getNextAwakeTime() { return nextAwakeTime; }
    public void setNextAwakeTime(long nextAwakeTime) { this.nextAwakeTime = nextAwakeTime; }

    @Override
    public int getHour() { return hour; }

    @Override
    public int getMin() { return min; }

    //
    @Override
    public long getNextTime() {
        Calendar now = Calendar.getInstance();

        Calendar setTime = Calendar.getInstance();

        int nowWeekOfMonth = now.get(Calendar.WEEK_OF_MONTH);//get today's week of month (1st, 2nd, etc)

        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMin = now.get(Calendar.MINUTE);
        int nowWeekday = now.get(Calendar.DAY_OF_WEEK);
        int nowMonth = now.get(Calendar.MONTH);
        setTime.set(Calendar.HOUR_OF_DAY, hour);
        setTime.set(Calendar.MINUTE, min);
        setTime.set(Calendar.SECOND, 00);
        setTime.set(Calendar.MONTH, nowMonth);

        if(nowWeekOfMonth == weekNumber) {//should happen in this week
            //haven't passed this week's alarm
            //have passed it need to go down to next case

            //check if we've passed it today
            //loop through array starting tomorrow

            //check if alarm needs to happen today
            if(hour > nowHour || (nowHour == hour && min > nowMin)){
                //set for today
                setTime.set(Calendar.WEEK_OF_MONTH, weekNumber);
                setTime.set(Calendar.DAY_OF_WEEK, nowWeekday);
                return setTime.getTimeInMillis() + snoozeCounter*minToMillis;
            }

            //if alarm between tomorrow and saturday (end of the week)
            for(int i = nowWeekday; i <= Calendar.SATURDAY; i++){

                //if there is a timer for this weekday
                if(weekdays[i-1]){//timer exists for this weekday (be careful to reach into correct index)
                    setTime.set(Calendar.WEEK_OF_MONTH, weekNumber);
                    setTime.set(Calendar.DAY_OF_WEEK, i);
                    return setTime.getTimeInMillis() + snoozeCounter*minToMillis;
                }
            }

            //if we didn't return then we've passed it this month and can set for earliest next month
            setTime.roll(Calendar.MONTH, 1);

            for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++){
                if(weekdays[i-1]){//timer exists for this weekday (be careful to reach into correct index)
                    setTime.set(Calendar.WEEK_OF_MONTH, weekNumber);
                    setTime.set(Calendar.DAY_OF_WEEK, i);
                    return setTime.getTimeInMillis() + snoozeCounter*minToMillis;
                }
            }

        }else{
            if(nowWeekOfMonth < weekNumber){//it is in upcoming week
                //set earliest day of week
                //in this month :)
                for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++){
                    if(weekdays[i-1]){//timer exists for this weekday (be careful to reach into correct index)
                        setTime.set(Calendar.WEEK_OF_MONTH, weekNumber);
                        setTime.set(Calendar.DAY_OF_WEEK, i);
                        return setTime.getTimeInMillis() + snoozeCounter*minToMillis;
                    }
                }

            }else{//nowWeekOfMonth > weekNumber //it is in a previous week to now
                //set the earliest week day
                //roll by a month
                setTime.roll(Calendar.MONTH, 1);
                for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++){
                    if(weekdays[i-1]){//timer exists for this weekday (be careful to reach into correct index)
                        setTime.set(Calendar.WEEK_OF_MONTH, weekNumber);
                        setTime.set(Calendar.DAY_OF_WEEK, i);
                        return setTime.getTimeInMillis() + snoozeCounter*minToMillis;
                    }
                }
            }
        }

        return -1;//error
    }

    @Override
    public boolean hasNextTime() {
        return true;
    }
}
