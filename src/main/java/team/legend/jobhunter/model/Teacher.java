package team.legend.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Teacher {
    private String tea_id;
    private String tea_nickname;
    private String tea_realname;
    private String tea_img_url;
    private Integer gender;
    private String tea_tele;
    private String tea_email;
    private String tea_description;
    private String tea_tag;
    private String tea_company;
    private String position;
    private String tea_type;
    private Integer isonline;
    private String verify_code;

}
