package com.unipdf.app;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

import com.unipdf.app.db.DatabaseHelper;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.services.FileCrawlerService;
import com.unipdf.app.services.FileReceiver;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.CategoryPDFMap;
import com.unipdf.app.vos.LightPDF;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by schotte on 17.04.14.
 */
public class Main extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Data", Environment.getDataDirectory().toString());
        mAppContext = this;
        mDbHelper = new DatabaseHelper(this);

       initDatabase();
    }

    public static void initDatabase() {
        ArrayList<Category> categories = null;

        try {
            categories = new ArrayList<Category>(mDbHelper.getCategoryDao().queryForAll());
            List<CategoryPDFMap> maps = new ArrayList<CategoryPDFMap>();

            for (Category category : categories) {
                maps.clear();
                maps = getDbHelper().getMappingDao().queryForEq(CategoryPDFMap.COLUMN_CATEGORY, category);
                for (CategoryPDFMap map : maps) {
                    LightPDF pdf = getDbHelper().getLightPDFDao().queryForId(map.getLightPDF().getId());
                    category.getLightPDFs().add(pdf);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ApplicationModel.getInstance().setCategories(categories);
    }

    public static Application getAppContext() {
        return mAppContext;
    }

    public static DatabaseHelper getDbHelper() {
        return mDbHelper;
    }

    private static Application mAppContext;

    public static DatabaseHelper mDbHelper;
}
