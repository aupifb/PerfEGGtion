package me.aupifb.perfeggtion;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.button1) Button button1;
    @Bind(R.id.buttontimeselect) Button buttontimeselect;
    @Bind(R.id.fabAlarm) android.support.design.widget.FloatingActionButton fabAlarm;

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

    @OnClick(R.id.button1)
    public void dothis() {
        ((MainActivity)getActivity()).countdownstart(8);

    }

    @OnClick(R.id.buttontimeselect)
    public void dothis3() {
        // Not needed anymore as CoordinatorLayout = getView()?
        // CoordinatorLayout rootLayout = (CoordinatorLayout) getActivity().findViewById(R.id.content_frame);
        // Snackbar.make(rootLayout, "it works", Snackbar.LENGTH_LONG)
        Snackbar.make(getView(), "it works", Snackbar.LENGTH_LONG)
                .setAction("Action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("lol", "ha");
                    }
                })
                .show();
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

    

}
