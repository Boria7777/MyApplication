package cn.syndu.eldertip.elder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.dom4j.Text;

/**
 * Created by Blue on 2015/8/28.
 */
public class FunctionSettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcation_setting);
        tvSerial = (TextView) findViewById(R.id.device_serial);
        switch_auto_accept = (Switch) findViewById(R.id.switch_auto_accept);
        switch_auto_accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchEntity entity = SwitchEntity.getInstance();
                entity.AutoAccept = isChecked;
                entity.putValue(FunctionSettingActivity.this);
            }
        });
        switch_location = (Switch) findViewById(R.id.switch_location);
        switch_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchEntity entity = SwitchEntity.getInstance();
                entity.AutoLocation = isChecked;
                entity.putValue(FunctionSettingActivity.this);
            }
        });

        SwitchEntity entity = SwitchEntity.getInstance();

        switch_auto_accept.setChecked(entity.AutoAccept);
        switch_location.setChecked(entity.AutoLocation);

        ((TextView)(findViewById(R.id.device_serial))).setText("子女端安装注册号：\n"+Utility.getDeviceId(FunctionSettingActivity.this));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    TextView tvSerial = null;
    Switch switch_auto_accept = null;
    Switch switch_location = null;
}
