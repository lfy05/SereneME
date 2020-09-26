package dev.feiyang.sereneme.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Calendar;

import dev.feiyang.sereneme.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPreferences;
    private Preference setTimePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
        setTimePreference = findPreference("set_reminder_time_preference");
        updateSetTimerPreferenceSummary();
        Preference setReminderPreference = findPreference("notification_category");
        setReminderPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateSetTimerPreferenceSummary();
                return true;
            }
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                updateSetTimerPreferenceSummary();
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

                        AlarmManager alarmManager =
                                (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minuteOfHour);

                        Intent intent = new Intent(getContext(), ReminderBroadcastReceiver.class);
                        PendingIntent notificationIntent = PendingIntent.getBroadcast(getContext(),
                                0, intent, 0);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                notificationIntent);
                    }
                });
                timePickerDialog.show(getParentFragmentManager(), "Time Picker");
                return true;
            }
        });
    }

    private void updateSetTimerPreferenceSummary(){
        if (sharedPreferences.getBoolean("reminder", false)){
            setTimePreference.setSummary(
                    getResources().getString(R.string.reminder_manual_time_summary,
                            sharedPreferences.getInt("reminder_hour", 0),
                            sharedPreferences.getInt("reminder_minute", 0)));
        } else
            setTimePreference.setSummary(
                    getResources().getString(R.string.no_reminder_summary));
    }
}