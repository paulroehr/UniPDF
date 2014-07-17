package com.unipdf.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipdf.app.R;
import com.unipdf.app.activities.ExplorerActivity;
import com.unipdf.app.utils.PreviewLoader;
import com.unipdf.app.vos.LightPDF;

import java.util.ArrayList;

/**
 * Favoriten Adapter mit Asynchroner Thumbnail Generierung
 */
public class PreviewFavoritesAdapter extends BaseAdapter{

    ArrayList<LightPDF> mLightPDFs = null;
    ExplorerActivity mContext;
    int mLayout;

    public PreviewFavoritesAdapter(ArrayList<LightPDF> _LightPDFs, Context _Context, int _Layout) {
        this.mLightPDFs = _LightPDFs;
        this.mContext = (ExplorerActivity) _Context;
        this.mLayout = _Layout;
    }

    @Override
    public int getCount() {
        return mLightPDFs.size();
    }

    @Override
    public Object getItem(int i) {
        return mLightPDFs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mLayout,viewGroup,false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        LightPDF pdf = mLightPDFs.get(i);

        if(pdf.getmPicture() == null) {
            // Thumbnail Async laden
            PreviewLoader.loadPreview(pdf, holder.mImage, mContext);
        }
        else {
            holder.mImage.setImageBitmap(pdf.getmPicture());
        }
        holder.mName.setText(pdf.getmName());

        return row;
    }

    class ViewHolder{

        ImageView mImage;
        TextView mName;

        ViewHolder(View v) {
            this.mImage = (ImageView) v.findViewById(R.id.previewImage);
            this.mName = (TextView) v.findViewById((R.id.previewText));
        }
    }

}
