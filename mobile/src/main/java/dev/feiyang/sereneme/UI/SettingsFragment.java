package dev.feiyang.sereneme.UI;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import dev.feiyang.sereneme.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
        final Preference setTimePreference = findPreference("set_reminder_time_preference");
        if (sharedPreferences.getBoolean("reminder", false)){
            setTimePreference.setSummary(
                    getResources().getString(R.string.reminder_manual_time_summary,
                            sharedPreferences.getInt("reminder_hour", 0),
                            sharedPreferences.getInt("reminder_minute", 0)));
        } else
            setTimePreference.setSummary(
                    getResources().getString(R.string.no_reminder_summary));

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (sharedPreferences.getBoolean("reminder", false)){
                    setTimePreference.setSummary(getResources().getString(
                            R.string.reminder_manual_time_summary,
                            sharedPreferences.getInt("reminder_hour", 0),
                            sharedPreferences.getInt("reminder_minute", 0)));
                } else{
                    setTimePreference.setSummary(getResources().getString(R.string.no_reminder_summary));
                }
            }
        });

        setTimePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                TimePicker timePickerDialog = new TimePicker();
                timePickerDialog.setTimePickedListener(new TimePicker.TimePickedListener() {
                    @Override
                    public void onTimePicked(int hourOfDay, int minuteOfHour) {
                        sharedPreferenceEditor.putInt("reminder_hour", hourOfDay);
                        sharedPreferenceEditor.putInt("reminder_minute", minuteOfHour);
                        sharedPreferenceEditor.commit();
                    }
                });
                timePickerDialog.show(getParentFragmentManager(), "Time Picker");
                return true;
            }
        });
    }
}