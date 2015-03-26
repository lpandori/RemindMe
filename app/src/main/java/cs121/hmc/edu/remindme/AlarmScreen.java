package cs121.hmc.edu.remindme;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by heatherseaman on 3/1/15.
 */
//TODO medium changes to handle new alarmModel
public class AlarmScreen extends Activity {

    public final String TAG = this.getClass().getSimpleName();
    private MediaPlayer mPlayer;
    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up layout
        this.setContentView(R.layout.alarm_screen);
        String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
        //int timeHour = getIntent().getIntExtra(AlarmManagerHelper.TIME_HOUR, 0);
        //int timeMinute = getIntent().getIntExtra(AlarmManagerHelper.TIME_MINUTE, 0);

        TextView tvName = (TextView) findViewById(R.id.alarm_screen_name);
        tvName.setText(name);
        //TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);//TODO remove time textview
        //tvTime.setText(String.format("02d : %02d", timeHour, timeMinute));

        Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                finish();
            }
        });

        //TODO temporarily removed
//        String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
//        mPlayer = new MediaPlayer();
//        try {
//            if (tone != null && !tone.equals("")) {
//                Uri toneUri = Uri.parse(tone);
//                if (toneUri != null) {
//                    mPlayer.setDataSource(this, toneUri);
//                    mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//                    mPlayer.setLooping(true);
//                    mPlayer.prepare();
//                    mPlayer.start();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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
            }
        };

        new Handler().postDelayed(releaseWakeLock, WAKELOCK_TIMEOUT);


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

        // Acquire Wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }
        if(!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mWakeLock != null & mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}
