package com.unipdf.app.models;

import com.unipdf.app.events.EventDispatcher;
import com.unipdf.app.events.SimpleEvent;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.LightPDF;
import com.unipdf.app.vos.ShufflePage;

import java.util.ArrayList;

/**
 * Globales Daten Model der App als Singleton.
 */
public class ApplicationModel extends EventDispatcher {

    public static class ChangeEvent extends SimpleEvent {
        public static final String EVENT_RECEIVED_PDFS = "receivedPDFS";

        public ChangeEvent(String type) {
            super(type);
        }
    }

    //##############################################################################################
    public void addPDFs(ArrayList<LightPDF> _List) {
        mPDFs.addAll(_List);
        notifyChange(ChangeEvent.EVENT_RECEIVED_PDFS);
    }

    public void setPDFs(ArrayList<LightPDF> _PDFs) {
        mPDFs = _PDFs;
        notifyChange(ChangeEvent.EVENT_RECEIVED_PDFS);
    }

    public ArrayList<LightPDF> getPDFs() {
        return mPDFs;
    }

    //##############################################################################################
    public ArrayList<LightPDF> getCopyPDFs() {
        return mCopyPDFs;
    }

    public void setCopyPDFs(ArrayList<LightPDF> mCopyPDFs) {
        this.mCopyPDFs = mCopyPDFs;
    }

    public void clearCopyList() {
        mCopyPDFs.clear();
    }

    //##############################################################################################
    public ArrayList<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(ArrayList<Category> mCategories) {
        this.mCategories = mCategories;
    }

    //##############################################################################################

    public ArrayList<ShufflePage> getShuffleThumbnails() {
        return mShuffleThumbnails;
    }

    public void setShuffleThumbnails(ArrayList<ShufflePage> _shuffleThumbnails) {
        mShuffleThumbnails = _shuffleThumbnails;
    }

    //##############################################################################################
    public static ApplicationModel getInstance() {
        if(ourInstance == null) {
            ourInstance = new ApplicationModel();
        }
        return ourInstance;
    }

    private void notifyChange(String type) {
        dispatchEvent(new ChangeEvent(type));
    }

    private ApplicationModel() {
        mPDFs = new ArrayList<LightPDF>();
        mCopyPDFs = new ArrayList<LightPDF>();
        mCategories = new ArrayList<Category>();
        mShuffleThumbnails = new ArrayList<ShufflePage>();
    }

    private static ApplicationModel ourInstance = null;
    private ArrayList<LightPDF>     mPDFs;
    private ArrayList<LightPDF>     mCopyPDFs;
    private ArrayList<Category>     mCategories;
    private ArrayList<ShufflePage>  mShuffleThumbnails;
}
