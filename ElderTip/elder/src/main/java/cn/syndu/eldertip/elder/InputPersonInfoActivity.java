package cn.syndu.eldertip.elder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Blue on 2015/8/14.
 */
public class InputPersonInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_person_info);
        setFinishOnTouchOutside(false);
        etUserName = (EditText) findViewById(R.id.cn_syndu_eldtip_username);
        etIdentityCode = (EditText) findViewById(R.id.cn_syndu_eldtip_person_identity);
        etSex = (Spinner) findViewById(R.id.cn_syndu_eldtip_sex);
        etAge = (EditText) findViewById(R.id.cn_syndu_eldtip_age);
        etAddress = (EditText) findViewById(R.id.cn_syndu_eldtip_address);
        etHotKey1 = (EditText) findViewById(R.id.cn_syndu_eldtip_hot_key_num1);
        etHotKey2 = (EditText) findViewById(R.id.cn_syndu_eldtip_hot_key_num2);
        etHotKey3 = (EditText) findViewById(R.id.cn_syndu_eldtip_hot_key_num3);
    }

    @Override
    protected void onStart() {

        super.onStart();

        SharedPreferences share = getSharedPreferences(Utility.PERSON_INFO, Context.MODE_PRIVATE);

        String xml = share.getString(Utility.PERSON_INFO, "");
        if (!xml.equals("")) {
            try {
                Element root = DocumentHelper.parseText(xml).getRootElement();

                etUserName.setText(root.element("NAME").getTextTrim());
                etIdentityCode.setText(root.element("IDENTITY").getTextTrim());
                etSex.setSelection(root.element("SEX").getTextTrim() == "男" ? 0 : 1);
                etAge.setText(root.element("AGE").getTextTrim());
                etAddress.setText(root.element("ADDRESS").getTextTrim());
                etHotKey1.setText(root.element("HOT_KEY1").getTextTrim());
                etHotKey2.setText(root.element("HOT_KEY2").getTextTrim());
                etHotKey3.setText(root.element("HOT_KEY3").getTextTrim());
            } catch (Exception ex) {

            }

            if (!share.getString("USER_NAME", "").equals(""))
                isEdit = true;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isEdit) {
            setResult(0);
            this.finish();
        } else if (keyCode == KeyEvent.KEYCODE_BACK && !isEdit) {
            new AlertDialog.Builder(this)
                    .setTitle("确定取消输入？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            sendBroadcast(new Intent(Protocols.SEND_USER_INFO_ACTION));
                            setResult(1);
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();

        }
        return true;
    }

    public void onSubmitClicked(View view) {
        try {
            if (Utility.isNetworkConnected(InputPersonInfoActivity.this)) {


                if (etIdentityCode.getText().toString().equals("")) {
                    Utility.showTipMsg(this, "身份证号为必填项，请检查！");
                    return;
                }

                Document doc = DocumentHelper.createDocument();
                Element root = doc.addElement("PROTOCOL");
                root.addElement("NAME").setText(etUserName.getText().toString());
                root.addElement("IDENTITY").setText(etIdentityCode.getText().toString());
                root.addElement("SEX").setText(etSex.getSelectedItem().toString());
                root.addElement("AGE").setText(etAge.getText().toString());
                root.addElement("ADDRESS").setText(etAddress.getText().toString());
                root.addElement("HOT_KEY1").setText(etHotKey1.getText().toString());
                root.addElement("HOT_KEY2").setText(etHotKey2.getText().toString());
                root.addElement("HOT_KEY3").setText(etHotKey3.getText().toString());
                SharedPreferences myShare = getSharedPreferences(Utility.PERSON_INFO, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myShare.edit();
                editor.putString(Utility.PERSON_INFO, doc.asXML());
                editor.putString(Utility.HOT_KEY_NUM1, etHotKey1.getText().toString());
                editor.putString(Utility.HOT_KEY_NUM2, etHotKey2.getText().toString());
                editor.putString(Utility.HOT_KEY_NUM3, etHotKey3.getText().toString());
                editor.putString("IDENTITY", etIdentityCode.getText().toString());
                editor.commit();

                Intent _intent = new Intent();
                _intent.putExtra(Protocols.BROAD_CAST_VALUE, doc.asXML());
                _intent.setAction(Protocols.SEND_USER_INFO_ACTION);
                sendBroadcast(_intent);

                this.setResult(0);
                this.finish();
            }
            else{

                Utility.showTipMsg(InputPersonInfoActivity.this,"当前网络无效，请先连接网络！");
            }
        } catch (Exception ex) {
            ex.toString();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    EditText etUserName = null;
    EditText etIdentityCode = null;
    Spinner etSex = null;
    EditText etAge = null;
    EditText etAddress = null;
    EditText etHotKey1 = null;
    EditText etHotKey2 = null;
    EditText etHotKey3 = null;
    private boolean isEdit = false;
}
