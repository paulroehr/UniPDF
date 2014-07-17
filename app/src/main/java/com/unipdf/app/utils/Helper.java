package com.unipdf.app.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.unipdf.app.vos.LightPDF;

import java.util.ArrayList;

/**
 * Enthält mehrfach benötigte Hilfsmethoden.
 */
public class Helper {

    /**
     * Konvertiert einen SparseArray zu einer ArrayList.
     * @param _Source       Umzuwandelnde SparseArrayList
     * @param _Destination  Ziel der Umwandlung
     */
    public static void convertSparseToArrayList(SparseArray<LightPDF> _Source, ArrayList<LightPDF> _Destination) {
        for(int index = 0; index < _Source.size(); ++index) {
            _Destination.add(_Source.valueAt(index));
        }
    }

    //hideKeyboard----------------------------------------------------------------------------------
    /**
     * Schließt das SoftKeyboard, falls es offen ist.
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

    /**
     * Öffnet das SoftKeyboard.
     * @param _activity
     * @param _view
     */
    public static void showKeyboard(Activity _activity, View _view) {
        InputMethodManager inputMethodManager=(InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(_view, 0);
    }

    /**
     * Schließt das SoftKeyboard.
     * @param _activity
     * @param _view
     */
    public static void hideKeyboard(Activity _activity, View _view) {
        InputMethodManager inputMethodManager=(InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
    }

    /**
     * Zeigt Standard Toast.
     * @param _Activity
     * @param _message Darzustellende Nachricht
     */
    public static void showToast(Activity _Activity, String _message)
    {
        Toast.makeText(_Activity, _message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Zeigt Positionierten Toast.
     * @param _Activity
     * @param _message  Darzustellende Nachricht
     * @param _Gravity  Position des Toasts
     */
    public static void showPositionedToast(Activity _Activity, String _message, int _Gravity)
    {
        Toast toast = Toast.makeText(_Activity, _message, Toast.LENGTH_SHORT);
        toast.setGravity(_Gravity, 0, 120);
        toast.show();
    }

    /**
     * Bitmap Auflösung wird auf gegebene Auflösung herunter skaliert.
     * @param bitmapWidth
     * @param bitmapHeight
     * @param newHeight
     * @param context
     * @param scale
     */
    public static void scaleDownSize(int bitmapWidth, int bitmapHeight, int newHeight, Context context, int[] scale) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        scale[1] = (int) (newHeight * densityMultiplier);
        scale[0] = (int) (scale[1] * ((double) bitmapWidth / ((double) bitmapHeight)));

    }

    /**
     * Überprüft ob gegebene PDF schon in Liste vorhanden ist.
     * @param _Path
     * @param _List
     * @return
     */
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
