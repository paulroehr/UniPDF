package com.unipdf.app.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.unipdf.app.R;
import com.unipdf.app.adapter.LightPDF_List_Adapt;
import com.unipdf.app.vos.FavLightPDFs;
import com.unipdf.app.vos.LightPDF;


public class Fav_List_Frag extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ArrayList<FavLightPDFs> mFavLightPDFs = null;
    private ArrayList<String> mFavs = null;
    private String mFav;
    private ICom_Fav_List_Frag mICom_fav_list_frag = null;
    private View mView;

    private ListView mLv;
    private GridView mGv;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(view.getId() == R.id.fav_list_View){
           mFav=mFavs.get(i);
           setGridView();
        }
        else
        {
            mICom_fav_list_frag.onItemShortClick_Fav_List_Frag(GetLightPDFs().get(i),view);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        ///TODO Long Cklick bei Listview und Gridview

        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mICom_fav_list_frag = (ICom_Fav_List_Frag) getActivity();
        mFavLightPDFs = mICom_fav_list_frag.GetFavLightPDFs_Fav_List_Frag();
        mFavs = GetFavNames();
        mFav = mICom_fav_list_frag.GetStartFav_Fav_List_Frag();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("LightPDFFavList", (android.os.Parcelable) mFavLightPDFs);
        outState.putStringArrayList("Favs",mFavs);
        outState.putString("Fav",mFav);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            mFavLightPDFs = savedInstanceState.getParcelableArrayList("LightPDFFavList");
            mFavs = savedInstanceState.getStringArrayList("Favs");
            mFav = savedInstanceState.getString("Fav");
        }

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fav_list, container, false);
        mLv= (ListView) mView.findViewById(R.id.fav_list_View);
        mGv = (GridView) mView.findViewById(R.id.fav_grid_View);
        setGridView();
        setListView();
        return mView;
    }

    private void setGridView(){
        LightPDF_List_Adapt la = new LightPDF_List_Adapt(GetLightPDFs(),getActivity(),R.layout.list_item);
        mGv.setAdapter(la);
        mGv.setOnItemClickListener(this);
    }

    private void setListView(){
        ArrayAdapter la = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mFavs);
        mLv.setAdapter(la);
        mLv.setOnItemClickListener(this);
    }

    private ArrayList<LightPDF> GetLightPDFs(){

        ArrayList<LightPDF> LightPDFs = new ArrayList<LightPDF>();
        for(int i=0;i<mFavLightPDFs.size(); ++i){
            if(mFavLightPDFs.get(i).getmFavName() == mFav){
                LightPDFs = mFavLightPDFs.get(i).getmLightPDFs();
            }
        }
        return LightPDFs;
    }

    public ArrayList<String> GetFavNames(){
       ArrayList<String> Favs= new ArrayList<String>();
        for(int i=0; i < mFavLightPDFs.size(); ++i){
            Favs.add(i,mFavLightPDFs.get(i).getmFavName());
        }
        return Favs;
    }

    public interface ICom_Fav_List_Frag{
        public ArrayList<FavLightPDFs> GetFavLightPDFs_Fav_List_Frag();
        public String GetStartFav_Fav_List_Frag();
        public void onItemShortClick_Fav_List_Frag(LightPDF _lightPDF, View v);
        public void onItemLongClick_Fav_List_Frag(LightPDF _lightPDF, View v);

    }

}
