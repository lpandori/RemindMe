package cs121.hmc.edu.remindme;


//import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import static android.widget.Toast.LENGTH_SHORT;

public class AlarmListActivity extends ActionBarActivity {

    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    private AlarmListAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
        setContentView(R.layout.activity_alarm_list);
        ListView alarmList=(ListView)findViewById(R.id.list);
        alarmList.setAdapter(mAdapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
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


        alarmList.setOnTouchListener(touchListener);
        alarmList.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
//        alarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (touchListener.existPendingDismisses()) {
//                    touchListener.undoPendingDismiss();
//                } else {
//                    Toast.makeText(AlarmListActivity.this, "Position " + position, LENGTH_SHORT).show();
//                }
//            }
//        });
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
                startAlarmDetailsActivity(-1);
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
        model.isEnabled = isEnabled;
        dbHelper.updateAlarm(model);

        // refreshing the adapter after the state of the toggle has changed
        // in the first list view item
        AlarmManagerHelper.setAlarms(this);
    }

    public void startAlarmDetailsActivity(long id) {
        Intent intent = new Intent(this, AlarmDetailsActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, 0);
    }
}