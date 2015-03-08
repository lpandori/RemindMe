package cs121.hmc.edu.remindme;

/**
 * Created by lepandori on 3/7/15.
 */

//represents an individual reminder timing scheme (based on its frequency)
//for a specific alarm (ex: "take meds")
public interface ReminderTime {

    //reminder types
    public static int ONE_TIME = 1;
    public static int DAILY = 2;
    public static int WEEKLY = 3;
    public static int MONTHLY = 4;

    //return what type of reminder it is
    public int getReminderType();

    //return hour of particular alarm (0-24)
    public int getHour();

    //return minute of alarm
    public int getMin();

    //pre: will always be the same as it's db id!
    public long getId();

    //returns the soonest upcoming alarm time (in milliseconds)
    public long getNextTime();

    //returns if this scheme will produce an upcoming alarm
    public boolean hasNextTime();
}
