package com.caoyang.tapon.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.caoyang.tapon.R;


public class ZTProgressBar {
    private static ProgressDialog mProgressDialog;

    /**
     * 使用默认
     *
     * @param context 上下文
     */
    public static void showProgressDialog(Context context) {
        showProgressDialog(context, context.getString(R.string.toast_please_wait), context.getString(R.string.toast_loading_hard));
    }

    /**
     * 修改内容
     *
     * @param context 上下文
     * @param content 内容
     */
    public static void showProgressDialog(Context context, String content) {
        showProgressDialog(context, context.getString(R.string.toast_please_wait), content);
    }

    /**
     * 修改标题 内容
     *
     * @param context 上下文
     * @param title   标题
     * @param content 内容
     */
    public static void showProgressDialog(Context context, String title, String content) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(context, title, content, true, false);
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public static void hideProgressDislog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        }, 150);
    }
}