package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class WXUser {
    private String openid;
    private String unionid;
    private String user_id;

    private String nickname;
    private String headimg_url;
    private Integer gender;


}
