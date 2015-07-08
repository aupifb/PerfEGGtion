package me.aupifb.perfeggtion;


import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment implements Preference.OnPreferenceClickListener {

    private int pref_easter_egg = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the generalpreferences from an XML resource
        addPreferencesFromResource(R.xml.generalpreferences);
        addPreferencesFromResource(R.xml.miscpreferences);

        Preference preferencebutton = findPreference("preferencebutton");
        preferencebutton.setOnPreferenceClickListener(this);

        Preference pref_version = findPreference("pref_version");
        pref_version.setSummary(BuildConfig.VERSION_NAME);
        pref_version.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "preferencebutton":
                DialogFragment newFragment2 = TimerConflictDialogFragment.newInstance(
                        R.string.alert_reset);
                newFragment2.show(getFragmentManager(), "resetdialog");
                return false;
            case "pref_version":
                ++pref_easter_egg;
                if (pref_easter_egg == 7) {
                    Toast.makeText(getActivity(), "GG", Toast.LENGTH_SHORT).show();
                    pref_easter_egg = 0;
                }
                return true;
            default:
                Log.d("g", "onPreferenceClick ");
                return true;

        }
    }

    public void resetprefs(){
        PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .edit()
                .clear()
                .commit();
        PreferenceManager.setDefaultValues(getActivity(), R.xml.generalpreferences, true);
        setPreferenceScreen(null);
        addPreferencesFromResource(R.xml.generalpreferences);
        addPreferencesFromResource(R.xml.miscpreferences);
        Preference preferencebutton = findPreference("preferencebutton");
        preferencebutton.setOnPreferenceClickListener(this);
        Preference pref_version = findPreference("pref_version");
        pref_version.setSummary(BuildConfig.VERSION_NAME);
        pref_version.setOnPreferenceClickListener(this);
    }

}
