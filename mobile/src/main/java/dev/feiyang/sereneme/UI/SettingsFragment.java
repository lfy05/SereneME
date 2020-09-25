package dev.feiyang.sereneme.UI;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import dev.feiyang.sereneme.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}