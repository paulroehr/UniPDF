package com.unipdf.app.vos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Mapping Klasse zwischen PDF und Kategorie.
 */
@DatabaseTable(tableName = "CategoryPDFMap")
public class CategoryPDFMap {

    public static final String COLUMN_LIGHTPDF = "LIGHTPDF_ID";
    public static final String COLUMN_CATEGORY = "CATEGORY_ID";
    public static final String COLUMN_ID = "ID";

    @DatabaseField( columnName = COLUMN_ID, generatedId = true )
    private long mId;

    @DatabaseField( columnName = COLUMN_CATEGORY, foreign = true, canBeNull = false )
    private Category mCategory;

    @DatabaseField( columnName = COLUMN_LIGHTPDF, foreign = true, canBeNull = false )
    private LightPDF mLightPDF;

    public CategoryPDFMap() {
    }

    public CategoryPDFMap(Category mCategory, LightPDF mLightPDF) {
        this.mCategory = mCategory;
        this.mLightPDF = mLightPDF;
    }

    public long getId() {
        return mId;
    }

    public void setId(long _id) {
        mId = _id;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category _category) {
        mCategory = _category;
    }

    public LightPDF getLightPDF() {
        return mLightPDF;
    }

    public void setLightPDF(LightPDF _lightPDF) {
        mLightPDF = _lightPDF;
    }
}
