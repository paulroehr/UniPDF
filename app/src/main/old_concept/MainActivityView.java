package com.unipdf.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.unipdf.app.R;
import com.unipdf.app.events.Event;
import com.unipdf.app.events.EventListener;
import com.unipdf.app.models.ApplicationModel;

/**
 * Created by schotte on 17.04.14.
 */
public class MainActivityView extends LinearLayout {

    /**
     * Leitet Events an den Controller weiter.
     */
    public interface IViewListener {
        public void onListItemClick(int _Position);
        public void onListItemLongClick(int _Position);
    }

    public MainActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void initializeView(IViewListener _Listener) {
        mViewListener = _Listener;

        /**
         * Verknüpfung des EventListeners mit dem DatenModel.
         */
        mModel = ApplicationModel.getInstance();
        mModel.addListener(ApplicationModel.ChangeEvent.EVENT_RECEIVED_PDFS, mReceivedPDFsListener);

        /**
         * Registrieren ClickListener mit der Liste.
         */
        mList.setOnItemClickListener(mClickListener);
    }

    public void destroy() {
        mModel.removeListener(ApplicationModel.ChangeEvent.EVENT_RECEIVED_PDFS, mReceivedPDFsListener);

    }

    public void initList() {
//        mList.setAdapter(new ArrayAdapter<String>(Main.getAppContext(), R.layout.item_pdfs, mModel.getPDFs()));
    }

    /**
     * Wird aufgerufen, nachdem XML Layout vollkommen "reingeladen" wurde.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mList = (ListView) findViewById(R.id.listView);
    }

    /**
     * Fängt Click Event der Liste ab.
     */
    private AdapterView.OnItemClickListener mClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            mViewListener.onListItemClick(position);
        }
    };

    private EventListener mReceivedPDFsListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            // Fülle Liste mit Daten
            initList();
        }
    };

    private ApplicationModel mModel;

    private IViewListener mViewListener;
    private ListView mList;
}
