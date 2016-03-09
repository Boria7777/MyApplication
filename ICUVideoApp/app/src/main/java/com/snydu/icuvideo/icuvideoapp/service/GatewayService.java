package com.snydu.icuvideo.icuvideoapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GatewayService extends Service implements Runnable {
    private boolean isThreadRunning = false;
    private final String TAG = "GetwayService";
    private BufferedInputStream in = null;
    private BufferedOutputStream out = null;
    private static Socket socket = null;
    private InetSocketAddress isa = null;
    private Thread thread = null;
    private String content = "";
    public static final String R_COMMON_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MEETING><PLATFORM>2</PLATFORM><USER>1</USER><PASSWORD>123456</PASSWORD></MEETING>";
    private Button loginButton;
    private String Tag = this.getClass().getSimpleName();
    private boolean isReConnceting = false;
    private static Context context = null;
    private static SocketReceiverHandler CmdHandler = null;

    public GatewayService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate方法被调用!");
        context = GatewayService.this;
        CmdHandler = new SocketReceiverHandler();
        reConnect();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    Runnable connectSocketRunnable = new Runnable() {

        @Override
        public void run() {

            do {
                if (!isThreadRunning) {
                    Log.e(Tag, "socket开始重连！");
                    Log.e(Tag, "socket开始重连！");
                    Log.e(Tag, "socket开始重连！");
                    Log.e(Tag, "socket开始重连！");
                    try {
                        socket = new Socket();
                        isa = new InetSocketAddress("192.168.1.210", 20000);
                        socket.connect(isa, 30000);
                        out = new BufferedOutputStream(socket.getOutputStream());
                        in = new BufferedInputStream(socket.getInputStream());
                        sendData(0x0001, R_COMMON_XML);
                        try {
                            if (null != thread && thread.isAlive()) {
                                Log.e(Tag, " 中断 Thread 接收线程！");
                                thread.interrupt();
                            } else {
                                thread = new Thread(GatewayService.this);
                                thread.start();
                                Log.d(Tag, "起新的接收线程：" + thread.getName());
                            }
                        } catch (Exception ex) {
                            Log.d(Tag, "老线程关闭失败：" + thread.getName());
                        }

                        break;

                    } catch (Exception ex) {
                        Log.i(Tag, Thread.currentThread().getName() + " InitSocket error " + ex.toString());
                        Log.i(Tag, "Socket 连接失败，稍候重试！");

                    }
                }
            } while (true);
        }
    };


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

    public int ReadInt(byte[] buf, int start) {
        int b4 = (int) ((buf[start + 3] << 24));
        int b3 = (int) ((buf[start + 2] << 24) >>> 8);
        int b2 = (int) ((buf[start + 1] << 8) & 0xFF00);
        int b1 = (int) buf[start + 0] & 0xff;
        return (int) (b1 | b2 | b3 | b4);
    }

    private void sendHead(byte[] cmd, byte[] length) throws IOException {
        out.write(length);
        Log.d(Tag, "发送字节：" + length.length);
        out.write(cmd);
        Log.d(Tag, "发送命令字节：" + cmd.length);
    }

    public void sendData(int order, String body) throws IOException {
        Log.e(Tag, "发送命令：" + order);
        byte[] cmd = int2Byte(order);
        byte[] _bBody = body.getBytes("utf-8");
        byte[] _length = int2Byte(_bBody.length + 4);
        sendHead(cmd, _length);
        if (!body.equals("")) {
            out.write(_bBody);
            Log.d(Tag, "发送内容字节" + new String(_bBody, "utf-8"));
        } else {
            Log.d(Tag, "无内容");
        }

        out.flush();

    }

    public void reConnect() {

        Object obj = new Object();
        if (!isReConnceting) {
            Log.e(Tag, "开始重新连接网关！");
            isReConnceting = true;
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
            try {
                if ((thread != null) && (thread.isAlive())) {
                    Log.d(Tag, "中断接收线程 ");
                    thread.interrupt();
                }
            } catch (Exception e) {

            }
            new Thread(AliveRunnable).start();
            new Thread(connectSocketRunnable).start();

        } else {
            Log.i(Tag, "reConnect 开始重新连接,已经重连，不再重连");
        }

    }


    private class SocketReceiverHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(Tag, "收到命令" + msg.what);
            Log.e(Tag, "收到数据" + msg.obj);

//            Document document = DocumentHelper.createDocument();
//            //添加节点信息
//            Element rootElement = document.addElement("modules");
//            //这里可以继续添加子节点，也可以指定内容
//            rootElement.setText("这个是module标签的文本信息");
//            Element element = rootElement.addElement("module");
//
//            Element nameElement = element.addElement("name");
//            Element valueElement = element.addElement("value");
//            Element descriptionElement = element.addElement("description");
//
//            nameElement.setText("名称");
//            nameElement.addAttribute("language", "java");//为节点添加属性值
//
//            valueElement.setText("值");
//            valueElement.addAttribute("language", "c#");
//
//            descriptionElement.setText("描述");
//            descriptionElement.addAttribute("language", "sql server");
//
//            System.out.println(document.asXML());

        }
    }

    Runnable AliveRunnable = new Runnable() {

        @Override
        public void run() {
            do {
                try {
//                 sendData(0x0000,"" );
//                Message aliveMsg = CmdHandler.obtainMessage();
//                aliveMsg.what = 0x0000;
                    thread.sleep(10000);
//                CmdHandler.sendMessage(aliveMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }while (true);

        }
    };




    @Override
    public void run() {
        Log.i(Tag, Thread.currentThread().getName() + " 接收网关消息");

        while (!Thread.interrupted()) {
            isThreadRunning = true;
            Log.i(Tag, Thread.currentThread().getName() + " 接收网关消息");
            try {
                Message msg = CmdHandler.obtainMessage();
                byte[] b4 = recBytes(4);
                // Thread.sleep(100);
                if (b4 == null) {
                    continue;
                }
                int length = byte2Int(b4);
                Log.d(Tag, "Get protocol length:" + length);
                byte[] _data = recBytes(length);

                // get cmd
                byte[] _bCmd = new byte[4];
                System.arraycopy(_data, 0, _bCmd, 0, 4);
                int _iCmd = byte2Int(_bCmd);
                Log.d(Tag, "Get cmd :" + _iCmd);
                msg.what = _iCmd;
                if (length > 4) {
                    // 协议长度大于4，说明有内容。
                    byte[] _content = new byte[length - 4];
                    Log.d(Tag, "get protocol's content:" + _content.length);
                    System.arraycopy(_data, 4, _content, 0, _content.length);
                    msg.obj = new String(_content, "utf-8");
                    Log.d(Tag, "get protocol content:" + new String(_content, "utf-8"));
                }
                CmdHandler.sendMessage(msg);
                Thread.sleep(200);

            } catch (Exception ex) {
                Log.e(Tag, "监听接收时出错：" + (String) (null == ex ? "" : ex.getMessage()));
                reConnect();
                break;
            }

        }

        isThreadRunning = false;
        Log.e(Tag, "接收线程中断！");
    }




}