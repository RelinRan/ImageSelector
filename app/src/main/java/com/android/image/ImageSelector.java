package com.android.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Relin
 * on 2018-10-10.</br>
 * 图片选择器，主要用于调用系统的相册、照相机获取图片，设置对应的
 * 参数可以对图片进行剪裁，并且获取到对应的结果。获取的结果需要在
 * 对应的页面中的{@link ImageSelector#onActivityResult}方法,ImageSelector
 * 会对返回的数据自行处理。</br>
 * image selector, mainly used to call the system's album, camera to get images, set corresponding
 * The * parameter can crop the image and get the corresponding result. The obtained results need to be in
 * {@link ImageSelector#onActivityResult} method in the corresponding page,ImageSelector
 * the returned data is processed by itself.
 */
public class ImageSelector {

    //剪裁请求码
    public final int REQUEST_CROP = 0x123;
    //拍照请求码
    public final int REQUEST_CAMERA = 0x124;
    //选择相册请求码
    public final int REQUEST_PICK = 0x125;
    //缓存文件夹
    public static final String CACHE_FOLDER = "ImageSelector";

    private Builder builder;
    private final Activity activity;
    private final Fragment fragment;

    private final OnImageSelectListener listener;
    private final OnImageSelectMenuListener menuListener;

    //拍照可见性
    public final int takeVisibility;
    //相册可见性
    public final int photoVisibility;
    //查看可见性
    public final int moreMenuVisibility;
    //更多菜单
    public final String[] menu;
    //是否修改原有跳转
    public final boolean defineIntent;

    //图片生成地址或者资源地址
    public final Uri uri;
    //压缩之后的大小限制
    public final int size;
    //是否压缩
    public final boolean compress;

    //宽缩放比例
    public final int aspectX;
    //高缩放比例
    public final int aspectY;
    //输出宽度
    public final int outputX;
    //输出高度
    public final int outputY;
    //是否剪裁
    public final boolean crop;
    //是否保留比例
    public final boolean scale;
    //是否将数据保留在Bitmap中返回
    public final boolean returnData;
    //是否取消人脸识别功能
    public final boolean noFaceDetection;
    //输出格式
    public final String outputFormat;

    private Dialog dialog;

    /**
     * 初始化，在Application的onCreate()方法里面初始化
     */
    public static void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public ImageSelector(Builder builder) {
        this.builder = builder;
        this.activity = builder.activity;
        this.fragment = builder.fragment;
        this.listener = builder.listener;
        this.takeVisibility = builder.takeVisibility;
        this.photoVisibility = builder.photoVisibility;
        this.moreMenuVisibility = builder.moreMenuVisibility;
        this.menuListener = builder.menuListener;
        this.menu = builder.menu;
        this.defineIntent = builder.defineIntent;
        this.uri = builder.uri;
        this.size = builder.size;
        this.compress = builder.compress;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
        this.crop = builder.crop;
        this.scale = builder.scale;
        this.returnData = builder.returnData;
        this.noFaceDetection = builder.noFaceDetection;
        this.outputFormat = builder.outputFormat;
        show();
    }

    public ImageSelector(Builder builder, boolean isShow) {
        this.builder = builder;
        this.activity = builder.activity;
        this.fragment = builder.fragment;
        this.listener = builder.listener;
        this.takeVisibility = builder.takeVisibility;
        this.photoVisibility = builder.photoVisibility;
        this.moreMenuVisibility = builder.moreMenuVisibility;
        this.menuListener = builder.menuListener;
        this.menu = builder.menu;
        this.defineIntent = builder.defineIntent;
        this.uri = builder.uri;
        this.size = builder.size;
        this.compress = builder.compress;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
        this.crop = builder.crop;
        this.scale = builder.scale;
        this.returnData = builder.returnData;
        this.noFaceDetection = builder.noFaceDetection;
        this.outputFormat = builder.outputFormat;
        if (isShow) {
            show();
        }
    }

    public static class Builder {

        private Activity activity;
        private Fragment fragment;
        private OnImageSelectListener listener;
        private OnImageSelectMenuListener menuListener;
        private int takeVisibility = View.VISIBLE;
        private int photoVisibility = View.VISIBLE;
        private int moreMenuVisibility = View.VISIBLE;
        private String[] menu;
        private boolean defineIntent;
        private Uri uri = createUri();
        private int size = 200;
        private boolean compress = true;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = Screen.width();
        private int outputY = Screen.width();
        private boolean crop = true;
        private boolean scale = false;
        private boolean returnData = false;
        private boolean noFaceDetection = true;
        private String outputFormat = Bitmap.CompressFormat.JPEG.toString();

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder(Fragment fragment) {
            this.fragment = fragment;
        }

        public Builder listener(OnImageSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder menuListener(OnImageSelectMenuListener menuListener) {
            this.menuListener = menuListener;
            return this;
        }

        public String[] menu() {
            return menu;
        }

        public Builder menu(String[] moreMenu) {
            this.menu = moreMenu;
            return this;
        }

        public boolean isDefineIntent() {
            return defineIntent;
        }

        public Builder defineIntent(boolean defineIntent) {
            this.defineIntent = defineIntent;
            return this;
        }

        public Builder uri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder compress(boolean compress) {
            this.compress = compress;
            return this;
        }

        public Builder aspectX(int aspectX) {
            this.aspectX = aspectX;
            this.outputX = aspectX * outputY / aspectY;
            return this;
        }

        public Builder aspectY(int aspectY) {
            this.aspectY = aspectY;
            this.outputY = aspectY * outputX / aspectX;
            return this;
        }

        public Builder outputX(int outputX) {
            this.outputX = outputX;
            this.aspectX = outputX * aspectY / outputY;
            return this;
        }

        public Builder outputY(int outputY) {
            this.outputY = outputY * aspectY / aspectX;
            this.aspectY = outputY * aspectX / aspectY;
            return this;
        }

        public Builder crop(boolean crop) {
            this.crop = crop;
            return this;
        }

        public Builder scale(boolean scale) {
            this.scale = scale;
            return this;
        }

        public Builder returnData(boolean returnData) {
            this.returnData = returnData;
            return this;
        }

        public Builder noFaceDetection(boolean noFaceDetection) {
            this.noFaceDetection = noFaceDetection;
            return this;
        }

        public Builder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public int takeVisibility() {
            return takeVisibility;
        }

        public Builder takeVisibility(int takeVisibility) {
            this.takeVisibility = takeVisibility;
            return this;
        }

        public int photoVisibility() {
            return photoVisibility;
        }

        public Builder moreMenuVisibility(int moreMenuVisibility) {
            this.moreMenuVisibility = moreMenuVisibility;
            return this;
        }

        public int moreMenuVisibility() {
            return moreMenuVisibility;
        }

        public Builder photoVisibility(int photoVisibility) {
            this.photoVisibility = photoVisibility;
            return this;
        }

        public ImageSelector build() {
            //处理华为圆角问题
            if (Build.MANUFACTURER.contains("HUAWEI")) {
                aspectX = 9998;
                aspectY = 9999;
            }
            return new ImageSelector(this);
        }

        public ImageSelector build(boolean isShow) {
            //处理华为圆角问题
            if (Build.MANUFACTURER.contains("HUAWEI")) {
                aspectX = 9998;
                aspectY = 9999;
            }
            return new ImageSelector(this, isShow);
        }
    }

    /**
     * 跳转到相册页面
     */
    public void startGalleryActivity() {
        Intent intent;
//        if (crop) {
//            intent = createCropIntent(Intent.ACTION_GET_CONTENT);
//        } else {
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }
        if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_PICK);
        }
        if (fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_PICK);
        }
    }

    /**
     * 跳转到照相机页面
     */
    public void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_CAMERA);
        }
        if (fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    /**
     * 跳转到剪切图片页面
     */
    public void startCropActivity() {
        Intent intent = createCropIntent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if (activity != null) {
            activity.startActivityForResult(intent, REQUEST_CROP);
        }
        if (fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_CROP);
        }
    }

    private Uri cropOutputUri;

    /**
     * 创建剪切图片意图
     *
     * @param action 动作
     * @return
     */
    public Intent createCropIntent(String action) {
        Intent intent = new Intent(action)
                .setDataAndType(uri, "image/*")
                .putExtra("crop", "true")//发送裁剪信号
                .putExtra("scale", scale)//是否保留比例
                .putExtra("aspectX", aspectX)//X方向上的比例
                .putExtra("aspectY", aspectY)//Y方向上的比例
                .putExtra("outputX", outputX)//裁剪区的宽
                .putExtra("outputY", outputY)//裁剪区的高
                .putExtra("return-data", returnData)//是否将数据保留在Bitmap中返回
                .putExtra("outputFormat", outputFormat)//输出格式
                .putExtra("noFaceDetection", noFaceDetection);//是否取消人脸识别功能
        cropOutputUri = createUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropOutputUri);
        return intent;
    }

    /**
     * 显示选择器
     */
    public void show() {
        if (builder == null) {
            return;
        }
        showImageSelector(getContext(), menu);
    }

    /**
     * 消失
     */
    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    /**
     * 显示选择图片对话框
     *
     * @param context  上下文
     */
    private android.app.Dialog showImageSelector(Context context,String[] moreMenu) {
        dialog = new Dialog.Builder(context)
                .width(LinearLayout.LayoutParams.MATCH_PARENT)
                .height(LinearLayout.LayoutParams.WRAP_CONTENT)
                .layoutResId(R.layout.android_dialog_image_selector)
                .animResId(R.style.android_anim_bottom)
                .themeResId(R.style.Android_Theme_Dialog_Translucent_Background)
                .gravity(Gravity.BOTTOM)
                .build();
        ListView lv_menu = dialog.contentView.findViewById(R.id.android_lv_menu);
        TextView tv_cancel = dialog.contentView.findViewById(R.id.tv_cancel);
        //数据填充
        List<String> list = new ArrayList<>();
        String names[] = {"拍照", "相册"};
        for (int i = 0; i < names.length; i++) {
            list.add(names[i]);
        }
        if (takeVisibility == View.GONE) {
            list.remove(0);
        }
        if (photoVisibility == View.GONE) {
            list.remove(1);
        }
        //更多菜单数据

        if (moreMenu != null) {
            if (moreMenuVisibility == View.VISIBLE) {
                for (int i = 0; i < moreMenu.length; i++) {
                    list.add(moreMenu[i]);
                }
            }
        }
        ImageSelectorAdapter adapter = new ImageSelectorAdapter(context);
        adapter.setSelector(this);
        lv_menu.setAdapter(adapter);
        adapter.setItems(list);
        adapter.setListener(new OnImageSelectMenuListener() {
            @Override
            public void onImageSelectMenu(ImageSelector selector, List<String> list, int position) {
                if (menuListener != null) {
                    menuListener.onImageSelectMenu(selector, list, position);
                }
                if (!defineIntent) {
                    if (position == 0) {
                        selector.startCameraActivity();
                        dialog.dismiss();
                    }
                    if (position == 1) {
                        selector.startGalleryActivity();
                        dialog.dismiss();
                    }
                }
            }
        });
        //取消按钮
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog.show().dialog;
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public Context getContext() {
        Context context = null;
        if (activity != null) {
            context = activity;
        }
        if (fragment != null) {
            context = fragment.getContext();
        }
        return context;
    }

    /**
     * 处理Activity结果
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            if (listener != null) {
                listener.onImageSelectFailed("The image capture action has been canceled.");
            }
        }
        Context context = null;
        if (activity != null) {
            context = activity;
        }
        if (fragment != null) {
            context = fragment.getContext();
        }
        if (context == null) {
            if (listener != null) {
                listener.onImageSelectFailed("An Activity or Fragment is empty and cannot be obtained into Context.");
            }
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK:
                    if (context != null && data != null && data.getData() != null) {
                        Uri reallyUri = Uri.parse(UriParser.parse(context, data.getData()));
                        copyFile(reallyUri.getPath(), uri.getPath());
                        if (crop) {
                            startCropActivity();
                            break;
                        }
                        if (listener != null) {
                            listener.onImageSelectSucceed(compress ? compress(uri) : uri);
                        }
                    }
                    break;
                case REQUEST_CROP:
                    if (listener != null) {
                        listener.onImageSelectSucceed(compress ? compress(cropOutputUri) : cropOutputUri);
                    }
                    break;
                case REQUEST_CAMERA:
                    if (crop) {
                        startCropActivity();
                        break;
                    }
                    Uri newUri = compress ? compress(uri) : uri;
                    sendBroadcastMediaScan(context, newUri);
                    if (listener != null) {
                        listener.onImageSelectSucceed(newUri);
                    }
                    break;
            }
        }
    }

    /**
     * 添加文件到相册
     *
     * @param context
     * @param uri
     */
    private void sendBroadcastMediaScan(Context context, Uri uri) {
        // 其次把文件插入到系统图库
        File file = IOUtils.decodeUri(activity, uri);
        MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    /**
     * 压缩文件
     *
     * @param uri 源文件Uri
     * @return
     */
    private Uri compress(Uri uri) {
        return Uri.fromFile(IOUtils.compress(new File(uri.getPath()), Bitmap.CompressFormat.JPEG, size));
    }

    /**
     * 创建Uri
     *
     * @return
     */
    public static Uri createUri() {
        File cacheFolder = new File(IOUtils.getSDCardPath() + File.separator + CACHE_FOLDER);
        if (!cacheFolder.exists()) {
            try {
                cacheFolder.mkdirs();
                Log.e(ImageSelector.class.getSimpleName(), "The cache directory was created successfully");
            } catch (Exception e) {
                Log.e(ImageSelector.class.getSimpleName(), "Failed to create cache directory," + e.toString());
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = String.format("IMG" + format.format(new Date()) + ".jpg", System.currentTimeMillis());
        File file = new File(cacheFolder + File.separator + name);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            return android.support.v4.content.FileProvider.getUriForFile(BaseApplication.app, BaseApplication.app.getPackageName() + ".fileprovider", file);
//        }
        return Uri.fromFile(file);
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        File cacheFolder = new File(IOUtils.getSDCardPath() + File.separator + CACHE_FOLDER);
        if (cacheFolder.exists()) {
            cacheFolder.deleteOnExit();
        }
    }

    /**
     * 拷贝文件
     *
     * @param filePath
     * @param destPath
     * @return
     */
    private boolean copyFile(String filePath, String destPath) {
        File originFile = new File(filePath);
        File destFile = new File(destPath);
        BufferedInputStream reader = null;
        BufferedOutputStream writer = null;
        try {
            if (!destFile.exists()) {
                boolean result = destFile.createNewFile();
            }
            InputStream in = new FileInputStream(originFile);
            OutputStream out = new FileOutputStream(destFile);
            reader = new BufferedInputStream(in);
            writer = new BufferedOutputStream(out);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, length);
            }
        } catch (Exception exception) {
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignore) {
                }
            }
        }
        return true;
    }

}
