package me.aupifb.perfeggtion;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends android.support.v4.app.Fragment implements View.OnClickListener {

    @Bind(R.id.alacoque)
    ImageButton alacoque;


    public TabFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view2 = inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
        setRetainInstance(true);

        ButterKnife.bind(this, view2);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return (view2);
    }


    @Override
    public void onClick(View v) {
    }

    @OnClick(R.id.alacoque)
    public void dothis() {
        Log.d("lol", "dothis ");
        ((MainActivity) getActivity()).countdownstart(180);
    }

}
