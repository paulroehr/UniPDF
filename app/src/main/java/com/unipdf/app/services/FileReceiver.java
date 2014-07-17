package com.unipdf.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.vos.LightPDF;

import java.util.ArrayList;

/**
 * Speichert empfangene PDF's in dem ApplicationModel.
 */
public class FileReceiver extends BroadcastReceiver {

    public static final String ACTION_FIND_PDFS = "actionFindPDFs";

    public FileReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Daten aus Intent rausholen

        ArrayList<LightPDF> tempList = intent.getParcelableArrayListExtra(FileCrawlerService.PARAM_SEND_PDFS);

        ApplicationModel.getInstance().addPDFs(tempList);
    }
}
