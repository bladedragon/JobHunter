package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import team.legend.jobhunter.dao.WXDao;
import team.legend.jobhunter.model.response.WxLoginResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WXLogin {
    private String openid;
    private String session_key;
    private String unionid;
    private String last_login;
    private String create_date;
    private String user_id;
    private String tea_id;

    public void setWXLogin(WXLogin wxLogin){
        this.openid = wxLogin.getOpenid();
        this.session_key = wxLogin.getSession_key();
        this.unionid = wxLogin.getUnionid();
        this.last_login = wxLogin.getLast_login();
        this.create_date = wxLogin.getCreate_date();
        this.user_id = wxLogin.getUser_id();
        this.tea_id = wxLogin.getTea_id();
    }


}
