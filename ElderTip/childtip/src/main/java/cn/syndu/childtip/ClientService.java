package cn.syndu.childtip;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.DocumentsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientService extends Service implements Runnable {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {

        context = ClientService.this;
        InboxHandler = new SocketReceiverHandler();

        IntentFilter filter = new IntentFilter();
        filter.addAction(DEVICE_ONLINE_ACTION);
        filter.addAction(DEVICE_CHECK_ACTION);
        filter.addAction(GET_POSITION_INFO_ACTION);
        registerReceiver(GateWayBroadcastReceiver, filter);

        reConnect();
        super.onCreate();
    }

    public BroadcastReceiver GateWayBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String action = intent.getAction();

                if (null != InboxHandler) {
                    if (action.equals(DEVICE_ONLINE)) {
                        ProtocolEntity entity = new ProtocolEntity();
                        entity.Command = DEVICE_ONLINE;
                        entity.Serial = getDeviceId();

                        Message msg = InboxHandler.obtainMessage();
                        msg.what = 0;
                        msg.obj = entity;
                        InboxHandler.sendMessage(msg);
                    } else if (action.equals(DEVICE_CHECK_ACTION)) {
                        try {
                            ProtocolEntity entity = new ProtocolEntity();
                            entity.Command = DEVICE_CHECK;
                            entity.Serial = getDeviceId();
                            String code = intent.getExtras().getString(BROAD_CAST_VALUE);
                            Document xml = DocumentHelper.createDocument();
                            Element root = xml.addElement("PROTOCOL");
                            root.addElement("REGCODE").setText(code);
                            entity.Content = xml.asXML().getBytes("utf-8");
                            Message msg = InboxHandler.obtainMessage();
                            msg.what = 0;
                            msg.obj = entity;
                            InboxHandler.sendMessage(msg);
                        } catch (Exception ex) {
                            Log.e("", null == ex ? "" : ex.toString());
                        }
                    } else if (action.equals(GET_POSITION_INFO_ACTION)) {
                        try {
                            ProtocolEntity entity = new ProtocolEntity();
                            entity.Command = GET_POSITION_INFO;
                            entity.Identity = getDeviceId();
                            Message msg = InboxHandler.obtainMessage();
                            msg.what = 0;
                            msg.obj = entity;
                            InboxHandler.sendMessage(msg);
                        } catch (Exception ex) {
                            Log.e("", null == ex ? "" : ex.toString());
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e("", "");
            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            Log.e(Tag, "Service Destroy error" + ex.getMessage());
        }
        Log.w(Tag, "Service is Destroy");
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.w(Tag, "Service is onLowMemory");
        super.onLowMemory();
    }

    public class SocketReceiverHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            Log.e(Tag, "收到消息" + msg.what);
            ProtocolEntity entity = ((ProtocolEntity) msg.obj);
            entity.Serial = getPhoneSerail();
            try {
                switch (entity.Command) {
                    case Alive: {
                        sendData(msg.what, "");
                        break;
                    }
                    case DEVICE_CHECK: {
                        out.write(((ProtocolEntity) msg.obj).toByteArray());
                        break;
                    }
                    case R_DEVICE_CHECK: {
                        ProtocolEntity obj = (ProtocolEntity) msg.obj;
                        Document doc = DocumentHelper.parseText(new String(entity.Content, "utf-8"));
                        String result = doc.getRootElement().element("ERRORCODE").getTextTrim();
                        Intent _intent = new Intent(DEVICE_CHECK_ACTION_RESULT);
                        _intent.putExtra(BROAD_CAST_VALUE, result);
                        sendBroadcast(_intent);
                        break;
                    }
                    case DEVICE_ONLINE: {
                        out.write(((ProtocolEntity) msg.obj).toByteArray());
                        break;
                    }
                    case R_DEVICE_ONLINE: {
                        ProtocolEntity obj = (ProtocolEntity) msg.obj;
                        String result = new String(entity.Content, "utf-8");
                        Intent _intent = new Intent(DEVICE_ONLINE_ACTION_RESULT);
                        _intent.putExtra(BROAD_CAST_VALUE, result);
                        sendBroadcast(_intent);
                        break;
                    }
                    case GET_POSITION_INFO: {
                        out.write(((ProtocolEntity) msg.obj).toByteArray());
                        break;
                    }
                    case R_GET_POSITION_INFO: {
                        ProtocolEntity obj = (ProtocolEntity) msg.obj;
                        String result = new String(entity.Content, "utf-8");
                        Intent _intent = new Intent(GET_POSITION_INFO_ACTION_RESULT);
                        _intent.putExtra(BROAD_CAST_VALUE, result);
                        sendBroadcast(_intent);
                        break;
                    }
                }
            } catch (Exception ex) {
                Log.e(Tag, "Socket reConnect" + ex.toString());
                reConnect();
            }
        }
    }

    ;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!Thread.interrupted()) {
            Log.i(Tag, Thread.currentThread().getName() + " ����������Ϣ");
            try {
                byte[] b4 = recBytes(4);
                // Thread.sleep(100);
                if (b4 == null) {
                    continue;
                }
                int length = byte2Int(b4);
                Log.d(Tag, "Get protocol length:" + length);
                Message msg = InboxHandler.obtainMessage();
                ProtocolEntity entity = new ProtocolEntity();
                Log.i(Tag,
                        "收到消息:" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()))
                                + "消息长度:" + length);
                byte[] _data = recBytes(length);
                // get 32byte serial
                byte[] _serial = new byte[36];
                System.arraycopy(_data, 0, _serial, 0, 36);
                entity.Serial = new String(_serial, "utf-8");
                Log.d(Tag, "Get Serial Num:" + new String(_serial, "utf-8"));
                // get cmd
                byte[] _bCmd = new byte[4];
                System.arraycopy(_data, 36, _bCmd, 0, 4);
                int _iCmd = byte2Int(_bCmd);
                entity.Command = _iCmd;
                msg.what = _iCmd;
                Log.d(Tag, "Get cmd :" + _iCmd);
                if (entity.Command == 0x0800) {
                    Log.d(Tag, "收到心跳！");
                } else {
                    byte[] _identity = new byte[36];
                    System.arraycopy(_data, 40, _identity, 0, 36);
                    entity.Identity = new String(_identity, "utf-8");
                    if (length > 76) {
                        byte[] _content = new byte[length - 76];
                        Log.d(Tag, "get protocol's content:" + _content.length);
                        System.arraycopy(_data, 76, _content, 0, _content.length);
                        entity.Content = _content;
                        Log.d(Tag, "get protocol content:" + new String(_content, "utf-8"));
                    }
                }
                msg.obj = entity;

                InboxHandler.sendMessage(msg);
                Thread.sleep(200);
                isOverTime = System.currentTimeMillis();
                Log.d("OverTime", "重新赋值超时：" + isOverTime);
            } catch (Exception ex) {
                Log.e(Tag, "接收线程出错：" + (String) (null == ex ? "" : ex.getMessage()));
                reConnect();
                return;
            }

        }
        Log.e(Tag, "�����߳��жϣ�");
    }

    public void reConnect() {

        Log.e(Tag, "开始重新连接网络！");
        if (!isReConnceting) {
            isReConnceting = true;
            try {
                if ((null != thread) && (thread.isAlive())) {
                    Log.d(Tag, "thread.interrupt()");
                    thread.interrupt();
                }
            } catch (Exception e) {

            }
            try {
                in.close();
            } catch (Exception ex) {

            }
            try {
                out.close();
            } catch (Exception ex) {

            }
            try {
                socket.close();
            } catch (Exception ex) {

            }

            new Thread(connectSocketRunnable).start();

        } else {
            Log.i(Tag, "reConnect 正在重连,不再重连！");
        }

    }

    public void getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public byte[] recBytes(int length) throws Exception {
        byte[] result = new byte[length];
        int hasRec = 0;
        int isRead = 0;
        do {
            byte[] buffer = new byte[length - hasRec];
            isRead = in.read(buffer);
            if (isRead != -1) {
                System.arraycopy(buffer, 0, result, hasRec, isRead);
                hasRec += isRead;
                Log.e(Tag, "Socket read" + isRead);
                if (isRead == 0)
                    Thread.sleep(100);
            } else {
                if (hasRec > 0) {
                    return null;
                }
            }

        } while (hasRec < length);
        return result;
    }

    Handler SendAliveHandler = new Handler();
    Runnable SendAliveRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                sendData(Alive, "");
                SendAliveHandler.removeCallbacks(SendAliveRunnable);
                SendAliveHandler.postDelayed(SendAliveRunnable, aliveSplit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable connectSocketRunnable = new Runnable() {

        @Override
        public void run() {
            do {
                try {
                    socket = new Socket();
                    IP = GetInetAddress(Host);
                    isa = new InetSocketAddress(IP, port);
                    socket.connect(isa);
                    out = new BufferedOutputStream(socket.getOutputStream());
                    in = new BufferedInputStream(socket.getInputStream());
                    try {
                        if (null != thread && thread.isAlive()) {
                            Log.e(Tag, "接收线程还在运程，停止！");
                            thread.interrupt();
                        } else {
                            thread = new Thread(ClientService.this);
                            thread.start();
                            Log.d(Tag, "开启新的读线程" + thread.getName());
                        }
                    } catch (Exception ex) {
                        Log.d(Tag, "结束读线程出错！" + thread.getName());
                    }
                    isReConnceting = false;
                    ProtocolEntity entity = new ProtocolEntity();
                    entity.Command = DEVICE_ONLINE;
                    Message obj = InboxHandler.obtainMessage();
                    obj.obj = entity;
                    InboxHandler.sendMessage(obj);
                    SendAliveHandler.removeCallbacks(SendAliveRunnable);
                    SendAliveHandler.postDelayed(SendAliveRunnable, aliveSplit);
                    OverTimeHandler.removeCallbacks(OverTimeRunnable);
                    OverTimeHandler.postDelayed(OverTimeRunnable, maxTimeOut);

                    break;
                } catch (Exception ex) {
                    Log.i(Tag, Thread.currentThread().getName() + " InitSocket error " + ex.toString());
                    Log.i(Tag, "Socket 连接失败！");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } while (true);
        }
    };

    public static int byte2Int(byte[] res) {
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    // int to byte[4]
    public static byte[] int2Byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);
        targets[1] = (byte) ((res >> 8) & 0xff);
        targets[2] = (byte) ((res >> 16) & 0xff);
        targets[3] = (byte) (res >>> 24);
        return targets;
    }

    // send head: length,serial,cmd,
    private void sendHead(byte[] cmd, byte[] length) throws IOException {
        out.write(length);
        Log.d(Tag, "�����ֽڣ�" + length.length);
        out.write(getPhoneSerail().getBytes("utf-8"));
        Log.d(Tag, "���ʹ����ֽڣ�" + getPhoneSerail().getBytes("utf-8").length);
        out.write(cmd);

        Log.d(Tag, "���������ֽڣ�" + cmd.length);
    }

    private void sendData(int order, String body) throws IOException {
        Log.e(Tag, "�������" + order);
        byte[] cmd = int2Byte(order);
        byte[] _bBody = body.getBytes("utf-8");
        byte[] _length = int2Byte(_bBody.length + 40);
        sendHead(cmd, _length);
        if (!body.equals("")) {
            out.write(_bBody);
            Log.d(Tag, "���������ֽ�" + new String(_bBody, "utf-8"));
        } else {
            Log.d(Tag, "������");
        }

        out.flush();
    }

    public static String getPhoneSerail() {
        if (serial.equals("")) {
            TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            serial = telephonemanage.getDeviceId();
        }
        for (int i = serial.length(); i < 36; i++) {
            serial += " ";
        }

        return serial;
    }

    private long isOverTime = System.currentTimeMillis();
    private Handler OverTimeHandler = new Handler();
    private Runnable OverTimeRunnable = new Runnable() {

        @Override
        public void run() {
            Log.d("Gateway OverTime", "��鳬ʱ:" + (System.currentTimeMillis() - isOverTime));
            if (System.currentTimeMillis() - isOverTime > maxTimeOut) {
                Log.e("Gateway OverTime", "���ط��������ӳ�ʱ���������ӣ�");
                reConnect();
            } else {
                isOverTime = System.currentTimeMillis();
                OverTimeHandler.postDelayed(this, maxTimeOut);
            }

        }
    };

    public String GetInetAddress(String host) {
        String IPAddress = "";
        InetAddress ReturnStr1 = null;
        try {
            ReturnStr1 = InetAddress.getByName(host);
            IPAddress = ReturnStr1.getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return IPAddress;
        }
        return IPAddress;
    }

    private String getDeviceId() {
        if (serial.equals("")) {
            TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            serial = telephonemanage.getDeviceId();
        }
        for (int i = serial.length(); i < 36; i++) {
            serial += " ";
        }

        return serial;
    }

    public static String version = "";
    private static String serial = "";
    private String Tag = this.getClass().getSimpleName();
    private static Context context = null;
    private BufferedInputStream in = null;
    private BufferedOutputStream out = null;
    private static Socket socket = null;
    private InetSocketAddress isa = null;
    public String IP = "";
    public static String Host = "www.yapon.cn";
    public final int aliveSplit = 30000;
    private int port = 40000;
    Thread thread = null;
    public static final String BROAD_CAST_VALUE = "BROAD_CAST_VALUE";

    private boolean isReConnceting = false;

    private static final long maxTimeOut = 60000;

    private final int CONNECTION_CMD = 0x0001;

    private boolean isConnected = false;

    private static SocketReceiverHandler InboxHandler = null;

    public static final String DEVICE_ONLINE_ACTION = "DEVICE_ONLINE_ACTION";
    public static final String DEVICE_ONLINE_ACTION_RESULT = "DEVICE_ONLINE_ACTION_RESULT";

    public static final String DEVICE_CHECK_ACTION = "DEVICE_CHECK_ACTION";
    public static final String DEVICE_CHECK_ACTION_RESULT = "DEVICE_CHECK_ACTION_RESULT";

    public static final String GET_POSITION_INFO_ACTION = "GET_POSITION_INFO_ACTION";
    public static final String GET_POSITION_INFO_ACTION_RESULT = "GET_POSITION_INFO_ACTION_RESULT";


    static final int Alive = 0x0000;
    static final int DEVICE_ONLINE = 0x0101;
    static final int R_DEVICE_ONLINE = 0x0901;

    static final int DEVICE_CHECK = 0x0100;
    static final int R_DEVICE_CHECK = 0x0900;

    static final int GET_POSITION_INFO = 0x0105;
    static final int R_GET_POSITION_INFO = 0x0905;
}
