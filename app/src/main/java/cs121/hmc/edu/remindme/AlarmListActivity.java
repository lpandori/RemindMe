package cs121.hmc.edu.remindme;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

/*
 * Class: AlarmOverViewActivity.java
 * Authors: Heather Seaman, Laura Pandori, Rachelle, Holmgren, Tyra He
 * Last Updated: 04-23-2015
 *
 * Description: This class is the main activity which runs on startup immediately after
 * the splash screen disappears. It displays a list of alarm which are currently set by the
 * user. From here users can tap an alarm to see all the reminder times associated with
 * that alarm title. They can also add new alarms.
 */


public class AlarmListActivity extends ActionBarActivity {
    // A DBHelper allows access to the database, the adapter populates the ListView
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private AlarmListAdapter mAdapter;
    private Context mContext;
    public static SwipeToDismissTouchListener<ListViewAdapter> touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        // adapter populates the list of existing alarms in the database
        // each alarm will have a number >= 1 of ReminderTimes associated with it
        // as well as a title, snooze time, and alarm ringtone.
        mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
        setContentView(R.layout.activity_alarm_list);
        ListView alarmList=(ListView)findViewById(R.id.alarm_list);
        alarmList.setAdapter(mAdapter);

        // touchListener is applied to each list item in getView()
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
                                AlarmManagerHelper.cancelAlarms(mContext);
                                mAdapter.remove(position);
                                View thisView = lvAdapter.getChildAt(position);
                                long viewId = (long) thisView.getTag();
                                AlarmManagerHelper.cancelAlarms(mContext);
                                dbHelper.deleteAlarm(viewId);
                                AlarmManagerHelper.setAlarms(mContext);
                            }
                        });

        alarmList.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_list, menu);
        return true;
    }

    /*
     * The menu contains one button which allows users to add a new alarm
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new_alarm: {
                Intent intent = new Intent(this, SetAlarmInfo.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * onActivityResult sets the alarms after the workflow of creating a new
     * alarm is complete.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            mAdapter.setAlarms(dbHelper.getAlarms());
            mAdapter.notifyDataSetChanged();
        }
    }

    /*
     * setAlarmEnabled enables or disables an alarm with the given id
     */
    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmModel model = dbHelper.getAlarm(id);
        // update the model
        model.setEnabled(isEnabled);
        dbHelper.updateAlarm(model);
    }

    /*
     * startAlarmDetailsActivity is called when an alarm list item is
     * tapped. The MainActivity is started which displays a list of
     * all reminder times and relevant parameters associated with the alarm
     * with the given id
     */
    public void startAlarmDetailsActivity(long id, String title, String ringtone) {
        Intent intent = new Intent(this, ReminderListActivity.class);
        intent.putExtra(ReminderListActivity.EXISTING_MODEL_ID, id);
        intent.putExtra(ReminderListActivity.ALARM_NAME, title);
        intent.putExtra(ReminderListActivity.ALARM_TONE, ringtone);
        startActivity(intent);
    }

    /*
     * AlarmListAdapter populates the ListView with content from the database.
     * Each AlarmModel has a view defined to display the alarm's title, number of
      * reminder associated with that title, and the set snooze time.
     */
    static class AlarmListAdapter extends BaseAdapter {
        private Context mContext;
        private List<AlarmModel> mAlarms;
        //private AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

        public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
            mContext = context;
            mAlarms = alarms;
        }


        /*
         * sets the content of the adapter to be a list of AlarmModels
         */
        public void setAlarms(List<AlarmModel> alarms) {
            mAlarms = alarms;
        }

        /*
         * gets the number of elements in the adapter
         */
        @Override
        public int getCount() {
            if (mAlarms != null) {
                return mAlarms.size();
            }
            return 0;
        }

        /*
         * gets the object at a given position
         */
        @Override
        public Object getItem(int position) {
            if (mAlarms != null) {
                return mAlarms.get(position);
            }
            return null;
        }

        /*
         * gets the id of an item at a given position
         */
        @Override
        public long getItemId(int position) {
            if (mAlarms != null) {
                return mAlarms.get(position).getId();
            }
            return 0;
        }

        /*
         * remove an alarm from the adapter.
         * Note: this does not remove the alarm from the database as well!
         */
        public void remove(int position) {
            if (mAlarms != null) {
                mAlarms.remove(position);
                notifyDataSetChanged();
            }
        }

        /*
         * Defines how the view looks for a specific reminder item
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we are creating a View for the first time we must
            // call the inflater. Otherwise we can update an existing view
            // and skip this control block.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);
            }

            // Populate the views in alarm_list_item.xml
            final AlarmModel model = (AlarmModel) getItem(position);
            TextView txtName = (TextView) convertView.findViewById(R.id.alarm_item_name);
            txtName.setText(model.name);
            TextView snoozeTime = (TextView) convertView.findViewById(R.id.alarm_snoozeTime);
            snoozeTime.setText("" + model.getSnooze() + " min");
            TextView reminderTime = (TextView) convertView.findViewById(R.id.alarm_reminderCount);
            reminderTime.setText("" + model.getReminders().size());

            // Define behavior for the on/off toggle button which appears in each alarm
            ToggleButton btnToggle = (ToggleButton) convertView.findViewById(R.id.alarm_item_toggle);
            btnToggle.setChecked(model.isEnabled());
            btnToggle.setTag(model.getId());
            btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((AlarmListActivity) mContext).setAlarmEnabled(((Long)
                            buttonView.getTag()), isChecked);

                }
            });

            // Define the view for the revealed screen after a view is swiped
            // deleteView removes a alarm from the database
            View deleteView = convertView.findViewById(R.id.txt_delete);
            setOnClickForDelete(deleteView);

            convertView.setTag(model.getId());
            // this onClickListener is defined over the whole view.
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // When there are other reminder times which have been "swiped"
                    // with being deleted or undone, this control statement tells
                    // the activity to preserve these swiped items when another list
                    // item is tapped. This prevents users from losing reminders that
                    // they did not mean to delete.
                    if (touchListener.existPendingDismisses()){
                        touchListener.undoPendingDismiss();

                    // otherwise we start the MainActivity
                    } else {
                        ((AlarmListActivity) mContext).startAlarmDetailsActivity((Long) v.getTag(),
                                model.name, model.alarmToneStr);

                    }
                }
            });
            // adds swipe-to-delete functionality to each alarm list item
            convertView.setOnTouchListener(touchListener);

            return convertView;
        }
        // onClick method for the txt_delete portion of alarm_list_item.
        // When a user taps the portion reading "Confirm
        // Delete" the alarm is removed from the database
        private void setOnClickForDelete(View deleteView) {

            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener.existPendingDismisses()) {
                        touchListener.processPendingDismisses();
                    }
                }
            });
        } // end setOnClickForDelete
    }// end AlarmListAdapter
} //end class AlarmOverviewActivity