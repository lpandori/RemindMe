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
 * Created by heatherseaman on 2/14/15.
 * Activity will show alarm details
 */


//TODO commented out for now
public class MainActivity extends ActionBarActivity {

    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private ReminderListAdapter mAdapter;
    private Context mContext;
    public static SwipeToDismissTouchListener<ListViewAdapter> touchListener;

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

    private static String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static String[] stringIndices = {"1st", "2nd", "3rd", "4th", "5th"};


    public static String alarm_tone = "Default";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);

        Intent prevIntent = getIntent(); // gets the previously created intent
        alarmId = prevIntent.getLongExtra(EXISTING_MODEL_ID, -1);
        alarmTitle = prevIntent.getStringExtra(ALARM_NAME);
        alarm_tone = prevIntent.getStringExtra(ALARM_TONE);
        ArrayList<ReminderTime> reminderList = dbHelper.getAlarm(alarmId).getReminders();

        mContext = this;
        mAdapter = new ReminderListAdapter(this, reminderList);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setTitle(alarmTitle);

        ListView alarmList = (ListView)findViewById(R.id.reminder_list);
        View addReminder = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_reminder, null, false);
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
            case R.id.home_button:
                Intent i = new Intent(MainActivity.this, AlarmOverviewActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        Intent i = new Intent(MainActivity.this, AlarmOverviewActivity.class);
        startActivity(i);
    }

    /*
         * ADAPTER CLASS!!!! YAY
         */
    static class ReminderListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ReminderTime> mReminders;
        private AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

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

        @Override
        public Object getItem(int position) {
            if (mReminders != null) {
                return mReminders.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mReminders != null) {
                return mReminders.get(position).getId();

            }
            return 0;
        }

        public void remove(int position) {
            if (mReminders != null) {
                mReminders.remove(position);
                notifyDataSetChanged();
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // get Item implemented above
            final ReminderTime reminderTime = (ReminderTime) getItem(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(reminderTime.getReminderType() == ReminderTime.WEEKLY) {
                    convertView = inflater.inflate(R.layout.weekly_reminder_item, parent, false);
                    return getWeeklyView(position, convertView, parent);
                }
                else {
                    convertView = inflater.inflate(R.layout.reminder_list_item, parent, false);
                }
            }

            final long reminderId = getItemId(position);

            String toDisplay = "";
            final String timeHour = "" + reminderTime.getHour();
            String timeMinute = "" + reminderTime.getMin();
            switch(reminderTime.getReminderType()){
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
                case ReminderTime.DAILY:
                    toDisplay = "Daily at";
                    break;
                case ReminderTime.MONTHLY:
                    int weekOfMonth = reminderTime.getWeekOfMonth();
                    String weekString = stringIndices[weekOfMonth - 1];
                    String week = reminderTime.getWeekdays();
                    int dayIndex = week.indexOf('1');
                    String dayString = weekdays[dayIndex];
                    toDisplay = "Every " + weekString + " " + dayString + " at";
                    break;
            }

            TextView txtDisplay = (TextView) convertView.findViewById(R.id.reminder_text);
            txtDisplay.setText(toDisplay);
            TextView txtTime = (TextView) convertView.findViewById(R.id.reminder_item_time);
            txtTime.setText(String.format("%02d : %02d", Integer.parseInt(timeHour), Integer.parseInt(timeMinute)));

            Button btn_edit = (Button) convertView.findViewById(R.id.reminder_edit_button);
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j;
                    switch(reminderTime.getReminderType()) {
                        case ReminderTime.ONE_TIME:
                            j = new Intent(mContext, EditOneTime.class);

                            //parse date
                            String dString = reminderTime.getDateString();//in format yyyy-mm-dd
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

            View deleteView = convertView.findViewById(R.id.txt_delete);
            setOnClickForDelete(deleteView);
            convertView.setTag(reminderTime.getId());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j;
                    if (touchListener.existPendingDismisses()){
                        touchListener.undoPendingDismiss();
                    }
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


            convertView.setOnTouchListener(touchListener);

            return convertView;
        }

        private View getWeeklyView(int position, View convertView, ViewGroup parent) {

            final ReminderTime reminderTime = (ReminderTime) getItem(position);
            final long reminderId = getItemId(position);
            final String timeHour = "" + reminderTime.getHour();
            final String timeMinute = "" + reminderTime.getMin();

            TextView txtTime = (TextView) convertView.findViewById(R.id.reminder_item_time);
            txtTime.setText(String.format("%02d : %02d", Integer.parseInt(timeHour), Integer.parseInt(timeMinute)));

            // The weekdays for which the alarm should ring
            String whichWeekdays = reminderTime.getWeekdays();
            char[] weekBools = whichWeekdays.toCharArray();

            TextView[] viewArray = {(TextView) convertView.findViewById(R.id.alarm_item_sunday),
                    (TextView)convertView.findViewById(R.id.alarm_item_monday),
                    (TextView)convertView.findViewById(R.id.alarm_item_tuesday),
                    (TextView)convertView.findViewById(R.id.alarm_item_wednesday),
                    (TextView)convertView.findViewById(R.id.alarm_item_thursday),
                    (TextView)convertView.findViewById(R.id.alarm_item_friday),
                    (TextView)convertView.findViewById(R.id.alarm_item_saturday),
            };
            for(int i=0; i < weekBools.length;i++){
                if (weekBools[i] == '1') {
                    updateTextColor(viewArray[i], true);
                }
                else if (weekBools[i] == '0') {
                    updateTextColor(viewArray[i], false);
                }
            }

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

            View deleteView = convertView.findViewById(R.id.txt_delete);
            setOnClickForDelete(deleteView);
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

        private void updateTextColor(TextView view, boolean isOn) {
            if (isOn) {
                view.setTextColor(Color.GREEN);
            } else {
                view.setTextColor(Color.WHITE);
            }
        }


    }


}
