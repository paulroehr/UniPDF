package com.unipdf.app.fragments;



import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
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

import com.unipdf.app.R;
import com.unipdf.app.adapter.Categories_List_Adapt;
import com.unipdf.app.adapter.LightPDF_List_Adapt;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.LightPDF;


public class PDFManagerFragment extends Fragment {

    public interface IPDFManagerCallbacks {
        public void onItemShortClickFavoritedPDF(LightPDF _lightPDF, View v);
        public void updateCurrentCategory(Category _Category);
        public void deleteCategory(Category _Category);
    }

    private static final String LOG_TAG = PDFManagerFragment.class.getSimpleName();

    private static final String EXTRA_CATEGORY_POS = "extraCategoryPos";

    private LightPDF_List_Adapt mFavsAdapter = null;
    private Categories_List_Adapt mCategoryAdapter = null;

    private ArrayList<Category> mCategories = null;
    private ArrayList<LightPDF> mFavs = null;
    private Category mCurrentCategory = null;
    private IPDFManagerCallbacks mIPDFManagerCallbacks = null;
    private View mView;

    private int mCategoryListPos;

    SparseArray<LightPDF> mDeleteList = new SparseArray<LightPDF>();

    private ApplicationModel mModel;

    private ListView mLv;
    private GridView mGv;
    private ImageButton mAddButton;
    private EditText mCategoryName;

    private int mRenamePos;
    private boolean mIsInRenameMode = false;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mModel = ApplicationModel.getInstance();

        mIPDFManagerCallbacks = (IPDFManagerCallbacks) getActivity();
        mCategories = mModel.getCategories();
        mFavs = new ArrayList<LightPDF>();
        mCurrentCategory = mCategories.get(0);

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
            if(mCategories.size() > 1) {
                menu.setHeaderTitle("Chose an action!");
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
            }
            else {
                menu.setHeaderTitle("Rename your category!");
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.context_menu_edit, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                // Edit
                mIsInRenameMode = true;
//                mCategoryName.requestFocus();
                Helper.showKeyboard(getActivity(), mCategoryName);
                mCategoryName.setText(mCategories.get(mRenamePos).getCategoryName());
                mAddButton.setImageResource(R.drawable.ic_action_edit);
                return true;
            case R.id.context_menu_delete:
                // Delete
                mIPDFManagerCallbacks.deleteCategory(mModel.getCategories().get(mRenamePos));
                mModel.getCategories().remove(mRenamePos);
                updateFavs(0);
                setCategoryView();
                setFavView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private boolean checkForExistingFavs(Uri _Path)
    {
        for (LightPDF PDF : mCurrentCategory.getLightPDFs()) {
            if(PDF.getFilePath().equals(_Path)) {
                return true;
            }
        }
        return false;
    }

    public void addFavsToCurrentCategory(ArrayList<LightPDF> _PDFs) {
        if(!mCategories.isEmpty()) {
            for (LightPDF pdf : _PDFs) {
                if(!checkForExistingFavs(pdf.getFilePath())) {
                    mCategories.get(mCategoryListPos).getLightPDFs().add(pdf);
                }
            }
            mFavs = mCategories.get(mCategoryListPos).getLightPDFs();

            setFavView();
        }
        else {
            Toast.makeText(getActivity(), "Please create a category!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFavView(){
        mFavsAdapter = new LightPDF_List_Adapt(mFavs,getActivity(),R.layout.list_item);
        mGv.setOnItemClickListener(mFavClickListener);
        mGv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mGv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                changeDeleteList(position, checked);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.delete_favs_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.action_delete_favs) {
                    deleteSelectedPDFsFromCategory();
                    mode.finish();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                mDeleteList.clear();
            }
        });
        mGv.setAdapter(mFavsAdapter);
    }

    private void deleteSelectedPDFsFromCategory() {
        for (int index = 0; index < mDeleteList.size(); index++) {
            mFavs.remove(mDeleteList.valueAt(index));
        }
        mIPDFManagerCallbacks.updateCurrentCategory(mCurrentCategory);
        mFavsAdapter.notifyDataSetChanged();
    }

    private void changeDeleteList(int _Pos, boolean _Checked) {
        if(_Checked) {
            mDeleteList.put(_Pos, mFavs.get(_Pos));
        }
        else {
            mDeleteList.remove(_Pos);
        }
    }

    private void setCategoryView() {
        mCategoryAdapter = new Categories_List_Adapt(getActivity(), mCategories);
        mLv.setOnItemClickListener(mCategoryClickListener);
        mLv.setOnItemLongClickListener(mCategoryLongClickListener);
        mLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mLv.setAdapter(mCategoryAdapter);
        mLv.setItemChecked(mCategoryListPos, true);
    }



    private boolean checkForExisitingCategory(String _Name) {
        for (Category category : mModel.getCategories()) {
            if(category.getCategoryName().equals(_Name)) {
                return true;
            }
        }
        return false;
    }

    private void addNewCategory() {
        String categoryName = mCategoryName.getText().toString();

        if(!categoryName.isEmpty()) {
            if(!checkForExisitingCategory(categoryName)) {
                Category category = new Category(new ArrayList<LightPDF>(), categoryName, -1, null);
                mModel.getCategories().add(category);
                setCategoryView();
                mCategoryName.setText("");
                Helper.hideKeyboard(getActivity());
            }
            else {
                Helper.showPositionedToast(getActivity(), "Category already exists.", Gravity.TOP | Gravity.CENTER);
            }
        }
        else {
//            Helper.showToast("Category name should not be empty!");
        }
    }

    private void updateFavs(int _Position) {
      //  Log.d(LOG_TAG, "Position: "+_Position);
        mCategoryListPos = _Position;
        mCurrentCategory = mCategories.get(_Position);
        mFavs = mCurrentCategory.getLightPDFs();
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
            if(mIsInRenameMode) {
                // Rename
                if(!mCategoryName.getText().toString().isEmpty()) {
                    Helper.hideKeyboard(getActivity(), mCategoryName);
                    mIsInRenameMode = false;
                    mCategories.get(mRenamePos).setCategoryName(mCategoryName.getText().toString());
                    mIPDFManagerCallbacks.updateCurrentCategory(mCategories.get(mRenamePos));
                    mAddButton.setImageResource(R.drawable.ic_action_new);
                    mCategoryAdapter.notifyDataSetChanged();
                    mCategoryName.setText("");
                }
                else {
                    Helper.showPositionedToast(getActivity(), "Category name should not be empty.", Gravity.TOP | Gravity.CENTER);
                }
            }
            else {
                addNewCategory();
            }
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
            mRenamePos = position;
            return false;
        }
    };

}
