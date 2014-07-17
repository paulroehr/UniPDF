package com.unipdf.app.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;


/**
 * Kategorien Modell.
 */
@DatabaseTable(tableName = "Category")
public class Category implements Parcelable {

    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_ID = "ID";

    private ArrayList<LightPDF> mLightPDFs = new ArrayList<LightPDF>();

    @DatabaseField( columnName = COLUMN_NAME, canBeNull = false)
    private String mCategoryName;

    @DatabaseField( columnName = COLUMN_ID, generatedId = true )
    private long mId;

    public Category() {
    }

    public Category(ArrayList<LightPDF> _LightPDFs, String _CategoryName) {
        mLightPDFs = _LightPDFs;
        mCategoryName = _CategoryName;
    }

    public Category(Parcel _In) {
        mCategoryName = _In.readString();
        _In.readTypedList(mLightPDFs, LightPDF.CREATOR);
        mId = _In.readLong();
    }

    public ArrayList<LightPDF> getLightPDFs() {
        return mLightPDFs;
    }

    public void setLightPDFs(ArrayList<LightPDF> mLightPDFs) {
        this.mLightPDFs = mLightPDFs;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String mFavName) {
        this.mCategoryName = mFavName;
    }


    public long getId() {
        return mId;
    }

    public void setId(long _Id) {
        mId = _Id;
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
        dest.writeString(mCategoryName);
        dest.writeTypedList(mLightPDFs);
        dest.writeLong(mId);
    }

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
