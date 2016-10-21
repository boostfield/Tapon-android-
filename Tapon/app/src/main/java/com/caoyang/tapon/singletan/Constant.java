package com.caoyang.tapon.singletan;


import com.caoyang.tapon.BuildConfig;

/**
 * 常量
 */
public class Constant {
    public static final String APP_NAME = BuildConfig.APPNAME;
    private static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;


    public static String acqBIN = "29002003";
    public static String tppAddr = "218.97.12.139";//太棒主机地址

    //太棒测试环境
    public static int tppPort = 18045;
    public static String merId = "666290050460001";//商户号
    public static String tppAddrCC = tppAddr;
    public static int tppPortCC = tppPort;
    public static int tppTimeOut = 30;
    public static final int MESSAGE_PAY_GETSMS_SUCCESS = 9000;
    public static final int MESSAGE_PAY_GETSMS_FAIL = MESSAGE_PAY_GETSMS_SUCCESS + 1;
    public static String userId = "";

}

