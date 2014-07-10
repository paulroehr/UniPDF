package com.unipdf.app.vos;

import android.graphics.Bitmap;


/**
 * Created by paul on 10.07.14.
 */
public class ShufflePage {

    public ShufflePage(Bitmap _thumbnail, int _page) {
        mThumbnail = _thumbnail;
        mPage = _page;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap _thumbnail) {
        mThumbnail = _thumbnail;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int _page) {
        mPage = _page;
    }

    private Bitmap mThumbnail;
    private int mPage;

}
