package com.unipdf.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.unipdf.app.Main;
import com.unipdf.app.vos.LightPDF;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by paul on 15.05.14.
 */
public class Helper {

    public static void convertSparseToArrayList(SparseArray<LightPDF> _Source, ArrayList<LightPDF> _Destination) {
        for(int index = 0; index < _Source.size(); ++index) {
            _Destination.add(_Source.valueAt(index));
        }
    }

    //hideKeyboard----------------------------------------------------------------------------------
    /**
     * hides the current soft keyboard, if it is open.
     */
    public static void hideKeyboard(android.app.Activity _activity)
    {
        InputMethodManager inputManager = (InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (_activity.getCurrentFocus() != null)
        {
            inputManager.hideSoftInputFromWindow(_activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        else
        {
            inputManager.hideSoftInputFromWindow(null, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity _activity) {
        InputMethodManager inputMethodManager=(InputMethodManager)_activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(_activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showKeyboard(Activity _activity, View _view) {
        InputMethodManager inputMethodManager=(InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(_view, 0);
    }

    public static void hideKeyboard(Activity _activity, View _view) {
        InputMethodManager inputMethodManager=(InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
    }

    //showToast-------------------------------------------------------------------------------------
    public static void showToast(Activity _Activity, String _message)
    {
        Toast.makeText(_Activity, _message, Toast.LENGTH_SHORT).show();
    }

    //showToast-------------------------------------------------------------------------------------
    public static void showPositionedToast(Activity _Activity, String _message, int _Gravity)
    {
        Toast toast = Toast.makeText(_Activity, _message, Toast.LENGTH_SHORT);
        toast.setGravity(_Gravity, 0, 120);
        toast.show();
    }

    //dpToPx----------------------------------------------------------------------------------------
    /**
     * Converts density-independent pixel (dp) to pixel (px)
     *
     * @param dp the dp value to convert in pixel
     *
     * @return the converted value in pixels
     */
    public static float dpToPx(float dp)
    {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }


    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight*densityMultiplier);
        int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public static void scaleDownSize(int bitmapWidth, int bitmapHeight, int newHeight, Context context, int[] scale) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        scale[1] = (int) (newHeight * densityMultiplier);
        scale[0] = (int) (scale[1] * ((double) bitmapWidth / ((double) bitmapHeight)));

    }

    public static boolean checkForExistingPDFs(Uri _Path, ArrayList<LightPDF> _List)
    {
        if (_List != null) {
            for (LightPDF PDF : _List) {
                if (PDF.getFilePath().equals(_Path)) {
                    return true;
                }
            }
        }
        return false;
    }
}
