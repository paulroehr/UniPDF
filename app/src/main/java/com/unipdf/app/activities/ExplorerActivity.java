package com.unipdf.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import com.unipdf.app.R;
import com.unipdf.app.fragments.AvailablePDFFragment;
import com.unipdf.app.fragments.PDFManagerFragment;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.services.FileCrawlerService;
import com.unipdf.app.services.FileReceiver;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.LightPDF;

public class ExplorerActivity extends Activity
        implements AvailablePDFFragment.IAvailablePDFCallbacks,
        PDFManagerFragment.IPDFManagerCallbacks {

    private static final String LOG_TAG = ExplorerActivity.class.getSimpleName();

    public static final String EXTRA_ALREADY_STARTED = "extraAlreadyStarted";

    private FileReceiver mReceiver;

    private AvailablePDFFragment mAlf;
    private PDFManagerFragment mFlf;

    private boolean mAlreadyStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

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

    //##############################################################################################
    // All_List_Frag
    //##############################################################################################
    @Override
    public void onItemShortClickAvailablePDF(LightPDF _lightPDF, View _v) {
        ///TODO zweite Activity öffnen und PDF übergeben
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
    public void onItemShortClickFavoritedPDF(LightPDF _lightPDF, View v) {

    }

    @Override
    public void updateCurrentCategory(Category _Category) {
        // TODO: Store in DataManager
    }

    @Override
    public void deleteCategory(Category _Category) {
        // TODO: Delete from DataManager
    }

}
