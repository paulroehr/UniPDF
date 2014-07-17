package com.unipdf.app.vos;

import android.graphics.Bitmap;
import android.net.Uri;


/**
 * Created by paul on 10.07.14.
 */
public class ShufflePage {

    public ShufflePage(Bitmap _thumbnail, int _page, Uri _path) {
        mThumbnail = _thumbnail;
        mPage = _page;
        mPath = _path;
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

    public Uri getPath() {
        return mPath;
    }

    public void setPath(Uri _path) {
        mPath = _path;
    }

    private Bitmap mThumbnail;
    private int mPage;
    private Uri mPath;

}
