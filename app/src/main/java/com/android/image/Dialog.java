package com.android.image;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

/**
 * Created by Relin
 * on 2018-10-12.
 */
public class Dialog {

    /**
     * 半透明主题
     */
    public static final int THEME_TRANSLUCENT = R.style.Android_Theme_Dialog_Translucent_Background;

    /**
     * 全透明主题
     */
    public static final int THEME_TRANSPARENT = R.style.Android_Theme_Dialog_Transparent_Background;

    /**
     * 底部动画
     */
    public static final int ANIM_BOTTOM = R.style.android_anim_bottom;

    /**
     * 缩放动画
     */
    public static final int ANIM_ZOOM = R.style.android_anim_zoom;

    /**
     * 上下文
     */
    public final Context context;
    /**
     * 布局资源ID
     */
    public final int layoutResId;

    /**
     * 主题资源ID
     */
    public final int themeResId;

    /**
     * 宽度
     */
    public final int width;
    /**
     * 高度
     */
    public final int height;
    /**
     * 动画ID
     */
    public final int animResId;
    /**
     * 位置
     */
    public final int gravity;

    /**
     * 是否能取消 - 返回键不可以
     */
    public final boolean cancelable;

    /**
     * 是否能取消 - 返回键可以
     */
    public final boolean canceledOnTouchOutside;

    /**
     * 视图
     */
    public View contentView;

    /**
     * 对话框
     */
    public android.app.Dialog dialog;

    /**
     * 窗口
     */
    public Window window;


    public Dialog(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.animResId = builder.animResId;
        this.themeResId = builder.themeResId;
        this.context = builder.context;
        this.gravity = builder.gravity;
        this.layoutResId = builder.layoutResId;
        this.cancelable = builder.cancelable;
        this.canceledOnTouchOutside = builder.canceledOnTouchOutside;
        createDialog();
        show();
    }

    public static class Builder {

        private Context context;
        private int width;
        private int height;
        private int layoutResId;
        private int themeResId;
        private int gravity;
        private int animResId;
        private boolean cancelable;
        private boolean canceledOnTouchOutside;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder layoutResId(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public Builder themeResId(@StyleRes int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public Builder animResId(@StyleRes int animResId) {
            this.animResId = animResId;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Dialog build() {
            return new Dialog(this);
        }
    }

    /**
     * 创建对话框
     */
    private void createDialog() {
        contentView = LayoutInflater.from(context).inflate(layoutResId, null);
        dialog = new android.app.Dialog(context, themeResId);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        window = dialog.getWindow();
        window.setGravity(gravity);
        window.setWindowAnimations(animResId);
        window.setLayout(width, height);
    }

    /**
     * 显示对话框
     *
     * @return 自定义Dialog
     */
    public Dialog show() {
        if (dialog != null) {
            dialog.show();
        }
        return this;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
