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
                    AlarmContract.Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME+ " BOOLEAN," +  //TODO integer booleans?
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY+ " BOOLEAN," + //integer booleans
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY+ " BOOLEAN," + //integer booleans
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY+ " BOOLEAN," + //integer booleans
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

        AlarmModel model = new AlarmModel();

        while(c.moveToNext()){
            ReminderTime reminderTime;

            int hour = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
            int min = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));
            boolean isEnabled = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED)) > 0;

            long id = c.getLong(c.getColumnIndex(AlarmContract.Alarm._ID));
            String name = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));//only need this once TODO

            //find which reminder time it is, AND Construct ReminderTime accordingly
            if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME)) > 0){

                //stored in format yyyy-mm-dd
                String dateString = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE));

                //passes input as yyyy-mm-dd-hh:mm (not with dashes and stuff just in that order
                int year = Integer.parseInt(dateString.substring(0,4));
                int month = Integer.parseInt(dateString.substring(5,7));
                int day = Integer.parseInt(dateString.substring(8,10));
                reminderTime = new OneTimeReminder(id, year, month, day, hour, min);

            }else if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY)) > 0){
                reminderTime = new DailyReminder(id, hour, min);

            }else if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY)) > 0){
                boolean[] weekdays =
                        makeWeekdayArray(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS)));
                reminderTime = new WeeklyReminder(id, hour, min, weekdays);

            }else if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY)) > 0){
                boolean[] weekdays =
                        makeWeekdayArray(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS)));
                int weekNumber = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH));
                reminderTime = new MonthlyReminder(id, hour, min, weekNumber, weekdays);

            }else{
                reminderTime = null;
                System.out.println("huge error, reminder time didn't fit any category");
            }

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

            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME, model.name);
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED, model.isEnabled());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE, model.snooze);
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_HOUR, r.getHour());
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, r.getMin());


            //rest depend on what reminder type it is
            switch ( r.getReminderType()) {
                case ReminderTime.ONE_TIME:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 1);//is this type
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 0);

                    break;
                case ReminderTime.DAILY:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 1);//is this type
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 0);

                    break;
                case ReminderTime.WEEKLY:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 1);//is this type
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 0);

                    break;
                case ReminderTime.MONTHLY:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 1);
                    break;
                default:
                    //error
                    System.out.println("oops");
                    break;
            }

            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, r.getDateString());//not relevant
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, r.getWeekdays());//not relevant
            values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, r.getWeekOfMonth());

            valueList.add(values);

        };

        return valueList;
    }

    //changed return type to an array list of ids of the new columns
    //TODO double check that this is the appropriate implementation
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

    //TODO edited this method to select for alarm name (i.e. "take meds") instead of id
    //this allows us to use our new alarm model
    public AlarmModel getAlarm(long id, String name) {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME + " WHERE "
                + AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " = " + name; //TODO, check that this works
        Cursor c = db.rawQuery(select, null);

        return populateModel(c);
    }

    //TODO edit this to select for id, title (one or both not sure yet)
    //returns number of rows affected
    public long updateAlarm(AlarmModel model) {
        ArrayList<ContentValues> valuesList = populateContent(model);
        //needs to use the name and id to find reminders for alarm model

        //will need to check and update all reminders for this given alarm
        String alarmName = model.name;

        long numRowsChanged = 0;
        for(ContentValues values : valuesList){

            numRowsChanged += getWritableDatabase().update(AlarmContract.Alarm.TABLE_NAME, values,
                    AlarmContract.Alarm._ID + " = ?", new String[] { values.getAsString(AlarmContract.Alarm._ID)});
        }

        return numRowsChanged;//TODO pretty dubious about this method
    }

    //TODO this method is not currently used
    //probably returns the number of rows deleted
    public int deleteAlarm(String name) {

        //TODO deleting based on name which may not be unique
        return getWritableDatabase().delete(AlarmContract.Alarm.TABLE_NAME,
                AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " = ?", new String[] { name });
    }

    //makes a list of all current alarms
    //requires nested for loops in our implementation
    //TODO im wondering why we're not closing cursors and db's?
    public List<AlarmModel> getAlarms() {

        List<AlarmModel> alarmList = new ArrayList<AlarmModel>();
        SQLiteDatabase db = getReadableDatabase();

        String findAllNames = "SELECT DISTINCT "+  AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME
                +" FROM " + AlarmContract.Alarm.TABLE_NAME;

        Cursor c1 = db.rawQuery(findAllNames, null);

        //run cursor through to find all distinct alarm names
        //once i have list of distinct names
        //run query to get cursor for every distinct name
        //for each of these cursors
        //call populateModel

        while(c1.moveToNext()){

            String alarmName =
                    c1.getString(c1.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));

            String reminderTimeQuery = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME +
                    " WHERE " + AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME + " = " + alarmName;

            Cursor c2 = db.rawQuery(reminderTimeQuery, null);

            alarmList.add(populateModel(c2));
        }

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }

}
