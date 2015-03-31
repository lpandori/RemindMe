package cs121.hmc.edu.remindme;

import android.provider.BaseColumns;

/**
 * Created by heatherseaman on 2/23/15.
 * A database scheme containing table definitions for storage
 */
public final class AlarmContract {

    public AlarmContract() {}

    public static abstract class Alarm implements BaseColumns {
//        public static final String TABLE_NAME = "alarm";
//        public static final String COLUMN_NAME_ALARM_NAME = "name";
//        public static final String COLUMN_NAME_ALARM_TIME_HOUR = "hour";
//        public static final String COLUMN_NAME_ALARM_TIME_MINUTE = "minute";
//        public static final String COLUMN_NAME_ALARM_REPEAT_DAYS = "days";
//        public static final String COLUMN_NAME_ALARM_REPEAT_WEEKLY = "weekly";
//        public static final String COLUMN_NAME_ALARM_TONE = "tone";
//        public static final String COLUMN_NAME_ALARM_SNOOZE = "snooze";//TODO
//        public static final String COLUMN_NAME_ALARM_ENABLED = "isEnabled";

        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_ALARM_ID = "alarm_id";
        public static final String COLUMN_NAME_ALARM_NAME = "name";
        public static final String COLUMN_NAME_ALARM_TIME_HOUR = "hour";
        public static final String COLUMN_NAME_ALARM_TIME_MINUTE = "minute";
        public static final String COLUMN_NAME_ALARM_SNOOZE = "snooze";
        public static final String COLUMN_NAME_ALARM_ENABLED = "isEnabled";
        public static final String COLUMN_NAME_ALARM_ONE_TIME = "one_time";
        public static final String COLUMN_NAME_ALARM_DAILY = "daily";
        public static final String COLUMN_NAME_ALARM_WEEKLY = "weekly";
        public static final String COLUMN_NAME_ALARM_MONTHLY = "monthly";
        public static final String COLUMN_NAME_ALARM_DATE = "date";
        public static final String COLUMN_NAME_ALARM_WHICH_WEEKDAYS = "whichweekdays";
        public static final String COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH = "whichweekofmonth";
                    //(1st, 2nd, 3rd, 4th)

        //more info
        //need daily, weekly, one-time, monthly, every x weeks

        //calendar gives us access to current hour, minute, day of week, etc.
    }

}