package me.aupifb.perfeggtion;

import com.bumptech.glide.signature.StringSignature;
import com.orm.SugarRecord;

public class Recipe extends SugarRecord {

    private String mTitle;
    private int mDurationSec;
    private StringSignature mStringSignature;

    public Recipe() {

    }

    public Recipe(String title, int durationSec, StringSignature stringSignature) {
        mTitle = title;
        mDurationSec = durationSec;
        mStringSignature = stringSignature;
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

    public StringSignature getStringSignature() {
        return mStringSignature;
    }

    public void setStringSignature(StringSignature stringSignature) {
        mStringSignature = stringSignature;
    }
}
