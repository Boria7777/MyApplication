package cn.syndu.eldertip.elder;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;

/**
 * Created by Blue on 2015/8/28.
 */
public class SwitchEntity {
    public boolean AutoAccept = true;
    public boolean AutoLocation = true;
    private static SwitchEntity instance = new SwitchEntity();

    public static SwitchEntity getInstance() {
        if (null == instance) {
            instance = new SwitchEntity();
        }
        return instance;
    }

    private SwitchEntity() {
    }

    public void putValue(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("SWITCH", Context.MODE_PRIVATE).edit();
        edit.putBoolean("AutoAccept", AutoAccept);
        edit.putBoolean("AutoLocation", AutoLocation);
        edit.commit();
    }

    public void getValue(Context context) {
        SharedPreferences share = context.getSharedPreferences("SWITCH", Context.MODE_PRIVATE);
        share.getBoolean("AutoAccept", true);
        share.getBoolean("AutoLocation", true);
    }
}
