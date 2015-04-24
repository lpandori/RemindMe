package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Class: MainActivity.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: The class displays a list of reminder times associated with a single alarm.
 * Each of these reminders can recur with difference frequency. Relevant parameters
 * such as the time, frequency, or date which are associated with each alarm appear
 * in each list item.
 * Attributions: Swipe-To-Dismi
 * ss functionality from third-party code
 * available on https://github.com/hudomju/android-swipe-to-dismiss-undo
 */


public class MainActivity extends ActionBarActivity {

    // A DBHelper allows access to the database, the adapter populates the ListView
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private ReminderListAdapter mAdapter;
    private Context mContext;
    public static SwipeToDismissTouchListener<ListViewAdapter> touchListener;

    // Global strings allow us to reference data passed through intents
    public static String ALARM_DATE = "alarm_date";
    public static String EXISTING_MODEL = "existing_model";
    public static String EXISTING_MODEL_ID = "existing_model_id";
    public static String ALARM_HOUR = "timeHour";
    public static String ALARM_MINUTE = "timeMinute";
    public static String ALARM_NAME = "alarm-title";
    public static String ALARM_TONE = "alarm_tone";
    public static String alarmTitle = "";
    public static String REMINDER_ID = "reminder_id";
    public static String WEEK_OF_MONTH = "week_month";
    public static String WEEKDAYS = "week_days";
    public static String MIN_BETWEEN_SNOOZE = "snooze";
    public static long alarmId = -1;
    private static String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static String[] stringIndices = {"1st", "2nd", "3rd", "4th", "5th"};

    public static String alarm_tone = "Default";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setTitle(alarmTitle);
        // Gets the Intent which got us to this activity and pulls data from passed Extras
        Intent prevIntent = getIntent();
        alarmId = prevIntent.getLongExtra(EXISTING_MODEL_ID, -1);
        alarmTitle = prevIntent.getStringExtra(ALARM_NAME);
        alarm_tone = prevIntent.getStringExtra(ALARM_TONE);
        ArrayList<ReminderTime> reminderList = dbHelper.getAlarm(alarmId).getReminders();

        mContext = this;
        // the list adapter populates the list view alarmList
        mAdapter = new ReminderListAdapter(this, reminderList);
        setContentView(R.layout.activity_details);
        ListView alarmList = (ListView)findViewById(R.id.reminder_list);

        // tapping the View addReminder allows users to set a new reminder time
        // to this specific alarm model
        View addReminder = ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.add_reminder, null, false);
        // add the button to the foot of the list
        alarmList.addFooterView(addReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SetFrequency.class);
                intent.putExtra(EXISTING_MODEL_ID, alarmId);//need model id
                intent.putExtra(ALARM_NAME, alarmTitle);
                System.out.println("ALARM TONE FROM DETAILS is " + alarm_tone);
                intent.putExtra(ALARM_TONE, alarm_tone);
                intent.putExtra(EXISTING_MODEL, true);
                intent.putExtra("prevActivity", "AlarmDetails");
                startActivity(intent);
            }
        });
        alarmList.setAdapter(mAdapter);

        // touchListener is applied to each list item in getView().
        // This allows users to use a swipe gesture
        // to delete an alarm
        touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(alarmList),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss (int position) {
                                return true;
                            }
                            @Override
                            public void onDismiss(ListViewAdapter lvAdapter, int position) {
                                mAdapter.remove(position);
                                View thisView = lvAdapter.getChildAt(position);
                                long viewId = (long) thisView.getTag();
                                dbHelper.deleteAlarm(viewId);
                            }
                        });

        alarmList.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarm_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            // allows users to return to the AlarmOverviewActivity
            case R.id.home_button:
                Intent i = new Intent(MainActivity.this, AlarmOverviewActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Defines specific behavior for back button presses.
     * We don't use the default back stack all the time in our app
     * navigation.
     */
    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        Intent i = new Intent(MainActivity.this, AlarmOverviewActivity.class);
        startActivity(i);
    }

    /*
     * ReminderListAdapter populates the ListView with content from the database.
     * Each ReminderTime has a view defined to display important information about
     * frequency, and set alarm time for the reminder.
     */
    static class ReminderListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ReminderTime> mReminders;
