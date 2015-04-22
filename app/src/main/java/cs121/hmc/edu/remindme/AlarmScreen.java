package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by heatherseaman on 3/1/15.
 */
public class AlarmScreen extends Activity {

    public final String TAG = this.getClass().getSimpleName();
    private MediaPlayer mPlayer;
    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 60 * 1000;
    private Context context= this;
    private Context mContext;
    private long reminderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //Setting up layout
        this.setContentView(R.layout.alarm_screen);
        String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
        String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
        System.out.println("TONE IS FROM ALARM SCREEN: " + tone);
        reminderId = getIntent().getLongExtra(AlarmManagerHelper.REMINDER_ID, -1);


        TextView tvName = (TextView) findViewById(R.id.alarm_screen_name);
        tvName.setText(name);
//        TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);
//        tvName.setText(name);



//        Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);
//        dismissButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.stop();
//                finish();
//            }
//        });

        findViewById(R.id.dismiss_start).setOnTouchListener(new DismissTouchListener());
        findViewById(R.id.dismiss_end).setOnDragListener(new DismissDragListener());





        mPlayer = new MediaPlayer();
        try {
            if (tone != null && !tone.equals("")) {
                Uri toneUri = Uri.parse(tone);
                //MediaPlayer mPlayer = MediaPlayer.create(mContext, toneUri);

                System.out.println("TONE is: " + tone);
                System.out.println("URITONE is: " + toneUri.toString());
//                Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
//                Ringtone defaultRingtone = RingtoneManager.getRingtone(getApplicationContext(), defaultRingtoneUri);
//                defaultRingtone.play();
                if(toneUri instanceof Uri){
                    Log.d("URI?", "I AM A URI!!!!!!!!!!");
                }

                if (toneUri != null) {
                    mPlayer.setDataSource(getApplicationContext(), toneUri);
                    mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mPlayer.setLooping(true);
                    mPlayer.prepare();
                    mPlayer.start();
                }
                else{
                    System.out.println("TONE URI IS NULL!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Add respond action to snooze button - UI for Snooze
        Button snoozeButton = (Button) findViewById(R.id.snooze);
        snoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Work with alarmDBHelper
                AlarmDBHelper dbHelper = new AlarmDBHelper(context);
                dbHelper.snoozeReminder(reminderId);
                AlarmManagerHelper.setAlarms(context);

                mPlayer.stop();
                finish();
            }
        });

        // Ensure wakelock release
        Runnable releaseWakeLock = new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
                if(mPlayer != null) {
                    mPlayer.release();
                }
            }
        };

        new Handler().postDelayed(releaseWakeLock, WAKELOCK_TIMEOUT);


    }

    private final class DismissTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }
            else return false;
        }
    }

    class DismissDragListener implements View.OnDragListener {

//        Drawable enterShape = getResources().getDrawable(R.drawable.oval_selected);
//        Drawable normalShape = getResources().getDrawable(R.drawable.oval_droptarget);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            // Handles each of the expected events
            switch (action) {
                //signal for the start of a drag and drops operation
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                // the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                    // the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:

                case DragEvent.ACTION_DROP:
                    // if the view is on the dismiss button, we accept the
                    // the drag item
                    if (v == findViewById(R.id.dismiss_end)) {
                        // Dropped, reassign View to ViewGroup
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        LinearLayout container = (LinearLayout) v;
                        container.addView(view);
                        view.setVisibility(View.INVISIBLE);
                        v.setVisibility(View.INVISIBLE);

                        AlarmDBHelper dbHelper = new AlarmDBHelper(context);
                        dbHelper.dismiss(reminderId);
                        finish();
                        break;
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        Toast.makeText(context, "You can't drop the image here",
                                Toast.LENGTH_LONG).show();
                        break;
                    }

                    // drag shadow has been released, the drag point is within
                    // within the bounding box of the View

                default:
                    View view2 = (View) event.getLocalState();
                    view2.setVisibility(View.VISIBLE);
//                    Context context2 = getApplicationContext();
//                    Toast.makeText(context2, "You can't drop the image here",
//                            Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        }

    }


    @Override
    protected  void onResume() {
        super.onResume();

        // Set the window to keep screen on
        // Force the screen on after wake on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        // Keep the screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Show on top of keyguard
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        // Dismiss the keyguard if unsecured
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//        Acquire Wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }
        if(!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mPlayer != null) {
            mPlayer.release();
        }

        if((mWakeLock != null) && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}
