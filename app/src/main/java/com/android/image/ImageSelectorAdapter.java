package com.android.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ImageSelectorAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private ImageSelector selector;
    private OnImageSelectMenuListener listener;

    public ImageSelectorAdapter(Context context) {
        this.context = context;
    }

    public ImageSelector getSelector() {
        return selector;
    }

    public void setSelector(ImageSelector selector) {
        this.selector = selector;
    }

    public void setListener(OnImageSelectMenuListener listener) {
        this.listener = listener;
    }

    public void setItems(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<String> getItems() {
        return list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.android_item_image_selector, viewGroup, false);
            holder.android_tv_name = view.findViewById(R.id.android_tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.android_tv_name.setText(getItem(position));
        if (getCount() == 1) {
            holder.android_tv_name.setBackgroundResource(R.drawable.android_dialog_bg);
        }
        if (getCount() == 2) {
            if (position == 0) {
                holder.android_tv_name.setBackgroundResource(R.drawable.android_take_photo);
            }
            if (position == 1) {
                holder.android_tv_name.setBackgroundResource(R.drawable.android_photo);
            }
        }
        if (getCount() > 2) {
            if (position == 0) {
                holder.android_tv_name.setBackgroundResource(R.drawable.android_take_photo);
            }
            if (position > 0 && position < getCount() - 1) {
                holder.android_tv_name.setBackgroundResource(R.drawable.android_photo_middle);
            }
            if (position == getCount() - 1) {
                holder.android_tv_name.setBackgroundResource(R.drawable.android_photo);
            }
        }
        holder.android_tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onImageSelectMenu(selector,getItems(), position);
                }
            }
        });
        return view;
    }

    public class ViewHolder {

        private TextView android_tv_name;

    }

}
