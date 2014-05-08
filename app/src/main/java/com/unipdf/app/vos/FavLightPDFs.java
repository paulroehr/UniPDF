package com.unipdf.app.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by schotte on 06.05.14.
 */
public class FavLightPDFs implements Parcelable {

    private  ArrayList<LightPDF> mLightPDFs = null;
    private String mFavName;

    public FavLightPDFs(ArrayList<LightPDF> mLightPDFs, String mFavName) {
        this.mLightPDFs = mLightPDFs;
        this.mFavName = mFavName;
    }

    public FavLightPDFs(Parcel _In) {
        mFavName = _In.readString();
        _In.readTypedList(mLightPDFs, LightPDF.CREATOR);
    }

    public ArrayList<LightPDF> getmLightPDFs() {
        return mLightPDFs;
    }

    public void setmLightPDFs(ArrayList<LightPDF> mLightPDFs) {
        this.mLightPDFs = mLightPDFs;
    }

    public String getmFavName() {
        return mFavName;
    }

    public void setmFavName(String mFavName) {
        this.mFavName = mFavName;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFavName);
        dest.writeTypedList(mLightPDFs);
    }

    public static final Parcelable.Creator<FavLightPDFs> CREATOR
            = new Parcelable.Creator<FavLightPDFs>() {
        public FavLightPDFs createFromParcel(Parcel in) {
            return new FavLightPDFs(in);
        }

        public FavLightPDFs[] newArray(int size) {
            return new FavLightPDFs[size];
        }
    };
}
