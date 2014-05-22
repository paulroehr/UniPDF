package com.unipdf.app.utils;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.unipdf.app.Main;
import com.unipdf.app.vos.LightPDF;

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

    //showToast-------------------------------------------------------------------------------------
    public static void showToast(Activity _Activity, String _message)
    {
        Toast.makeText(_Activity, _message, Toast.LENGTH_SHORT).show();
    }

}
