package com.caoyang.tapon.widget;

import android.widget.Toast;

import com.caoyang.tapon.AppData;


/**
 * Created by tonychen on 16/3/11.
 */
public class ZTToast {
    public static void show(CharSequence text) {
        Toast.makeText(AppData.getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
