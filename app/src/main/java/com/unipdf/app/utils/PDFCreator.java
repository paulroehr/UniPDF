package com.unipdf.app.utils;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.unipdf.app.Main;
import com.unipdf.app.activities.WorkbenchActivity;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.vos.LightPDF;
import com.unipdf.app.vos.ShufflePage;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Erstellt ein neues PDF Dokument anhand der übergebenen ShufflePages.
 */
public class PDFCreator extends AsyncTask<String, Void, Boolean> {

    public interface IPDFCreatorListener {
        void onFinish();
    }

    List<ShufflePage> mPages;
    IPDFCreatorListener mListener;
    Uri mOldPath = null;
    String mPDFName;
    WorkbenchActivity activity;

    /** The decode service used for decoding the PDF */
    private DecodeService decodeService;

    public void setListener(IPDFCreatorListener _listener) {
        mListener = _listener;
    }

    public void setPages(List<ShufflePage> _pages) {
        mPages = _pages;
    }

    public void setActivity(WorkbenchActivity _activity) {
        activity = _activity;
    }

    public void setPDFName(String _PDFName) {
        mPDFName = _PDFName;
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

            // PDF Dokument erstellen
            PdfDocument document = new PdfDocument();

            for (int i = 0; i < mPages.size(); i++) {
                if (!isCancelled()) {

                    Uri newPath = mPages.get(i).getPath();
                    if(mOldPath == null || !mOldPath.equals(newPath)) {
                        decodeService.open(newPath);
                        mOldPath = newPath;
                    }
                    int pageNumber = mPages.get(i).getPage();
                    CodecPage page = decodeService.getPage(pageNumber);

                    // Seite wird als Bitmap gerendet
                    Bitmap pageBitmap = page.renderBitmap(decodeService.getPageWidth(pageNumber), decodeService.getPageHeight(pageNumber), new RectF(0, 0, 1, 1));

                    // Bitmap auf Page zeichnen
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(decodeService.getPageWidth(pageNumber), decodeService.getPageHeight(pageNumber), i).create();
                    PdfDocument.Page pdfPage = document.startPage(pageInfo);
                    pdfPage.getCanvas().drawBitmap(pageBitmap,0 ,0, pdfPaint);
                    // Beenden der aktuellen Seite
                    document.finishPage(pdfPage);

                } else {
                    break;
                }
            }

            String path = file.getPath() + File.separator + mPDFName + ".pdf";
            OutputStream os = new FileOutputStream(path);
            // Schreibt PDF in das FileSystem
            document.writeTo(os);
            // Schließt das Dokument
            document.close();

            LightPDF lightPDF = new LightPDF(null, mPDFName + ".pdf", Uri.fromFile(new File(path)));
            ApplicationModel.getInstance().getPDFs().add(lightPDF);
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
