package me.aupifb.perfeggtion;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.mTextField)
    TextView mTextField;
    @Bind(R.id.fabAlarm) android.support.design.widget.FloatingActionButton fabAlarm;
    @Bind(R.id.circleprogress)
    ProgressBar circleprogress;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        setRetainInstance(true);

        ButterKnife.bind(this, view);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return(view);
    }

    @OnClick(R.id.fabAlarm)
    public void dothis2() {
        ((MainActivity)getActivity()).timeselectalertdialog();

    }

    @Override
    public void onClick(View view) {
    }

    public void snackinfragment(String text, String actiontext) {
        Snackbar.make(getView(), text, Snackbar.LENGTH_LONG)
                .setAction(actiontext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("lol", "snackmethodinfragment");
                    }
                })
                .show();
    }

    public void snackstoptimerinfragment() {
        Snackbar.make(getView(), R.string.snackbar_timer_stopped, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo_timer_stopped, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).stoptimer2();
                    }
                })
                .show();
    }


    public void snackstoppausedtimerinfragment() {
        Snackbar.make(getView(), R.string.snackbar_timer_stopped, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo_timer_stopped, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).stoptimer3();
                    }
                })
                .show();
    }

    public void circleprogress(int mProgressStatus) {
        circleprogress.setProgress(mProgressStatus);
    }

    public void settextviewtext(String textviewtext) {
        mTextField.setText(textviewtext);
    }
}
