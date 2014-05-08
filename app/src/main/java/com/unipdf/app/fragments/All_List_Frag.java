package com.unipdf.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.unipdf.app.R;
import com.unipdf.app.adapter.LightPDF_List_Adapt;
import com.unipdf.app.vos.LightPDF;


public class All_List_Frag extends Fragment implements AdapterView.OnItemClickListener {

    //eventuell muss eine Parcebel list erstellt werden
    ArrayList<LightPDF> mAllPDFs = null;
    SparseArray<LightPDF> mCopyList = null;
    ICom_All_List_Frag mICom_all_list_frag = null;

    private ListView mLv;
    private LightPDF_List_Adapt mLa;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mICom_all_list_frag.onItemShortClick_All_List_Frag(mAllPDFs.get(i), view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mICom_all_list_frag = (ICom_All_List_Frag) activity;
        mAllPDFs = mICom_all_list_frag.GetAllLightPDFs_All_List_Frag();
        mCopyList = new SparseArray<LightPDF>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("LightPDFAllList", mAllPDFs);
        outState.putSparseParcelableArray("CopyList", mCopyList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            mAllPDFs = savedInstanceState.getParcelableArrayList("LightPDFAllList");
            mCopyList = savedInstanceState.getSparseParcelableArray("CopyList");
            Log.d("Test", "Restored");
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_list, container, false);
        mLv = (ListView) v.findViewById(R.id.all_list_View);
        setListView();
        return v;
    }

    public void notifyListChange() {
        // Adapter wird informaiert das Daten geändert wurden und lädt diese neu in die View.
        mLa.notifyDataSetChanged();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    private void setListView(){
        mLa = new LightPDF_List_Adapt(mAllPDFs,getActivity(),R.layout.list_item);
        mLv.setOnItemClickListener(this);
        mLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mLv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                changeCopyList(position, checked);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        mLv.setAdapter(mLa);

        if(mCopyList.size() > 0) {
            for (int index = 0; index < mCopyList.size(); ++index) {
                int pos = mCopyList.keyAt(index);
                Log.d("Test", ""+pos);
                mLv.setItemChecked(pos, true);
            }
        }
    }

    private void changeCopyList(int _Pos, boolean _Checked) {
        if(_Checked) {
            mCopyList.put(_Pos, mAllPDFs.get(_Pos));
        }
        else {
            mCopyList.remove(_Pos);
        }
    }

    public interface ICom_All_List_Frag{
       public ArrayList<LightPDF> GetAllLightPDFs_All_List_Frag();
       public void onItemShortClick_All_List_Frag(LightPDF _lightPDF, View v);
    }
}
