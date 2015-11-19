package cn.syndu.eldertip.elder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Blue on 2015/8/14.
 */
public class HotCallActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneIDX = 0;

    }

    @Override
    protected void onStart() {
        super.onStart();

        String[] phoneNum = getPhoneNum().split(";");
        if (phoneNum.length == 0) {
            Toast.makeText(this, "请先输入要拨打的号码！",Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (phoneIDX < phoneNum.length) {
            if (phoneNum[phoneIDX].equals("")) {

            } else {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + phoneNum[phoneIDX]));
                startActivity(intent);
            }
            phoneIDX++;
        } else {
            finish();
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            //state 当前状态 incomingNumber,貌似没有去电的API
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    String[] phoneNum = getPhoneNum().split(";");

                    if (phoneIDX < phoneNum.length) {
                        if (!phoneNum[phoneIDX].equals("")) {
                            phoneIDX++;
                        } else {
                            Intent intent = new Intent();

                            intent.setAction("android.intent.action.CALL");

                            intent.setData(Uri.parse("tel:" + phoneNum[phoneIDX]));

                            startActivity(intent);
                        }

                    } else {
                        finish();
                    }
            }
        }
    };

    private String getPhoneNum() {
        SharedPreferences mydata = getSharedPreferences(Utility.PERSON_INFO, Activity.MODE_PRIVATE);

        return mydata.getString(Utility.HOT_KEY_NUM1, "") + ";" + mydata.getString(Utility.HOT_KEY_NUM2, "") + ";" + mydata.getString(Utility.HOT_KEY_NUM3, "");
    }


    private static int phoneIDX = 0;
}