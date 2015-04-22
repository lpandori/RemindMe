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

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "alarmclock.db";

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
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_ONE_TIME+ " BOOLEAN," + //integer booleans for all remindertype identifier
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DAILY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WEEKLY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_MONTHLY+ " BOOLEAN," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_DATE+ " TEXT," + //text as input type "yyyy-mm-dd"
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEKDAYS+ " TEXT," + //stored as string of 0s and 1s, starting on sunday
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_WHICH_WEEK_OF_MONTH + " INTEGER," +
                    AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE + " TEXT" +" )";


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
        //AlarmModel model = null;// = new AlarmModel(alarmName);TODO using null feels hacky
        AlarmModel alarmModel = new AlarmModel("test");
        int i = 0;
        while(c.moveToNext()){
           if(i == 0){//need to look at the first row
                String alarmName = c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_NAME));
                alarmModel = new AlarmModel(alarmName);
                boolean isEnabled = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ENABLED)) > 0;
                long model_id = c.getLong(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_ID));
                int snooze = c.getInt(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE));
                alarmModel.alarmTone = Uri.parse(c.getString(c.getColumnIndex(AlarmContract.Alarm.COLUMN_NAME_ALARM_TONE)));

                //System.out.println("ALARM TONE IS : " + alarmTone);

                alarmModel.setEnabled(isEnabled);
                alarmModel.setId(model_id);
                alarmModel.setSnooze(snooze);

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
                reminderTime = new OneTimeReminder(year, month, day, hour, min);
                reminderTime.setMinBetweenSnooze(alarmModel.getSnooze());

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

            }

            reminderTime.setSnoozeCounter(snoozeCounter);//TODO write
            reminderTime.setNextAwakeTime(nextAwakeTime);//TODO write
            reminderTime.setMinBetweenSnooze(alarmModel.getSnooze());

            reminderTime.setId(id);

            alarmModel.addReminder(reminderTime);
        }

        if(alarmModel.getReminders().size() == 0){
            return null;
        }else{
            return alarmModel;
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

    private ContentValues populateReminderContent(ReminderTime r, long mId,
                                                  String mName, boolean enabled, int snooze, long reminderId
                                                  ,String tone
                                                    ){
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

        values.put(AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE, r.getMinBetweenSnooze());
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

    // returns an array list of content values from an alarm model
    private ArrayList<ContentValues> populateContent(AlarmModel model) {
        ArrayList<ContentValues> valueList= new ArrayList<ContentValues>();
        for(ReminderTime r : model.getReminders()){


            System.out.println("MODEL ALARM TONE: " + model.getAlarmTone().toString());

            ContentValues values = populateReminderContent(r, model.getId(), model.name,
                    model.isEnabled(), model.getSnooze(), model.getReminderId(), model.getAlarmTone().toString());


            valueList.add(values);
        }
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

    //pre: reminder must already exist
    //takes reminder that should replace old reminder with same id in db
    //and id of the alarm model that it's a part of
    //post: database contains updated version of reminder
    public void updateReminder(ReminderTime reminder, long mId){

        AlarmModel parentAlarm = getAlarm(mId);

        ContentValues rVals = populateReminderContent(reminder, parentAlarm.getId(),
            parentAlarm.name, parentAlarm.isEnabled(), parentAlarm.getSnooze(), reminder.getId(), parentAlarm.getAlarmTone().toString());

        long rId = reminder.getId();

        getWritableDatabase().update(AlarmContract.Alarm.TABLE_NAME,
                rVals, AlarmContract.Alarm._ID + " = "+rId, null);
    }

    //increment the snooze counter for this reminderId
    //TODO potentially rewrite dealing more with objects (maybe not)
    public void snoozeReminder(long reminderId){

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        int minBetweenSnooze = 0;
        //TODO not written in very safe manner
        SQLiteDatabase db = getReadableDatabase();
        String retrieve = "SELECT " + AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER +
                "," + AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE +
                " FROM "+AlarmContract.Alarm.TABLE_NAME+" WHERE " + AlarmContract.Alarm._ID + " = " + reminderId;

        Cursor c = db.rawQuery(retrieve, null);
        int previousSnooze = -1;//TODO check this for safety!!!!
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

    //set the snooze counter of given reminder to zero
    public void dismiss(long reminderId){

        String update = "UPDATE "+AlarmContract.Alarm.TABLE_NAME+" SET "+
                AlarmContract.Alarm.COLUMN_NAME_ALARM_SNOOZE_COUNTER + " = 0 ," +
                AlarmContract.Alarm.COLUMN_NAME_ALARM_NEXT_AWAKE_TIME + " = 0  WHERE " +
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
