package com.unipdf.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipdf.app.R;
import com.unipdf.app.vos.ShufflePage;

import java.util.List;

/**
 * Anzeige der ShuffleThumbnails und Seitenanzahl
 */
public class ShufflePageAdapter extends BaseAdapter {
    public static class ViewHolder {
        ImageView mImage;
        TextView mText;

        public ViewHolder(View _view) {
            mImage = (ImageView) _view.findViewById(R.id.imageAdapterView);
            mText  = (TextView)  _view.findViewById(R.id.imageAdapterText);
        }
    }

    private Context mContext;
    private List<ShufflePage> mPages;

    public ShufflePageAdapter(Context _context, List<ShufflePage> _pages) {
        mContext = _context;
        mPages = _pages;
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public ShufflePage getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_image_adapter, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int pos = position + 1;
        viewHolder.mImage.setImageBitmap(mPages.get(position).getThumbnail());
        viewHolder.mText.setText(""+pos);

        return convertView;
    }
}
