/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.syndu.eldertip.elder;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author blue
 */
public class ProtocolEntity {
    public String Serial = "";          //an identity String for client length 36
    public int Command = 0;             //a command
    public byte[] Content = new byte[0];       //content
    public String Identity = "";        // length 36
    private int _length = 0;


    public byte[] toByteArray() throws Exception {
        _length = 4+36 + 4 + 36 + Content.length;

        Log.e("ProtocolEntity", "包长度：" + _length);
        // package length 4 bytes
        byte[] protocol = new byte[_length];
        System.arraycopy(HexTools.int2Byte(_length-4), 0, protocol, 0, 4);

        //36 bytes serial
        if (Serial.length() != 36) {
            throw new Exception("Serial's length is not 36");
        }
        byte[] bSerial = Serial.getBytes("utf-8");
        System.arraycopy(bSerial, 0, protocol, 4, 36);

        //command 4 bytes 
        System.arraycopy(HexTools.int2Byte(Command), 0, protocol, 40, 4);
        //command identity
        System.arraycopy(getComandIdentity(), 0, protocol, 44, 36);
        //content bytes
        System.arraycopy(Content, 0, protocol, 80, Content.length);
        Log.e("ProtocolEntity", "包真实长度：" + protocol);
        Log.e("ProtocolEntity", String.valueOf(Content));
        return protocol;
    }

    public ProtocolEntity() {
    }

    public ProtocolEntity(byte[] value) throws Exception {
        byte[] _serial = new byte[36];
        System.arraycopy(value, 0, _serial, 0, _serial.length);
        this.Serial = new String(_serial, "utf-8");

        byte[] _command = new byte[4];
        System.arraycopy(value, 36, _command, 0, 4);
        this.Command = HexTools.byte2Int(_command);

        byte[] _identity = new byte[36];
        System.arraycopy(value, 40, _identity, 0, 36);
        this.Identity = new String(_identity, "utf-8");

        int _length = value.length - 76;
        this.Content = new byte[_length];
        System.arraycopy(value, 76, this.Content, 0, _length);
    }

    protected byte[] getDeviceId() {
        return new byte[36];
    }

    protected byte[] getComandIdentity() throws UnsupportedEncodingException {
        String temp = UUID.randomUUID().toString();
        for (int i = temp.length(); i < 36; i++) {
            temp += " ";
        }
        return temp.getBytes("utf-8");
    }
}
