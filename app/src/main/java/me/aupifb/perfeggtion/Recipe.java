package me.aupifb.perfeggtion;

import java.util.UUID;

/**
 * Created by aupifb on 28/09/2015.
 */
public class Recipe {

    private UUID mId;
    private String mTitle;
    private int mDurationSec;

    public Recipe() {

    }

    public Recipe(String title, int durationSec) {
        mTitle = title;
        mDurationSec = durationSec;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
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
