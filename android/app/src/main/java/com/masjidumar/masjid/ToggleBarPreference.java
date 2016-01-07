package com.masjidumar.masjid;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

/**
 * Created by Haidar on 6/27/2015.
 *
 * a Preference that is a row of two state buttons
 * the persisted preference is a String who's length is equal to the number of buttons
 * every position in the string corresponds to a button
 * and is either '1' or '0' indicating the state of the button
 */
public class ToggleBarPreference extends Preference {
    private CharSequence[] mEntries;
    private String DEFAULT_VALUE;
    private String currentValue;
    private final int offset = 787;

    public ToggleBarPreference(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ToggleBarPreference);

        mEntries = a.getTextArray(R.styleable.ToggleBarPreference_entries);
        //        create a string of 1's the length of entries as a default value
        DEFAULT_VALUE = new String(new char[mEntries.length]).replace("\0", "1");
        a.recycle();
    }

    public ToggleBarPreference(Context context){
        this(context, null);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            currentValue = this.getPersistedString(DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            currentValue = (String) defaultValue;
            persistString(currentValue);
        }

        super.onSetInitialValue(restorePersistedValue, defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected View onCreateView( ViewGroup parent ) {
        super.onCreateView(parent);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = li.inflate( R.layout.toggle_bar, parent, false);

        currentValue = getPersistedString(DEFAULT_VALUE);

        RelativeLayout toggleBar = (RelativeLayout) rowView.findViewById(R.id.toggle_bar);

        for (int i = 0; i < mEntries.length; i++){
            ToggleButton tB = new ToggleButton(getContext());
            tB.setTextOff(mEntries[i]);
            tB.setTextOn(mEntries[i]);
            tB.setText(mEntries[i]);
            int id = offset+i;
            tB.setId(id);
            //tB.setPadding(2,2,2,2);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = 130;
            if(i == 0){
                //layoutParams.addRule(RelativeLayout.START_OF, id+1);
            }
            else{
                layoutParams.addRule(RelativeLayout.END_OF, id-1);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, id-1);
            }
            tB.setLayoutParams(layoutParams);

            tB.setOnClickListener(onToggleListener);
            if(currentValue.charAt(i) == '1'){
                tB.setChecked(true);
            } else{
                tB.setChecked(false);
            }
            toggleBar.addView(tB);
        }

        return rowView;
    }

    public View.OnClickListener onToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            int index = -1;
            for(int i = 0; i < mEntries.length; i++){
                if(i+offset == id){
                    index = i;
                    break;
                }
            }
            if(index != -1) {
                // update the persistent string array
                currentValue = getPersistedString(DEFAULT_VALUE);
                char[] charCurrentValue = currentValue.toCharArray();
                if (((ToggleButton) v).isChecked()) {
                    charCurrentValue[index] = '1';
                } else {
                    charCurrentValue[index] = '0';
                }
                currentValue = new String(charCurrentValue);
                persistString(currentValue);
            }
        }
    };

}
