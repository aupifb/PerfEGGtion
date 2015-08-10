package me.aupifb.perfeggtion;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerConflictDialogFragment extends DialogFragment {

    public static TimerConflictDialogFragment newInstance(int title) {
        TimerConflictDialogFragment fraga2 = new TimerConflictDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        fraga2.setArguments(args);
        return fraga2;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.timer_conflict).setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity) getActivity()).stoptimer();
                        ((MainActivity) getActivity()).stopalarm();
                        ((MainActivity) getActivity()).timeselectalertdialog2();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}