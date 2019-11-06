package team.legend.jobhunter.utils;

public class Constant {
    public static final String[] ALLOW_SUFFIXS = {"jpg", "png","jpeg","svg"};

    public static final int MALE = 1 ;
    public static final int FEMALE = 2;
    public static final int UNKNOW = 0;

    public static final long LOCK_EXPIRE_TIME = 5000;


    //错误码
    public static final int ERROR_CODE = 10000;
    public static final int PARAM_CODE = 10001;
    public static final int UPLOAD_ERROR_CODE = 10002;
    public static final int SPIDER_FAIL_CODE   = 10003;
    public static final int INFO_NOTFILL_CODE = 10004;

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


    public static final int ERROR_Tea_ERROR_CODE = 40000;
    public static final int ERROR_Tea_InfoError = 40001;
    public static final int ERROR_TEA_VERIFY_FAIL = 40002;
    public static final int ERROR_TEA_VERIFY_DUPLICATION = 40003;

}
