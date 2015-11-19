package cn.syndu.childtip;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Blue on 2015/9/6.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressBar("正在和服务器通信，请稍后...");
        startService(new Intent(this, ClientService.class));
        IntentFilter filter = new IntentFilter();
        filter.addAction(ClientService.DEVICE_CHECK_ACTION_RESULT);
        filter.addAction(ClientService.DEVICE_ONLINE_ACTION_RESULT);
        filter.addAction(ClientService.GET_POSITION_INFO_ACTION_RESULT);
        registerReceiver(GateWayBroadcastReceiver, filter);

        //发送上线
        Intent _intent = new Intent(ClientService.DEVICE_ONLINE_ACTION);
        sendBroadcast(_intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public BroadcastReceiver GateWayBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String action = intent.getAction();
                if (action.equals(ClientService.DEVICE_ONLINE_ACTION_RESULT)) {
                    String xml = intent.getExtras().getString(ClientService.BROAD_CAST_VALUE);
                    Document doc = DocumentHelper.parseText(xml);
                    Element root = doc.getRootElement();
                    if (root.element("ERRORCODE").getTextTrim().equals("1")) {
                        hideProgressBar();
                        View dialogView = getLayoutInflater().inflate(R.layout.alertdialog_relation, (ViewGroup) findViewById(R.id.dialog));
                        final EditText etCode = (EditText) dialogView.findViewById(R.id.alertdialog_relation_edittext_serialnum);

                        new AlertDialog.Builder(MainActivity.this).setTitle("请输入要关联的设备号：").setView(dialogView)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent _intent = new Intent(ClientService.DEVICE_CHECK_ACTION);
                                        _intent.putExtra(ClientService.BROAD_CAST_VALUE, etCode.getText().toString());
                                        sendBroadcast(_intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                    } else if (root.element("ERRORCODE").getTextTrim().equals("0")) {
                        UpdateUrl = root.element("APP_URL").getTextTrim();
                        Version = root.element("VERSION").getTextTrim();
                        SMS_PHONE = root.element("SMS_PHONE").getTextTrim();
                        Intent _intent = new Intent(ClientService.GET_POSITION_INFO_ACTION);
                        sendBroadcast(_intent);
                        showProgressBar("正在获取GPS信息,请稍候...");
                    }
                } else if (action.equals(ClientService.GET_POSITION_INFO_ACTION_RESULT)) {

                    hideProgressBar();
                    String xml = intent.getExtras().getString(ClientService.BROAD_CAST_VALUE);
                    Document doc = DocumentHelper.parseText(xml);
                    Element root = doc.getRootElement();
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_gcoding);
                    Iterator iterable = root.elementIterator();
                    ArrayList<String[]> list = new ArrayList();
                    while (iterable.hasNext()) {
                        Element item = (Element) iterable.next();
                        String[] pos = item.getTextTrim().split(",");
                        list.add(pos);
                    }

                    if (list.size() > 0) {
                        LatLng p = new LatLng(Double.parseDouble(list.get(0)[1]), Double.parseDouble(list.get(0)[0]));
                        mMapView = new MapView(MainActivity.this,
                                new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                                        .target(p).build()));
                        mBaiduMap = mMapView.getMap();
                        mBaiduMap.setTrafficEnabled(false);
                        setContentView(mMapView);
                    } else {

                    }
                    for (int i = 0; i < list.size(); i++) {
                        LatLng point = new LatLng(Double.parseDouble(list.get(i)[1]),
                                Double.parseDouble(list.get(i)[0]));
                        OverlayOptions option = new MarkerOptions()
                                .position(point)
                                .icon(bitmap);
                        mBaiduMap.addOverlay(option);

                    }
                }
            } catch (Exception ex) {
                Log.e("", "");
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(GateWayBroadcastReceiver);
        super.onDestroy();
    }

    public void showProgressBar(String title) {
        if (null != m_pDialog) {
            m_pDialog.hide();
            m_pDialog = null;
        }
        m_pDialog = new ProgressDialog(MainActivity.this);
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_pDialog.setMessage(title);
        m_pDialog.setIndeterminate(false);
        m_pDialog.setCancelable(false);
        m_pDialog.show();
    }

    private void hideProgressBar(){
        if (null != m_pDialog) {
            m_pDialog.hide();
            m_pDialog = null;
        }
    }


    ProgressDialog m_pDialog = null;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    public static String UpdateUrl = "";
    public static String Version = "";
    public static String SMS_PHONE = "";
}
