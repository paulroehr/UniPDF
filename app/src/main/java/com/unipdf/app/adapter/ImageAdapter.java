package com.unipdf.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipdf.app.R;

import java.util.List;

/**
 * Adapter zur Anzeige von Bildern mit Untertiteln
 */
public class ImageAdapter extends BaseAdapter {

    public static class ViewHolder {
        ImageView mImage;
        TextView mText;

        public ViewHolder(View _view) {
            mImage = (ImageView) _view.findViewById(R.id.imageAdapterView);
            mText  = (TextView)  _view.findViewById(R.id.imageAdapterText);
        }
    }

    private Context mContext;
    private List<Bitmap> mImages;

    public ImageAdapter(Context _context, List<Bitmap> _images) {
        mContext = _context;
        mImages = _images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return mImages.get(position);
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
        viewHolder.mImage.setImageBitmap(mImages.get(position));
        viewHolder.mText.setText(""+pos);

        return convertView;
    }
}
