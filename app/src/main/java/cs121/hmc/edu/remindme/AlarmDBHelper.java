package cs121.hmc.edu.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class: AlarmDBHelper.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-29-2015
 * Adapted from StevenTrigg's AlarmClock  project.
 * Provides a suite of methods for retrieving alarm information from the database
 * and writing information into the database.
 * The database is designed so that every row corresponds to a reminder time for a given alarm.
 * Multiple rows can be associated with a single alarm.
 * Each row contains information on:
 *  _ID : unique id managed by the SQLite db that identifies a reminder time
 *  ALARM_ID: id that identifies an alarm (can be in the db multiple times)
 *  ENABLED: if they alarm is currently enabled (true/false for all rows of a given ALARM_ID)
 *  SNOOZE: amount of time this alarm is snoozed
 *  SNOOZE_COUNTER: number of times, snooze has been used consecutively for this alarm
 *  NEXT_AWAKE_TIME: next time to ring (calculated based on snooze counter and snooze time)
 *  TIME_HOUR: hour of alarm from 0-23
 *  TIME_MINUTE: minute of alarm from 0-59
 *  ONE_TIME, MONTHLY, DAILY, WEEKLY : integer boolean to indicate what type of reminder this is
 *  DATE: date ("yyyy-mm-dd") that the alarm goes of "" for non-one_time reminders
 *  WHICH_WEEKDAYS: array of days of the week alarm goes off on,
 *      stored as string of 0s and 1s, starting on sunday ("" when not applicable)
 *  WHICH_WEEK_OF_MONTH: integer representing which week of the month reminder is for
 *      1st = 1, 2nd = 2, .. 4th = 4
 *  ALARM_TONE: uri of chosen alarm tone
