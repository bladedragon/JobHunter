package team.legend.jobhunter.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OfferInfo {
    private String offer_id;
    private String offer_name;
    private String offer_requirement;
    private String offer_location;
    private String offer_company_name;
    private String offer_type;
    private String offer_description;
    private String offer_post_way;
    private String offer_salary;
    private Long  update_timestamp;
    private String logo;

//     OfferInfo(String offer_id,String offer_name,String offer_requirement,String offer_location,
//              String offer_company_name,String offer_type,String offer_description,String offer_post_way,
//              String offer_salary){
//            this.offer_id = offer_id;
//            this.offer_company_name = offer_company_name;
//            this.offer_description  = offer_description;
//            this.offer_location = offer_location;
//            this.offer_name = offer_name;
//            this.offer_requirement = offer_requirement;
//            this.offer_post_way = offer_post_way;
//            this.offer_salary = offer_salary;
//            this.offer_type = offer_type;
//            this.update_timestamp = System.currentTimeMillis();
//
//    }

}
