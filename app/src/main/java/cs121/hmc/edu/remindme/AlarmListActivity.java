package cs121.hmc.edu.remindme;


//import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
//import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;
import android.widget.ToggleButton;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

//import static android.widget.Toast.LENGTH_SHORT;

public class AlarmListActivity extends ActionBarActivity {

    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    private AlarmListAdapter mAdapter;
    private Context mContext;
    public static SwipeToDismissTouchListener<ListViewAdapter> touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
        setContentView(R.layout.activity_alarm_list);
        ListView alarmList=(ListView)findViewById(R.id.list);

//
//        addReminder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity
//            }
//        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_new_alarm: {
                //startAlarmDetailsActivity(-1);
                Intent intent = new Intent(this, SetName.class);
                startActivity(intent);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            mAdapter.setAlarms(dbHelper.getAlarms());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setAlarmEnabled(long id, boolean isEnabled) {
        AlarmManagerHelper.cancelAlarms(this);
        AlarmModel model = dbHelper.getAlarm(id);
        model.setEnabled(isEnabled);
        dbHelper.deleteAlarm(id);
        dbHelper.createAlarm(model);

        // refreshing the adapter after the state of the toggle has changed
        // in the first list view item
        AlarmManagerHelper.setAlarms(this);
    }

    public void startAlarmDetailsActivity(long id, String title) {
        Intent intent = new Intent(this, AlarmDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("alarm-title", title);
        startActivity(intent);
    }

    /*
     * ADAPTER CLASS!!!! YAY
     */
    static class AlarmListAdapter extends BaseAdapter {
        private Context mContext;
        private List<AlarmModel> mAlarms;
        private AlarmDBHelper dbHelper = new AlarmDBHelper(mContext);

        public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
            mContext = context;
            mAlarms = alarms;
        }


        public void setAlarms(List<AlarmModel> alarms) {
            mAlarms = alarms;
        }

        @Override
        public int getCount() {
            if (mAlarms != null) {
                return mAlarms.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mAlarms != null) {
                return mAlarms.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (mAlarms != null) {
                return mAlarms.get(position).getId();
            }
            return 0;
        }

        public void remove(int position) {
            if (mAlarms != null) {
                mAlarms.remove(position);
                notifyDataSetChanged();
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);
            }

            // get Item implemented above
            final AlarmModel model = (AlarmModel) getItem(position);

//            TextView txtTime = (TextView) convertView.findViewById(R.id.alarm_item_time);
//            txtTime.setText(String.format("%02d : %02d", model.timeHour, model.timeMinute));
            TextView txtName = (TextView) convertView.findViewById(R.id.alarm_item_name);
            txtName.setText(model.name);

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
            View deleteView = convertView.findViewById(R.id.txt_delete);
            setOnClickForDelete(deleteView);
            convertView.setTag(model.getId());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (touchListener.existPendingDismisses()){
                        touchListener.undoPendingDismiss();
                    } else {
                        ((AlarmListActivity) mContext).startAlarmDetailsActivity((Long) v.getTag(), model.name);
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
                view.setTextColor(Color.BLACK);
            }
        }


    }

}