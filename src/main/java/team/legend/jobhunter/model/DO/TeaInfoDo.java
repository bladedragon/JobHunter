package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeaInfoDo {
    private String service_id;
    private String tea_id;
    private String tea_nickname;
    private String tea_img_url;
    private int gender;
    private String tea_tele;
    private String tea_email;
    private String tea_description;
    private String tea_tag;
    private String tea_company;
    private String position;
    private String tea_type;
    private int isonline;
    private long service_timestamp;
    private int service_status;
}
