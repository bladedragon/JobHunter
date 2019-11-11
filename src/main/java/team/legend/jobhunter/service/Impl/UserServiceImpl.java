package team.legend.jobhunter.service.Impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.legend.jobhunter.dao.WXDao;
import team.legend.jobhunter.exception.AuthorizeErrorException;
import team.legend.jobhunter.jwt.Jwt;
import team.legend.jobhunter.jwt.JwtHelper;
import team.legend.jobhunter.model.WXLogin;
import team.legend.jobhunter.model.WXUser;
import team.legend.jobhunter.service.UserService;
import team.legend.jobhunter.utils.HttpUtil;
import team.legend.jobhunter.utils.IDGenerator;
import team.legend.jobhunter.utils.SecretUtil;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${wxapi.code2Session}")
    String code2Session;

    @Value("${wxapi.appid}")
    private String appid;

    @Value("${wxapi.appSecret}")
    private String secretId;

    @Autowired
    JwtHelper<WXLogin> wxLoginJwtHelper;
    @Autowired
    IDGenerator idGenerator;

    @Autowired
    WXDao wxDao;

    public Map<Integer,String> getSessionKey(String code) {
        Map<Integer,String> result = new HashMap<>();
        String code2SessionUrl = code2Session+appid+"&secret=" + secretId + "&js_code=" + code + "&grant_type=authorization_cde";
        String res = HttpUtil.httpGet(code2SessionUrl);
        JSONObject resJson = JSONObject.parseObject(res);
        switch(resJson.getInteger("errcode")){
            case -1:
                log.error(">>login: errmsg:{}",resJson.getString("errmsg"));
                result.put(-1,"系统繁忙，此时请开发者稍候再试");
                result.put(-2,String.valueOf(resJson.getInteger("errcode")));
                break;
            case 40029:
                log.error(">>login:errmsg:{}",resJson.getString("errmsg"));
                result.put(-1,"code 无效");
                result.put(-2,String.valueOf(resJson.getInteger("errcode")));
                break;
            case 45011:
                log.error(">>login:errmsg:{}",resJson.getString("errmsg"));
                result.put(-1,"频率限制，每个用户每分钟100次");
                result.put(-2,String.valueOf(resJson.getInteger("errcode")));
                break;
            case 0:
                result.put(0,resJson.getString("session_key"));
                result.put(1,resJson.getString("openid"));
                if(resJson.containsKey("unionid")){
                    result.put(2,resJson.getString("unionid"));
                }
                break;
            default:
                throw new RuntimeException("请求失败");
        }

        return result;
    }


    @Override
    public Map<Integer,String> login(String code) {
        List<Object> loginList = new ArrayList<>();
        String user_id = null;
        Map<Integer,String> sessionMap = getSessionKey(code);
        WXLogin wxLogin = new WXLogin();
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date  = simpleDateFormat.format(new Date());

        if(sessionMap.containsKey(0)){
            int rank = wxDao.getCount();
            user_id = idGenerator.user_idEncode(sessionMap.get(0),sessionMap.get(1),rank);
            wxLogin.setOpenid(sessionMap.get(1));
            wxLogin.setSessionkey(sessionMap.get(0));
            if(sessionMap.containsKey(2)){
                wxLogin.setUnionid(sessionMap.get(2));
            }else{
                //防止jwt生成的参数出现null，导致无法生成jwt
                wxLogin.setUnionid("");
            }
            WXLogin login  = wxDao.selectByOpenid(sessionMap.get(1));
            if(login==null){
                //新插入
                wxLogin.setCreate_date(date);
                wxDao.insertNewUser(wxLogin);
                sessionMap.put(4,date);
                sessionMap.put(5,date);
            }else{
                //更新最后登录时间和sessionkey
                wxDao.updateLastLogin(date,sessionMap.get(0));
                sessionMap.put(4,wxLogin.getCreate_date());
                sessionMap.put(5,wxLogin.getLast_login());
                String tea_id = wxLogin.getTea_id();
                if(tea_id!=null || !tea_id.equals("")){
                    sessionMap.put(10,tea_id);
                }
            }

            sessionMap.put(3,user_id);

            Jwt jwt = null;
            wxLoginJwtHelper.createJwt(wxLogin);
            log.info(">>log:code:[{}] id:[{}] openid:[{}] 校验成功", code, user_id,sessionMap.get(2));
            sessionMap.put(6,jwt.getJwtString());


        }
        return sessionMap;
    }

    @Override
    public Map<String,Object> authorizeData(String user_id, String openid, String rawData,String signature) throws AuthorizeErrorException {
        String signature2 = SecretUtil.shaCheck(rawData,signature);
        if(signature2.equals(signature)){
            JSONObject userInfo = JSONObject.parseObject(rawData);
            WXUser wxUser = new WXUser(openid,"",user_id,userInfo.getString("nickName")
                    ,userInfo.getString("avatarUrl"),userInfo.getInteger("gender"));
            //更新用户信息
            wxDao.updateUser(wxUser);
            WXUser wxUserNew = wxDao.selectUserByUserId(user_id);
            Map<String,Object> wxUserMap = new HashMap<>();
            wxUserMap.put("nickname",wxUserNew.getNickname());
            wxUserMap.put("headimg_url",wxUserNew.getHeadimg_url());
            wxUserMap.put("gender",wxUserNew.getGender());
            return wxUserMap;
        }else{
            log.error("log>>rawData check fail");
            throw  new AuthorizeErrorException("rawData check fail");
        }
    }

    @Override
    public Map<String,Object> authorizeEncrypted(String user_id,String openid,String encryptedData, String iv) throws AuthorizeErrorException {

        //请求数据库获得sessionKey
        String sessionKey_origin = wxDao.selectSessionKeyByOpenid(openid);
        byte[] sessionKey = SecretUtil.decodeBase64ToBtye("tiihtNczf5v6AKRyjwEUhQ==");
        byte[] ivs = SecretUtil.decodeBase64ToBtye(iv);

        String userinfoStr = SecretUtil.decrypt(encryptedData,sessionKey,ivs);
        System.out.println(userinfoStr);
        JSONObject userinfo = JSONObject.parseObject(userinfoStr);
        JSONObject watermark = userinfo.getJSONObject("watermark");

        if(!watermark.get("appid").equals(appid) || !userinfo.getString("openid").equals(openid)){
            log.error("log>>appid or openid authorized error");
            throw new AuthorizeErrorException("appid or openid authorized error");
        }
        WXUser wxUser = new WXUser(userinfo.getString("openid"),userinfo.getString("unionId"),user_id,userinfo.getString("nickName")
                ,userinfo.getString("avatarUrl"),userinfo.getInteger("gender"));
                //更新微信用户信息

                wxDao.updateUser(wxUser);
        //查询获取最新
                WXUser wxUserNew = wxDao.selectUserByUserId(user_id);
        Map<String,Object> wxUserMap = new HashMap<>();
        wxUserMap.put("nickname",wxUserNew.getNickname());
        wxUserMap.put("headimg_url",wxUserNew.getHeadimg_url());
        wxUserMap.put("gender",wxUserNew.getGender());
        return wxUserMap;
    }

        @Override
        public Map<String,Object> getOldUserData(String user_id){
            WXUser wxUser = wxDao.selectUserByUserId(user_id);
            Map<String,Object> wxUserMap = new HashMap<>();
            if(wxUser.getNickname()!=null&!wxUser.getNickname().equals("")){
                wxUserMap.put("nickname",wxUser.getNickname());
                wxUserMap.put("headimg_url",wxUser.getHeadimg_url());
                wxUserMap.put("gender",wxUser.getGender());
            }else{
                wxUserMap.put("empty","");
            }
            return wxUserMap;
        }


    public static void main(String[] args) throws AuthorizeErrorException {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        String openid = "";
        String user_id = "";
        userServiceImpl.authorizeEncrypted(user_id,openid,"CiyLU1Aw2KjvrjMdj8YKliAjtP" +
                        "4gsMZMQmRzooG2xrDcvSnxIMXFufNstNGTyaGS9uT5geRa0W4o" +
                        "TOb1WT7fJlAC+oNPdbB+3hVbJSRgv+4lGOETKUQz6OYStslQ142" +
                        "dNCuabNPGBzlooOmB231qMM85d2/fV6ChevvXvQP8Hkue1poOFt" +
                        "nEtpyxVLW1zAo6/1Xx1COxFvrc2d7UL/lmHInNlxuacJXwu0fjpX" +
                        "fz/YqYzBIBzD6WUfTIF9GRHpOn/Hz7saL8xz+W//FRAUid1OksQaQ" +
                        "x4CMs8LOddcQhULW4ucetDf96JcR3g0gfRK4PC7E/r7Z6xNrXd2UI" +
                        "eorGj5Ef7b1pJAYB6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns/8wR" +
                        "2SiRS7MNACwTyrGvt9ts8p12PKFdlqYTopNHR1Vf7XjfhQlVsAJd" +
                        "NiKdYmYVoKlaRv85IfVunYzO0IKXsyl7JCUjCpoG20f0a04COwfne" +
                        "QAGGwd5oa+T8yO5hzuyDb/XcxxmK01EpqOyuxINew=="
        ,"r7BXXKkLb8qrSNn05n0qiA==");
    }

}
