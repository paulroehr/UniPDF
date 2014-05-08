package com.unipdf.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.unipdf.app.R;
import com.unipdf.app.fragments.All_List_Frag;
import com.unipdf.app.vos.LightPDF;

public class FirstActivity extends Activity implements All_List_Frag.ICom_All_List_Frag{

    private All_List_Frag mAlf;
    private ArrayList<LightPDF> mAllPDFs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mAllPDFs = new ArrayList<LightPDF>();

        mAllPDFs.add(new LightPDF(null, "Montag"));
        mAllPDFs.add(new LightPDF(null, "Dienstag"));
        mAllPDFs.add(new LightPDF(null, "Mittwoch"));
        mAllPDFs.add(new LightPDF(null, "Donnerstag"));
        mAllPDFs.add(new LightPDF(null, "Freitag"));
        mAllPDFs.add(new LightPDF(null, "Samstag"));

        mAlf = new All_List_Frag();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.all_list_frame__container, mAlf)
                .commit();

        // Service wird erstellt
        // mIsd wird an Service als Listener übergeben.

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ArrayList<LightPDF> GetAllLightPDFs_All_List_Frag() {
        return mAllPDFs;
    }

    @Override
    public void onItemShortClick_All_List_Frag(LightPDF _lightPDF, View _v) {
        ///TODO zweite Activity öffnen und PDF übergeben


    }

    private IService_Data mIsd = new IService_Data() {

        /**
         * Teilliste wird vom Service übermittelt nach noch festzulegender Zeit.
         * @param _List
         */
        @Override
        public void serviceChangeListData(ArrayList<LightPDF> _List) {
            mAllPDFs.addAll(_List);
            mAlf.notifyListChange();
        }
    };

    // Steht in dem Service
    public interface IService_Data {
        void serviceChangeListData(ArrayList<LightPDF> _List);
    }
}
