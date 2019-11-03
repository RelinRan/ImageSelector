package com.android.image;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by Relin
 * on 2018-11-21.<br/>
 * Uri转文件路径，支持Android4.4特殊Uri地址处理<br/>
 * Uri path to the file, support Android4.4 <br/>
 * Uri addresses special processing<br/>
 */
public class UriParser {

    /**
     * Uri转path
     *
     * @param context 上下文
     * @param uri     Uri地址
     * @return
     */
    public static String parse(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&& DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
//        }
// else {
//                 String scheme = uri.getScheme();
//                String path = null;
//                if (scheme == null)
//                    path = uri.getPath();
//                else {
//                    if (ContentResolver.SCHEME_FILE.equals(scheme)) {
//                        path = uri.getPath();
//                    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
//                        Cursor cursor = context.getContentResolver()
//                                .query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
//                        if (null != cursor) {
//                            if (cursor.moveToFirst()) {
//                                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                                if (index > -1) {
//                                    path = cursor.getString(index);
//                                }
//                            }
//                            cursor.close();
//                        }
//                    }
//                }
//                return path;
//

//            String[] projection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//            if (cursor != null) {
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                return cursor.getString(column_index);
//            }
//        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
