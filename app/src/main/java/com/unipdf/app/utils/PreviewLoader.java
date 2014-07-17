package com.unipdf.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.unipdf.app.R;
import com.unipdf.app.activities.ExplorerActivity;
import com.unipdf.app.vos.LightPDF;

import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;

/**
 * Generiert Vorschaubild der Favoriten.
 */
public class PreviewLoader {

    public static void loadPreview(LightPDF _pdf, ImageView _view, ExplorerActivity _activity) {
        ThumbnailLoader loader = new ThumbnailLoader();
        loader.setActivity(_activity);
        loader.setView(_view);
        loader.setPdf(_pdf);
        loader.execute(new String[]{});
    }

    private static class ThumbnailLoader extends AsyncTask<String, Bitmap, Bitmap> {

        private LightPDF mPdf;
        private ExplorerActivity activity;
        private ImageView mView;

        /** The decode service used for decoding the PDF */
        private DecodeService decodeService;

        public void setPdf(LightPDF _pdf) {
            mPdf = _pdf;
        }

        public void setView(ImageView _View) {
            mView = _View;
        }

        public void setActivity(ExplorerActivity _activity) {
            activity = _activity;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap pageBitmap = mPdf.getmPicture();
            int[] scale = new int[2];

            if(pageBitmap == null) {
                decodeService = new DecodeServiceBase(new PdfContext());
                decodeService.setContentResolver(activity.getContentResolver());
                try {
                    decodeService.open(mPdf.getFilePath());
                    CodecPage page = decodeService.getPage(0);

                    Helper.scaleDownSize(decodeService.getPageWidth(0), decodeService.getPageHeight(0), 128, activity, scale);
                    pageBitmap = page.renderBitmap(scale[0], scale[1], new RectF(0, 0, 1, 1));
                } catch (Exception e) {

                }
                finally {
                    decodeService.recycle();
                }
            }

            return pageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if(result == null) {
                result = BitmapFactory.decodeResource(activity.getResources(), R.drawable.pdf_corrupted);
            }

            mPdf.setmPicture(result);
            mView.setImageBitmap(result);
        }

    }

}
