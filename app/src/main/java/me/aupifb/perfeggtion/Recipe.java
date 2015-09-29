package me.aupifb.perfeggtion;

import com.orm.SugarRecord;

public class Recipe extends SugarRecord {

    private String mTitle;
    private int mDurationSec;

    public Recipe() {

    }

    public Recipe(String title, int durationSec) {
        mTitle = title;
        mDurationSec = durationSec;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getDurationSec() {
        return mDurationSec;
    }

    public void setDurationSec(int durationSec) {
        mDurationSec = durationSec;
    }
}
