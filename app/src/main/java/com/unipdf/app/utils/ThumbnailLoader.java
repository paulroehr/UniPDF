package com.unipdf.app.utils;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;

import com.unipdf.app.Main;
import com.unipdf.app.activities.WorkbenchActivity;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Generiert Thumbnails aus gegebener PDF Datei.
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
        decodeService.setContentResolver(Main.getAppContext().getContentResolver());
        int[] scale = new int[2];

        try {
            decodeService.open(mPath);
            int pageCount = decodeService.getPageCount();

            for (int i = 0; i < pageCount; i++) {
                if (!isCancelled()) {
                    CodecPage page = decodeService.getPage(i);
                    Bitmap pageBitmap;

                    Helper.scaleDownSize(decodeService.getPageWidth(i), decodeService.getPageHeight(i), 100, activity, scale);
                    pageBitmap = page.renderBitmap(scale[0], scale[1], new RectF(0, 0, 1, 1));

                    mThumbnails.add(pageBitmap);
                    publishProgress(pageBitmap);
                } else {
                    break;
                }
            }
        }
        catch (Exception e) {

        }
        finally {
            decodeService.recycle();
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
