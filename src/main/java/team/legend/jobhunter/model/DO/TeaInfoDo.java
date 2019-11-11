package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.legend.jobhunter.model.Teacher;

@Data
@AllArgsConstructor
public class TeaInfoDo {
    private String service_id;
    private String tea_id;
    private String tea_nickname;
    private String tea_img_url;
    private Integer tea_gender;
    private String tea_tele;
    private String tea_email;
    private String tea_description;
    private String tea_tag;
    private String tea_company;
    private String position;
    private Integer isonline;
    private Long service_timestamp;
    private Integer service_status;

    public TeaInfoDo(Teacher teacher,String service_id,Long service_timestamp,Integer service_status){
        this.tea_id = teacher.getTea_id();
        this.tea_nickname = teacher.getTea_nickname();
        this.tea_img_url = teacher.getTea_img_url();
        this.tea_gender  = teacher.getGender();
        this.tea_tele = teacher.getTea_tele();
        this.tea_email = teacher.getTea_email();
        this.tea_description = teacher.getTea_description();
        this.tea_tag = teacher.getTea_tag();
        this.tea_company = teacher.getTea_company();
        this.position = teacher.getPosition();
        this.isonline = teacher.getIsonline();
        this.service_id = service_id;
        this.service_timestamp = service_timestamp;
        this.service_status = service_status;
    }
}
