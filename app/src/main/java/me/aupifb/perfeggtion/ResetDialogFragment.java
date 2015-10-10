package me.aupifb.perfeggtion;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetDialogFragment extends DialogFragment {


    public static ResetDialogFragment newInstance(int title) {
        ResetDialogFragment fraga = new ResetDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        fraga.setArguments(args);
        return fraga;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        return new AlertDialog.Builder(getActivity())
                /*.setIcon(R.drawable.ic_launcher)*/
                .setTitle(title)
                .setMessage(R.string.alertresettext)
                .setPositiveButton(R.string.alert_dialog_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                PreferenceFragment fragmentset = (PreferenceFragment) getFragmentManager().findFragmentByTag("PreferenceFragmentTAG");
                                fragmentset.resetprefs();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();
    }

}
