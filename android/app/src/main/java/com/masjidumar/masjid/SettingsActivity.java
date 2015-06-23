package com.masjidumar.masjid;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        public static final String STATE_ENABLE_KEY = "state_enable";
        public static final String AUDIO_STATE_KEY = "audio_state";
        public static final String TIME_BEFORE_KEY = "time_before";
        public static final String TIME_DURATION_KEY = "time_duration";
        public static final String GEOLOCATION_KEY = "geolocation";
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);

            // set summaries to current values
            //get preferences
            Context hostActivity = getActivity();
            SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(hostActivity);
            Preference pref;
            String[] keys = {AUDIO_STATE_KEY, TIME_BEFORE_KEY, TIME_DURATION_KEY};
            for(String key : keys){
                pref = findPreference(key);
                pref.setSummary(sP.getString(key,""));
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
            Preference pref = findPreference(key);
            switch(key){
                case AUDIO_STATE_KEY:
                    pref.setSummary(sharedPreferences.getString(key, ""));
                    break;
                case TIME_BEFORE_KEY:
                    pref.setSummary(sharedPreferences.getString(key, ""));
                    break;
                case TIME_DURATION_KEY:
                    pref.setSummary(sharedPreferences.getString(key, ""));
                    break;
            }
        }
    }


}