package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StuDO {
    private String stuId;
    private String nickname;
    private String realname;
    private String img_url;
    private String tele;
    private String email;
    private int gender;
    private String descripton;


}
