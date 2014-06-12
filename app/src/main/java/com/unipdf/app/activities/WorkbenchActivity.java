package com.unipdf.app.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.unipdf.app.R;
import com.unipdf.app.adapter.ImageAdapter;
import com.unipdf.app.utils.Helper;
import com.unipdf.app.utils.ThumbnailLoader;
import com.unipdf.app.vos.LightPDF;

import net.sf.andpdf.nio.ByteBuffer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class WorkbenchActivity extends Activity {

    ListView thumbnailList;
    List<Bitmap> thumbnails;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workbench);

        LightPDF pdf = getIntent().getParcelableExtra(ExplorerActivity.EXTRA_CHOSEN_PDF);

        thumbnailList = (ListView) findViewById(R.id.thumbnailList);
        thumbnails = new ArrayList<Bitmap>();
        adapter  = new ImageAdapter(this, thumbnails);


        ThumbnailLoader loader = new ThumbnailLoader();
        loader.setListener(mLoaderListener);
        loader.execute(pdf.getFilePath().getPath());

        thumbnailList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        thumbnailList.setAdapter(adapter);

        final PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
        pdfView.fromFile(new File(pdf.getFilePath().getPath()))
                .onPageChange(mPageChangeListener)
                .defaultPage(1)
                .showMinimap(true)
                .enableSwipe(true)

                .load();

        thumbnailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                thumbnailList.setItemChecked(position , true);
                pdfView.jumpTo(position+1);
            }
        });
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
        }

        @Override
        public void onFinish() {
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
