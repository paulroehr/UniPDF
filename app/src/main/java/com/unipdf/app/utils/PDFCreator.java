package com.unipdf.app.utils;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.view.View;

import com.unipdf.app.Main;
import com.unipdf.app.activities.WorkbenchActivity;
import com.unipdf.app.vos.ShufflePage;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 12.06.14.
 */
public class PDFCreator extends AsyncTask<String, Void, Boolean> {

    public interface IPDFCreatorListener {
        void onFinish();
    }

    List<ShufflePage> mPages;
    IPDFCreatorListener mListener;
    Uri mPath;
    WorkbenchActivity activity;

    /** The decode service used for decoding the PDF */
    private DecodeService decodeService;

    public void setListener(IPDFCreatorListener _listener) {
        mListener = _listener;
    }

    public void setPath(Uri _path) {
        mPath = _path;
    }

    public void setPages(List<ShufflePage> _pages) {
        mPages = _pages;
    }

    public void setActivity(WorkbenchActivity _activity) {
        activity = _activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "UniPDF");
        if(!file.exists()) {
            file.mkdirs();
        }

        Paint pdfPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        decodeService = new DecodeServiceBase(new PdfContext());
        decodeService.setContentResolver(Main.getAppContext().getContentResolver());

        try {
            decodeService.open(mPath);

            // PDF Dokument erstellen
            PdfDocument document = new PdfDocument();

            for (int i = 0; i < mPages.size(); i++) {
                if (!isCancelled()) {
                    int pageNumber = mPages.get(i).getPage();
                    CodecPage page = decodeService.getPage(pageNumber);
                    Bitmap pageBitmap = page.renderBitmap(decodeService.getPageWidth(pageNumber), decodeService.getPageHeight(pageNumber), new RectF(0, 0, 1, 1));

                    // Bitmap auf Page zeichnen
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(decodeService.getPageWidth(pageNumber), decodeService.getPageHeight(pageNumber), i).create();
                    PdfDocument.Page pdfPage = document.startPage(pageInfo);
                    pdfPage.getCanvas().drawBitmap(pageBitmap,0 ,0, pdfPaint);
                    // finish the page
                    document.finishPage(pdfPage);

                } else {
                    break;
                }
            }

            OutputStream os = new FileOutputStream(file.getPath() + File.separator + "test.pdf");
            // write the document content
            document.writeTo(os);
            //close the document
            document.close();
        }
        catch (Exception e) {

        }
        finally {
            decodeService.recycle();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mListener.onFinish();
        super.onPostExecute(result);
    }

}
