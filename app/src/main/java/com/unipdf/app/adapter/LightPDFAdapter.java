package com.unipdf.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import com.unipdf.app.R;
import com.unipdf.app.vos.LightPDF;

/**
 * Created by schotte on 06.05.14.
 */
public class LightPDFAdapter extends BaseAdapter{

    ArrayList<LightPDF> mLightPDFs = null;
    Context mContext;
    int mLayout;

    public LightPDFAdapter(ArrayList<LightPDF> _LightPDFs, Context _Context, int _Layout) {
        this.mLightPDFs = _LightPDFs;
        this.mContext = _Context;
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

//        holder.mImage.setImageURI(mLightPDFs.get(i).getmPicture());
        holder.mName.setText(mLightPDFs.get(i).getmName());

        return row;
    }

    class ViewHolder{

        ImageView mImage;
        TextView mName;

        ViewHolder(View v) {
            this.mImage = (ImageView) v.findViewById(R.id.item_imageView);
            this.mName = (TextView) v.findViewById((R.id.item_textView));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(mLightPDFs);
        super.notifyDataSetChanged();
    }
}
