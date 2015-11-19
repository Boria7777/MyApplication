package cn.syndu.eldertip.elder;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

/**
 * Created by Blue on 2015/8/14.
 */
public class Utility {
    public static String getDeviceId(Context context) {
        if (serial.equals("")) {
            TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            serial = telephonemanage.getDeviceId();
        }
        for (int i = serial.length(); i < 36; i++) {
            serial += " ";
        }

        return serial;
    }


    public static void showTipMsg(Context context, String tipInfo) {
        new AlertDialog.Builder(context).setTitle(tipInfo).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public static boolean isNetworkConnected(Context context) {
        if (null != context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (null != mNetworkInfo) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String getSimCardSerial(Context context) {
        String result = "";
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(context.TELEPHONY_SERVICE);
        result = tm.getSimSerialNumber();
        if (tm.getSimSerialNumber() != null) {
            return result;
        } else {
            return "";
        }

    }

    public static boolean sendSMS(Context mContext, final String sendtext,
                           final String receiver) {
        // to send SMS
        if (sendtext == null)
            return false;
        String sendText = new String(sendtext);

        Intent localIntent = new Intent("SENT_SMS_ACTION");
        SmsManager localSmsManager = SmsManager.getDefault();
        try {
            ArrayList<String> messages = localSmsManager
                    .divideMessage(sendText);
            int messageCount = messages.size();
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>(
                    messageCount);
            for (int i = 0; i < messageCount; i++) {
                int requestCode = 0;
                if (i == messageCount - 1) {
                    requestCode = 1;
                }
                sentIntents.add(PendingIntent.getBroadcast(mContext,
                        requestCode, localIntent, 0));
            }
            localSmsManager.sendMultipartTextMessage(receiver, null, messages,
                    sentIntents, null);
        } catch (Exception localException) {
            localException.printStackTrace();
            return false;
        }
        return true;
		/**/
    }

    public static final String APP_PARAMS = "APP_PARAMS";
    public static final String TEL_NUMBER = "TEL_NUMBER";

    public static final String PERSON_INFO = "PERSON_INFO";

    public static final String HOT_KEY_NUM1 = "HOT_KEY_NUM1";
    public static final String HOT_KEY_NUM2 = "HOT_KEY_NUM2";
    public static final String HOT_KEY_NUM3 = "HOT_KEY_NUM3";

    private static String serial = "";
    public static String UpdateUrl = "";
    public static String DescTelephoneNum = "";

}
