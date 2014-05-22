package com.unipdf.app.fragments;



import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.unipdf.app.Main;
import com.unipdf.app.R;
import com.unipdf.app.adapter.Categories_List_Adapt;
import com.unipdf.app.adapter.LightPDF_List_Adapt;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.vos.Categories;
import com.unipdf.app.vos.LightPDF;


public class Fav_List_Frag extends Fragment {

    public interface ICom_Fav_List_Frag{
        public void onItemShortClick_Fav_List_Frag(LightPDF _lightPDF, View v);
    }

    private static final String LOG_TAG = Fav_List_Frag.class.getSimpleName();

    private static final String EXTRA_CATEGORY_POS = "extraCategoryPos";

    private LightPDF_List_Adapt mFavsAdapter = null;
    private Categories_List_Adapt mCategoryAdapter = null;

    private ArrayList<Categories> mCategories = null;
    private ArrayList<LightPDF> mFavs = null;
    private ICom_Fav_List_Frag mICom_fav_list_frag = null;
    private View mView;

    private int mCategoryListPos;

    private ApplicationModel mModel;

    private ListView mLv;
    private GridView mGv;
    private ImageButton mAddButton;
    private EditText mCategoryName;

    private int mDeleteablePos;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mModel = ApplicationModel.getInstance();

        mICom_fav_list_frag = (ICom_Fav_List_Frag) getActivity();
        mCategories = mModel.getCategories();
        mFavs = new ArrayList<LightPDF>();

        mCategoryListPos = 0;
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
        }
        else {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fav_list, container, false);
        mLv= (ListView) mView.findViewById(R.id.fav_list_View);
        mGv = (GridView) mView.findViewById(R.id.fav_grid_View);
        mAddButton = (ImageButton) mView.findViewById(R.id.addCategoryButton);
        mCategoryName = (EditText) mView.findViewById(R.id.addCategoryName);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddButton.setOnClickListener(mAddButtonListener);

        registerForContextMenu(mLv);

        setFavView();
        setCategoryView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.fav_list_View) {
            menu.setHeaderTitle("Chose an action!");
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                // Edit

                return true;
            case R.id.context_menu_delete:
                // Delete
//                mModel.getCategories().remove(mDeleteablePos);
//                setCategoryView();
//                updateFavs(mGv.getCheckedItemPosition());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addFavsToCurrentCategory(ArrayList<LightPDF> _PDFs) {
        if(!mModel.getCategories().isEmpty()) {
            mModel.getCategories().get(mCategoryListPos).getLightPDFs().addAll(_PDFs);
            mFavs = mModel.getCategories().get(mCategoryListPos).getLightPDFs();

            setFavView();
        }
        else {
            Toast.makeText(getActivity(), "Please create a category!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFavView(){
        mFavsAdapter = new LightPDF_List_Adapt(mFavs,getActivity(),R.layout.list_item);
        mGv.setAdapter(mFavsAdapter);
        mGv.setOnItemClickListener(mFavClickListener);
    }

    private void setCategoryView() {
        mCategoryAdapter = new Categories_List_Adapt(getActivity(), mCategories);
        mLv.setOnItemClickListener(mCategoryClickListener);
        mLv.setOnItemLongClickListener(mCategoryLongClickListener);
        mLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mLv.setAdapter(mCategoryAdapter);
        mLv.setItemChecked(mCategoryListPos, true);
    }

    private void addNewCategory() {
        String categoryName = mCategoryName.getText().toString();

        if(!categoryName.isEmpty()) {
            mModel.getCategories().add(new Categories(new ArrayList<LightPDF>(), mCategoryName.getText().toString(), -1, null));
            setCategoryView();
            mCategoryName.setText("");
            Helper.hideKeyboard(getActivity());
        }
        else {
//            Helper.showToast("Category name should not be empty!");
        }
    }

    private void updateFavs(int _Position) {
        Log.d(LOG_TAG, "Position: "+_Position);
        mCategoryListPos = _Position;
        mFavs = mModel.getCategories().get(_Position).getLightPDFs();
    }

    private AdapterView.OnItemClickListener mCategoryClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            updateFavs(position);
            setFavView();

        }
    };

    private View.OnClickListener mAddButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           addNewCategory();
        }
    };

    private AdapterView.OnItemClickListener mFavClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    private AdapterView.OnItemLongClickListener mCategoryLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mDeleteablePos = position;
            return false;
        }
    };

}
