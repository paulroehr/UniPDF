package com.unipdf.app.vos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by schotte on 06.05.14.
 */
public class Categories implements Parcelable {

    private ArrayList<LightPDF> mLightPDFs = null;
    private String mFavName;
    private int mId;
    private Uri mPicture;



    public Categories(ArrayList<LightPDF> mLightPDFs, String mFavName, int mId, Uri mPicture) {
        this.mLightPDFs = mLightPDFs;
        this.mFavName = mFavName;
        this.mId = mId;
        this.mPicture = mPicture;
    }

    public Categories(Parcel _In) {
        mFavName = _In.readString();
        _In.readTypedList(mLightPDFs, LightPDF.CREATOR);
        mId = _In.readInt();
        mPicture = _In.readParcelable(Uri.class.getClassLoader());
    }

    public ArrayList<LightPDF> getLightPDFs() {
        return mLightPDFs;
    }

    public void setLightPDFs(ArrayList<LightPDF> mLightPDFs) {
        this.mLightPDFs = mLightPDFs;
    }

    public String getFavName() {
        return mFavName;
    }

    public void setFavName(String mFavName) {
        this.mFavName = mFavName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public Uri getPicture() {
        return mPicture;
    }

    public void setPicture(Uri mPicture) {
        this.mPicture = mPicture;
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
        dest.writeInt(mId);
        dest.writeParcelable(mPicture, PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static final Parcelable.Creator<Categories> CREATOR
            = new Parcelable.Creator<Categories>() {
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}
