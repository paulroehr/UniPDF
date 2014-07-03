package com.unipdf.app.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.unipdf.app.R;
import com.unipdf.app.adapter.ImageAdapter;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.utils.ThumbnailLoader;
import com.unipdf.app.vos.LightPDF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class WorkbenchActivity extends Activity {

    PDFView mPDFView;

    ListView thumbnailList;
    List<Bitmap> thumbnails;
    ImageAdapter adapter;

    ThumbnailLoader mLoader;

//    File mCpy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workbench);

        LightPDF pdf = getIntent().getParcelableExtra(ExplorerActivity.EXTRA_CHOSEN_PDF);

        File src = new File(pdf.getFilePath().getPath());
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

        mLoader = new ThumbnailLoader();
        mLoader.setListener(mLoaderListener);
        mLoader.setPath(Uri.fromFile(src));
        mLoader.setActivity(WorkbenchActivity.this);
        mLoader.execute(new String[]{});

        thumbnails = new ArrayList<Bitmap>();
        adapter  = new ImageAdapter(this, thumbnails);

        thumbnailList = (ListView) findViewById(R.id.thumbnailList);
        thumbnailList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        thumbnailList.setAdapter(adapter);

        mPDFView = (PDFView) findViewById(R.id.pdfview);
        mPDFView.fromFile(src)
                .onPageChange(mPageChangeListener)
                .defaultPage(1)
                .showMinimap(true)
                .enableSwipe(true)
                .load();

        thumbnailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                thumbnailList.setItemChecked(position , true);
                mPDFView.jumpTo(position+1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        mLoader.cancel(true);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ThumbnailLoader.IThumbnailLoaderListener mLoaderListener = new ThumbnailLoader.IThumbnailLoaderListener() {
        @Override
        public void onProgress(Bitmap _Bitmap) {
            thumbnails.add(_Bitmap);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish() {
//            mCpy.delete();
            adapter.notifyDataSetChanged();

        }
    };

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {
            thumbnailList.setItemChecked(page-1 , true);
            thumbnailList.smoothScrollToPosition(page-1);
        }
    };

}
