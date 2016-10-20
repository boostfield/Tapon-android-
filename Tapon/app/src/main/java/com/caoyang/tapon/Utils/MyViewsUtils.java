package com.caoyang.tapon.Utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * 视图工具
 */
public class MyViewsUtils {

    /**
     * 设置高度
     */
    public static void setHeight(View v, int height) {
        setViewLayoutParams(v, -1, height);
    }

    /**
     * 设置宽度
     */
    public static void setWidth(View v, int width) {
        setViewLayoutParams(v, width, -1);
    }

    /**
     * 设置宽度和宽度
     */
    private static void setViewLayoutParams(View v, int width, int height) {
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        if (width != -1) {
            p.width = width;
        }
        if (height != -1) {
            p.height = height;
        }
        v.setLayoutParams(p);
    }

    /**
     * 设置外边界
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    //隐藏所有视图
    public static void hideAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    //稍微所有视图
    public static void inVisibiltyAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    //显示所有视图
    public static void showAllViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    //请求获取焦点
    public static void requestFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public static void setAlpha00(View view, float alpha) {
        view.setAlpha(alpha);
    }

    //寻找ID
    public static <T extends View> T findViewsById(View parent, int viewId) {
        View view = parent.findViewById(viewId);
        return (T) view;
    }


}
