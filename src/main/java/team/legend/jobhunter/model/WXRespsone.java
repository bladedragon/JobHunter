package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WXRespsone {
    private String openid;
    private String unionid;
    private String user_id;
    private String nickname;
    private String headimg_url;
    private String last_login;
    private Integer gender;
}
