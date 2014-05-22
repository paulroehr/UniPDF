package com.unipdf.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.unipdf.app.utils.PDFFinder;
import com.unipdf.app.vos.LightPDF;

import java.io.File;
import java.util.ArrayList;


public class FileCrawlerService extends IntentService {

    public static final String LOG_TAG = FileCrawlerService.class.getSimpleName();
    public static final String KEY_FILE_CRAWLER = "keyFileCrawler";

    public static final String ACTION_SEARCH_ALL = "actionSearchAll";

    public static final String PARAM_SEND_PDFS = "paramSendPDFs";

    public FileCrawlerService() {
        super("FileCrawlerService");
    }
    private PDFFinder mFinder;

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent != null) {

            String action = intent.getStringExtra(KEY_FILE_CRAWLER);
            if(action.equals(ACTION_SEARCH_ALL)) {

                mFinder = new PDFFinder();
                mFinder.setListener(new PDFFinder.IPDFFinderListener() {
                    @Override
                    public void onProgress(ArrayList<File> _Files) {
                        ArrayList<LightPDF> list = new ArrayList<LightPDF>();

                        for (File file : _Files) {
                            list.add(new LightPDF(null, file.getName()));
                        }

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(FileReceiver.ACTION_FIND_PDFS);
                        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        broadcastIntent.putExtra(PARAM_SEND_PDFS, list);
                        sendBroadcast(broadcastIntent);
                        mFinder.clearPdfList();
                    }

                    @Override
                    public void onFinish() {
                        stopSelf();
                    }
                });

                Log.d(LOG_TAG, "Finde Start");
                mFinder.execute();

            }

        }

    }


}
