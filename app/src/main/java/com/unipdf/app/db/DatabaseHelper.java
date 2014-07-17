package com.unipdf.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.CategoryPDFMap;
import com.unipdf.app.vos.LightPDF;

import java.sql.SQLException;

/**
 * Schnittestelle zur Datenbank und deren DAO Objekte.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "unipdf.db";
    private static final int    DB_VERSION = 1;

    private Dao<Category, Long> mCategoryDao = null;
    private Dao<LightPDF, Long> mLightPDFDao = null;
    private Dao<CategoryPDFMap, Long> mMappingDao = null;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase _sqLiteDatabase, ConnectionSource _connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, LightPDF.class);
            TableUtils.createTable(connectionSource, CategoryPDFMap.class);

            getCategoryDao().create(new Category(null, "Favoriten"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase _sqLiteDatabase, ConnectionSource _connectionSource, int i, int i2) {
        try {
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, LightPDF.class, true);
            TableUtils.dropTable(connectionSource, CategoryPDFMap.class, true);

            onCreate(_sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return Rückgabe des Kategorien DAO Objektes
     */
    public Dao<Category, Long> getCategoryDao() {
        if (mCategoryDao == null) {
            try {
                mCategoryDao = getDao(Category.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mCategoryDao;
    }

    /**
     *
     * @return Rückgabe des LightPDF DAO Objektes
     */
    public Dao<LightPDF, Long> getLightPDFDao() {
        if (mLightPDFDao == null) {
            try {
                mLightPDFDao = getDao(LightPDF.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mLightPDFDao;
    }

    /**
     * Auflösung der m zu n Beziehungen
     * @return Rückgabe des Mapping DAO Objektes zwischen Kategorien und LightPDF's
     */
    public Dao<CategoryPDFMap, Long> getMappingDao() {
        if (mMappingDao == null) {
            try {
                mMappingDao = getDao(CategoryPDFMap.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mMappingDao;
    }
}
