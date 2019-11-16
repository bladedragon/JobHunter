package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderTeaDO {
    private String tea_nickname;
    private String tea_tag;
    private String tea_img_url;
    private Integer tea_gender;
    private String tea_email;
    private String tea_description;
    private String tea_company;
    private String position;
    private Integer isonline;
}
