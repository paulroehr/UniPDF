package com.unipdf.app.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.pdfview.PDFView;
import com.unipdf.app.R;
import com.unipdf.app.vos.LightPDF;

import java.io.File;

public class WorkbenchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workbench);

        LightPDF pdf = getIntent().getParcelableExtra(ExplorerActivity.EXTRA_CHOSEN_PDF);

        PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
        pdfView.fromFile(new File(pdf.getFilePath().getPath()))

                .defaultPage(1)
                .showMinimap(true)
                .enableSwipe(true)

                .load();
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
}
