package me.aupifb.perfeggtion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.activityVisible = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