**/
public class AlarmDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "alarmclock.db";

    private Context mContext;

    private static final String SQL_CREATE_ALARM =
            "CREATE TABLE " + AlarmContract.Alarm.TABLE_NAME + " (" +
                    AlarmContract.Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + //remindertime id
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " INTEGER," + //alarm id
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " INTEGER,"+
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_NEXT_AWAKE_TIME + " INTEGER,"+
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE+ " TEXT," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS+ " TEXT," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE + " TEXT" +" )";


    private static final String SQL_DELETE_AlARM = "DROP TABLE IF EXISTS " + AlarmContract.Alarm.TABLE_NAME;


    public AlarmDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    /*
     * Creates table first time database is used
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALARM);
    }

    /*
     * Upgrades database when new version is created
     * Deletes old table and recreates it
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_AlARM);
        onCreate(db);
    }

    /*
     * Builds an AlarmModel from a given cursor
     * pre - cursor must be for rows with all same ALARM_ID (with no rows missing)
     * @return - AlarmModel corresponding to cursor's rows
     * @param c - cursor to those rows in the table
     */
    private AlarmModel populateModel(Cursor c) {

        AlarmModel alarmModel = new AlarmModel("test");
        int i = 0;
        while(c.moveToNext()){
           if(i == 0){//need to look at the first row
                String alarmName = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));
                alarmModel = new AlarmModel(alarmName);
                boolean isEnabled = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED)) > 0;
                long model_id = c.getLong(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ID));
                int snooze = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE));
                String alarm_tone = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE));

                alarmModel.setEnabled(isEnabled);
                alarmModel.setId(model_id);
                alarmModel.setSnooze(snooze);
                alarmModel.setAlarmTone(alarm_tone);

            }
            i++;

            ReminderTime reminderTime = null;

            int hour = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
            int min = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));

            long id = c.getLong(c.getColumnIndex(AlarmContract.Alarm._ID));
            long nextAwakeTime = alarmModel.isEnabled() ? c.getLong(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NEXT_AWAKE_TIME)) : 0;
            int snoozeCounter = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER));

            //find which reminder type is in this row
            boolean isOneTime = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME)) > 0;
            boolean isDaily = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY)) > 0;
            boolean isWeekly = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY)) > 0;
            boolean isMonthly = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY)) > 0;

            //find which reminder time it is, AND Construct ReminderTime accordingly
            if(isOneTime){

                //stored in format yyyy-mm-dd
                String dateString = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE));

                //passes input as yyyy-mm-dd
                int year = Integer.parseInt(dateString.substring(0,4));
                int month = Integer.parseInt(dateString.substring(5,7));
                int day = Integer.parseInt(dateString.substring(8,10));
                reminderTime = new ReminderOneTime(year, month, day, hour, min);
                reminderTime.setSnoozeTime(alarmModel.getSnooze());

            }else if(isDaily){

                reminderTime = new ReminderDaily(hour, min);

            }else if(isWeekly){

                boolean[] weekdays =
                        makeWeekdayArray(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS)));
                reminderTime = new ReminderWeekly(hour, min, weekdays);

            }else if(isMonthly){

                boolean[] weekdays =
                        makeWeekdayArray(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS)));
                int weekNumber = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH));
                reminderTime = new ReminderMonthly(hour, min, weekNumber, weekdays);

            }

            reminderTime.setSnoozeCounter(snoozeCounter);
            reminderTime.setNextAwakeTime(nextAwakeTime);
            reminderTime.setId(id);

            alarmModel.addReminder(reminderTime);
        }

        if(alarmModel.getReminders().size() == 0){
            return null;
        }else{
            return alarmModel;
        }

    }

    /*
     * Makes boolean array from input of form "0110001"
     * Represents which days of the week are selected
     * pre: string must be 7 characters long and be composed of "0"s and "1"s
     * @param boolString - string of 0s and 1s that will be converted into boolean array
     * @return - boolean array corresponding to input (first cell is sunday)
     */
    private boolean[] makeWeekdayArray(String boolString){
        boolean[] result = new boolean[7];

        for(int i=0; i < result.length; i++){
            result[i] = boolString.substring(i,i+1).equals("1");
        }
        return result;

    }

    /*
     * Return a table row represented as a contentvalues object
     */
    private ContentValues populateReminderContent(ReminderTime r, long mId, String mName,
                                                  boolean enabled, int snooze, String tone ){
        ContentValues values = new ContentValues();
        if(r.getId() != -1){
            values.put(AlarmContract.Alarm._ID, r.getId());
        }
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ID, mId);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME, mName);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED, enabled);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR, r.getHour());
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, r.getMin());
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, r.getDateString());
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, r.getWeekdays());
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, r.getWeekOfMonth());
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER, r.getSnoozeCounter());

        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE, snooze);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_NEXT_AWAKE_TIME, r.getNextAwakeTime());

        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE, tone);


        int isOneTime = 0;
        int isDaily = 0;
        int isWeekly = 0;
        int isMonthly = 0;
        //rest depend on what reminder type it is
        switch ( r.getReminderType()) {
            case ReminderTime.ONE_TIME: isOneTime = 1;
                break;
            case ReminderTime.DAILY: isDaily = 1;
                break;
            case ReminderTime.WEEKLY: isWeekly = 1;
                break;
            case ReminderTime.MONTHLY: isMonthly = 1;
                break;
        }
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, isOneTime);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, isDaily);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, isWeekly);
        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, isMonthly);

        return values;
    }

    /*
      * returns an array list of content values from an alarm model
      */
    private ArrayList<ContentValues> populateContent(AlarmModel model) {
        ArrayList<ContentValues> valueList= new ArrayList<ContentValues>();
        for(ReminderTime r : model.getReminders()){

            ContentValues values = populateReminderContent(r, model.getId(), model.name,
                    model.isEnabled(), model.getSnooze(), model.getAlarmTone().toString());


            valueList.add(values);
        }
        return valueList;
    }

    /*
     *returns an array list of ids of the new table columns
     * adds alarm model into db
     * pre alarm cannot already exist in the db
     */
    public ArrayList<Long> createAlarm(AlarmModel model) {
        ArrayList<ContentValues> valuesList = populateContent(model);
        ArrayList<Long> ids = new ArrayList<Long>();
        for(ContentValues values : valuesList){
            long id = getWritableDatabase().insert(AlarmContract.Alarm.TABLE_NAME, null, values);
            ids.add(id);
        }
        AlarmManagerHelper.setAlarms(mContext);
        return ids;
    }

    /*
     * updateAlarm in alarmModel
     */
    public ArrayList<Long> updateAlarm(AlarmModel model){
        deleteAlarm(model.getId());
        return createAlarm(model);
    }

    /*
     * increment the snooze counter for this reminderId
     */
    public void snoozeReminder(long reminderId){

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        int minBetweenSnooze = 0;

        SQLiteDatabase db = getReadableDatabase();
        String retrieve = "SELECT " + AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER +
                "," + AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE +
                " FROM "+AlarmContract.Alarm.TABLE_NAME+" WHERE " + AlarmContract.Alarm._ID + " = " + reminderId;

        Cursor c = db.rawQuery(retrieve, null);
        int previousSnooze = -1;
        while(c.moveToNext()){
            previousSnooze  = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER));
            minBetweenSnooze  = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE));
        }

        c.close();
        db.close();

        String update = "UPDATE "+AlarmContract.Alarm.TABLE_NAME+" SET "+
                AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " = " + (previousSnooze+1)
                + "," + AlarmContract.Alarm.COLUMN_NAME_ALARM_NEXT_AWAKE_TIME + "=" +
                (currentTimeInMillis + minBetweenSnooze * ReminderTime.minToMillis) +
                " WHERE "+AlarmContract.Alarm._ID+ " = "+reminderId;

        getWritableDatabase().execSQL(update);
    }

    /*
     * set the snooze counter of given reminder to zero
     */
    public void dismiss(long reminderId){

        String update = "UPDATE "+AlarmContract.Alarm.TABLE_NAME+" SET "+
                AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " = 0 ," +
                AlarmContract.Alarm.COLUMN_NAME_ALARM_NEXT_AWAKE_TIME + " = 0  WHERE " +
                AlarmContract.Alarm._ID+ " = "+reminderId;

        getWritableDatabase().execSQL(update);

    }

    /*
     * Returns alarm model from the database based on id
     */
    public AlarmModel getAlarm(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME + " WHERE "
                + AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " = " +id;
        Cursor c = db.rawQuery(select, null);
        AlarmModel toReturn = populateModel(c);
        c.close();
        db.close();
        return toReturn;
    }

    /*
     * Deletes all rows related to a given alarm
     */
    public int deleteAlarm(long id) {

        int rowsDeleted;
        AlarmManagerHelper.cancelAlarms(mContext);

        rowsDeleted = getWritableDatabase().delete(AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " = "+id, null);

        AlarmManagerHelper.setAlarms(mContext);

        return rowsDeleted;
    }


    /*
     * Returns a list of all current alarms stored in the database
     */
    public List<AlarmModel> getAlarms() {

        List<AlarmModel> alarmList = new ArrayList<AlarmModel>();
        SQLiteDatabase db = getReadableDatabase();

        String findAllNames = "SELECT DISTINCT "+  AlarmContract.Alarm.COLUMN_NAME_ALARM_ID
                +" FROM " + AlarmContract.Alarm.TABLE_NAME;

        Cursor c1 = db.rawQuery(findAllNames, null);

        //run cursor through to find all distinct alarm names
        //once i have list of distinct names
        //run query to get cursor for every distinct name
        //for each of these cursors
        //call populateModel

        while(c1.moveToNext()){

            long alarmId =
                    c1.getLong(c1.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ID));

            String reminderTimeQuery = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME +
                    " WHERE " + AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " = " + alarmId;

            Cursor c2 = db.rawQuery(reminderTimeQuery, null);

            alarmList.add(populateModel(c2));

            c2.close();
        }

        c1.close();
        db.close();

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }

}
