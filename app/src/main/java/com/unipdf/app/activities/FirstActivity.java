package com.unipdf.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.unipdf.app.R;
import com.unipdf.app.fragments.All_List_Frag;
import com.unipdf.app.fragments.Fav_List_Frag;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.services.FileCrawlerService;
import com.unipdf.app.services.FileReceiver;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.vos.LightPDF;

public class FirstActivity extends Activity
        implements All_List_Frag.ICom_All_List_Frag,
        Fav_List_Frag.ICom_Fav_List_Frag {

    private static final String LOG_TAG = FirstActivity.class.getSimpleName();

    public static final String EXTRA_ALREADY_STARTED = "extraAlreadyStarted";

    private FileReceiver mReceiver;

    private All_List_Frag mAlf;
    private Fav_List_Frag mFlf;

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

        mAlf = new All_List_Frag();
        mFlf = new Fav_List_Frag();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.all_list_frame__container, mAlf)
                .replace(R.id.fav_list_frame__container, mFlf)
                .commit();

        Log.d(LOG_TAG, "Data: "+Environment.getDataDirectory().getAbsolutePath());
        Log.d(LOG_TAG, "External: "+Environment.getExternalStorageDirectory().getAbsolutePath());


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    //##############################################################################################
    // All_List_Frag
    //##############################################################################################
    @Override
    public void onItemShortClick_All_List_Frag(LightPDF _lightPDF, View _v) {
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
    public void onItemShortClick_Fav_List_Frag(LightPDF _lightPDF, View v) {

    }

}
