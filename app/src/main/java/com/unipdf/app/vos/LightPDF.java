package com.unipdf.app.vos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Vereinfachtes PDF Modell.
 */
@DatabaseTable(tableName = "LightPDF")
public class LightPDF implements Parcelable, Comparable {

    public static final String COLUMN_NAME      = "NAME";
    public static final String COLUMN_PICTURE   = "PICTURE";
    public static final String COLUMN_PATH      = "PATH";
    public static final String COLUMN_ID        = "ID";

    @DatabaseField( columnName = COLUMN_ID, generatedId = true )
    private long mId;

    private Bitmap mPicture;

    private byte[] mPictureByteArray;

    @DatabaseField( columnName = COLUMN_NAME, canBeNull = false)
    private String mName;

    private Uri mFilePath;

    @DatabaseField( columnName = COLUMN_PATH, canBeNull = false, useGetSet = true)
    private String mStringFilePath;

    public LightPDF(){

    }

    public LightPDF(Parcel _In){
        mName    = _In.readString();
        mPicture = _In.readParcelable(Bitmap.class.getClassLoader());
        mFilePath = _In.readParcelable(Uri.class.getClassLoader());
    }

    public LightPDF(Bitmap _Picture, String _Name, Uri _FilePath) {
        mName = _Name;
        mPicture = _Picture;
        mFilePath = _FilePath;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String _Name) {
        mName = _Name;
    }

    public Bitmap getmPicture() {
        return mPicture;
    }

    public void setmPicture(Bitmap _Picture) {
        mPicture = _Picture;
    }

    public Uri getFilePath() {
        return mFilePath;
    }

    public void setFilePath(Uri _filePath) {
        mFilePath = _filePath;
    }

    public long getId() {
        return mId;
    }

    public void setId(long _id) {
        mId = _id;
    }

    public byte[] getMPictureByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mPicture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mPictureByteArray = stream.toByteArray();
        return mPictureByteArray;
    }

    public void setMPictureByteArray(byte[] _pictureByteArray) {
        mPictureByteArray = _pictureByteArray;
        mPicture = BitmapFactory.decodeByteArray(_pictureByteArray, 0, _pictureByteArray.length);
    }

    public String getMStringFilePath() {
        mStringFilePath = mFilePath.getPath();
        return mStringFilePath;
    }

    public void setMStringFilePath(String _stringFilePath) {
        mStringFilePath = _stringFilePath;
        mFilePath = Uri.fromFile(new File(_stringFilePath));
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
        dest.writeParcelable(mFilePath, PARCELABLE_WRITE_RETURN_VALUE);
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

    @Override
    public int compareTo(Object another) {
        LightPDF pdf = (LightPDF) another;
        return this.getmName().toUpperCase().compareTo(pdf.getmName().toUpperCase());
    }
}
