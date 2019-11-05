package team.legend.jobhunter.schedule;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.legend.jobhunter.utils.AccessTokenHelper;
import team.legend.jobhunter.utils.HttpUtil;

@Component
@Slf4j
public class AsTokenTask {
    @Autowired
    AccessTokenHelper accessTokenHelper;
    @Value("${wxapi.appid}")
    private String appid;
    @Value("${wxapi.appSecret}")
    private String appSecret;
    @Value("${wxapi.get_accesstoken}")
    private String get_accesstoken_url;

    @Scheduled(fixedDelay = 7180000)
    public String getAccessTokenFromURL() {
        String access_token = null;
        String grant_type = "client_credential";
        String AppId = appid;
        String secret = appSecret;
        String url = get_accesstoken_url+ grant_type + "&appid=" + AppId + "&secret=" + secret;

        String responseStr = HttpUtil.httpGet(url);
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        access_token = responseJson.getString("access_token");

        accessTokenHelper.saveKey("global_token", access_token);

        return access_token;
    }

    public String getAccess_Token() {
        String access_token = accessTokenHelper.getKey("global_token");
        if (access_token.equals("1")) {
            log.info("ZLOG=> no ACCESS_TOKEN cache");
            getAccessTokenFromURL();
            access_token = accessTokenHelper.getKey("global_token");
        }
        return access_token;
    }
}
