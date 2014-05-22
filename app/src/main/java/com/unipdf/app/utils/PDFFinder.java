package com.unipdf.app.adapter;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;

import com.unipdf.app.vos.LightPDF;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;


/**
 * Created by markus on 13.05.14.
 */

// this is a helper class
// I traverse the directory tree
// first the directories documents, downloads
//

public class PDFFinder extends AsyncTask<Void, ArrayList<File>, ArrayList<File>>{

    private ArrayList<File> mPDFList;
    private Stack<File> mDirectories;

    private File mDataDir;
    private File mDownloadCacheDir;

    private File mCurrentDir;

    private File mExternal;

    private int mMaxSizeOfPdfList;

    private final int DEFAULT_MAX_SIZE = 25;

    public PDFFinder() {
        mPDFList = new ArrayList<File>();
        mDirectories = new Stack<File>();


        mDataDir = Environment.getDataDirectory();
        mExternal = Environment.getExternalStorageDirectory();

        mMaxSizeOfPdfList = DEFAULT_MAX_SIZE;
    }

    public PDFFinder(int _SizeOfPdfList) {
        mPDFList = new ArrayList<File>();
        mDirectories = new Stack<File>();


        mDataDir = Environment.getDataDirectory();
        mExternal = Environment.getExternalStorageDirectory();

        mMaxSizeOfPdfList = _SizeOfPdfList;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {

        traverseDirs();
        return mPDFList;
    }

    @Override
    protected void onProgressUpdate(ArrayList<File>... values) {
        super.onProgressUpdate(values[0]);
    }

    private void traverseDirs() {
        if (mDataDir.isDirectory() && (mDataDir != null)) {
            mDirectories.push(mDataDir);
        }

        if ( mExternal.isDirectory() && (mExternal
                 != null)) {
            mDirectories.push(mDataDir);
        }

        while (!mDirectories.isEmpty()) {
            traversePath(mDirectories.pop());
        }
    }

    private void traversePath(File _Directory) throws IllegalArgumentException {

        if ( ! _Directory.isDirectory()) {
            throw new IllegalArgumentException();
        }
        File[] fileList = _Directory.listFiles();
        for (File F: fileList) {

            if ( F.isDirectory() ) {
                mDirectories.push(F);
            }
            else if (F.isFile()) {
                F.getName().matches(".*\\.pdf");
                mPDFList.add(F);

                if (getSizeOfPdfList() >= mMaxSizeOfPdfList) {
                    onProgressUpdate(mPDFList);
                }
            }

        }
    }

    public void clearPdfList() {
        mPDFList.clear();
    }

    public int getSizeOfPdfList() {
        return mPDFList.size();
    }


    public ArrayList<File> getPdfList() {
        return mPDFList;
    }

    public ArrayList<File> getPdfListAndClear() {
        ArrayList<File> temp = getPdfList();
        clearPdfList();
        return temp;
    }
}