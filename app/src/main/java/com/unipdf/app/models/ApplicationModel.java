package com.unipdf.app.models;

import java.util.ArrayList;

import com.unipdf.app.events.EventDispatcher;
import com.unipdf.app.events.SimpleEvent;
import com.unipdf.app.vos.Categories;
import com.unipdf.app.vos.LightPDF;

/**
 * Created by schotte on 17.04.14.
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
    public ArrayList<Categories> getCategories() {
        return mCategories;
    }

    public void setCategories(ArrayList<Categories> mCategories) {
        this.mCategories = mCategories;
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
        mCategories = new ArrayList<Categories>();

        mCategories.add(new Categories(null, "Action", 0, null));
        mCategories.add(new Categories(null, "Drama", 1, null));
    }

    private static ApplicationModel ourInstance = null;
    private ArrayList<LightPDF> mPDFs;
    private ArrayList<LightPDF> mCopyPDFs;
    private ArrayList<Categories> mCategories;
}
