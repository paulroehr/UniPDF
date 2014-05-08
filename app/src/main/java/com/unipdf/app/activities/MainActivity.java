package com.unipdf.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import com.unipdf.app.R;
import com.unipdf.app.models.MainActivityModel;
import com.unipdf.app.views.MainActivityView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setzt Layout zur view
        mView = (MainActivityView) getLayoutInflater().inflate(R.layout.activity_main, null, false);
        // damit View Referenz auf Listener hat
        mView.initializeView(mIViewListener);
        setContentView(mView);

        ArrayList<String> temp = new ArrayList<String>();
        temp.add("1");
        temp.add("2");
        temp.add("3");
        temp.add("4");
        temp.add("5");
        temp.add("6");
        temp.add("1");
        temp.add("2");
        temp.add("3");
        temp.add("4");
        temp.add("5");
        temp.add("6");
        temp.add("1");
        temp.add("2");
        temp.add("3");
        temp.add("4");
        temp.add("5");
        temp.add("6");

        MainActivityModel.getInstance().setPDFs(temp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_back) {
            return true;
        }
        if (id == R.id.action_discard) {
            return true;
        }
        if (id == R.id.action_new) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Memberklasse die nur das Intereface enthält
    private MainActivityView.IViewListener mIViewListener = new MainActivityView.IViewListener() {
        @Override
        public void onListItemClick(int _Position) {
            Log.d("Liste", "Position: " + _Position);
            Intent Int = new Intent(MainActivity.this,PdfActivity.class);
            startActivity(Int);
        }

        @Override
        public void onListItemLongClick(int _Position) {

        }
    };

    private MainActivityView mView;
}
