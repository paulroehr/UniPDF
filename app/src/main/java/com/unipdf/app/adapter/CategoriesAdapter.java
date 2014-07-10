package com.unipdf.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipdf.app.R;
import com.unipdf.app.vos.Category;

import java.util.ArrayList;

/**
 * Created by paul on 15.05.14.
 */
public class CategoriesAdapter extends BaseAdapter {

    public static class ViewHolder {
        ImageView mImage;
        TextView mName;

        ViewHolder(View v) {
            this.mImage = (ImageView) v.findViewById(R.id.item_imageView);
            this.mName = (TextView) v.findViewById((R.id.item_textView));
        }
    }

    private Context mContext;
    private ArrayList<Category> mItems;

    public CategoriesAdapter(Context _context, ArrayList<Category> _Items) {
        mItems = _Items;
        mContext = _context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Category getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_standard, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        holder.mImage.setImageResource(R.drawable.shelf);
        holder.mImage.setVisibility(View.VISIBLE);
        holder.mName.setText(mItems.get(position).getCategoryName());

        return row;
    }
}
