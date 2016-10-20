package com.caoyang.tapon.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.caoyang.tapon.AppData;


/**
 * Created by Administrator on 2016/06/03.
 */
public class IntentUtils {
    /**
     * 进入拨号键面
     */
    public static void gotoDialog(String phoneNumber) {
        Context context = AppData.getContext();
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }


    /**
     * 直接拨打电话
     */
    public static void gotoCall(String phoneNumber) {
        Context context = AppData.getContext();
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("call:" + phoneNumber));
        context.startActivity(intent);
    }

    private static final String SCHEME = "package";

    /**
     * 打开系统app信息界面
     */
    public static void openMyAppInfo(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, mContext.getPackageName(), null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }


    /**
     * 打开系统app信息界面-其他
     */
    public static void openOhterAppInfo(Context mContext, String pakageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts(SCHEME, pakageName, null);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

    /**
     * 打开系统app列表界面
     */
    public static void openAppList(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 打开相对应的网页地址
     *
     * @param url
     */
    public static void openWebSite(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        AppData.getContext().startActivity(intent);
    }

    /**
     * 进入市场
     */
    public static void openMark(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
