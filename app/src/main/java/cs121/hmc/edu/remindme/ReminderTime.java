package cs121.hmc.edu.remindme;

/**
 * Class: ReminderTime.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-29-2015
 * Interface for ReminderTimes
 * Defines behavior all Reminder(*) objects should follow
 * represents an individual reminder timing scheme (based on its frequency)
 * for a specific alarm (ex: "take meds")
 */
public interface ReminderTime {

    //reminder types
    public static int ONE_TIME = 1;
    public static int DAILY = 2;
    public static int WEEKLY = 3;
    public static int MONTHLY = 4;

    public static int minToMillis = 60000;
    public static int DEFAULT_MIN_BETWEEN_SNOOZE = 1;

    //return what type of reminder it is
    public int getReminderType();

    //return hour of particular alarm (0-24)
    public int getHour();

    //return string boolean rep of weekdays sunday-saturday (will be entirely false boolean array for non-relevants)
    public String getWeekdays();

    //return int for the week of each month the alarm goes off (-1 if not applicable)
    public int getWeekOfMonth();

    //return string rep of one time event (will be empty string for non-relevants)
    public String getDateString();

    //return minute of alarm
    public int getMin();

    //pre: will always be the same as it's db id
    public long getId();
    public void setId(long id);

    //getter and setter for snoozeCounter
    public int getSnoozeCounter();
    public void setSnoozeCounter(int snoozeCounter);

    //setter and getter for snooze time
    public int getSnoozeTime();
    public void setSnoozeTime(int minBetweenSnooze);

    //setter and getter for next awake time
    public long getNextAwakeTime();
    public void setNextAwakeTime(long nextAwakeTime);

    //returns the soonest upcoming alarm time (in milliseconds)
    public long getNextTime();

    //returns if this scheme will produce an upcoming alarm
    public boolean hasNextTime();
}
