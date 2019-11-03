package com.android.image;

import android.net.Uri;

/**
 * Created by Relin
 * on 2018-10-10.
 */
public interface OnImageSelectListener {

    void onImageSelectSucceed(Uri uri);

    void onImageSelectFailed(String msg);

}
