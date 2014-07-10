package com.unipdf.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.SparseArray;

import com.j256.ormlite.dao.Dao;
import com.unipdf.app.Main;
import com.unipdf.app.R;
import com.unipdf.app.fragments.AvailablePDFFragment;
import com.unipdf.app.fragments.PDFManagerFragment;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.services.FileCrawlerService;
import com.unipdf.app.services.FileReceiver;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.CategoryPDFMap;
import com.unipdf.app.vos.LightPDF;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;

import java.sql.SQLException;
import java.util.List;

public class ExplorerActivity extends Activity
        implements AvailablePDFFragment.IAvailablePDFCallbacks,
        PDFManagerFragment.IPDFManagerCallbacks {

    private static final String LOG_TAG = ExplorerActivity.class.getSimpleName();

    public static final String EXTRA_ALREADY_STARTED = "extraAlreadyStarted";
    public static final String EXTRA_CHOSEN_PDF = "extraChosenPDF";

    private FileReceiver mReceiver;

    private AvailablePDFFragment mAlf;
    private PDFManagerFragment mFlf;

    private boolean mAlreadyStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        if(savedInstanceState != null) {
            mAlreadyStarted = savedInstanceState.getBoolean(EXTRA_ALREADY_STARTED);
        }
        else {
            mAlreadyStarted = false;
        }

        mAlf = new AvailablePDFFragment();
        mFlf = new PDFManagerFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.all_list_frame__container, mAlf)
                .replace(R.id.fav_list_frame__container, mFlf)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // BroadcastReceiver registrieren
        IntentFilter filter = new IntentFilter(FileReceiver.ACTION_FIND_PDFS);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new FileReceiver();
        registerReceiver(mReceiver, filter);

        if(!mAlreadyStarted) {
            mAlreadyStarted = true;

            // Wenn neue Daten geladen werden sollen alte rausgehauen werden.
            ApplicationModel.getInstance().getPDFs().clear();

            // Service wird gestartet
            Intent msgIntent = new Intent(this, FileCrawlerService.class);
            msgIntent.putExtra(FileCrawlerService.KEY_FILE_CRAWLER, FileCrawlerService.ACTION_SEARCH_ALL);
            startService(msgIntent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_ALREADY_STARTED, mAlreadyStarted);
    }

    private boolean checkPdf(LightPDF _pdf) {
        DecodeService decodeService = new DecodeServiceBase(new PdfContext());
        decodeService.setContentResolver(this.getContentResolver());
        try {
            decodeService.open(_pdf.getFilePath());
        }
        catch(Exception e) {
            return false;
        }
        finally {
            decodeService.recycle();
        }
        return true;
    }

    private void startWorkbench(LightPDF _pdf) {

        if(checkPdf(_pdf)) {
            Intent intent = new Intent(this, WorkbenchActivity.class);
            intent.putExtra(EXTRA_CHOSEN_PDF, _pdf);
            startActivity(intent);
        }
        else {
            Helper.showToast(this, "File is corrupted");
        }
    }

    //##############################################################################################
    // All_List_Frag
    //##############################################################################################
    @Override
    public void onItemShortClickAvailablePDF(LightPDF _lightPDF) {
        startWorkbench(_lightPDF);
    }

    @Override
    public void onSendCopyList(SparseArray<LightPDF> _CopyList) {
        Helper.convertSparseToArrayList(_CopyList, ApplicationModel.getInstance().getCopyPDFs());

        mFlf.addFavsToCurrentCategory(ApplicationModel.getInstance().getCopyPDFs());
        ApplicationModel.getInstance().clearCopyList();
    }


    //##############################################################################################
    // Fav_List_frag
    //##############################################################################################
    @Override
    public void onItemShortClickFavoritedPDF(LightPDF _lightPDF) {
        startWorkbench(_lightPDF);
    }

    @Override
    public void updateCurrentCategory(Category _Category) {
        try {
            Main.getDbHelper().getCategoryDao().update(_Category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCategory(Category _Category) {
        try {
            List<CategoryPDFMap> maps;
            Dao<CategoryPDFMap, Long> mappingDao = Main.getDbHelper().getMappingDao();
            maps = mappingDao.queryForEq(CategoryPDFMap.COLUMN_CATEGORY, _Category);

            for (CategoryPDFMap map : maps) {
                mappingDao.delete(map);
            }

            Main.getDbHelper().getCategoryDao().delete(_Category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCategory(Category _Category) {
        try {
            Main.getDbHelper().getCategoryDao().create(_Category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPDFToCategory(Category _Category, LightPDF _PDF) {
        try {
            List<LightPDF> list = Main.getDbHelper().getLightPDFDao().queryForEq(LightPDF.COLUMN_PATH, _PDF.getFilePath().getPath());

            if(list.isEmpty()) {
                Main.getDbHelper().getLightPDFDao().create(_PDF);
            }
            else {
                _PDF = list.get(0);
            }

            CategoryPDFMap map = new CategoryPDFMap(_Category, _PDF);
            Main.getDbHelper().getMappingDao().create(map);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePDFFromCategory(Category _Category, LightPDF _PDF) {

        try {
            // TODO: Schöner bauen
            Dao<CategoryPDFMap, Long> mappingDao = Main.getDbHelper().getMappingDao();
            List<CategoryPDFMap> list = mappingDao.queryForEq(CategoryPDFMap.COLUMN_CATEGORY, _Category);

            for (CategoryPDFMap map : list) {
                if(map.getLightPDF().getId() == _PDF.getId() && map.getCategory().getId() == _Category.getId()) {
                    mappingDao.delete(map);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
