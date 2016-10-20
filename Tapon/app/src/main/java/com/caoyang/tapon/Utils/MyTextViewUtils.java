package com.caoyang.tapon.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;


public class MyTextViewUtils {
    //获取编辑框的内容
    public static String getContent(TextView textView) {
        if (textView != null) {
            return textView.getText().toString().trim();
        }
        return null;
    }

    /**
     * 左边画drawable
     */
    public static void setLeftDrwable(Context context, TextView mTextView, int recourse) {
        Drawable drawable = context.getResources().getDrawable(recourse);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mTextView.setCompoundDrawables(drawable, null, null, null);//画在左边边
    }

    //显示阴影
    public static void showAllShadow(TextView... texteViews) {
        for (TextView mTextView : texteViews) {
            mTextView.setShadowLayer(20, 0, 10, android.R.color.black);
        }
    }
}
