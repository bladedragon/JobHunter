package team.legend.jobhunter.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonUtil {

    public static String returnFormat(int code, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("code", code);
        jsonObject.put("data",data);
        return jsonObject.toString(SerializerFeature.DisableCircularReferenceDetect);
    }
    public static String returnFormatSimp(int code,String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return jsonObject.toJSONString();
    }


    public static String getNowTime(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }

    public static String getNowDate(String pattern){
        //pattern  = "yyyy-MM-dd HH:mm"
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;

    }




    public static String getRandomStr(int num){
        return "";
    }

    public static List<String> toStrList(String str){

        if(str != null){
            String[] strs = str.split(":");
            List<String> strList = Arrays.asList(strs);
            return strList;
        }
        return null;
    }



    public static void main(String[] args) {
//        Map<String,String> map = new HashMap<>();
//        map.put("test","AAA");
//        map.put("test2","BBB");
//        map.put("test3","CCC");

//        String returnStr = returnFormat(200,"123",map);

    }
}
