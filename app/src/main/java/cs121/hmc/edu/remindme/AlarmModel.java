package cs121.hmc.edu.remindme;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by heatherseaman on 2/14/15.
 * Defines the data that is stored in the alarm
 */
public class AlarmModel {

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    private long id = -1; //TODO remove
    //public int timeHour;
    //public int timeMinute;
    //private boolean repeatingDays[];
    //public boolean repeatWeekly;
    //public Uri alarmTone;
    public String name;
    public int snooze;//TODO added snooze
    private boolean isEnabled = true;//TODO is setting this true initially ok?
    private ArrayList<ReminderTime> reminders;
    private ReminderTime currentSet;//TODO fill this in later

    /**
     * Default constructor creates an alarm model with a given name
     */
    public AlarmModel(String name) {
        this.name = name;
        reminders = new ArrayList<ReminderTime>();
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    //return list of reminders
    public ArrayList<ReminderTime> getReminders() {
        return reminders;
    }

    //add a reminder time to the alarm model
    public void addReminder(ReminderTime toAdd){
        reminders.add(toAdd);
    }

    //remove a specific reminder time from the alarm model
    public void removeReminder(long reminderId){
        //search through arraylist comparing id of reminder to all others
        //remove it
        for(ReminderTime r : reminders){//check how to do for each loops
            if(r.getId() == reminderId){
                //remove it
                //TODO finish
            }
        }
        //TODO look into if this is worthwhile
    }

    //check if this alarm type is on/off
    public boolean isEnabled() {
        return isEnabled;
    }

    //enable or disable
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    //from the list of reminders return the next one (closest upcoming)
    //in milliseconds
    public long getNextReminderTime(){

        long currentSmallest = -1;
        for(ReminderTime r : reminders){
            if(r.hasNextTime()) {
                long rtime = r.getNextTime();
                if (rtime != -1) {
                    //if this is the first valid (non-negative) next
                    // time that has been read
                    //or if this remindertime's next time is earlier than
                    //the earliest we've found so far
                    //update the currentSmallest
                    if (currentSmallest == -1 || rtime < currentSmallest) {
                        currentSmallest = rtime;
                        currentSet = r;
                    }
                }
            }
        }
        return currentSmallest;
    }


}
