package com.unipdf.app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.unipdf.app.R;
import com.unipdf.app.adapter.ImageAdapter;
import com.unipdf.app.adapter.ShufflePageAdapter;
import com.unipdf.app.dialogs.LoadingDialog;
import com.unipdf.app.models.ApplicationModel;
import com.unipdf.app.utils.PDFCreator;
import com.unipdf.app.utils.ThumbnailLoader;
import com.unipdf.app.vos.LightPDF;
import com.unipdf.app.vos.ShufflePage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WorkbenchActivity extends Activity {

    private ApplicationModel mModel;

    private LightPDF mPDF;
    private File     mFileSrc;

    private PDFView  mPDFView;
    private ListView mThumbnailList;
    private ListView mShuffleList;
    private RelativeLayout mLoadingLayout;

    private ImageButton mUpButton;
    private ImageButton mDownButton;
    private ImageButton mNewButton;
    private ImageButton mSaveButton;
    private ImageButton mDeleteButton;

    private int mCurrentPage = 0;
    private int mCurrentShuffle = -1;
    private ArrayList<Bitmap>       mThumbnails;
    private ThumbnailLoader         mThumbnailLoader;
    private ImageAdapter            mThumbnailAdapter;
    private ShufflePageAdapter      mShufflePageAdapter;

    private LoadingDialog           mLoadingDialog;
    private PDFCreator              mPDFCreator;


//    File mCpy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workbench);

        mModel = ApplicationModel.getInstance();

        mPDF = getIntent().getParcelableExtra(ExplorerActivity.EXTRA_CHOSEN_PDF);

        getActionBar().setTitle(mPDF.getmName().substring(0, mPDF.getmName().length()-4));

        mFileSrc = new File(mPDF.getFilePath().getPath());
//        mCpy = null;
//        try {
//            File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "UniPDF");
//            if(!file.exists()) {
//                file.mkdirs();
//            }
//
//            mCpy = new File(Environment.getExternalStorageDirectory() + File.separator + "UniPDF" + File.separator + src.getName());
//            Helper.copy(src, mCpy);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        mThumbnailList = (ListView) findViewById(R.id.thumbnailList);
        mShuffleList = (ListView) findViewById(R.id.shuffleList);
        mPDFView = (PDFView) findViewById(R.id.pdfview);
        mLoadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        mUpButton = (ImageButton) findViewById(R.id.shuffleUp);
        mDownButton = (ImageButton) findViewById(R.id.shuffleDown);
        mNewButton = (ImageButton) findViewById(R.id.shuffleNew);
        mSaveButton = (ImageButton) findViewById(R.id.shuffleSave);
        mDeleteButton = (ImageButton) findViewById(R.id.shuffleDelete);

        switchVisibility(true);

        mUpButton.setOnClickListener(mUpListener);
        mDownButton.setOnClickListener(mDownListener);
        mNewButton.setOnClickListener(mNewListener);
        mSaveButton.setOnClickListener(mSaveListener);
        mDeleteButton.setOnClickListener(mDeleteListener);

        mThumbnailLoader = new ThumbnailLoader();
        mThumbnailLoader.setListener(mLoaderListener);
        mThumbnailLoader.setPath(Uri.fromFile(mFileSrc));
        mThumbnailLoader.setActivity(WorkbenchActivity.this);
        mThumbnailLoader.execute(new String[]{});

        mThumbnails = new ArrayList<Bitmap>();

        mThumbnailAdapter   = new ImageAdapter(this, mThumbnails);
        mShufflePageAdapter = new ShufflePageAdapter(this, mModel.getShuffleThumbnails());

        mThumbnailList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mThumbnailList.setAdapter(mThumbnailAdapter);

        mShuffleList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mShuffleList.setAdapter(mShufflePageAdapter);

       initializePDFView();

        mThumbnailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mThumbnailList.setItemChecked(position, true);
                mPDFView.jumpTo(position + 1);
            }
        });

        mShuffleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mShuffleList.setItemChecked(position, true);
                mCurrentShuffle = position;
            }
        });
    }

    @Override
    public void onBackPressed() {
        mThumbnailLoader.cancel(true);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.workbench, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_accept) {
            // Page aus Dokument holen und als PDPage speichern.

            mModel.getShuffleThumbnails().add(new ShufflePage(mThumbnails.get(mCurrentPage), mCurrentPage, mPDF.getFilePath()));
            mShufflePageAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializePDFView() {
        mPDFView.fromFile(mFileSrc)
                .onPageChange(mPageChangeListener)
                .defaultPage(mCurrentPage+1)
                .showMinimap(true)
                .enableSwipe(true)
                .load();
    }

    private void switchVisibility(boolean _showLoading) {
        if(_showLoading) {
            mPDFView.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.VISIBLE);
        }
        else {
            mPDFView.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    private void swapThumbnails(boolean _swapUp) {
        if(!mModel.getShuffleThumbnails().isEmpty() && mCurrentShuffle >= 0) {
            if (_swapUp) {
                if (mCurrentShuffle != 0) {
                    Collections.swap(mModel.getShuffleThumbnails(), mCurrentShuffle, mCurrentShuffle - 1);
                    mShufflePageAdapter.notifyDataSetChanged();
                    mShuffleList.setItemChecked(mCurrentShuffle - 1, true);
                    mCurrentShuffle -= 1;
                }
            } else {
                if (mCurrentShuffle < mModel.getShuffleThumbnails().size() - 1) {
                    Collections.swap(mModel.getShuffleThumbnails(), mCurrentShuffle, mCurrentShuffle + 1);
                    mShufflePageAdapter.notifyDataSetChanged();
                    mShuffleList.setItemChecked(mCurrentShuffle + 1, true);
                    mCurrentShuffle += 1;
                }
            }
            mShuffleList.smoothScrollToPosition(mCurrentShuffle);
        }
    }

    private void generateNewPDF(String _name) {
        mPDFCreator = new PDFCreator();
        mPDFCreator.setListener(mCreatorListener);
        mPDFCreator.setPDFName(_name);
        mPDFCreator.setActivity(WorkbenchActivity.this);
        mPDFCreator.setPages(mModel.getShuffleThumbnails());
        mPDFCreator.execute(new String[]{});
    }

    private void buildNewPDF(String _name) {
        mPDFView.recycle();
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getFragmentManager(), "loadingDialog");
        generateNewPDF(_name);
    }

    private void clearShuffleList() {
        mShuffleList.setItemChecked(mCurrentShuffle, false);
        mCurrentShuffle = -1;
        mModel.getShuffleThumbnails().clear();
        mShufflePageAdapter.notifyDataSetChanged();
    }

    private ThumbnailLoader.IThumbnailLoaderListener mLoaderListener = new ThumbnailLoader.IThumbnailLoaderListener() {
        @Override
        public void onProgress(Bitmap _Bitmap) {
            mThumbnails.add(_Bitmap);
            mThumbnailAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish() {
//            mCpy.delete();
            mThumbnailAdapter.notifyDataSetChanged();
            switchVisibility(false);
        }
    };

    private PDFCreator.IPDFCreatorListener mCreatorListener = new PDFCreator.IPDFCreatorListener() {
        @Override
        public void onFinish() {
            mLoadingDialog.dismiss();
            clearShuffleList();
            initializePDFView();
        }
    };

    private View.OnClickListener mUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            swapThumbnails(true);
        }
    };

    private View.OnClickListener mDownListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            swapThumbnails(false);
        }
    };

    private View.OnClickListener mNewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    WorkbenchActivity.this);

            // set title
            alertDialogBuilder.setTitle("New Document");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Do you really want to start a new document?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            clearShuffleList();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

    };

    private View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!mModel.getShuffleThumbnails().isEmpty()) {
                final EditText input = new EditText(WorkbenchActivity.this);

                new AlertDialog.Builder(WorkbenchActivity.this)
                        .setTitle("Create PDF")
                        .setMessage("Choose a name for your new PDF")
                        .setView(input)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                buildNewPDF(value);

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();


            }
        }
    };

    private View.OnClickListener mDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mCurrentShuffle >= 0) {
                mShuffleList.setItemChecked(mCurrentShuffle, false);
                mModel.getShuffleThumbnails().remove(mCurrentShuffle);
                mShufflePageAdapter.notifyDataSetChanged();
                mCurrentShuffle = -1;
            }
        }
    };

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {
            mCurrentPage = page - 1;
            mThumbnailList.setItemChecked(page - 1, true);
            mThumbnailList.smoothScrollToPosition(page - 1);
        }
    };

}
