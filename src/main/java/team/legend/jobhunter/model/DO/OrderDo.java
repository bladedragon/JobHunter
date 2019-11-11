package team.legend.jobhunter.model.DO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDo {
    private String order_id;
    private String tea_id;
    private String tea_nickname;
    private String tea_headimg_url;
    private Integer tea_gender;
    private String stu_id;
    private String item_time;
    private String item_price;
    private Integer item_isonline;
    private String item_time_detail;
    private String order_date;
    private String order_type;
    private Integer order_status;
    private String order_modify_date;
    private String order_actual_payment;
    private Integer  iseffective;
}
