package me.aupifb.perfeggtion;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by aupifb on 17/12/2015.
 */
public class AboutFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.backofficepreferences);
    }
}
