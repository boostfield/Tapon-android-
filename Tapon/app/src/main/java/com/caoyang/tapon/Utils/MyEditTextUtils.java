package com.caoyang.tapon.Utils;


import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

public class MyEditTextUtils {


    //获取编辑框的内容
    public static String getContent(EditText editText) {
        if (editText != null) {
            return editText.getText().toString().trim();
        }
        return null;
    }

    //删除编辑框的内容
    public static void clearContent(EditText... editTexts) {
        for (EditText ed : editTexts) {
            ed.setText("");
        }
    }

    //查看是否有空的值
    public static boolean isEmptys(EditText... editTexts) {
        for (EditText ed : editTexts) {
            String str = ed.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    //展示密码 InputTypes
    public static void showPassword(EditText editText) {
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        setSeletionEnd(editText);
    }

    //隐藏密码  InputType
    public static void hidePassword(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setSeletionEnd(editText);
    }

    //使光标指向末尾处
    public static void setSeletionEnd(EditText editText) {
        editText.setSelection(editText.getText().length());
    }
}