//        private AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

        public ReminderListAdapter(Context context, ArrayList<ReminderTime> reminders) {
            mContext = context;
            mReminders = reminders;
        }

        /*
         * Gets the count of the number of reminder times
         */
        @Override
        public int getCount() {
            if (mReminders != null) {
                return mReminders.size();
            }
            return 0;
        }
        /*
         * Gets an object at a specific position in the adapter
         */
        @Override
        public Object getItem(int position) {
            if (mReminders != null) {
                return mReminders.get(position);
            }
            return null;
        }
        /*
         * Gets the id of an object at a given position
         */
        @Override
        public long getItemId(int position) {
            if (mReminders != null) {
                return mReminders.get(position).getId();

            }
            return 0;
        }
        /*
         * Removes an object from an adapter
         * Note: this does not remove the reminder from the database.
         */
        public void remove(int position) {
            if (mReminders != null) {
                mReminders.remove(position);
                notifyDataSetChanged();
            }
        }

        /*
         * Defines how the view looks for a specific list item
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ReminderTime reminderTime = (ReminderTime) getItem(position);
            // if we are creating a View for the first time we must
            // call the inflater. Otherwise we can update an existing view
            // and skip this control block.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Weekly reminders have a different xml file defining their view so
                // we need to check if the ReminderTime is a weekly reminder.
                if(reminderTime.getReminderType() == ReminderTime.WEEKLY) {
                    convertView = inflater.inflate(R.layout.weekly_reminder_item, parent, false);
                    // for weekly view creation, run a helper method below
                    return getWeeklyView(position, convertView, parent);
                }
                else {
                    convertView = inflater.inflate(R.layout.reminder_list_item, parent, false);
                }
            }

            final long reminderId = getItemId(position);
            convertView.setTag(reminderTime.getId());
            // toDisplay will appear in the view. We update this string to different
            // values depending on the reminder type.
            String toDisplay = "";
            // Strings to display the set alarm time
            final String timeHour = "" + reminderTime.getHour();
            String timeMinute = "" + reminderTime.getMin();
            switch(reminderTime.getReminderType()){
                // One Time Alarms are displayed in the format "<Date> at <Time>"
                case ReminderTime.ONE_TIME:
                    Date date = new Date();
                    String dateString = reminderTime.getDateString();
                    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = (Date) dateFormatter.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat outFormat = SimpleDateFormat.getDateInstance();
                    toDisplay = outFormat.format(date) + " at";
                    break;
                // Daily Alarms are displayed in the format "Daily at <Time>"
                case ReminderTime.DAILY:
                    toDisplay = "Daily at";
                    break;
                // Monthly Alarms are displayed in the format
                // "Every <1st, 2nd, 3rd, ...> <Weekday> at <Time>"
                case ReminderTime.MONTHLY:
                    int weekOfMonth = reminderTime.getWeekOfMonth();
                    String weekString = stringIndices[weekOfMonth - 1];
                    String week = reminderTime.getWeekdays();
                    int dayIndex = week.indexOf('1');
                    String dayString = weekdays[dayIndex];
                    toDisplay = "Every " + weekString + " " + dayString + " at";
                    break;
            }

            // Populate the views in reminder_list_item.xml
            TextView txtDisplay = (TextView) convertView.findViewById(R.id.reminder_text);
            txtDisplay.setText(toDisplay);
            TextView txtTime = (TextView) convertView.findViewById(R.id.reminder_item_time);
            txtTime.setText(String.format("%02d : %02d", Integer.parseInt(timeHour),
                    Integer.parseInt(timeMinute)));

            // Each reminder has an edit button which takes
            // users to the appropriate screen so they can edit
            // existing alarms
            Button btn_edit = (Button) convertView.findViewById(R.id.reminder_edit_button);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j;
                    switch(reminderTime.getReminderType()) {
                        case ReminderTime.ONE_TIME:
                            j = new Intent(mContext, EditOneTime.class);
                            //parse date
                            String dString = reminderTime.getDateString();
                            j.putExtra(REMINDER_ID, reminderId);
                            j.putExtra(ALARM_TONE, alarm_tone);
                            j.putExtra(ALARM_DATE, dString);
                            j.putExtra(ALARM_HOUR, reminderTime.getHour());
                            j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                            j.putExtra(ALARM_NAME, alarmTitle);
                            j.putExtra(EXISTING_MODEL_ID, alarmId);
                            mContext.startActivity(j);
                            break;
                        case ReminderTime.DAILY:
                            j = new Intent(mContext, EditDaily.class);
                            j.putExtra(ALARM_HOUR, reminderTime.getHour());
                            j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                            j.putExtra(ALARM_NAME, alarmTitle);
                            j.putExtra(ALARM_TONE, alarm_tone);
                            j.putExtra(EXISTING_MODEL_ID, alarmId);
                            mContext.startActivity(j);
                            break;

                        case ReminderTime.WEEKLY:
                            j = new Intent(mContext, EditWeekly.class);
                            j.putExtra(WEEKDAYS, reminderTime.getWeekdays());
                            j.putExtra(ALARM_HOUR, reminderTime.getHour());
                            j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                            j.putExtra(ALARM_NAME, alarmTitle);
                            j.putExtra(ALARM_TONE, alarm_tone);
                            j.putExtra(EXISTING_MODEL_ID, alarmId);
                            mContext.startActivity(j);
                            break;

                        case ReminderTime.MONTHLY:
                            j = new Intent(mContext, EditMonthly.class);
                            j.putExtra(WEEKDAYS, reminderTime.getWeekdays());
                            j.putExtra(WEEK_OF_MONTH, reminderTime.getWeekOfMonth());
                            j.putExtra(ALARM_HOUR, reminderTime.getHour());
                            j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                            j.putExtra(ALARM_NAME, alarmTitle);
                            j.putExtra(ALARM_TONE, alarm_tone);
                            j.putExtra(EXISTING_MODEL_ID, alarmId);

                            mContext.startActivity(j);
                            break;
                    }

                }
            });

            // Define the view for the revealed screen after a view is swiped
            // deleteView removes a reminder from the database
            View deleteView = convertView.findViewById(R.id.txt_delete);
            // helper method to define behavior on delete
            setOnClickForDelete(deleteView);

            // onClick Listener for the view itself
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j;

                    // When there are other reminder times which have been "swiped"
                    // with being deleted or undone, this control statement tells
                    // the activity to preserve these swiped items when another list
                    // item is tapped. This prevents users from losing reminders that
                    // they did not mean to delete.
                    if (touchListener.existPendingDismisses()){
                        touchListener.undoPendingDismiss();
                    }
                    // Duplicate code from above. This allows the user to tap anywhere on
                    // the list item to start editing the parameters. There is probably a
                    // more efficient way to do this by changing the nesting of our views, but
                    // this is the solution we have for now.
                    else {
                        switch(reminderTime.getReminderType()) {
                            case ReminderTime.ONE_TIME:
                                j = new Intent(mContext, EditOneTime.class);
                                //parse date
                                String dString =reminderTime.getDateString();//in format yyyy-mm-dd
                                j.putExtra(REMINDER_ID, reminderId);
                                j.putExtra(ALARM_DATE, dString);
                                j.putExtra(ALARM_HOUR, reminderTime.getHour());
                                j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                                j.putExtra(ALARM_NAME, alarmTitle);
                                j.putExtra(ALARM_TONE, alarm_tone);
                                j.putExtra(EXISTING_MODEL_ID, alarmId);

                                mContext.startActivity(j);
                                break;
                            case ReminderTime.DAILY:
                                j = new Intent(mContext, EditDaily.class);
                                j.putExtra(REMINDER_ID, reminderId);
                                j.putExtra(ALARM_HOUR, reminderTime.getHour());
                                j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                                j.putExtra(ALARM_NAME, alarmTitle);
                                j.putExtra(ALARM_TONE, alarm_tone);
                                j.putExtra(EXISTING_MODEL_ID, alarmId);
                                mContext.startActivity(j);
                                break;

                            case ReminderTime.WEEKLY:
                                j = new Intent(mContext, EditWeekly.class);
                                j.putExtra(REMINDER_ID, reminderId);
                                j.putExtra(WEEKDAYS, reminderTime.getWeekdays());
                                j.putExtra(ALARM_HOUR, reminderTime.getHour());
                                j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                                j.putExtra(ALARM_NAME, alarmTitle);
                                j.putExtra(ALARM_TONE, alarm_tone);
                                j.putExtra(EXISTING_MODEL_ID, alarmId);
                                mContext.startActivity(j);
                                break;

                            case ReminderTime.MONTHLY:
                                j = new Intent(mContext, EditMonthly.class);
                                j.putExtra(REMINDER_ID, reminderId);
                                j.putExtra(WEEKDAYS, reminderTime.getWeekdays());
                                j.putExtra(WEEK_OF_MONTH, reminderTime.getWeekOfMonth());
                                j.putExtra(ALARM_HOUR, reminderTime.getHour());
                                j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                                j.putExtra(ALARM_NAME, alarmTitle);
                                j.putExtra(ALARM_TONE, alarm_tone);
                                j.putExtra(EXISTING_MODEL_ID, alarmId);

                                mContext.startActivity(j);
                                break;
                        }
                    }
                }
            });
            // adds swipe-to-delete functionality to each reminder list item
            convertView.setOnTouchListener(touchListener);
            return convertView;
        }

        /*
         * getWeeklyView is a helper method that defines a special view for weekly
         * Reminders. This view allows users to easily see which alarms are on or
         * off by color-coding
         */
        private View getWeeklyView(int position, View convertView, ViewGroup parent) {

            // Same as getView
            final ReminderTime reminderTime = (ReminderTime) getItem(position);
            final long reminderId = getItemId(position);
            final String timeHour = "" + reminderTime.getHour();
            final String timeMinute = "" + reminderTime.getMin();
            TextView txtTime = (TextView) convertView.findViewById(R.id.reminder_item_time);
            txtTime.setText(String.format("%02d : %02d", Integer.parseInt(timeHour), Integer.parseInt(timeMinute)));

            // The weekdays for which the alarm should ring
            String whichWeekdays = reminderTime.getWeekdays();
            char[] weekBools = whichWeekdays.toCharArray();

            // Define an array of the views in weekly_reminder_item.xml
            TextView[] viewArray = {(TextView) convertView.findViewById(R.id.alarm_item_sunday),
                    (TextView)convertView.findViewById(R.id.alarm_item_monday),
                    (TextView)convertView.findViewById(R.id.alarm_item_tuesday),
                    (TextView)convertView.findViewById(R.id.alarm_item_wednesday),
                    (TextView)convertView.findViewById(R.id.alarm_item_thursday),
                    (TextView)convertView.findViewById(R.id.alarm_item_friday),
                    (TextView)convertView.findViewById(R.id.alarm_item_saturday),
            };

            // populate an array with 1s or 0s depending on for which weekdays
            // the reminder should ring
            for(int i=0; i < weekBools.length;i++){
                if (weekBools[i] == '1') {
                    updateTextColor(viewArray[i], true);
                }
                else if (weekBools[i] == '0') {
                    updateTextColor(viewArray[i], false);
                }
            }

            // Define Edit Button, same as getView(). Direct users to a particular edit
            // screen for weekly alarms.
            Button btn_edit = (Button) convertView.findViewById(R.id.reminder_edit_button);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                Intent j;
                @Override
                public void onClick(View v) {
                    j = new Intent(mContext, EditWeekly.class);
                    j.putExtra(WEEKDAYS, reminderTime.getWeekdays());
                    j.putExtra(ALARM_HOUR, reminderTime.getHour());
                    j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                    j.putExtra(ALARM_NAME, alarmTitle);
                    j.putExtra(EXISTING_MODEL_ID, alarmId);
                    mContext.startActivity(j);
                }
            });

            // Define views for revealed views after swipe gesture
            View deleteView = convertView.findViewById(R.id.txt_delete);
            setOnClickForDelete(deleteView);

            // same as in getView(), we use duplicate code to allow users
            // to tap anywhere on the list item to edit. This is inefficient, but
            // we did not have time to refactor this portion.
            convertView.setTag(reminderTime.getId());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j;
                    if (touchListener.existPendingDismisses()) {
                        touchListener.undoPendingDismiss();
                    } else {
                        j = new Intent(mContext, EditWeekly.class);
                        j.putExtra(REMINDER_ID, reminderId);
                        j.putExtra(WEEKDAYS, reminderTime.getWeekdays());
                        j.putExtra(ALARM_HOUR, reminderTime.getHour());
                        j.putExtra(ALARM_MINUTE, reminderTime.getMin());
                        j.putExtra(ALARM_NAME, alarmTitle);
                        j.putExtra(EXISTING_MODEL_ID, alarmId);
                        mContext.startActivity(j);
                    }
                }
            });

            convertView.setOnTouchListener(touchListener);

            return convertView;
        }

        // onClick method for the txt_delete portion of reminder_list_item or
        // weekly_reminder_list_item. When a user taps the portion reading "Confirm
        // Delete" the alarm is removed from the database
        private void setOnClickForDelete(View deleteView) {

            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener.existPendingDismisses()){
                        touchListener.processPendingDismisses();
                    }
                }
            });
        }

        /*
         * Helper method for getWeeklyView. Green colored weekdays denote alarms which
         * should ring on those days.
         */
        private void updateTextColor(TextView view, boolean isOn) {
            if (isOn) {
                view.setTextColor(Color.GREEN);
            } else {
                view.setTextColor(Color.WHITE);
            }
        } // end class updateTextColor
    } //end ReminderListAdapter
} // end class MainActivity.java