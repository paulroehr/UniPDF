package com.unipdf.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.unipdf.app.vos.LightPDF;


public class FileCrawlerService extends IntentService {

    public static final String KEY_FILE_CRAWLER = "keyFileCrawler";

    public static final String ACTION_SEARCH_ALL = "actionSearchAll";

    public static final String PARAM_SEND_PDFS = "paramSendPDFs";

    public FileCrawlerService() {
        super("FileCrawlerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent != null) {

            String action = intent.getStringExtra(KEY_FILE_CRAWLER);
            if(action.equals(ACTION_SEARCH_ALL)) {

                SystemClock.sleep(5000);
                LightPDF temp = new LightPDF(null, "Sonntag");

                Log.d("Service", "5 Sekunden um");

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(FileReceiver.ACTION_FIND_PDFS);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_SEND_PDFS, temp);
                sendBroadcast(broadcastIntent);

                SystemClock.sleep(5000);
                temp = new LightPDF(null, "Sehr bald");

                Log.d("Service", "5 Sekunden um");

                broadcastIntent.setAction(FileReceiver.ACTION_FIND_PDFS);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_SEND_PDFS, temp);
                sendBroadcast(broadcastIntent);

                SystemClock.sleep(5000);
                temp = new LightPDF(null, "Sehr bald123");

                Log.d("Service", "5 Sekunden um");

                broadcastIntent.setAction(FileReceiver.ACTION_FIND_PDFS);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(PARAM_SEND_PDFS, temp);
                sendBroadcast(broadcastIntent);
            }

        }

        stopSelf();
    }


}
