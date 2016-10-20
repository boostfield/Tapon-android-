package com.caoyang.tapon.Utils;

import android.os.Environment;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/05/23.
 */
public class Utils {
    public static final String PARAM_EQUAL = "=";
    public static final String PARAM_AND = "&";

    /**
     * 是否有有sd卡
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        // 有存储的SDCard
        return state.equals(Environment.MEDIA_MOUNTED);
    }





    /**
     * 将bean转换成键值对列表
     *
     * @param bean
     * @return
     */
    public static List<NameValuePair> bean2Parameters(Object bean) {
        if (bean == null) {
            return null;
        }
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        // 取得bean所有public 方法
        Method[] Methods = bean.getClass().getMethods();
        for (Method method : Methods) {
            if (method != null && method.getName().startsWith("get")
                    && !method.getName().startsWith("getClass")) {
                // 得到属性的类名
                String value = "";
                // 得到属性值
                try {
                    String className = method.getReturnType().getSimpleName();
                    if (className.equalsIgnoreCase("int")) {
                        int val = 0;
                        try {
                            val = (Integer) method.invoke(bean);
                        } catch (InvocationTargetException e) {
                            Log.e("InvocationTargetExcep", e.getMessage());
                        }
                        value = String.valueOf(val);
                    } else if (className.equalsIgnoreCase("String")) {
                        try {
                            value = (String) method.invoke(bean);
                        } catch (InvocationTargetException e) {
                            Log.e("InvocationTargetExcep", e.getMessage());
                        }
                    }
                    if (value != null && value != "") {
                        // 添加参数
                        // 将方法名称转化为id，去除get，将方法首字母改为小写
                        String param = method.getName().replaceFirst("get", "");
                        if (param.length() > 0) {
                            String first = String.valueOf(param.charAt(0))
                                    .toLowerCase();
                            param = first + param.substring(1);
                        }
                        parameters.add(new BasicNameValuePair(param, value));
                    }
                } catch (IllegalArgumentException e) {
                    Log.e("InvocationTargetExcep", e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    Log.e("IllegalAccessException", e.getMessage(), e);
                }
            }
        }
        return parameters;
    }

    /**
     * 对Object进行List<NameValuePair>转换后按key进行升序排序，以key=value&...形式返回
     */
    public static String sortParam(Object order) {
        List<NameValuePair> list = bean2Parameters(order);
        return sortParam(list);
    }


    /**
     * 对Object进行List<NameValuePair>转换后按key进行升序排序，以key=value&...形式返回
     */
    public static String sortParamForSignCard(Object order) {
        List<NameValuePair> list = bean2Parameters(order);
        return sortParamForSignCard(list);
    }

    /**
     * 对List<NameValuePair>按key进行升序排序，以key=value&...形式返回
     */
    public static String sortParam(List<NameValuePair> list) {
        if (list == null) {
            return null;
        }
        Collections.sort(list, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair lhs, NameValuePair rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        StringBuffer sb = new StringBuffer();
        for (NameValuePair nameVal : list) {

            if (null != nameVal.getValue() && !"".equals(nameVal.getValue())
                    && !nameVal.getName().equals("id_type")
                    && !nameVal.getName().equals("id_no")
                    && !nameVal.getName().equals("acct_name")
                    && !nameVal.getName().equals("flag_modify")
                    && !nameVal.getName().equals("user_id")
                    && !nameVal.getName().equals("no_agree")
                    && !nameVal.getName().equals("card_no")
                    && !nameVal.getName().equals("test_mode")) {
                sb.append(nameVal.getName());
                sb.append(PARAM_EQUAL);
                sb.append(nameVal.getValue());
                sb.append(PARAM_AND);
            }
        }
        String params = sb.toString();
        if (sb.toString().endsWith(PARAM_AND)) {
            params = sb.substring(0, sb.length() - 1);
        }
        Log.v("待签名串", params);
        return params;
    }

    /**
     * 随机1万以下，1000以上的数字
     */
    public static String RandomTenThousand() {
        Random random = new Random();
        int randNum = random.nextInt(9000) + 1000;
        return randNum + "";
    }
}
