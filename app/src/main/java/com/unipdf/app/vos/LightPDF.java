package com.unipdf.app.vos;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by schotte on 06.05.14.
 */
public class LightPDF implements Parcelable{
    private Uri mPicture;
    private String mName;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }



    public Uri getmPicture() {
        return mPicture;
    }

    public void setmPicture(Uri mPicture) {
        this.mPicture = mPicture;
    }

    public LightPDF(){

    }

    public LightPDF(Parcel _In){
        mName    = _In.readString();
        mPicture = _In.readParcelable(Uri.class.getClassLoader());
    }

    public LightPDF(Uri mPicture, String mName) {
        this.mName = mName;
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
        dest.writeString(mName);
        dest.writeParcelable(mPicture, PARCELABLE_WRITE_RETURN_VALUE);
    }

    public static final Parcelable.Creator<LightPDF> CREATOR
            = new Parcelable.Creator<LightPDF>() {
        public LightPDF createFromParcel(Parcel in) {
            return new LightPDF(in);
        }

        public LightPDF[] newArray(int size) {
            return new LightPDF[size];
        }
    };
}
