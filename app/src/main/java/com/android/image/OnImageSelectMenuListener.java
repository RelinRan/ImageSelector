package com.android.image;

import java.util.List;

public interface OnImageSelectMenuListener {

    /**
     * 图片选择菜单
     *
     * @param selector  选择器
     * @param list     菜单
     * @param position 菜单位置
     */
    void onImageSelectMenu(ImageSelector selector,List<String> list, int position);

}
