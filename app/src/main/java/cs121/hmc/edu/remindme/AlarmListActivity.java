package cs121.hmc.edu.remindme;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
                startSetNameActivity();
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
        AlarmModel model = dbHelper.getAlarm(id);
        model.isEnabled = isEnabled;
        dbHelper.updateAlarm(model);
        // refreshing the adapter after the state of the toggle has changed
        // in the first list view item
        mAdapter.setAlarms(dbHelper.getAlarms());
        mAdapter.notifyDataSetChanged();
        AlarmManagerHelper.setAlarms(this);
    }

    public void startAlarmFrequencyActivity(long id) {
        //Intent intent = new Intent(this, AlarmDetailsActivity.class);
        //intent.putExtra("id", id);
        //startActivityForResult(intent, 0);
        Intent intent = new Intent(AlarmListActivity.this,AlarmFrequency.class);
        //intent.putExtra("id",id);
        startActivity(intent);

    }

    public void startAlarmDetailsActivity(long id){
        Intent intent = new Intent(this, AlarmDetailsActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent,0);
    }

    public void startSetNameActivity(){
        Intent intent = new Intent(AlarmListActivity.this, SetName.class);
        //intent.putExtra("id",id);
        startActivity(intent);
    }
}