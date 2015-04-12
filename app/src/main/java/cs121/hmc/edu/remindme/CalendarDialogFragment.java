package cs121.hmc.edu.remindme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by rachelleholmgren on 3/22/15.
 */
public class CalendarDialogFragment extends DialogFragment{
    private CalendarView calendar;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.calendar_main, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Choose a date");
        alertDialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Date set", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog = alertDialogBuilder.create();
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putFloat(KEY_SAVE_RATING_BAR_VALUE, mRatingBar.getRating());
        super.onSaveInstanceState(outState);
    }



}
