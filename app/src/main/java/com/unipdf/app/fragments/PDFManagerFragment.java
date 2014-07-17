package com.unipdf.app.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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

import com.unipdf.app.R;
import com.unipdf.app.adapter.CategoriesAdapter;
import com.unipdf.app.adapter.PreviewFavoritesAdapter;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.vos.Category;
import com.unipdf.app.vos.LightPDF;

import java.util.ArrayList;

/**
 * Verwaltung der erstellten Kategorien und deren Favoriten.
 */
public class PDFManagerFragment extends Fragment {

    /**
     * Dient zur Kommunikation zwischen Activity und Fragment.
     */
    public interface IPDFManagerCallbacks {
        public void onItemShortClickFavoritedPDF(LightPDF _lightPDF);
        public void addCategory(Category _Category);
        public void updateCurrentCategory(Category _Category);
        public void deleteCategory(Category _Category);
        public void addPDFToCategory(Category _Category, LightPDF _PDF);
        public void deletePDFFromCategory(Category _Category, LightPDF _PDF);
    }

    private static final String LOG_TAG = PDFManagerFragment.class.getSimpleName();

    private static final String EXTRA_CATEGORY_POS = "extraCategoryPos";

    private PreviewFavoritesAdapter mFavsAdapter = null;
    private CategoriesAdapter mCategoryAdapter = null;

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
        mCurrentCategory = mCategories.get(0);
        mFavs = mCurrentCategory.getLightPDFs();

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
        mLv = (ListView) mView.findViewById(R.id.fav_list_View);
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
                Category category = mModel.getCategories().get(mRenamePos);
                mIPDFManagerCallbacks.deleteCategory(category);
                mModel.getCategories().remove(mRenamePos);
                updateFavs(0);
                setCategoryView();
                setFavView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Fügt ausgewählte PDF's zur aktuellen Kategorie hinzu.
     * @param _PDFs Ausgewählte PDF's
     */
    public void addFavsToCurrentCategory(ArrayList<LightPDF> _PDFs) {
        if(!mCategories.isEmpty()) {
            for (LightPDF pdf : _PDFs) {
                if(!Helper.checkForExistingPDFs(pdf.getFilePath(), mCurrentCategory.getLightPDFs())) {
                    mIPDFManagerCallbacks.addPDFToCategory(mCategories.get(mCategoryListPos), pdf);
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

    /**
     * Initialisierung der Favoriten Ansicht.
     */
    private void setFavView(){
        mFavsAdapter = new PreviewFavoritesAdapter(mFavs, getActivity(), R.layout.item_favorites_preview);
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

    /**
     * Entfernt ausgewählte PDF's von der aktuellen Kategorie.
     */
    private void deleteSelectedPDFsFromCategory() {
        for (int index = 0; index < mDeleteList.size(); index++) {
            mIPDFManagerCallbacks.deletePDFFromCategory(mCurrentCategory, mDeleteList.valueAt(index));
            mFavs.remove(mDeleteList.valueAt(index));
        }
        mIPDFManagerCallbacks.updateCurrentCategory(mCurrentCategory);
        mFavsAdapter.notifyDataSetChanged();
    }

    /**
     * Entfernt oder Setzt gegebene Position in die Lösch Liste, anhand des gesetzten _Checked Flags.
     * @param _Pos      Listen Position
     * @param _Checked  Flag zur Indikation ob Position schon gesetzt ist oder nicht
     */
    private void changeDeleteList(int _Pos, boolean _Checked) {
        if(_Checked) {
            mDeleteList.put(_Pos, mFavs.get(_Pos));
        }
        else {
            mDeleteList.remove(_Pos);
        }
    }

    /**
     * Initialiserung der Kategorien Ansicht
     */
    private void setCategoryView() {
        mCategoryAdapter = new CategoriesAdapter(getActivity(), mCategories);
        mLv.setOnItemClickListener(mCategoryClickListener);
        mLv.setOnItemLongClickListener(mCategoryLongClickListener);
        mLv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mLv.setAdapter(mCategoryAdapter);
        mLv.setItemChecked(mCategoryListPos, true);
    }

    /**
     * Überprüfung ob gewählte Kategorie schon vorhanden ist.
     * @param _Name Zu Überprüfende Kategorie
     * @return true wenn schon vorhanden
     */
    private boolean checkForExisitingCategory(String _Name) {
        for (Category category : mModel.getCategories()) {
            if(category.getCategoryName().equals(_Name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Erstellt neue Kategorie falls noch nicht vorhanden.
     */
    private void addNewCategory() {
        String categoryName = mCategoryName.getText().toString();

        if(!categoryName.isEmpty()) {
            if(!checkForExisitingCategory(categoryName)) {
                Category category = new Category(new ArrayList<LightPDF>(), categoryName);

                mIPDFManagerCallbacks.addCategory(category);

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
            Helper.showToast(getActivity(), "Category name should not be empty!");
        }
    }

    /**
     * Wenn neue Kategorie ausgewählt wird, werden die Favoriten aktualisiert.
     * @param _Position Kategorie Position in der Liste
     */
    private void updateFavs(int _Position) {
        mCategoryListPos = _Position;
        mCurrentCategory = mCategories.get(_Position);
        mFavs = mCurrentCategory.getLightPDFs();
    }

    private AdapterView.OnItemClickListener mCategoryClickListener = new AdapterView.OnItemClickListener() {

        /**
         * Wenn auf Kategorie Listen Item geklickt wurde, wird neue Kategorie mit den enthaltenen PDF's geladen.
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            updateFavs(position);
            setFavView();

        }
    };

    private View.OnClickListener mAddButtonListener = new View.OnClickListener() {

        /**
         * Startet das hinzufügen einer neuen Kategorie.
         * @param v
         */
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

        /**
         * Wenn auf ein Favorit geklickt wird, wird die Activity benachrichtigt und startet die Workbench.
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mIPDFManagerCallbacks.onItemShortClickFavoritedPDF(mFavs.get(position));
        }
    };

    private AdapterView.OnItemLongClickListener mCategoryLongClickListener = new AdapterView.OnItemLongClickListener() {

        /**
         * Setzt die Position des Items welches den LongClick abfing.
         * @param parent
         * @param view
         * @param position
         * @param id
         * @return
         */
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mRenamePos = position;
            return false;
        }
    };

}
