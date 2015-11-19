package cn.syndu.eldertip.elder;

/**
 * Created by Blue on 2015/8/14.
 */
public class Protocols {
    public static final int ALIVE = 0x0000;
    public static final int R_ALIVE = 0x0800;

    public static final int DEVICE_ONLINE = 0x0001;
    public static final int R_DEVICE_ONLINE = 0x0801;

    public static final int UPLOAD_HEALTH_INFO = 0x0002;
    public static final int R_UPLOAD_HEALTH_INFO = 0x0802;

    public static final int REQUEST_SETP_INFO = 0x0003;
    public static final int R_REQUEST_SETP_INFO = 0x0803;

    public static final int UPLOAD_POSITION = 0x0004;
    public static final int R_UPLOAD_POSITION = 0x0804;

    public static final int REGIST_USER_INFO = 0X0006;
    public static final int R_REGIST_USER_INFO = 0x0806;

    public static final String SEND_STEP_INFO_ACTION = "SEND_STEP_INFO_ACTION";
    public static final String REQUEST_STEP_INFO_ACTION = "REQUEST_STEP_INFO_ACTION";
    public static final String STEP_INFO_RECEIVED_ACTION = "STEP_INFO_RECEIVED_ACTION";
    public static final String SEND_USER_INFO_ACTION = "SEND_USER_INFO_ACTION";

    public static final String BROAD_CAST_VALUE = "BROAD_CAST_VALUE";

    public static final String UPDATE_ACTION = "UPDATE_ACTION";
    public static final String DEVICE_ON_SUCCESS = "DEVICE_ON_SUCCESS";

}
