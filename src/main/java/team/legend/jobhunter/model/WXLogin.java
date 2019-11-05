package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WXLogin {
    private String openid;
    private String sessionkey;
    private String unionid;
    private String last_login;
    private String create_date;

    public WXLogin(WXLogin wxLogin){
        this.openid = wxLogin.getOpenid();
        this.sessionkey = wxLogin.getSessionkey();
        this.unionid = wxLogin.getUnionid();
        this.last_login = wxLogin.getLast_login();
        this.create_date = wxLogin.getCreate_date();

    }
}
