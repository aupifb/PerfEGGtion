package me.aupifb.perfeggtion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
    @Bind(R.id.circleprogress)
    ProgressBar circleprogress;
    @Bind(R.id.buttonstop)
    ImageButton buttonstop;
    @Bind(R.id.buttonpause)
    ImageButton buttonpause;
    @Bind(R.id.buttonalarm)
    ImageButton buttonalarm;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setRetainInstance(true);

        ButterKnife.bind(this, view);

        return (view);
    }

    @OnClick(R.id.buttonstop)
    public void dothis6() {
        ((MainActivity) getActivity()).stoptimer();
    }

    @OnClick(R.id.buttonpause)
    public void dothis4() {
        ((MainActivity) getActivity()).pausetimer();
        Log.d("lol", "infragment");
    }

    @OnClick(R.id.buttonalarm)
    public void dothis5() {
        ((MainActivity) getActivity()).stopalarm();
    }

    @Override
    public void onClick(View view) {
    }

    public void circleprogress(int mProgressStatus) {
        circleprogress.setProgress(mProgressStatus);
    }

    public void settextviewtext(String textviewtext) {
        mTextField.setText(textviewtext);
    }

    public void setmTextField(String text) {
        mTextField.setText(text);
    }

    public void showbuttonalarm() {
        buttonalarm.setVisibility(View.VISIBLE);
    }

    public void testmethod() {
        Log.d("lol", "testmethod ");
    }
}
