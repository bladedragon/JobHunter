package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeaDO {
    private String tea_id;
    private String tea_nickname;
    private String tea_img_url;
    private String tea_tele;
    private String tea_email;
    private String tea_description;
    private String tea_tag;
    private String tea_company;
    private String position;
    private String tea_type;
    private Integer isonline;

//     TeaDO(String tea_id,String tea_nickname,String tea_img_url,String tea_tele,String tea_email,String tea_description,
//                 String tea_tag,String tea_company,String position,String tea_type,Integer isonline){
//        this.tea_id = tea_id;
//        this.tea_nickname = tea_nickname;
//        this.tea_img_url = tea_img_url;
//        this.tea_tele = tea_tele;
//        this.tea_email = tea_email;
//        this.tea_description = tea_description;
//        this.tea_tag = tea_tag;
//        this.tea_company = tea_company;
//        this.position = position;
//        this.tea_type = tea_type;
//        this.isonline = isonline;
//    }

}
