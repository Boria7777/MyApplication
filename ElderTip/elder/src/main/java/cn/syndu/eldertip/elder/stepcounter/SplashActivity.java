package cn.syndu.eldertip.elder.stepcounter;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;

import cn.syndu.eldertip.elder.ClientService;
import cn.syndu.eldertip.elder.Protocols;
import cn.syndu.eldertip.elder.R;
import cn.syndu.eldertip.elder.UpdateByHttp;
import cn.syndu.eldertip.elder.Utility;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Protocols.UPDATE_ACTION);
        filter.addAction(Protocols.DEVICE_ON_SUCCESS);
        registerReceiver(MyReceiver, filter);
        setContentView(R.layout.splash);
        startService(new Intent(this, ClientService.class));

    }


    BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Protocols.UPDATE_ACTION)) {
                UpdateByHttp update = new UpdateByHttp(SplashActivity.this);
                update.start(Utility.UpdateUrl, "update.apk");
            } else if (intent.getAction().equals(Protocols.DEVICE_ON_SUCCESS)) {
                startActivity(new Intent(context, StepCounterActivity.class));
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(MyReceiver);
    }
}

