package cs121.hmc.edu.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by heatherseaman on 2/23/15.
 */

public class AlarmDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarmclock.db";

    private static final String SQL_CREATE_ALARM =
            "CREATE TABLE " + AlarmContract.Alarm.TABLE_NAME + " (" +
                    AlarmContract.Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + //remindertime id
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " INTEGER," + //alarm id
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " INTEGER,"+
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME+ " BOOLEAN," + //integer booleans for all remindertype identifier
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE+ " TEXT," + //text as input type "yyyy-mm-dd"
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS+ " TEXT," + //stored as string of 0s and 1s, starting on sunday
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH + " INTEGER" + " )";


    private static final String SQL_DELETE_AlARM = "DROP TABLE IF EXISTS " + AlarmContract.Alarm.TABLE_NAME;

    public AlarmDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_AlARM);
        onCreate(db);
    }

    //returns an AlarmModel object from a given db query (which gives us the cursor)
    private AlarmModel populateModel(Cursor c) {

        //String alarmName = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));
        AlarmModel model = null;// = new AlarmModel(alarmName);TODO using null feels hacky

        int i = 0;
        while(c.moveToNext()){
            if(i == 0){//need to look at the first row
                String alarmName = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));
                boolean isEnabled = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED)) > 0;
                long model_id = c.getLong(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ID));
                int snooze = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE));
                model = new AlarmModel(alarmName);
                model.setEnabled(isEnabled);
                model.setId(model_id);
                model.setSnooze(snooze);
            }
            i++;

            ReminderTime reminderTime;

            int hour = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
            int min = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));

            long id = c.getLong(c.getColumnIndex(AlarmContract.Alarm._ID));
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
                reminderTime = new OneTimeReminder(year, month, day, hour, min);

            }else if(isDaily){

                reminderTime = new DailyReminder(hour, min);

            }else if(isWeekly){

                boolean[] weekdays =
                        makeWeekdayArray(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS)));
                reminderTime = new WeeklyReminder(hour, min, weekdays);

            }else if(isMonthly){

                boolean[] weekdays =
                        makeWeekdayArray(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS)));
                int weekNumber = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH));
                reminderTime = new MonthlyReminder(hour, min, weekNumber, weekdays);

            }else{//TODO remove else when we are sure code is working
                reminderTime = null;
                System.out.println("huge error, reminder time didn't fit any category");
            }

            reminderTime.setSnoozeCounter(snoozeCounter);//TODO write
            reminderTime.setId(id);

            model.addReminder(reminderTime);
        }

        if(model.getReminders().size() == 0){
            return null;
        }else{
            return model;
        }

    }
    //takes in a string of the form "0110001" and makes boolean array
    private boolean[] makeWeekdayArray(String boolString){
        boolean[] result = new boolean[7];

        for(int i=0; i < result.length; i++){
            result[i] = boolString.substring(i,i+1).equals("1");
        }
        return result;

    }

    //TODO: Read up on Content Providers!
    //now returns an array list of content values!!
    //TODO need to test this
    private ArrayList<ContentValues> populateContent(AlarmModel model) {

        ArrayList<ContentValues> valueList= new ArrayList<ContentValues>();

        for(ReminderTime r : model.getReminders()){

            ContentValues values = new ContentValues();
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ID, model.getId());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME, model.name);
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED, model.isEnabled());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE, model.getSnooze());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR, r.getHour());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, r.getMin());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, r.getDateString());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, r.getWeekdays());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, r.getWeekOfMonth());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER, r.getSnoozeCounter());

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

            valueList.add(values);

        };

        return valueList;
    }

    //returns an array list of ids of the new columns
    //adds alarm model into db
    public ArrayList<Long> createAlarm(AlarmModel model) {
        ArrayList<ContentValues> valuesList = populateContent(model);
        ArrayList<Long> ids = new ArrayList<Long>();
        for(ContentValues values : valuesList){
            long id = getWritableDatabase().insert(AlarmContract.Alarm.TABLE_NAME, null, values);
            ids.add(id);
        }
        return ids;
    }

    //TODO write createReminder to add new reminder ONLY (for when new reminder is added to existing model)
    //TODO delete and update reminders
        //TODO as well as deleting and updating alarms
    //Can use reminder deletion and editing as helper for deleting/editing alarms

    //increment the snooze counter for this reminderId
    //TODO potentially rewrite dealing more with objects (maybe not)
    public void snoozeReminder(long reminderId){

        //TODO not written in very safe manner
        SQLiteDatabase db = getReadableDatabase();
        String retrieve = "SELECT " + AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER +
                " FROM "+AlarmContract.Alarm.TABLE_NAME+" WHERE " + AlarmContract.Alarm._ID + " = " + reminderId;

        Cursor c = db.rawQuery(retrieve, null);
        int previousSnooze = -1;//TODO check this for safety!!!!
        while(c.moveToNext()){
            previousSnooze  = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER));
        }

        c.close();
        db.close();

        String update = "UPDATE "+AlarmContract.Alarm.TABLE_NAME+" SET "+
                AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " = " + (previousSnooze+1) +
                " WHERE "+AlarmContract.Alarm._ID+ " = "+reminderId;

        getWritableDatabase().execSQL(update);
    }

    //set the snooze counter of given reminder to zero
    public void dismiss(long reminderId){

        String update = "UPDATE "+AlarmContract.Alarm.TABLE_NAME+" SET "+
                AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " = 0  WHERE "+
                AlarmContract.Alarm._ID+ " = "+reminderId;

        getWritableDatabase().execSQL(update);

    }

    //TODO edited this method to select for alarm name (i.e. "take meds") instead of id
    //TODO CHECK that this even works
    //this allows us to use our new alarm model
    public AlarmModel getAlarm(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME + " WHERE "
                + AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " = " +id; //TODO, check that this works
        Cursor c = db.rawQuery(select, null);
        AlarmModel toReturn = populateModel(c);
        c.close();
        db.close();
        return toReturn;
    }

    //deletes all rows related to a given alarm
    public int deleteAlarm(long id) {

        return getWritableDatabase().delete(AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.Alarm.COLUMN_NAME_ALARM_ID + " = "+id, null);
    }

    //makes a list of all current alarms
    //requires nested for loops in our implementation
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
