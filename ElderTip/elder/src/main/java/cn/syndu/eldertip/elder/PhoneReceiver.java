package cn.syndu.eldertip.elder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * Created by Blue on 2015/8/28.
 */
public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context cnt, Intent intent) {
        this.context = cnt;
        if (SwitchEntity.getInstance().AutoAccept && !intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            toAnswer();
        }
    }

    void toAnswer() {
        try {
            manager = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            Method getITelephonyMethod = TelephonyManager.class
                    .getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            mITelephony = (ITelephony) getITelephonyMethod.invoke(manager,
                    (Object[]) null);

            answerRingingCallWithBroadcast(context);
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);

            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setMode(AudioManager.ROUTE_SPEAKER);
                audioManager.setSpeakerphoneOn(true);
            }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

    private void answerRingingCallWithBroadcast(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);

        // 以下适用于Android2.3及2.3以上的版本上 ，但测试发现4.1系统上不管用。
        Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
        localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        localIntent1.putExtra("state", 1);
        localIntent1.putExtra("microphone", 1);
        localIntent1.putExtra("name", "Headset");
        context.sendOrderedBroadcast(localIntent1,
                "android.permission.CALL_PRIVILEGED");

        Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_HEADSETHOOK);
        localIntent2.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent1);
        context.sendOrderedBroadcast(localIntent2,
                "android.permission.CALL_PRIVILEGED");

        Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_HEADSETHOOK);
        localIntent3.putExtra(Intent.EXTRA_KEY_EVENT, localKeyEvent2);
        context.sendOrderedBroadcast(localIntent3,
                "android.permission.CALL_PRIVILEGED");

        Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
        localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        localIntent4.putExtra("state", 0);
        localIntent4.putExtra("microphone", 1);
        localIntent4.putExtra("name", "Headset");
        context.sendOrderedBroadcast(localIntent4,
                "android.permission.CALL_PRIVILEGED");
    }


    private synchronized void answerRingingCall(Context context) {
        try {
            Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent1.putExtra("state", 1);
            localIntent1.putExtra("microphone", 1);
            localIntent1.putExtra("name", "Headset");
            context.sendOrderedBroadcast(localIntent1,
                    "android.permission.CALL_PRIVILEGED");
            Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent2.putExtra("android.intent.extra.KEY_EVENT",
                    localKeyEvent1);
            context.sendOrderedBroadcast(localIntent2,
                    "android.permission.CALL_PRIVILEGED");
            Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
                    KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent3.putExtra("android.intent.extra.KEY_EVENT",
                    localKeyEvent2);
            context.sendOrderedBroadcast(localIntent3,
                    "android.permission.CALL_PRIVILEGED");
            Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent4.putExtra("state", 0);
            localIntent4.putExtra("microphone", 1);
            localIntent4.putExtra("name", "Headset");
            context.sendOrderedBroadcast(localIntent4,
                    "android.permission.CALL_PRIVILEGED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TelephonyManager manager;
    private static ITelephony mITelephony;
    private TelephonyManager tm;
    private Context context = null;
}
