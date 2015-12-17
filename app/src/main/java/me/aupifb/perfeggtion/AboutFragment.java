package me.aupifb.perfeggtion;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by aupifb on 17/12/2015.
 */
public class AboutFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.backofficepreferences);

        Preference pref_version = findPreference("pref_licenses");
        pref_version.setSummary(BuildConfig.VERSION_NAME);

        Preference licensesPreference = findPreference("licensesPreference");
        licensesPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                android.support.v4.app.DialogFragment licensesDialogFragment = new LicensesDialogFragment();
                licensesDialogFragment.show(getFragmentManager(), "licensesDialogFragment");
                return false;
            }
        });
    }
}
