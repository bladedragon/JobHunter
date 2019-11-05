package team.legend.jobhunter.utils;

public class Constant {
    public static final String[] ALLOW_SUFFIXS = {"jpg", "png","jpeg","svg"};

    public static final int MALE = 1 ;
    public static final int FEMALE = 2;
    public static final int UNKNOW = 0;

    public static final long LOCK_EXPIRE_TIME = 5000;


    //错误码
    public static final int ERROR_CODE = 10000;

    //支付接口相关错误码
    public static final int ERROR_PAY_CODE = 20000;
    public static final int ERROR_PAY_NOTENOUGH = 20001;
    public static final int ERROR_PAY_ORDERPAID = 20002;
    public static final int ERROR_PAY_ORDERCLOSED = 20003;
    public static final int ERROR_PAY_SYSTEMERROR = 20004;

}
