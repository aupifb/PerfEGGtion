package me.aupifb.perfeggtion;


import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    boolean hasvibratorboolean;
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

        hasvibratorboolean = ((PreferenceActivity) getActivity()).vibratorcheck();
        if (hasvibratorboolean) {
            Log.d("lol", "YES IT VIBRATES ");
        } else {
            Log.d("lol", "NO VIBRATES ");
            PreferenceCategory pref_general = (PreferenceCategory) findPreference("pref_general");
            Preference vibratorpreference = findPreference("vibratorpreference");
            pref_general.removePreference(vibratorpreference);
        }

        Preference ringtonepreference = findPreference("ringtonepreference");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        String strRingtonePreference = sharedPref.getString("ringtonepreference", getString(R.string.preference_alarm_tone_summary));
        if (strRingtonePreference.equals(getString(R.string.preference_alarm_tone_summary))) {
            ringtonepreference.setSummary(strRingtonePreference);
        } else {
            Uri ringtoneUri = Uri.parse(strRingtonePreference);
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
            String name = ringtone.getTitle(getActivity());
            ringtonepreference.setSummary(name);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case "preferencebutton":
                DialogFragment newFragment2 = ResetDialogFragment.newInstance(
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("ringtonepreference")) {
            Preference ringtonepreference = findPreference("ringtonepreference");
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String strRingtonePreference = sharedPref.getString("ringtonepreference", getString(R.string.preference_alarm_tone_summary));
            if (strRingtonePreference.equals(getString(R.string.preference_alarm_tone_summary))) {
                ringtonepreference.setSummary(strRingtonePreference);
            } else {
                Uri ringtoneUri = Uri.parse(strRingtonePreference);
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
                String name = ringtone.getTitle(getActivity());
                ringtonepreference.setSummary(name);
            }
        }
    }
}
