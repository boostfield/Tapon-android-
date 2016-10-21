package com.caoyang.tapon.service;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ChinaTvPay {
    private static ChinaTvPay INSTANCE;

    private ChinaTvPay() {

    }

    public static ChinaTvPay getInstance() {
        if (INSTANCE == null) {
            synchronized ((ChinaTvPay.class)) {
                if (INSTANCE == null) {
                    INSTANCE = new ChinaTvPay();
                    try {
                        System.loadLibrary("chinatvpay_jni");
                    } catch (Error e2) {
                        Logger.d("load lib err==data==" + e2.toString());
                    }
                }
            }
        }
        return INSTANCE;
    }

    private String tppAddress;
    private int tppPort;
    private int timeout;
    private String xml;
    private String cardno;
    private String aucAccountIn;
    private String passwd;
    private String newPasswd;
    private String validate;
    private String dynamicpasswd;
    private String aucRandomNum;
    private String CVN2;
    private boolean isTimeOut = false;
    private boolean isFinished = false;
    private String returnXml = "<Quan><Quanbody><Resp><respCode>098</respCode></Resp></Quanbody></Quan>";
    private String readerApdu;
    private String returnApdu = "6200";
    private String tempToken;

    public String processTxn(String tppAddress, int tppPort, int timeout, String xml, String cardno, String aucAccountIn, String passwd, String newPasswd, String validate, String dynamicpasswd, String CVN2) {
        try {
            this.tppAddress = InetAddress.getByName(tppAddress)
                    .getHostAddress();
        } catch (UnknownHostException e) {
            // /MyLogger.getLogger().e("InetAddress.getByName error " +
            // tppAddress);
        }
        this.tppPort = tppPort;
        this.timeout = timeout;
        this.xml = xml;
        this.cardno = cardno;
        this.aucAccountIn = aucAccountIn;
        this.passwd = passwd;
        this.newPasswd = newPasswd;
        this.validate = validate;
        this.dynamicpasswd = dynamicpasswd;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(Math.abs(random.nextInt() % 10));
        }
        this.aucRandomNum = sb.toString();
        this.CVN2 = CVN2;
        isTimeOut = false;
        isFinished = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setTimeOut();
            }
        }, (timeout + 5) * 1000);
        // MyLogger.getLogger().d("sendRequest======" + xml);
        Log.i("tag", "============================================");
        new Thread() {
            public void run() {
                System.out.println("@@@@ 1 process_Txn");
                process_Txn();
            }
        }.start();
        while (!isTimeOut) {
            if (isFinished)
                break;
        }
        timer.cancel();
        // MyLogger.getLogger().d("getResponse======" + returnXml);
        String tmpStr[] = returnXml.split("</MHome>");
        String resString = tmpStr[0] + "</MHome>";

        if ((resString.trim()).equals("</MHome>".trim())) {
            resString = "<MHome><respCode>098</respCode></MHome>";
        }
        return resString;
    }

    public String processApdu(String receivedApdu, String token) {

        this.readerApdu = receivedApdu;
        this.tempToken = token;

        isTimeOut = false;
        isFinished = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setTimeOut();
            }
        }, (timeout + 5) * 1000);
        // MyLogger.getLogger().d("sendRequest======" + xml);
        Log.i("tag", "============================================");
        new Thread() {
            public void run() {
                process_Apdu();
            }
        }.start();
        while (!isTimeOut) {
            if (isFinished)
                break;
        }
        timer.cancel();
        // MyLogger.getLogger().d("getResponse======" + returnXml);

        if (returnApdu.length() == 0) {
            returnApdu = "6200";
        }
        return returnApdu;
    }

    private void setTimeOut() {
        isTimeOut = true;
    }

    private void process_Txn() {
        returnXml = "<MHome><respCode>98</respCode></MHome>";
        System.out.println("@@@@ 2 processTxn");
        returnXml = ProcessTxn(tppAddress, tppPort, timeout, xml, cardno, aucAccountIn, passwd, newPasswd, validate, dynamicpasswd, aucRandomNum, CVN2);
        isFinished = true;
    }

    private void process_Apdu() {
        returnApdu = "6200";
        returnApdu = ProcessApdu(timeout, readerApdu, tempToken);
        isFinished = true;
    }

    private native static String ProcessTxn(String tppAddress, int tppPort,
                                            int timeout, String xml, String cardno, String aucAccountIn,
                                            String passwd, String newPasswd, String validate, String dynamicpasswd,
                                            String aucRandomNum, String CVN2);

    private native static String ProcessApdu(int timeout, String receivedApdu,
                                             String token);

}
