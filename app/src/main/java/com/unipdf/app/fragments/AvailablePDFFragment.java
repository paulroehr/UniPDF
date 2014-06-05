package com.unipdf.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.unipdf.app.R;
import com.unipdf.app.adapter.LightPDF_List_Adapt;
import com.unipdf.app.events.Event;
import com.unipdf.app.events.EventListener;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.vos.LightPDF;


public class AvailablePDFFragment extends Fragment implements AdapterView.OnItemClickListener {

    public interface IAvailablePDFCallbacks {
        public void onItemShortClickAvailablePDF(LightPDF _lightPDF, View v);
        public void onSendCopyList(SparseArray<LightPDF> _CopyList);
    }

    //eventuell muss eine Parcebel list erstellt werden
    ArrayList<LightPDF> mAllPDFs = null;
    SparseArray<LightPDF> mCopyList = null;
    IAvailablePDFCallbacks mIAvailablePDFCallbacks = null;

    private ListView mLv;
    private LightPDF_List_Adapt mLa;

    private ApplicationModel mModel;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mIAvailablePDFCallbacks.onItemShortClickAvailablePDF(mAllPDFs.get(i), view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mModel = ApplicationModel.getInstance();

        mIAvailablePDFCallbacks = (IAvailablePDFCallbacks) activity;
        mAllPDFs = mModel.getPDFs();
        mCopyList = new SparseArray<LightPDF>();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("LightPDFAllList", mAllPDFs);
        outState.putSparseParcelableArray("CopyList", mCopyList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
//            mAllPDFs = savedInstanceState.getParcelableArrayList("LightPDFAllList");
            mCopyList = savedInstanceState.getSparseParcelableArray("CopyList");
            Log.d("Test", "Restored");
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_list, container, false);
        mLv = (ListView) v.findViewById(R.id.all_list_View);
        setListView();

        // EventListener wird registriert. Wenn neue PDFs hinzugefügt wurden, wird der EventListener benachrichtigt.
        ApplicationModel.getInstance().addListener(ApplicationModel.ChangeEvent.EVENT_RECEIVED_PDFS, mReceivedNewPDFsListener);
        return v;
    }

    public void notifyListChange() {
        // Adapter wird informaiert das Daten geändert wurden und lädt diese neu in die View.
        mLa.notifyDataSetChanged();
        // Bei neuen Daten, nach unten scrollen.
        mLv.smoothScrollToPosition(mLa.getCount());
    }


    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // EventListener wird wieder ausgeklinkt um keine sinnlosen Events zu feuern bzw. zu empfangen --> kann NullPointerExceptiopn auslösen
        ApplicationModel.getInstance().removeListener(ApplicationModel.ChangeEvent.EVENT_RECEIVED_PDFS, mReceivedNewPDFsListener);
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
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.add_favs_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.action_add_favs) {
                    mIAvailablePDFCallbacks.onSendCopyList(mCopyList);
                    mode.finish();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mCopyList.clear();
            }
        });
        mLv.setAdapter(mLa);
    }

    private void changeCopyList(int _Pos, boolean _Checked) {
        if(_Checked) {
            mCopyList.put(_Pos, mAllPDFs.get(_Pos));
        }
        else {
            mCopyList.remove(_Pos);
        }
    }

    private EventListener mReceivedNewPDFsListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            notifyListChange();
        }
    };


}
