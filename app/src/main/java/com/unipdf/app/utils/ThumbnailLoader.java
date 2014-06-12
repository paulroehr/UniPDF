package com.unipdf.app.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
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

    public void setListener(IThumbnailLoaderListener _listener) {
        mListener = _listener;
    }

    @Override
    protected List<Bitmap> doInBackground(String... params) {

        String path = params[0];
        float width = Helper.dpToPx(88);

        try {
            // select a document and get bytes
            File file = new File(path);
            RandomAccessFile raf = null;
            raf = new RandomAccessFile(file, "r");
            FileChannel channel = raf.getChannel();
            ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
            raf.close();
            // create a pdf doc
            PDFFile pdfFile = new PDFFile(bb);
            //Get the first page from the pdf doc
            PDFPage pdfPage = pdfFile.getPage(1, true);
            //create a scaling value according to the WebView Width
            float scale = width / pdfPage.getWidth();
            //convert the page into a bitmap with a scaling value
            Bitmap page = pdfPage.getImage((int) (pdfPage.getWidth() * scale), (int) (pdfPage.getHeight() * scale), null, true, true);

            mThumbnails.add(page);
            onProgressUpdate(page);

            int pageCount = pdfFile.getNumPages();

            for (int i = 2; i <= pageCount; i++) {
                pdfPage = pdfFile.getPage(i, true);
                page = pdfPage.getImage((int) (pdfPage.getWidth() * scale), (int) (pdfPage.getHeight() * scale), null, true, true);

                mThumbnails.add(page);
                onProgressUpdate(page);
            }

        } catch (Exception e) {
//            e.printStackTrace();
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
        super.onProgressUpdate(values);
        mListener.onProgress(values[0]);
    }
}
