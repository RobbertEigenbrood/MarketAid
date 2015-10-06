package group6.kb_50.marketaid;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import group6.kb_50.marketaid.R;

/**
 * Created by Robbert on 15-9-2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
