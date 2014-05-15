package com.unipdf.app.utils;

import android.util.SparseArray;

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

}
