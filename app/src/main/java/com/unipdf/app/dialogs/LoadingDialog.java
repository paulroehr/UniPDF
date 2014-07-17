package com.unipdf.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.unipdf.app.Main;
import com.unipdf.app.R;


/**
 * Dialog zur Indikation des Speichervorgangs
 */
public class LoadingDialog extends DialogFragment {

    public LoadingDialog() {
    }

    //onCreateDialog--------------------------------------------------------------------------------
    @Override
    public Dialog onCreateDialog(Bundle _savedInstanceState)
    {
        Context context  = Main.getAppContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();

        setCancelable(false);

        mBaseLayout   = (RelativeLayout) inflater.inflate(R.layout.include_loading, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mBaseLayout);
        builder.setCancelable(false);

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private RelativeLayout mBaseLayout;
}
