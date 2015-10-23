package me.aupifb.perfeggtion;

import com.orm.SugarRecord;

public class Recipe extends SugarRecord {

    private String mTitle;
    private int mDurationSec;
    private String mSignature;

    public Recipe() {

    }

    public Recipe(String title, int durationSec, String signature) {
        mTitle = title;
        mDurationSec = durationSec;
        mSignature = signature;
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

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getSignature() {
        return mSignature;
    }

    public void setSignature(String signature) {
        mSignature = signature;
    }
}
