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
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME+ " BOOLEAN," +  //integer booleans? TODO
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY+ " BOOLEAN," + //integer booleans
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY+ " BOOLEAN," + //integer booleans
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY+ " BOOLEAN," + //integer booleans
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE+ " INTEGER," + //unix time stamp
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS+ " TEXT," +
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

    //TODO still some implementation left
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

                Calendar timerDate = Calendar.getInstance(); //TODO actually implement finding the time
                reminderTime = new OneTimeReminder(id, timerDate);

            }else if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY)) > 0){
                reminderTime = new DailyReminder(id, hour, min);

            }else if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY)) > 0){
                //TODO actually implement finding array
                boolean[] weekdays = {false,false,false,false,false,false,false};
                reminderTime = new WeeklyReminder(id, hour, min, weekdays);

            }else if(c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY)) > 0){
                //TODO actually implement finding array and weeknumber
                boolean[] weekdays = {false,false,false,false,false,false,false};
                int weeknumber = 1;
                reminderTime = new MonthlyReminder(id, hour, min, weeknumber, weekdays);

            }else{
                reminderTime = null;
                System.out.println("huge error, reminder time didn't fit any catagory");
            }

            model.addReminder(reminderTime);

        }
        if(model.getReminders().size() == 0){
            return null;
        }else{
            return model;
        }

    }

    //    TODO: Read up on Content Providers!
    //now returns an array list of content values!!
    //TODO still some implementation left
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
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, "???");//TODO find ggod date rep
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, "");//not relevant
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, -1);//not relevant

                    break;
                case ReminderTime.DAILY:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 1);//is this type
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, "");//not relevant
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, "");//not relevant
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, -1);//not relevant

                    break;
                case ReminderTime.WEEKLY:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 1);//is this type
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, "");
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, "???");//TODO figure out how to rep
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, -1);

                    break;
                case ReminderTime.MONTHLY:
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY, 0);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY, 1);
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE, "");
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS, "???");//TODO figure out later
                    values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH, -1);//TODO figure out later

                    break;
                default:
                    //error
                    System.out.println("oops");
                    break;
            }

            valueList.add(values);

        };

        return valueList;
    }

    //changed return type to an array list of ids of the new columns
    //TODO double check that this is the appropriate impementation
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
    public long updateAlarm(AlarmModel model) {
        ContentValues values = populateContent(model);
        // whereClause asks if the id of the database entry/row is the same as the model
        // if so it updates the row
        return getWritableDatabase().update(AlarmContract.Alarm.TABLE_NAME, values,
                AlarmContract.Alarm._ID + " = ?", new String[] { String.valueOf(model.id)});
    }

    //TODO edit this to select for id, title (one or both not sure yet)
    public int deleteAlarm(long id) {
        return getWritableDatabase().delete(AlarmContract.Alarm.TABLE_NAME, AlarmContract.Alarm._ID
                + " = ?", new String[] { String.valueOf(id) });
    }

    public List<AlarmModel> getAlarms() {
        SQLiteDatabase db = getReadableDatabase();
        String select = "SELECT * FROM " + AlarmContract.Alarm.TABLE_NAME;
        Cursor c = db.rawQuery(select, null);
        List<AlarmModel> alarmList = new ArrayList<AlarmModel>();

        while (c.moveToNext()) {
            alarmList.add(populateModel(c));
        }
        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }

}
