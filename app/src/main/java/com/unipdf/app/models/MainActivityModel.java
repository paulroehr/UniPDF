package com.unipdf.app.models;

import java.util.List;

import com.unipdf.app.events.EventDispatcher;
import com.unipdf.app.events.SimpleEvent;

/**
 * Created by schotte on 17.04.14.
 */
public class MainActivityModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String EVENT_RECEIVED_PDFS = "receivedPDFS";

        public ChangeEvent(String type) {
            super(type);
        }
    }

    public void setPDFs(List<String> _PDFs) {
        mPDFs = _PDFs;
        notifyChange(ChangeEvent.EVENT_RECEIVED_PDFS);
    }

    public List<String> getPDFs() {
        return mPDFs;
    }

    public static MainActivityModel getInstance() {
        if(ourInstance == null) {
            ourInstance = new MainActivityModel();
        }
        return ourInstance;
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }

    private MainActivityModel() {
    }

    private static MainActivityModel ourInstance = null;
    private List<String> mPDFs;
}
