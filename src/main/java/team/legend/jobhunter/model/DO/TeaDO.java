package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeaDO {
    private String teaId;
    private String nickname;
    private String realname;
    private String img_url;
    private String tele;
    private String email;
    private int gender;
    private String description;
    private String tag;
    private String company;
    private String profession;

}
