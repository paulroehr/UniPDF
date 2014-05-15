package com.unipdf.app.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import com.unipdf.app.R;
import com.unipdf.app.adapter.Categories_List_Adapt;
import com.unipdf.app.adapter.LightPDF_List_Adapt;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.vos.Categories;
import com.unipdf.app.vos.LightPDF;


public class Fav_List_Frag extends Fragment {

    public interface ICom_Fav_List_Frag{
        public void onItemShortClick_Fav_List_Frag(LightPDF _lightPDF, View v);
    }

    private static final String EXTRA_CATEGORY_POS = "extraCategoryPos";

    private ArrayList<Categories> mCategories = null;
    private ArrayList<LightPDF> mFavs = null;
    private ICom_Fav_List_Frag mICom_fav_list_frag = null;
    private View mView;

    private int mCategoryListPos;

    private ApplicationModel mModel;

    private ListView mLv;
    private GridView mGv;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mModel = ApplicationModel.getInstance();

        mICom_fav_list_frag = (ICom_Fav_List_Frag) getActivity();
        mCategories = mModel.getCategories();
        mFavs = new ArrayList<LightPDF>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CATEGORY_POS, mCategoryListPos);
//        outState.putParcelable("LightPDFFavList", (android.os.Parcelable) mCategories);
//        outState.putStringArrayList("Favs",mFavs);
//        outState.putString("Fav",mFav);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            mCategoryListPos = savedInstanceState.getInt(EXTRA_CATEGORY_POS);
            Log.d("Test", "Position: "+mCategoryListPos);
        }
        else {
//            mCategoryListPos = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fav_list, container, false);
        mLv= (ListView) mView.findViewById(R.id.fav_list_View);
        mGv = (GridView) mView.findViewById(R.id.fav_grid_View);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFavView();
        Log.d("Test", "Position12: "+mCategoryListPos);
        setCategoryView();
        Log.d("Test", "Position1: "+mCategoryListPos);
    }

    public void addFavsToCurrentCategory(ArrayList<LightPDF> _PDFs) {
        mFavs.addAll(_PDFs);

        // Logik zum hinzufügen der Kategorien

        setFavView();
    }

    private void setFavView(){
        LightPDF_List_Adapt la = new LightPDF_List_Adapt(mFavs,getActivity(),R.layout.list_item);
        mGv.setAdapter(la);
        mGv.setOnItemClickListener(mFavClickListener);
    }

    private void setCategoryView(){
        Categories_List_Adapt la = new Categories_List_Adapt(getActivity(), mCategories);
        mLv.setOnItemClickListener(mCategoryClickListener);
        mLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mLv.setAdapter(la);
        mLv.setItemChecked(mCategoryListPos, true);
    }

    private AdapterView.OnItemClickListener mCategoryClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Click", "click");
            mCategoryListPos = position;
        }
    };


    private AdapterView.OnItemClickListener mFavClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

}
