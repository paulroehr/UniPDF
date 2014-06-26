package com.unipdf.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.unipdf.app.activities.WorkbenchActivity;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 12.06.14.
 */
public class ThumbnailLoader extends AsyncTask<String, Bitmap, List<Bitmap>> {

    public interface IThumbnailLoaderListener {
        void onProgress(Bitmap _Bitmap);
        void onFinish();
    }

    final List<Bitmap> mThumbnails = new ArrayList<Bitmap>();
    IThumbnailLoaderListener mListener;
    Uri mPath;
    WorkbenchActivity activity;

    /** The decode service used for decoding the PDF */
    private DecodeService decodeService;

    public void setListener(IThumbnailLoaderListener _listener) {
        mListener = _listener;
    }

    public void setPath(Uri _path) {
        mPath = _path;
    }

    public void setActivity(WorkbenchActivity _activity) {
        activity = _activity;
    }

    @Override
    protected List<Bitmap> doInBackground(String... params) {

        decodeService = new DecodeServiceBase(new PdfContext());
        decodeService.setContentResolver(activity.getContentResolver());
        decodeService.open(mPath);
        int pageCount = decodeService.getPageCount();

        for (int i = 0; i < pageCount; i++) {
            CodecPage page = decodeService.getPage(i);
            Bitmap pageBitmap;

//            synchronized (decodeService.getClass()) {
                pageBitmap = page.renderBitmap(decodeService.getPageWidth(i), decodeService.getPageHeight(i), new RectF(0, 0, 1, 1));
//            }

            mThumbnails.add(pageBitmap);
            publishProgress(pageBitmap);
        }

        return mThumbnails;
    }

    @Override
    protected void onPostExecute(List<Bitmap> result) {
        mListener.onFinish();
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        super.onProgressUpdate(values[0]);
        mListener.onProgress(values[0]);
    }
}
