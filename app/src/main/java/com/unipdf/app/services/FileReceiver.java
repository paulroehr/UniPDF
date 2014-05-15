package com.unipdf.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.vos.LightPDF;

import java.util.ArrayList;

public class FileReceiver extends BroadcastReceiver {

    public static final String ACTION_FIND_PDFS = "actionFindPDFs";

    public FileReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Daten aus Intent rausholen

        LightPDF temp = intent.getParcelableExtra(FileCrawlerService.PARAM_SEND_PDFS);

        ArrayList<LightPDF> tempList = new ArrayList<LightPDF>();
        tempList.add(temp);
        ApplicationModel.getInstance().addPDFs(tempList);

        Log.d("Receiver", "Daten empfangen");
    }
}
