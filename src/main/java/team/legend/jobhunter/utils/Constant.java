package team.legend.jobhunter.utils;

public class Constant {
    public static final String[] ALLOW_IMG_SUFFIXS = {"jpg", "png","jpeg","svg"};
    public static final String[] ALLOOW_FILE_SUFFIXS ={""};

    public static final int MALE = 1 ;
    public static final int FEMALE = 2;
    public static final int UNKNOW = 0;

    public static final long LOCK_EXPIRE_TIME = 5000;

    public static final long MAX_FILE_SIZE = 15*1024*1000;

    //特殊成功码
    public static final int EMPTY_CODE = 201;


    //错误码
    public static final int ERROR_CODE = 10000;
    public static final int PARAM_CODE = 10001;
    public static final int UPLOAD_ERROR_CODE = 10002;
    public static final int SPIDER_FAIL_CODE   = 10003;
    public static final int INFO_NOTFILL_CODE = 10004;
    public static final int INFO_EMPTY_CODE = 10005;
    public static final int REPEAT_CODE = 10006;

    //支付接口相关错误码
    public static final int ERROR_PAY_CODE = 20000;
    public static final int ERROR_PAY_NOTENOUGH = 20001;
    public static final int ERROR_PAY_ORDERPAID = 20002;
    public static final int ERROR_PAY_ORDERCLOSED = 20003;
    public static final int ERROR_PAY_SYSTEMERROR = 20004;

    //jwt相关错误
    public static final int ERROR_JWT_TIMWOUT = 30001;
    public static final int ERROR_JWT_AUTHORIZE_FAIL = 30002;
    public static final int ERROR_JWT_NOTEXIST = 30003;


    //教师端相关错误码
    public static final int ERROR_Tea_ERROR_CODE = 40000;
    public static final int ERROR_Tea_InfoError = 40001;
    public static final int ERROR_TEA_VERIFY_FAIL = 40002;
    public static final int ERROR_TEA_VERIFY_DUPLICATION = 40003;

    //订单相关错误码
    public static final int ERROR_ORDER_CODE = 50000;
    public static final int ERROR_DUPLICATION_ORDER = 50001;
    public static final int ERROR_ORDER_OVERTIME = 50002;



}
