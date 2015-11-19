package cn.syndu.eldertip.elder;

/**
 * Created by blue on 8/7/15.
 * 进制间转换工具类
 */
public class HexTools {

    //byte[4] to int
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
}
