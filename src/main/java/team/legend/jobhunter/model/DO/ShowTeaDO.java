package team.legend.jobhunter.model.DO;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ShowTeaDO {
    private String service_id;
    private String tea_id;
    private String tea_nickname;
    private String tea_img_url;
    private String tea_description;
    private String tea_tag;
    private String tea_company;
    private Integer tea_gender;
    private Integer isonline;
    private Long service_timestamp;
    private Integer service_status;
    private String position;



}
