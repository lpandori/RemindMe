package cs121.hmc.edu.remindme;

import android.media.RingtoneManager;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by heatherseaman on 2/14/15.
 * Defines the data that is stored in the alarm
 * Contains multiple reminder schemes
 * REpresents an event such as "Take Meds" or "CS121 Meeting"
 */
public class AlarmModel {

    private long id;
    public String name;
    private int snooze = 1;
    private boolean isEnabled = true;
    private ArrayList<ReminderTime> reminders;
    public String alarmToneStr;
    public Uri alarmTone;
    private ReminderTime currentSet;

    /**
     * Default constructor creates an alarm model with a given name
     */
    public AlarmModel(String name) {
        this.id = -1;
        this.name = name;
        reminders = new ArrayList<ReminderTime>();
        alarmToneStr = "Argo Navis";
        alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    //setter and getter for model id
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    //setter and getter for snooze
    public int getSnooze() { return snooze; }
    public void setSnooze(int snooze) {
        this.snooze = snooze;
        for(ReminderTime r : reminders){
            r.setSnoozeTime(snooze);
        }
    }

    //return the id of the reminder time that is the most upcoming
    public long getReminderId(){
        if(currentSet != null) {
            return currentSet.getId();
        }else{
            return -1;
        }
    }

    /*
     * return list of reminders
     */
    public ArrayList<ReminderTime> getReminders() {
        return reminders;
    }

    /*
     * add a reminder time to the alarm model
     */
    public void addReminder(ReminderTime toAdd){
        reminders.add(toAdd);
        toAdd.setSnoozeTime(snooze);
    }

    //remove a specific reminder time from the alarm model
    //return if it was successfully removed
    public boolean removeReminder(long reminderId){
        boolean successfulRemoved = false;

        //search through arraylist comparing id of reminder to all others
        //remove it
        for(ReminderTime r : reminders){//check how to do for each loops
            if(r.getId() == reminderId){
                successfulRemoved = reminders.remove(r);
            }
        }
        return successfulRemoved;
    }

    //swap out a reminder with the same id
    public void replaceReminder(ReminderTime replacement){
        removeReminder(replacement.getId());
        addReminder(replacement);
    }

    //check if this alarm type is on/off
    public boolean isEnabled() {
        return isEnabled;
    }

    //enable or disable
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Uri getAlarmTone() {
        return alarmTone;
    }

    public void setAlarmTone(String alarm) {
        alarmToneStr = alarm;
       Uri converted = Uri.parse(alarm);
       alarmTone = converted;
    }

    //from the list of reminders return the next one (closest upcoming)
    //in milliseconds
    public long getNextReminderTime(){

        long currentSmallest = -1;
        currentSet = null;//remove previous current set
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
        if (currentSmallest - Calendar.getInstance().getTimeInMillis() < 1000) {
            return -1;
        }
        return currentSmallest;
    }


}
