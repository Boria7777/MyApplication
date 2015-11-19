package cn.syndu.childtip;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Blue on 2015/9/28.
 */
public class GloabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
    }

}