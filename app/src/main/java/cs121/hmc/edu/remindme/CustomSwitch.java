package cs121.hmc.edu.remindme;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by heatherseaman on 2/18/15.
 * This class creates a toggle slider to turn alarms on or off
 * on specific days
 */

public class CustomSwitch extends FrameLayout {

    private TextView label;
    private CompoundButton button;

    public CustomSwitch (Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize and configure the layout
        RelativeLayout layout = new RelativeLayout(context);
        //    Set height and width of the ViewGroup
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                button.toggle();
            }
        });

//        layout.setBackgroundResource(R.drawable.view_touch_selector);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            button = new Switch(context);
        } else {
            button = new CheckBox(context);
        }
        button.setId(1);

        // Initialise and configure button label
        label = new TextView(context);
        label.setId(2);

        //    Set height and width of the ViewGroup
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.topMargin = 16;
        buttonParams.bottomMargin = 16;
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonParams.addRule(RelativeLayout.ALIGN_RIGHT, 2);


        RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        labelParams.leftMargin = 8;
        labelParams.addRule(RelativeLayout.ALIGN_BASELINE, 1);

        // Empty view to force bottom margin
        View emptyView = new View(context);
        RelativeLayout.LayoutParams emptyViewParams = new RelativeLayout.LayoutParams(0,0);
        emptyViewParams.addRule(RelativeLayout.BELOW, button.getId());

        //Add out components to the layout
        layout.addView(label, labelParams);
        layout.addView(button, buttonParams);
        layout.addView(emptyView, emptyViewParams);
        addView(layout);

        // Manage attributes
        int[] attributeSet = {android.R.attr.text, android.R.attr.checked};

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, attributeSet, 0, 0);
        try {
            // get text in a at index 0
            label.setText(a.getText(0));
            button.setChecked(a.getBoolean(2, false));
        } finally {
            a.recycle();
        }
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setChecked(boolean isChecked) {
        button.setChecked(isChecked);
    }

    public boolean isChecked() {
        return button.isChecked();
    }
}
