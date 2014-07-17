package com.unipdf.app.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.util.Log;

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

    public interface IPDFFinderListener {
        void onProgress(ArrayList<File> _Files);
        void onFinish();
    }

    public static final String LOG_TAG = PDFFinder.class.getSimpleName();

    private IPDFFinderListener mListener;

    private ArrayList<File> mPDFList;
    private Stack<File> mDirectories;

//    private File mDataDir;
    private File mDownloadCacheDir;

    private File mCurrentDir;

    private File mExternal;

    private int mMaxSizeOfPdfList;

    private final int DEFAULT_MAX_SIZE = 10;

    public PDFFinder() {
        mPDFList = new ArrayList<File>();
        mDirectories = new Stack<File>();


        mExternal = Environment.getExternalStorageDirectory();

        mMaxSizeOfPdfList = DEFAULT_MAX_SIZE;
    }

    public PDFFinder(int _SizeOfPdfList) {
        mPDFList = new ArrayList<File>();
        mDirectories = new Stack<File>();

        mExternal = Environment.getExternalStorageDirectory();

        mMaxSizeOfPdfList = _SizeOfPdfList;
    }

    public void setListener(IPDFFinderListener _Listener) {
        mListener = _Listener;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {

        traverseDirs();
        return mPDFList;
    }

    @Override
    protected void onProgressUpdate(ArrayList<File>... values) {
        super.onProgressUpdate(values[0]);
        mListener.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<File> files) {
        super.onPostExecute(files);
        mListener.onFinish();
    }

    private void traverseDirs() {

        if ( (mExternal != null) && mExternal.isDirectory() ) {
            mDirectories.push(mExternal);
        }

        while (!mDirectories.isEmpty()) {
            File temp = mDirectories.pop();
            traversePath(temp);
        }

        if(!mPDFList.isEmpty()) {
            publishProgress(mPDFList);
        }
    }

    private void traversePath(File _Directory) throws IllegalArgumentException {

        if ( ! _Directory.isDirectory()) {
            throw new IllegalArgumentException();
        }
        File[] fileList = _Directory.listFiles();
        if(fileList == null)
            return;
        for (File F: fileList) {

            if ( F.isDirectory() ) {
                mDirectories.push(F);
            }
            else if (F.isFile()) {
                if(F.getName().matches(".*\\.pdf"))
                    mPDFList.add(F);

                if (getSizeOfPdfList() >= mMaxSizeOfPdfList) {
                    publishProgress(mPDFList);
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