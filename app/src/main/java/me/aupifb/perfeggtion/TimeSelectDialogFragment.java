package me.aupifb.perfeggtion;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;


public class TimeSelectDialogFragment extends android.support.v4.app.DialogFragment {

    NumberPicker alertnumberpickerMIN, alertnumberpickerSEC;

    public static TimeSelectDialogFragment newInstance(int title) {
        TimeSelectDialogFragment frag = new TimeSelectDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View alertv = inflater.inflate(R.layout.fragment_time_select_dialog, null);

        alertnumberpickerMIN = (NumberPicker) alertv.findViewById(R.id.alertnumberpickerMIN);
        alertnumberpickerMIN.setMaxValue(200);
        alertnumberpickerMIN.setMinValue(0);
        alertnumberpickerMIN.setWrapSelectorWheel(false);
        alertnumberpickerMIN.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        alertnumberpickerSEC = (NumberPicker) alertv.findViewById(R.id.alertnumberpickerSEC);
        alertnumberpickerSEC.setMaxValue(60);
        alertnumberpickerSEC.setMinValue(0);
        alertnumberpickerSEC.setWrapSelectorWheel(false);
        alertnumberpickerSEC.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        return new AlertDialog.Builder(getActivity())
                .setView(alertv)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return1method();
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

    private void return1method() {
        int minutes = alertnumberpickerMIN.getValue();
        int seconds = alertnumberpickerSEC.getValue();
        long timeinsec = minutes * 60 + seconds;
        ((MainActivity) getActivity()).countdownstart(timeinsec);

    }
}