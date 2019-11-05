package team.legend.jobhunter.utils;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

    public static String returnFormat(int code, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("code", code);
        jsonObject.put("data",data);
        return jsonObject.toString();
    }
    public static String returnFormatSimp(int code,String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
//        jsonObject.put("timestamp",(System.currentTimeMillis()/100));
        return jsonObject.toJSONString();
    }

    public static String getNowTime(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }

    public static String getNowDate(){
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        return date;

    }

    public static String getRandomStr(int num){
        return "";
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("test","AAA");
        map.put("test2","BBB");
        map.put("test3","CCC");

        String returnStr = returnFormat(200,"123",map);

        System.out.println(returnStr);
    }
}
