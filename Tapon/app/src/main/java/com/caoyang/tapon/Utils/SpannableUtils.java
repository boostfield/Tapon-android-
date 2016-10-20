package com.caoyang.tapon.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.caoyang.tapon.AppData;
import com.caoyang.tapon.R;


/**
 * Spannable工具类，用于设置文字的前景色、背景色、Typeface、粗体、斜体、字号、超链接、删除线、下划线、上下标等
 */
public class SpannableUtils {
    private SpannableUtils() {

    }

    /**
     * 改变字符串中某一段文字的字号
     * setTextSize("",24,0,2) = null;
     * setTextSize(null,24,0,2) = null;
     * setTextSize("abc",-2,0,2) = null;
     * setTextSize("abc",24,0,4) = null;
     * setTextSize("abc",24,-2,2) = null;
     * setTextSize("abc",24,0,2) = normal string
     */
    public static SpannableString setTextSize(String content, int startIndex, int endIndex, int fontSize) {
        if (TextUtils.isEmpty(content) || fontSize <= 0 || startIndex >= endIndex || startIndex < 0 || endIndex >= content.length()) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new AbsoluteSizeSpan(fontSize), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextSub(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new SubscriptSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextSuper(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new SuperscriptSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextStrikethrough(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StrikethroughSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextUnderline(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 加粗
     *
     * @param content
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static SpannableString setTextBold(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextItalic(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBoldItalic(String content, int startIndex, int endIndex) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextForeground(String content, int startIndex, int endIndex, int foregroundColor) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(foregroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static SpannableString setTextBackground(String content, int startIndex, int endIndex, int backgroundColor) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new BackgroundColorSpan(backgroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    /**
     * 设置文本的超链接
     *
     * @param content    需要处理的文本
     * @param startIndex
     * @param endIndex   被处理文本中需要处理字串的开始和结束索引
     * @param url        文本对应的链接地址，需要注意格式：
     *                   （1）电话以"tel:"打头，比如"tel:02355692427"
     *                   （2）邮件以"mailto:"打头，比如"mailto:zmywly8866@gmail.com"
     *                   （3）短信以"sms:"打头，比如"sms:02355692427"
     *                   （4）彩信以"mms:"打头，比如"mms:02355692427"
     *                   （5）地图以"geo:"打头，比如"geo:68.426537,68.123456"
     *                   （6）网络以"http://"打头，比如"http://www.google.com"
     */
    public static SpannableString setTextURL(String content, int startIndex, int endIndex, String url) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new URLSpan(url), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString setTextImg(String content, int startIndex, int endIndex, Drawable drawable) {
        if (TextUtils.isEmpty(content) || startIndex < 0 || endIndex >= content.length() || startIndex >= endIndex) {
            return null;
        }

        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ImageSpan(drawable), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }


    /**
     * 给关键字染色--主色
     */
    public static void colorKeyWords(String title, String keyWord, TextView textView) {
        colorKeyWords(title, keyWord, textView, R.color.colorPrimaryDark);
    }

    /**
     * 给关键字染色--自定义
     */
    public static void colorKeyWords(String title, String keyWord, TextView textView, int color) {
        SpannableString str = new SpannableString(title);
        int start = 0;
        start = title.indexOf(keyWord);
        for (; ; ) {
            start = title.indexOf(keyWord, start);
            if (start >= title.length()) {
                break;
            }
            if (start < 0) {
                break;
            }
            str.setSpan(new ForegroundColorSpan(AppData.getContext().getResources().getColor(color)), start, start + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//染色
            //str.setSpan(new StyleSpan(Typeface.BOLD), start, start + keyWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            start += keyWord.length();
        }
        textView.setText(str);
    }

    /**
     * 适用于一次性染色
     */
    public static void colorKeyWords02(String title, String keyWord, TextView textView, int color) {
        textView.setText(Html.fromHtml("我已阅读并同意 <font color=" + AppData.getContext().getResources().getColor(color) + ">《法律声明以及隐私政策》</font>"));
    }

    public static class NoLineSpan extends ClickableSpan {
        Context context;

        public NoLineSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.colorPrimary));
            ds.setUnderlineText(false);
        }
    }
}
