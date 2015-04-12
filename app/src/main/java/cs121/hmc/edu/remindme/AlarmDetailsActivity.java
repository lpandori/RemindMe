package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heatherseaman on 2/14/15.
 * Activity will show alarm details
 */


//TODO commented out for now
public class AlarmDetailsActivity extends ActionBarActivity {

    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private ReminderListAdapter mAdapter;
    private Context mContext;
    public static SwipeToDismissTouchListener<ListViewAdapter> touchListener;

    public static String EXISTING_MODEL = "existing_model";
    public static String EXISTING_MODEL_ID = "existing_model_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);

//        getSupportActionBar().setTitle("Create New Alarm");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent prevIntent = getIntent(); // gets the previously created intent
        final long alarmId = prevIntent.getLongExtra("id", -1);
        final String alarmTitle = prevIntent.getStringExtra("alarm-title");

        mContext = this;
        mAdapter = new ReminderListAdapter(this, dbHelper.getAlarm(alarmId).getReminders());
        setContentView(R.layout.activity_details);

        getSupportActionBar().setTitle(alarmTitle);
        ListView alarmList = (ListView)findViewById(R.id.reminder_list);
        View addReminder = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_reminder, null, false);
        alarmList.addFooterView(addReminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(mContext, AlarmFrequency.class);
                intent.putExtra(EXISTING_MODEL_ID, alarmId);//need model id
                intent.putExtra(SetName.ALARM_NAME, alarmTitle);
                intent.putExtra(EXISTING_MODEL, true);
                //need model
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


//        public void setAlarms(ArrayList<ReminderTime> reminders) {
//            mReminders = reminders;
//        }

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
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.reminder_list_item, parent, false);
            }

            // get Item implemented above
            final ReminderTime reminderTime = (ReminderTime) getItem(position);
            String toDisplay = "";
            String timeHour = "" + reminderTime.getHour();
            String timeMinute = "" + reminderTime.getMin();
            char[] dayLetters = {'s','m','t','w','t','f','s'};
            switch(reminderTime.getReminderType()){
                case ReminderTime.ONE_TIME:
                    toDisplay = reminderTime.getDateString();
                    break;
                case ReminderTime.DAILY:
                    toDisplay = "DAILY at";
                    break;
                case ReminderTime.WEEKLY:
                    String dayOfWeek = reminderTime.getWeekdays();
                    char[] weekBools = dayOfWeek.toCharArray();
                    for(int i=0; i < weekBools.length;i++){
                        toDisplay += weekBools[i] == '0' ? dayLetters[i] : Character.toUpperCase(dayLetters[i]);
                    }

                    break;
                case ReminderTime.MONTHLY:
                    toDisplay = "Every " + reminderTime.getWeekOfMonth();
                    String week = reminderTime.getWeekdays();
                    int dayIndex = week.indexOf('1');
                    toDisplay += " " + dayLetters[dayIndex];
                    break;
            }

            TextView txtDisplay = (TextView) convertView.findViewById(R.id.reminder_text);
            txtDisplay.setText(toDisplay);
            TextView txtTime = (TextView) convertView.findViewById(R.id.reminder_item_time);
            txtTime.setText(String.format("%02d : %02d", Integer.parseInt(timeHour), Integer.parseInt(timeMinute)));


//            ToggleButton btnToggle = (ToggleButton) convertView.findViewById(R.id.alarm_item_toggle);
//            btnToggle.setChecked(model.isEnabled());
//            btnToggle.setTag(model.getId());
//            btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    ((AlarmListActivity) mContext).setAlarmEnabled(((Long)
//                            buttonView.getTag()), isChecked);
//                    dbHelper.updateAlarm(model);
//
//                }
//            });
            View deleteView = convertView.findViewById(R.id.txt_delete);
            setOnClickForDelete(deleteView);
            convertView.setTag(reminderTime.getId());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener.existPendingDismisses()){
                        touchListener.undoPendingDismiss();
                    }
                    // TODO:create edit activity here
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

//        private void updateTextColor(TextView view, boolean isOn) {
//            if (isOn) {
//                view.setTextColor(Color.GREEN);
//            } else {
//                view.setTextColor(Color.BLACK);
//            }
//        }


    }


}
