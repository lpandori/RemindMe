package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

/**
 * Created by heatherseaman on 2/28/15.
 * Adapted Alarms from database to display in the layout
 */
public class AlarmListAdapter extends BaseAdapter {
    private Context mContext;
    private List<AlarmModel> mAlarms;

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
            return mAlarms.get(position).id;
        }
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, parent, false);
            //view = inflater.inflate(R.layout.alarm_list_item, parent, false);
        }

        // get Item implemented above
        AlarmModel model = (AlarmModel) getItem(position);

        //TextView txtTime = (TextView) view.findViewById(R.id.alarm_item_time);
        //txtTime.setText(String.format("%02d : %02d", model.timeHour, model.timeMinute));
        TextView txtName = (TextView) view.findViewById(R.id.alarm_item_name);
        txtName.setText(model.name);

//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_sunday), model.getRepeatingDay(AlarmModel.SUNDAY));
//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_monday), model.getRepeatingDay(AlarmModel.MONDAY));
//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_tuesday), model.getRepeatingDay(AlarmModel.TUESDAY));
//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_wednesday), model.getRepeatingDay(AlarmModel.WEDNESDAY));
//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_thursday), model.getRepeatingDay(AlarmModel.THURSDAY));
//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_friday), model.getRepeatingDay(AlarmModel.FRIDAY));
//        updateTextColor((TextView) view.findViewById(R.id.alarm_item_saturday), model.getRepeatingDay(AlarmModel.SATURDAY));

        ToggleButton btnToggle = (ToggleButton) view.findViewById(R.id.alarm_item_toggle);
        btnToggle.setChecked(model.isEnabled());
        btnToggle.setTag(model.id);
        //TODO will need to put this back in (but working
//        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ((AlarmListActivity) mContext).setAlarmEnabled(((Long)
//                        buttonView.getTag()), isChecked);
//            }
//        });
        view.setTag(model.id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlarmListActivity) mContext).startAlarmDetailsActivity((Long) v.getTag());
            }
        });

        Button edit = (Button) view.findViewById(R.id.edit);
        edit.setTag(model.id);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AlarmListActivity) mContext).startAlarmDetailsActivity((Long) view.getTag());
            }
        });

        Button addNew = (Button) view.findViewById(R.id.newTime);
        //addNew.setTag(model.id);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent((AlarmListActivity)mContext, AlarmFrequency.class);
                ((AlarmListActivity)mContext).startActivity(intent);

                //((AlarmListActivity) mContext).startSetNameActivity();
                //mContext.startActivity(i);
            }
        });
        return view;
    }

    private void updateTextColor(TextView view, boolean isOn) {
        if (isOn) {
            view.setTextColor(Color.parseColor("#33B5E5"));
        } else {
            view.setTextColor(Color.BLACK);
        }
    }


}
