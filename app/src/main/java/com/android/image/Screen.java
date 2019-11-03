package com.android.image;

import android.content.res.Resources;
import android.net.Uri;

import java.io.File;
import java.util.List;

/**
 * Created by Relin on 2016/2/13.
 * 屏幕工具
 * This is a screen utils when you need
 * screen width or screen height.
 */
public class Screen {

    /**
     * Get the screen of density
     * @return
     */
    public static float density() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * The screen of width.
     * @return
     */
    public static int width(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * The screen of height.
     * @return
     */
    public static int height(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * Px to dp
     * @param px
     * @return
     */
    public static float pxToDp(float px){
        return px/density();
    }

    /**
     * dp to px
     * @param dp
     * @return
     */
    public static float dpToPx(float dp){
        return dp*density();
    }


    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int pxToSp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }



}
